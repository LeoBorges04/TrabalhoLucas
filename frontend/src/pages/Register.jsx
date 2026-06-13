import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api';

export function Register() {
  const navigate = useNavigate();
  const [role, setRole] = useState('PACIENTE');
  const [formData, setFormData] = useState({
    nome: '', email: '', senha: '', cpf: '', telefone: '', registroConselho: '', especialidades: ['CLINICA_MEDICA'], cargo: 'Médico', turno: 'Integral',
    dataNascimento: '', sexo: '', endereco: '', convenio: '', numeroCarteirinha: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const especialidades = [
    'CLINICA_MEDICA', 'PEDIATRIA', 'CIRURGIA_GERAL', 'GINECOLOGIA_E_OBSTETRICIA',
    'MEDICINA_DO_TRABALHO', 'ORTOPEDIA_E_TRAUMATOLOGIA', 'CARDIOLOGIA', 'OFTALMOLOGIA',
    'PSIQUIATRIA', 'DERMATOLOGIA', 'MEDICINA_INTENSIVA', 'RADIOLOGIA_E_DIAGNOSTICO_POR_IMAGEM',
    'OTORRINOLARINGOLOGIA', 'UROLOGIA', 'ENDOCRINOLOGIA', 'NEUROLOGIA', 'CIRURGIA_PLASTICA',
    'GASTROENTEROLOGIA', 'ONCOLOGIA'
  ];

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      const payload = { ...formData, role };
      await api.post('/api/auth/register', payload);
      setSuccess('Cadastro realizado com sucesso! Redirecionando...');
      setTimeout(() => navigate('/'), 2000);
    } catch (err) {
      setError(err.response?.data || 'Erro ao realizar o cadastro. Tente novamente.');
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', backgroundColor: 'var(--bg-color)', padding: '2rem' }}>
      <div className="card" style={{ width: '800px', maxWidth: '100%' }}>
        <div className="text-center mb-4">
          <h2 style={{ color: 'var(--primary-color)' }}>MedCore</h2>
          <p>Criar nova conta</p>
        </div>

        {error && <div style={{ color: 'var(--danger-color)', marginBottom: '1rem', textAlign: 'center', fontSize: '0.875rem' }}>{error}</div>}
        {success && <div style={{ color: 'var(--success-color)', marginBottom: '1rem', textAlign: 'center', fontSize: '0.875rem' }}>{success}</div>}

        <form onSubmit={handleSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div className="form-group" style={{ gridColumn: '1 / -1' }}>
              <label>Tipo de Conta</label>
              <select className="form-control" value={role} onChange={(e) => setRole(e.target.value)}>
                <option value="PACIENTE">Paciente</option>
                <option value="MEDICO">Médico Profissional</option>
              </select>
            </div>

            <div className="form-group">
              <label>Nome Completo</label>
              <input type="text" name="nome" className="form-control" onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>Email</label>
              <input type="email" name="email" className="form-control" onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>Senha</label>
              <input type="password" name="senha" className="form-control" onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>Telefone</label>
              <input type="text" name="telefone" className="form-control" onChange={handleChange} required />
            </div>

            {role === 'PACIENTE' && (
              <>
                <div className="form-group">
                  <label>CPF</label>
                  <input type="text" name="cpf" className="form-control" onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Data de Nascimento</label>
                  <input type="date" name="dataNascimento" className="form-control" onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Sexo</label>
                  <select name="sexo" className="form-control" onChange={handleChange} required>
                    <option value="">Selecione</option>
                    <option value="Masculino">Masculino</option>
                    <option value="Feminino">Feminino</option>
                    <option value="Outro">Outro</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Endereço</label>
                  <input type="text" name="endereco" className="form-control" onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Convênio</label>
                  <input type="text" name="convenio" className="form-control" onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Número da Carteirinha</label>
                  <input type="text" name="numeroCarteirinha" className="form-control" onChange={handleChange} required />
                </div>
              </>
            )}

            {role === 'MEDICO' && (
              <>
                <div className="form-group">
                  <label>Registro Conselho (CRM)</label>
                  <input type="text" name="registroConselho" className="form-control" onChange={handleChange} required />
                </div>
                <div className="form-group">
                  <label>Especialidades (Segure Ctrl para selecionar várias)</label>
                  <select 
                    name="especialidades" 
                    className="form-control" 
                    multiple
                    value={formData.especialidades} 
                    onChange={(e) => {
                      const values = Array.from(e.target.selectedOptions, option => option.value);
                      setFormData({...formData, especialidades: values});
                    }}
                    style={{ minHeight: '120px' }}
                  >
                    {especialidades.map(esp => (
                      <option key={esp} value={esp}>{esp.replace(/_/g, ' ')}</option>
                    ))}
                  </select>
                </div>
              </>
            )}
          </div>

          <button type="submit" className="btn btn-primary btn-block mt-4">
            Cadastrar
          </button>

          <div className="text-center mt-3" style={{ fontSize: '0.875rem' }}>
            <Link to="/">Já possui uma conta? Faça login.</Link>
          </div>
        </form>
      </div>
    </div>
  );
}
