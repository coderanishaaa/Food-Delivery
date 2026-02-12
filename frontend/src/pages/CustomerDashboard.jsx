import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import axiosInstance from '../api/axiosConfig';
import './Dashboard.css';

const CustomerDashboard = () => {
  const { user } = useAuth();
  const [restaurants, setRestaurants] = useState([]);
  const [orders, setOrders] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const restaurantsRes = await axiosInstance.get('/restaurants');
        const ordersRes = await axiosInstance.get('/orders/my-orders');
        setRestaurants(restaurantsRes.data);
        setOrders(ordersRes.data);
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
      <h1>Welcome, {user?.name}!</h1>
      {error && <div className="error-message">{error}</div>}

      <section className="dashboard-section">
        <h2>Available Restaurants</h2>
        <div className="restaurants-grid">
          {restaurants.map((restaurant) => (
            <div key={restaurant.id} className="restaurant-card">
              <h3>{restaurant.name}</h3>
              <p>{restaurant.cuisine}</p>
              <p>Rating: {restaurant.rating}/5</p>
              <button>View Menu</button>
            </div>
          ))}
        </div>
      </section>

      <section className="dashboard-section">
        <h2>Your Orders</h2>
        <div className="orders-list">
          {orders.length === 0 ? (
            <p>No orders yet</p>
          ) : (
            orders.map((order) => (
              <div key={order.id} className="order-item">
                <h4>Order #{order.id}</h4>
                <p>Status: {order.status}</p>
                <p>Total: ₹{order.totalAmount}</p>
                <p>Ordered on: {new Date(order.createdAt).toLocaleDateString()}</p>
              </div>
            ))
          )}
        </div>
      </section>
    </div>
  );
};

export default CustomerDashboard;
