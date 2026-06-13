import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import SockJS from 'sockjs-client/dist/sockjs';
import { Client } from '@stomp/stompjs';
import api from '../services/api';

export function AdminDashboard() {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const [solicitacoes, setSolicitacoes] = useState([]);
  const [conectado, setConectado] = useState(false);

  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [selectedAtendimento, setSelectedAtendimento] = useState(null);
  const [profissionaisDisponiveis, setProfissionaisDisponiveis] = useState([]);
  const [selectedProfissionalId, setSelectedProfissionalId] = useState('');

  const fetchSolicitacoes = async () => {
    try {
      const res = await api.get('/api/atendimentos');
      // Filtra apenas as PENDENTES
      setSolicitacoes(res.data.filter(a => a.status === 'PENDENTE').sort((a,b) => new Date(b.dataHora) - new Date(a.dataHora)));
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchSolicitacoes();

    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-medcore'),
      reconnectDelay: 5000,
    });

    client.onConnect = function () {
      setConectado(true);
      client.subscribe('/topic/solicitacoes', () => {
        fetchSolicitacoes(); // Refresh list automatically
      });
    };
    
    client.onDisconnect = () => setConectado(false);
    client.activate();

    return () => {
      if (client.active) client.deactivate();
    };
  }, []);

  const handleDesignarClick = async (atendimento) => {
    setSelectedAtendimento(atendimento);
    setShowModal(true);
    // Fetch professionals by specialty
    try {
      const res = await api.get(`/api/profissionais/especialidade/${atendimento.especialidade}`);
      setProfissionaisDisponiveis(res.data);
      if (res.data.length > 0) {
        setSelectedProfissionalId(res.data[0].id);
      } else {
        setSelectedProfissionalId('');
      }
    } catch (err) {
      console.error(err);
    }
  };

  const confirmDesignacao = async () => {
    if (!selectedProfissionalId) return alert('Selecione um médico disponível!');
    try {
      await api.patch(`/api/atendimentos/${selectedAtendimento.id}`, {
        profissionalId: selectedProfissionalId,
        status: 'MARCADA'
      });
      setShowModal(false);
      fetchSolicitacoes(); // Reload the list
    } catch (err) {
      alert('Erro ao designar: ' + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div style={{ padding: '2rem', maxWidth: '1000px', margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h2>Administração Hospitalar</h2>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <span style={{ fontSize: '0.875rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <span style={{ display: 'inline-block', width: '10px', height: '10px', borderRadius: '50%', backgroundColor: conectado ? 'var(--success-color)' : 'var(--danger-color)' }}></span>
            {conectado ? 'WebSocket Conectado' : 'Conectando...'}
          </span>
          <button className="btn" style={{ backgroundColor: 'var(--danger-color)', color: 'white' }} onClick={() => { logout(); navigate('/'); }}>Sair</button>
        </div>
      </div>

      <div className="card">
        <h3>Solicitações Pendentes (Ao Vivo)</h3>
        {solicitacoes.length === 0 ? (
          <p style={{ marginTop: '1rem', color: 'var(--text-muted)' }}>Nenhuma solicitação pendente no momento.</p>
        ) : (
          <div style={{ marginTop: '1rem', display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {solicitacoes.map(solic => (
              <div key={solic.id} style={{ padding: '1.5rem', border: '1px solid rgba(255,255,255,0.5)', borderRadius: '12px', background: 'rgba(255, 255, 255, 0.4)', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <strong>Nova Solicitação PENDENTE</strong> <br/>
                  <span style={{color: 'var(--primary-color)', fontWeight: '600'}}>{solic.especialidade}</span> <br/>
                  <small>Data: {new Date(solic.dataHora).toLocaleString()}</small>
                </div>
                <button className="btn btn-primary" onClick={() => handleDesignarClick(solic)}>
                  Designar Médico
                </button>
              </div>
            ))}
          </div>
        )}
      </div>

      {showModal && selectedAtendimento && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', backdropFilter: 'blur(5px)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000 }}>
          <div className="card" style={{ width: '450px', backgroundColor: 'rgba(255,255,255,0.95)' }}>
            <h3>Designar Médico</h3>
            <p className="mt-2 text-muted">Especialidade: <strong>{selectedAtendimento.especialidade}</strong></p>
            
            <div className="form-group mt-3">
              <label>Selecione um Profissional (Role a lista):</label>
              {profissionaisDisponiveis.length === 0 ? (
                <div style={{ padding: '1rem', background: '#ffebee', color: '#c62828', borderRadius: '4px' }}>Nenhum médico com essa especialidade encontrado no banco.</div>
              ) : (
                <select 
                  className="form-control" 
                  size={4} // Allows scrolling
                  value={selectedProfissionalId} 
                  onChange={e => setSelectedProfissionalId(e.target.value)}
                  style={{ backgroundColor: 'rgba(255,255,255,0.8)' }}
                >
                  {profissionaisDisponiveis.map(prof => (
                    <option key={prof.id} value={prof.id}>{prof.nome} - CRM {prof.registroConselho}</option>
                  ))}
                </select>
              )}
            </div>

            <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
              <button className="btn btn-primary" onClick={confirmDesignacao} disabled={profissionaisDisponiveis.length === 0} style={{ flex: 1 }}>Confirmar Designação</button>
              <button className="btn" onClick={() => setShowModal(false)} style={{ flex: 1, backgroundColor: '#ddd' }}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
