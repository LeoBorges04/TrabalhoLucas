import { useContext, useState } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

export function Login() {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      const loggedRole = await login(email, senha);
      if (loggedRole === 'ADMIN') navigate('/admin');
      if (loggedRole === 'MEDICO') navigate('/medico');
      if (loggedRole === 'PACIENTE') navigate('/paciente');
    } catch (err) {
      setError('Credenciais inválidas ou erro no servidor.');
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: 'var(--bg-color)' }}>
      <div className="card" style={{ width: '400px' }}>
        <div className="text-center mb-4">
          <h2 style={{ color: 'var(--primary-color)' }}>MedCore</h2>
          <p>Acesse sua conta</p>
        </div>

        {error && <div style={{ color: 'var(--danger-color)', marginBottom: '1rem', textAlign: 'center', fontSize: '0.875rem' }}>{error}</div>}

        <form onSubmit={handleSubmit}>

          <div className="form-group">
            <label>Email</label>
            <input 
              type="email" 
              className="form-control" 
              placeholder="Digite seu email" 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          
          <div className="form-group mb-4">
            <label>Senha</label>
            <input 
              type="password" 
              className="form-control" 
              placeholder="Sua senha" 
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary btn-block">
            Entrar no Sistema
          </button>
          
          <div className="text-center mt-3" style={{ fontSize: '0.875rem' }}>
            <Link to="/register">Ainda não tem conta? Cadastre-se.</Link>
          </div>
        </form>
      </div>
    </div>
  );
}
