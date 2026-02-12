import React, { useState, useEffect } from 'react';
import './NotificationPage.css';

const NotificationPage = () => {
  const [notifications, setNotifications] = useState([
    {
      id: 1,
      type: 'order',
      title: 'Order Confirmed',
      message: 'Your order #12345 has been confirmed by the restaurant.',
      timestamp: '2 minutes ago',
      read: false,
      icon: '✓'
    },
    {
      id: 2,
      type: 'delivery',
      title: 'Order is Being Prepared',
      message: 'Your food is being prepared. Your delivery will start soon.',
      timestamp: '15 minutes ago',
      read: false,
      icon: '👨‍🍳'
    },
    {
      id: 3,
      type: 'delivery',
      title: 'Out for Delivery',
      message: 'Your order is out for delivery. Your delivery partner will arrive in 15 minutes.',
      timestamp: '30 minutes ago',
      read: true,
      icon: '🚗'
    },
    {
      id: 4,
      type: 'promo',
      title: 'Special Offer',
      message: 'Get 20% off on your next order with code SAVE20.',
      timestamp: '1 hour ago',
      read: true,
      icon: '🎉'
    },
    {
      id: 5,
      type: 'payment',
      title: 'Payment Successful',
      message: 'Your payment of $49.99 has been processed successfully.',
      timestamp: '2 hours ago',
      read: true,
      icon: '💳'
    },
    {
      id: 6,
      type: 'order',
      title: 'Order Delivered',
      message: 'Your previous order has been successfully delivered.',
      timestamp: '1 day ago',
      read: true,
      icon: '📦'
    }
  ]);

  const [filter, setFilter] = useState('all');

  const handleMarkAsRead = (id) => {
    setNotifications(notifications.map(notif =>
      notif.id === id ? { ...notif, read: true } : notif
    ));
  };

  const handleMarkAllAsRead = () => {
    setNotifications(notifications.map(notif => ({ ...notif, read: true })));
  };

  const handleDeleteNotification = (id) => {
    setNotifications(notifications.filter(notif => notif.id !== id));
  };

  const handleClearAll = () => {
    setNotifications([]);
  };

  const filteredNotifications = filter === 'all' 
    ? notifications 
    : notifications.filter(notif => notif.type === filter);

  const unreadCount = notifications.filter(notif => !notif.read).length;

  const getNotificationColor = (type) => {
    switch(type) {
      case 'order': return '#2ecc71';
      case 'delivery': return '#3498db';
      case 'promo': return '#f39c12';
      case 'payment': return '#9b59b6';
      default: return '#95a5a6';
    }
  };

  return (
    <div className="notification-container">
      <div className="notification-wrapper">
        <div className="notification-header">
          <h1>Notifications</h1>
          <div className="header-actions">
            {unreadCount > 0 && (
              <button className="unread-badge" onClick={handleMarkAllAsRead}>
                Mark all as read ({unreadCount})
              </button>
            )}
            {notifications.length > 0 && (
              <button className="clear-all-btn" onClick={handleClearAll}>
                Clear All
              </button>
            )}
          </div>
        </div>

        <div className="notification-filters">
          <button 
            className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
            onClick={() => setFilter('all')}
          >
            All
          </button>
          <button 
            className={`filter-btn ${filter === 'order' ? 'active' : ''}`}
            onClick={() => setFilter('order')}
          >
            Orders
          </button>
          <button 
            className={`filter-btn ${filter === 'delivery' ? 'active' : ''}`}
            onClick={() => setFilter('delivery')}
          >
            Delivery
          </button>
          <button 
            className={`filter-btn ${filter === 'payment' ? 'active' : ''}`}
            onClick={() => setFilter('payment')}
          >
            Payments
          </button>
          <button 
            className={`filter-btn ${filter === 'promo' ? 'active' : ''}`}
            onClick={() => setFilter('promo')}
          >
            Promotions
          </button>
        </div>

        <div className="notifications-list">
          {filteredNotifications.length > 0 ? (
            filteredNotifications.map(notification => (
              <div 
                key={notification.id} 
                className={`notification-item ${notification.read ? 'read' : 'unread'}`}
              >
                <div 
                  className="notification-icon"
                  style={{ backgroundColor: getNotificationColor(notification.type) }}
                >
                  {notification.icon}
                </div>
                <div className="notification-content">
                  <h3 className="notification-title">{notification.title}</h3>
                  <p className="notification-message">{notification.message}</p>
                  <span className="notification-time">{notification.timestamp}</span>
                </div>
                <div className="notification-actions">
                  {!notification.read && (
                    <button 
                      className="read-btn"
                      onClick={() => handleMarkAsRead(notification.id)}
                      title="Mark as read"
                    >
                      ●
                    </button>
                  )}
                  <button 
                    className="delete-btn"
                    onClick={() => handleDeleteNotification(notification.id)}
                    title="Delete notification"
                  >
                    ✕
                  </button>
                </div>
              </div>
            ))
          ) : (
            <div className="empty-state">
              <p>No notifications</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default NotificationPage;
