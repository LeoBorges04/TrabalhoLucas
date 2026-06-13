import { createContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import api from '../services/api';

export const AuthContext = createContext({});

export function AuthProvider({ children }) {
  const [userRole, setUserRole] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const recoveredToken = localStorage.getItem('medcore-token');

    if (recoveredToken) {
      try {
        const decoded = jwtDecode(recoveredToken);
        const roleClaim = decoded.role || decoded.AUTHORITIES; // Need to map correctly depending on Spring implementation
        // Se Spring Security coloca authorities como array:
        let role = null;
        if (Array.isArray(decoded.roles)) {
            role = decoded.roles[0];
        } else if (decoded.role) {
            role = decoded.role;
        } else if (Array.isArray(decoded.authorities)) {
            role = decoded.authorities[0];
        } else {
            // Decodificação basica sem campos customizados requer que saibamos o claim.
            // Para simplificar, o login vai retornar as infos necessárias ou leremos do token.
        }

        // Simplesmente vamos armazenar o sub e pegar o role do localStorage se não vier no token JWT padrão do Auth0 (sem claims custom).
        const storedRole = localStorage.getItem('medcore-role');
        setUserRole(storedRole);
        setToken(recoveredToken);
      } catch (err) {
        logout();
      }
    }
    setLoading(false);
  }, []);

  const login = async (email, senha) => {
    const response = await api.post('/api/auth/login', { email, senha });
    const { token, role } = response.data;
    
    localStorage.setItem('medcore-token', token);
    localStorage.setItem('medcore-role', role); 
    
    setToken(token);
    setUserRole(role);
    
    return role;
  };

  const logout = () => {
    localStorage.removeItem('medcore-token');
    localStorage.removeItem('medcore-role');
    setToken(null);
    setUserRole(null);
  };

  return (
    <AuthContext.Provider value={{ authenticated: !!token, userRole, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}
