import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import PrivateRoute from './components/PrivateRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import CustomerDashboard from './pages/CustomerDashboard';
import RestaurantDashboard from './pages/RestaurantDashboard';
import DeliveryDashboard from './pages/DeliveryDashboard';
import AdminDashboard from './pages/AdminDashboard';
import './App.css';

function AppRoutes() {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <h1>Loading...</h1>
      </div>
    );
  }

  return (
    <>
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          <Route
            path="/customer-dashboard"
            element={
              <PrivateRoute allowedRoles={['CUSTOMER']}>
                <CustomerDashboard />
              </PrivateRoute>
            }
          />
          
          <Route
            path="/restaurant-dashboard"
            element={
              <PrivateRoute allowedRoles={['RESTAURANT']}>
                <RestaurantDashboard />
              </PrivateRoute>
            }
          />
          
          <Route
            path="/delivery-dashboard"
            element={
              <PrivateRoute allowedRoles={['DELIVERY']}>
                <DeliveryDashboard />
              </PrivateRoute>
            }
          />
          
          <Route
            path="/admin-dashboard"
            element={
              <PrivateRoute allowedRoles={['ADMIN']}>
                <AdminDashboard />
              </PrivateRoute>
            }
          />
          
          <Route path="/" element={<Navigate to={isAuthenticated ? "/customer-dashboard" : "/login"} replace />} />
        </Routes>
      </main>
    </>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  );
}

export default App;
