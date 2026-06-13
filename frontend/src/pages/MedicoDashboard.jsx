import { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import SockJS from 'sockjs-client/dist/sockjs';
import { Client } from '@stomp/stompjs';

export function MedicoDashboard() {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();
  
  const [atendimentos, setAtendimentos] = useState([]);
  const [activeTab, setActiveTab] = useState('MARCADA'); // MARCADA, FINALIZADA
  
  const [showModal, setShowModal] = useState(false);
  const [selectedAtendimento, setSelectedAtendimento] = useState(null);
  const [observacoes, setObservacoes] = useState('');

  const [userProfile, setUserProfile] = useState(null);
  const [showProfileModal, setShowProfileModal] = useState(false);
  const [profileData, setProfileData] = useState({});
  const [mensagem, setMensagem] = useState('');

  const especialidades = [
    'CLINICA_MEDICA', 'PEDIATRIA', 'CIRURGIA_GERAL', 'GINECOLOGIA_E_OBSTETRICIA',
    'MEDICINA_DO_TRABALHO', 'ORTOPEDIA_E_TRAUMATOLOGIA', 'CARDIOLOGIA', 'OFTALMOLOGIA',
    'PSIQUIATRIA', 'DERMATOLOGIA', 'MEDICINA_INTENSIVA', 'RADIOLOGIA_E_DIAGNOSTICO_POR_IMAGEM',
    'OTORRINOLARINGOLOGIA', 'UROLOGIA', 'ENDOCRINOLOGIA', 'NEUROLOGIA', 'CIRURGIA_PLASTICA',
    'GASTROENTEROLOGIA', 'ONCOLOGIA'
  ];

  const fetchAtendimentos = async () => {
    try {
      const res = await api.get('/api/atendimentos/me');
      setAtendimentos(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchProfile = async () => {
    try {
      const res = await api.get('/api/profissionais/me');
      setUserProfile(res.data);
      setProfileData(res.data);
    } catch (err) {
      console.error("Erro ao buscar perfil:", err);
    }
  };

  const handleOpenProfileModal = async () => {
    try {
      const res = await api.get('/api/profissionais/me');
      setUserProfile(res.data);
      setProfileData(res.data);
      setShowProfileModal(true);
    } catch (err) {
      alert("Erro ao carregar dados do perfil: " + (err.response?.data?.message || err.message));
    }
  };

  const saveProfile = async () => {
    try {
      const payload = { ...profileData };
      Object.keys(payload).forEach(key => {
        if (payload[key] === '') {
          payload[key] = null;
        }
      });
      const res = await api.patch(`/api/profissionais/${userProfile.id}`, payload);
      setUserProfile(res.data);
      setShowProfileModal(false);
      setMensagem('Perfil atualizado com sucesso!');
      setTimeout(() => setMensagem(''), 4000);
    } catch (err) {
      alert('Erro ao atualizar perfil: ' + (err.response?.data?.message || err.message));
    }
  };

  useEffect(() => {
    fetchProfile();
    fetchAtendimentos();

    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-medcore'),
      reconnectDelay: 5000,
    });

    client.onConnect = function () {
      client.subscribe('/topic/solicitacoes', () => {
        fetchAtendimentos(); // Re-fetch on any update
      });
    };
    client.activate();

    return () => {
      if (client.active) client.deactivate();
    };
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const abrirModalFinalizar = (atendimento) => {
    setSelectedAtendimento(atendimento);
    setObservacoes(atendimento.observacoes || '');
    setShowModal(true);
  };

  const confirmarFinalizacao = async () => {
    try {
      await api.patch(`/api/atendimentos/${selectedAtendimento.id}`, {
        status: 'FINALIZADA',
        observacoes
      });
      setShowModal(false);
      fetchAtendimentos();
    } catch (err) {
      alert('Erro ao finalizar consulta: ' + (err.response?.data?.message || err.message));
    }
  };

  const deletarAtendimento = async (id) => {
    if (!window.confirm("Deseja realmente ocultar este atendimento do histórico?")) return;
    try {
      await api.delete(`/api/atendimentos/${id}`);
      fetchAtendimentos();
    } catch (err) {
      alert("Erro ao ocultar: " + (err.response?.data?.message || err.message));
    }
  };

  const filteredAtendimentos = atendimentos.filter(a => a.status === activeTab).sort((a,b) => new Date(b.dataHora) - new Date(a.dataHora));

  return (
    <div style={{ padding: '2rem', maxWidth: '800px', margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h2>Painel do Médico {userProfile ? `- ${userProfile.nome}` : ''}</h2>
        <div style={{ display: 'flex', gap: '1rem' }}>
          <button className="btn" style={{ backgroundColor: 'var(--primary-color)', color: 'white' }} onClick={handleOpenProfileModal}>Perfil</button>
          <button className="btn" style={{ backgroundColor: 'var(--danger-color)', color: 'white' }} onClick={handleLogout}>Sair</button>
        </div>
      </div>

      {mensagem && (
        <div style={{ backgroundColor: 'var(--success-color)', color: 'white', padding: '1rem', borderRadius: '8px', marginBottom: '1rem', backdropFilter: 'blur(5px)' }}>
          {mensagem}
        </div>
      )}

      <div className="card mb-4">
        <h3>Sua Agenda</h3>
        <p>Acompanhe suas consultas agendadas e histórico de prontuários.</p>
      </div>

      <div className="card">
        <div className="tabs-container">
          <button className={`tab-btn ${activeTab === 'MARCADA' ? 'active' : ''}`} onClick={() => setActiveTab('MARCADA')}>Consultas Marcadas</button>
          <button className={`tab-btn ${activeTab === 'FINALIZADA' ? 'active' : ''}`} onClick={() => setActiveTab('FINALIZADA')}>Histórico (Finalizadas)</button>
        </div>

        {filteredAtendimentos.length === 0 ? (
          <p style={{ marginTop: '1rem' }}>Nenhum atendimento nesta categoria.</p>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {filteredAtendimentos.map((solic, idx) => (
              <div key={idx} style={{ padding: '1.5rem', border: '1px solid rgba(255,255,255,0.4)', borderRadius: '12px', backgroundColor: 'rgba(255,255,255,0.4)', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', position: 'relative' }}>
                {activeTab === 'FINALIZADA' && (
                  <button 
                    onClick={() => deletarAtendimento(solic.id)} 
                    style={{ position: 'absolute', top: '10px', right: '10px', background: 'transparent', border: 'none', cursor: 'pointer', fontSize: '1.2rem' }}
                    title="Ocultar do histórico"
                  >
                    🗑️
                  </button>
                )}
                <div>
                  <strong>Data:</strong> {new Date(solic.dataHora).toLocaleString()} <br/>
                  <strong>Especialidade:</strong> {solic.especialidade} <br/>
                  {activeTab === 'FINALIZADA' && (
                    <div style={{ marginTop: '0.5rem', padding: '0.5rem', backgroundColor: 'rgba(255,255,255,0.7)', borderRadius: '4px', maxWidth: '90%' }}>
                      <strong>Prontuário:</strong> {solic.observacoes || 'Nenhuma observação.'}
                    </div>
                  )}
                </div>
                {activeTab === 'MARCADA' && (
                  <button className="btn btn-primary" onClick={() => abrirModalFinalizar(solic)}>
                    Finalizar Consulta
                  </button>
                )}
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && selectedAtendimento && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', backdropFilter: 'blur(5px)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div className="card" style={{ width: '500px', backgroundColor: 'rgba(255,255,255,0.95)' }}>
            <h3>Finalizar Consulta</h3>
            <p className="mt-2 text-muted">Preencha o prontuário para o atendimento do dia {new Date(selectedAtendimento.dataHora).toLocaleDateString()}</p>
            
            <div className="form-group mt-3">
              <label>Observações Médicas / Prontuário:</label>
              <textarea 
                className="form-control" 
                rows={5}
                value={observacoes} 
                onChange={(e) => setObservacoes(e.target.value)}
                placeholder="Descreva sintomas, receitas médicas, etc..."
              ></textarea>
            </div>

            <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
              <button className="btn btn-primary" onClick={confirmarFinalizacao} style={{ flex: 1 }}>Salvar e Finalizar</button>
              <button className="btn" onClick={() => setShowModal(false)} style={{ flex: 1, backgroundColor: '#ddd' }}>Cancelar</button>
            </div>
          </div>
        </div>
      )}

      {showProfileModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.4)', backdropFilter: 'blur(4px)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000 }}>
          <div className="card" style={{ width: '500px', backgroundColor: 'rgba(255,255,255,0.95)', maxHeight: '90vh', overflowY: 'auto' }}>
            <h3>Meu Perfil</h3>
            
            <div className="form-group mt-3">
              <label>Nome Completo</label>
              <input type="text" className="form-control" value={profileData.nome || ''} onChange={e => setProfileData({...profileData, nome: e.target.value})} />
            </div>

            <div className="form-group mt-2">
              <label>Registro no Conselho (ex: CRM, COREN)</label>
              <input type="text" className="form-control" value={profileData.registroConselho || ''} onChange={e => setProfileData({...profileData, registroConselho: e.target.value})} />
            </div>
            <div className="form-group mt-2">
              <label>Especialidades (Segure Ctrl para selecionar várias)</label>
              <select 
                className="form-control" 
                multiple
                value={profileData.especialidades || []} 
                onChange={e => {
                  const values = Array.from(e.target.selectedOptions, option => option.value);
                  setProfileData({...profileData, especialidades: values});
                }} 
                style={{ minHeight: '120px', overflowY: 'auto' }}
              >
                {especialidades.map(esp => (
                  <option key={esp} value={esp}>{esp.replace(/_/g, ' ')}</option>
                ))}
              </select>
            </div>

            <div className="form-group mt-2">
              <label>Cargo</label>
              <input type="text" className="form-control" value={profileData.cargo || ''} onChange={e => setProfileData({...profileData, cargo: e.target.value})} />
            </div>

            <div className="form-group mt-2">
              <label>Turno</label>
              <input type="text" className="form-control" value={profileData.turno || ''} onChange={e => setProfileData({...profileData, turno: e.target.value})} />
            </div>

            <div className="form-group mt-2">
              <label>Telefone</label>
              <input type="text" className="form-control" value={profileData.telefone || ''} onChange={e => setProfileData({...profileData, telefone: e.target.value})} />
            </div>

            <div className="form-group mt-2">
              <label>Email</label>
              <input type="email" className="form-control" value={profileData.email || ''} onChange={e => setProfileData({...profileData, email: e.target.value})} />
            </div>
            
            <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
              <button className="btn btn-primary" onClick={saveProfile} style={{ flex: 1 }}>Salvar</button>
              <button className="btn" onClick={() => { setProfileData(userProfile); setShowProfileModal(false); }} style={{ flex: 1, backgroundColor: '#ddd' }}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
