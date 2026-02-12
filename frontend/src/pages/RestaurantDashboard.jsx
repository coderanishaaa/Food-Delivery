import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import axiosInstance from '../api/axiosConfig';
import './Dashboard.css';

const RestaurantDashboard = () => {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [menu, setMenu] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const ordersRes = await axiosInstance.get('/orders/restaurant-orders');
        const menuRes = await axiosInstance.get('/menu/my-menu');
        setOrders(ordersRes.data);
        setMenu(menuRes.data);
      } catch (err) {
        setError('Failed to load data');
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  const updateOrderStatus = async (orderId, status) => {
    try {
      await axiosInstance.put(`/orders/${orderId}`, { status });
      setOrders(orders.map(o => o.id === orderId ? { ...o, status } : o));
    } catch (err) {
      setError('Failed to update order');
    }
  };

  if (isLoading) return <div className="dashboard">Loading...</div>;

  return (
    <div className="dashboard">
      <h1>Restaurant Dashboard - {user?.name}</h1>
      {error && <div className="error-message">{error}</div>}

      <section className="dashboard-section">
        <h2>Pending Orders</h2>
        <div className="orders-list">
          {orders.filter(o => o.status !== 'DELIVERED').length === 0 ? (
            <p>No pending orders</p>
          ) : (
            orders.map((order) => (
              <div key={order.id} className="order-item">
                <h4>Order #{order.id}</h4>
                <p>Status: {order.status}</p>
                <p>Items: {order.items?.length || 0}</p>
                <select 
                  value={order.status}
                  onChange={(e) => updateOrderStatus(order.id, e.target.value)}
                >
                  <option value="PENDING">Pending</option>
                  <option value="CONFIRMED">Confirmed</option>
                  <option value="PREPARING">Preparing</option>
                  <option value="READY">Ready</option>
                </select>
              </div>
            ))
          )}
        </div>
      </section>

      <section className="dashboard-section">
        <h2>Menu Items</h2>
        <div className="menu-grid">
          {menu.map((item) => (
            <div key={item.id} className="menu-item">
              <h4>{item.name}</h4>
              <p>{item.description}</p>
              <p>Price: ₹{item.price}</p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};

export default RestaurantDashboard;
