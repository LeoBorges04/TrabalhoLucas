import { Routes, Route } from 'react-router-dom';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { AdminDashboard } from './pages/AdminDashboard';
import { MedicoDashboard } from './pages/MedicoDashboard';
import { PacienteDashboard } from './pages/PacienteDashboard';
import { ProtectedRoute } from './components/ProtectedRoute';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route 
        path="/admin/*" 
        element={
          <ProtectedRoute allowedRoles={['ADMIN']}>
            <AdminDashboard />
          </ProtectedRoute>
        } 
      />
      <Route 
        path="/medico/*" 
        element={
          <ProtectedRoute allowedRoles={['MEDICO']}>
            <MedicoDashboard />
          </ProtectedRoute>
        } 
      />
      <Route 
        path="/paciente/*" 
        element={
          <ProtectedRoute allowedRoles={['PACIENTE']}>
            <PacienteDashboard />
          </ProtectedRoute>
        } 
      />
    </Routes>
  );
}

export default App;
