import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import axiosInstance from '../api/axiosConfig';
import './Dashboard.css';

const DeliveryDashboard = () => {
  const { user } = useAuth();
  const [availableOrders, setAvailableOrders] = useState([]);
  const [myOrders, setMyOrders] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const availableRes = await axiosInstance.get('/orders/available-for-delivery');
        const myOrdersRes = await axiosInstance.get('/orders/my-deliveries');
        setAvailableOrders(availableRes.data);
        setMyOrders(myOrdersRes.data);
      } catch (err) {
        setError('Failed to load data');
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  const acceptOrder = async (orderId) => {
    try {
      await axiosInstance.post(`/orders/${orderId}/accept-delivery`);
      setAvailableOrders(availableOrders.filter(o => o.id !== orderId));
      // Refresh my orders
      const myOrdersRes = await axiosInstance.get('/orders/my-deliveries');
      setMyOrders(myOrdersRes.data);
    } catch (err) {
      setError('Failed to accept order');
    }
  };

  const updateDeliveryStatus = async (orderId, status) => {
    try {
      await axiosInstance.put(`/orders/${orderId}/delivery-status`, { status });
      setMyOrders(myOrders.map(o => o.id === orderId ? { ...o, status } : o));
    } catch (err) {
      setError('Failed to update status');
    }
  };

  if (isLoading) return <div className="dashboard">Loading...</div>;

  return (
    <div className="dashboard">
      <h1>Delivery Dashboard - {user?.name}</h1>
      {error && <div className="error-message">{error}</div>}

      <section className="dashboard-section">
        <h2>Available Orders</h2>
        <div className="orders-list">
          {availableOrders.length === 0 ? (
            <p>No available orders</p>
          ) : (
            availableOrders.map((order) => (
              <div key={order.id} className="order-item">
                <h4>Order #{order.id}</h4>
                <p>From: {order.restaurant?.name}</p>
                <p>To: {order.deliveryAddress}</p>
                <p>Distance: {order.distance} km</p>
                <button onClick={() => acceptOrder(order.id)}>Accept Order</button>
              </div>
            ))
          )}
        </div>
      </section>

      <section className="dashboard-section">
        <h2>My Deliveries</h2>
        <div className="orders-list">
          {myOrders.length === 0 ? (
            <p>No active deliveries</p>
          ) : (
            myOrders.map((order) => (
              <div key={order.id} className="order-item">
                <h4>Order #{order.id}</h4>
                <p>Status: {order.deliveryStatus}</p>
                <select 
                  value={order.deliveryStatus}
                  onChange={(e) => updateDeliveryStatus(order.id, e.target.value)}
                >
                  <option value="PICKED_UP">Picked Up</option>
                  <option value="IN_TRANSIT">In Transit</option>
                  <option value="DELIVERED">Delivered</option>
                </select>
              </div>
            ))
          )}
        </div>
      </section>
    </div>
  );
};

export default DeliveryDashboard;
