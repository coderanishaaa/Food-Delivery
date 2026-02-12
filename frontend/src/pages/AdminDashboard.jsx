import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import axiosInstance from '../api/axiosConfig';
import './Dashboard.css';

const AdminDashboard = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalOrders: 0,
    totalRevenue: 0,
    activeDeliveries: 0,
  });
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const statsRes = await axiosInstance.get('/admin/statistics');
        const usersRes = await axiosInstance.get('/admin/users');
        setStats(statsRes.data);
        setUsers(usersRes.data);
      } catch (err) {
        setError('Failed to load data');
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  if (isLoading) return <div className="dashboard">Loading...</div>;

  return (
    <div className="dashboard">
      <h1>Admin Dashboard</h1>
      {error && <div className="error-message">{error}</div>}

      <section className="dashboard-section">
        <h2>Statistics</h2>
        <div className="stats-grid">
          <div className="stat-card">
            <h3>{stats.totalUsers}</h3>
            <p>Total Users</p>
          </div>
          <div className="stat-card">
            <h3>{stats.totalOrders}</h3>
            <p>Total Orders</p>
          </div>
          <div className="stat-card">
            <h3>₹{stats.totalRevenue}</h3>
            <p>Total Revenue</p>
          </div>
          <div className="stat-card">
            <h3>{stats.activeDeliveries}</h3>
            <p>Active Deliveries</p>
          </div>
        </div>
      </section>

      <section className="dashboard-section">
        <h2>Users Management</h2>
        <table className="users-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.name}</td>
                <td>{u.email}</td>
                <td>{u.role}</td>
                <td>{u.active ? 'Active' : 'Inactive'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
};

export default AdminDashboard;
