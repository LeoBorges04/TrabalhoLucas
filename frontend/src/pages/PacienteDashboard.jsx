import { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import SockJS from 'sockjs-client/dist/sockjs';
import { Client } from '@stomp/stompjs';

export function PacienteDashboard() {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [especialidade, setEspecialidade] = useState('CLINICA_MEDICA');
  const [mensagem, setMensagem] = useState('');
  
  const [atendimentos, setAtendimentos] = useState([]);
  const [activeTab, setActiveTab] = useState('PENDENTE'); // PENDENTE, MARCADA, FINALIZADA

  const [userProfile, setUserProfile] = useState(null);
  const [showProfileModal, setShowProfileModal] = useState(false);
  const [profileData, setProfileData] = useState({});

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
      const res = await api.get('/api/pacientes/me');
      setUserProfile(res.data);
      setProfileData(res.data);
    } catch (err) {
      console.error("Erro ao buscar perfil:", err);
    }
  };

  const handleOpenProfileModal = async () => {
    try {
      const res = await api.get('/api/pacientes/me');
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
      const res = await api.patch(`/api/pacientes/${userProfile.id}`, payload);
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

  const solicitarAtendimento = async () => {
    try {
      await api.post('/api/atendimentos', {
        especialidade,
        dataHora: new Date().toISOString(),
        tipo: 'Consulta',
        status: 'PENDENTE'
      });
      setMensagem('Solicitação enviada! Acompanhe na aba "Solicitadas".');
      setShowModal(false);
      setTimeout(() => setMensagem(''), 4000);
      fetchAtendimentos();
    } catch (err) {
      alert('Erro ao solicitar: ' + (err.response?.data?.message || err.message));
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

  const filteredAtendimentos = atendimentos.filter(a => a.status === activeTab);

  return (
    <div style={{ padding: '2rem', maxWidth: '800px', margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h2>Painel do Paciente {userProfile ? `- ${userProfile.nome}` : ''}</h2>
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
        <h3>Bem-vindo!</h3>
        <p>Aqui você pode solicitar e visualizar seus agendamentos médicos em tempo real.</p>
        <button className="btn btn-primary mt-3" onClick={() => setShowModal(true)}>
          Solicitar Novo Atendimento
        </button>
      </div>

      <div className="card">
        <div className="tabs-container">
          <button className={`tab-btn ${activeTab === 'PENDENTE' ? 'active' : ''}`} onClick={() => setActiveTab('PENDENTE')}>Solicitadas</button>
          <button className={`tab-btn ${activeTab === 'MARCADA' ? 'active' : ''}`} onClick={() => setActiveTab('MARCADA')}>Marcadas</button>
          <button className={`tab-btn ${activeTab === 'FINALIZADA' ? 'active' : ''}`} onClick={() => setActiveTab('FINALIZADA')}>Finalizadas</button>
        </div>

        {filteredAtendimentos.length === 0 ? (
          <p style={{ marginTop: '1rem' }}>Nenhum atendimento nesta categoria.</p>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {filteredAtendimentos.map((solic, idx) => (
              <div key={idx} style={{ padding: '1rem', border: '1px solid rgba(255,255,255,0.4)', borderRadius: '8px', backgroundColor: 'rgba(255,255,255,0.4)', position: 'relative' }}>
                {activeTab === 'FINALIZADA' && (
                  <button 
                    onClick={() => deletarAtendimento(solic.id)} 
                    style={{ position: 'absolute', top: '10px', right: '10px', background: 'transparent', border: 'none', cursor: 'pointer', fontSize: '1.2rem' }}
                    title="Ocultar do histórico"
                  >
                    🗑️
                  </button>
                )}
                <strong>Especialidade:</strong> {solic.especialidade} <br/>
                <strong>Data:</strong> {new Date(solic.dataHora).toLocaleString()} <br/>
                {solic.profissionalNome && (
                  <>
                    <strong>Médico:</strong> {solic.profissionalNome} <br/>
                    <strong>CRM:</strong> {solic.profissionalCrm || 'Não informado'} <br/>
                  </>
                )}
                {activeTab === 'MARCADA' && <span style={{ color: 'var(--primary-color)', fontWeight: '600' }}>Médico Atribuído! Aguarde a consulta.</span>}
                {activeTab === 'FINALIZADA' && (
                  <div style={{ marginTop: '0.5rem', padding: '0.5rem', backgroundColor: 'rgba(255,255,255,0.7)', borderRadius: '4px' }}>
                    <strong>Prontuário/Observações:</strong> {solic.observacoes || 'Nenhuma observação.'}
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.4)', backdropFilter: 'blur(4px)', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div className="card" style={{ width: '400px', backgroundColor: 'rgba(255,255,255,0.85)' }}>
            <h3>Solicitar Atendimento</h3>
            <p className="mt-3">Qual a especialidade necessária?</p>
            <select className="form-control mt-2" value={especialidade} onChange={(e) => setEspecialidade(e.target.value)}>
              {especialidades.map(esp => <option key={esp} value={esp}>{esp.replace(/_/g, ' ')}</option>)}
            </select>
            <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
              <button className="btn btn-primary" onClick={solicitarAtendimento} style={{ flex: 1 }}>Confirmar</button>
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
              <label>CPF</label>
              <input type="text" className="form-control" value={profileData.cpf || ''} onChange={e => setProfileData({...profileData, cpf: e.target.value})} placeholder="Apenas números (11 dígitos)" />
            </div>
            <div className="form-group mt-2">
              <label>Data de Nascimento</label>
              <input type="date" className="form-control" value={profileData.dataNascimento || ''} onChange={e => setProfileData({...profileData, dataNascimento: e.target.value})} />
            </div>
            <div className="form-group mt-2">
              <label>Sexo</label>
              <select className="form-control" value={profileData.sexo || ''} onChange={e => setProfileData({...profileData, sexo: e.target.value})}>
                <option value="">Selecione...</option>
                <option value="Masculino">Masculino</option>
                <option value="Feminino">Feminino</option>
                <option value="Outro">Outro</option>
              </select>
            </div>
            <div className="form-group mt-2">
              <label>Telefone</label>
              <input type="text" className="form-control" value={profileData.telefone || ''} onChange={e => setProfileData({...profileData, telefone: e.target.value})} />
            </div>
            <div className="form-group mt-2">
              <label>Email</label>
              <input type="email" className="form-control" value={profileData.email || ''} onChange={e => setProfileData({...profileData, email: e.target.value})} />
            </div>
            <div className="form-group mt-2">
              <label>Endereço</label>
              <input type="text" className="form-control" value={profileData.endereco || ''} onChange={e => setProfileData({...profileData, endereco: e.target.value})} />
            </div>
            <div className="form-group mt-2">
              <label>Convênio</label>
              <input type="text" className="form-control" value={profileData.convenio || ''} onChange={e => setProfileData({...profileData, convenio: e.target.value})} />
            </div>
            <div className="form-group mt-2">
              <label>Número Carteirinha</label>
              <input type="text" className="form-control" value={profileData.numeroCarteirinha || ''} onChange={e => setProfileData({...profileData, numeroCarteirinha: e.target.value})} />
            </div>
            <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
              <button className="btn btn-primary" onClick={saveProfile} style={{ flex: 1 }}>Salvar</button>
              <button className="btn" onClick={() => { 
                setProfileData(userProfile); 
                setShowProfileModal(false); 
              }} style={{ flex: 1, backgroundColor: '#ddd' }}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
