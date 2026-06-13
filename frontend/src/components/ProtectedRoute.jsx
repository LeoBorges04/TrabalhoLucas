import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

export function ProtectedRoute({ children, allowedRoles }) {
  const { authenticated, userRole, loading } = useContext(AuthContext);

  if (loading) {
    return <div>Carregando...</div>;
  }

  if (!authenticated) {
    return <Navigate to="/" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(userRole)) {
    return <Navigate to="/" replace />;
  }

  return children;
}
