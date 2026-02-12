import React, { useState, useEffect } from 'react';
import './OrderTrackingPage.css';

const OrderTrackingPage = () => {
  const [orders, setOrders] = useState([
    {
      id: '12345',
      restaurant: 'Pizza Palace',
      items: ['Margherita Pizza', 'Caesar Salad'],
      total: 49.99,
      status: 'delivered',
      orderDate: '2026-02-10',
      deliveryDate: '2026-02-10',
      estimatedTime: '45 mins',
      address: '123 Main Street, New York',
      deliveryPerson: 'John Smith',
      deliveryPhone: '+1 (555) 123-4567',
      rating: null
    },
    {
      id: '12344',
      restaurant: 'Burger King',
      items: ['Whopper Meal', 'Chicken Nuggets'],
      total: 35.50,
      status: 'out_for_delivery',
      orderDate: '2026-02-12',
      deliveryDate: '2026-02-12',
      estimatedTime: '15 mins',
      address: '456 Oak Avenue, Brooklyn',
      deliveryPerson: 'Sarah Johnson',
      deliveryPhone: '+1 (555) 987-6543',
      rating: null
    },
    {
      id: '12343',
      restaurant: 'Thai House',
      items: ['Pad Thai', 'Green Curry', 'Spring Rolls'],
      total: 42.75,
      status: 'preparing',
      orderDate: '2026-02-12',
      estimatedTime: '30 mins',
      address: '789 Elm Street, Manhattan',
      deliveryPerson: null,
      rating: null
    },
    {
      id: '12342',
      restaurant: 'Sushi Express',
      items: ['Salmon Roll', 'California Roll', 'Miso Soup'],
      total: 58.99,
      status: 'confirmed',
      orderDate: '2026-02-12',
      estimatedTime: '40 mins',
      address: '321 Pine Road, Queens',
      deliveryPerson: null,
      rating: null
    }
  ]);

  const [expandedOrder, setExpandedOrder] = useState(null);
  const [ratingData, setRatingData] = useState({});

  const getStatusInfo = (status) => {
    const statusMap = {
      confirmed: { label: 'Confirmed', color: '#3498db', step: 1 },
      preparing: { label: 'Preparing', color: '#f39c12', step: 2 },
      ready_for_pickup: { label: 'Ready for Pickup', color: '#f39c12', step: 3 },
      out_for_delivery: { label: 'Out for Delivery', color: '#2ecc71', step: 3 },
      delivered: { label: 'Delivered', color: '#27ae60', step: 4 },
      cancelled: { label: 'Cancelled', color: '#e74c3c', step: 0 }
    };
    return statusMap[status] || { label: 'Unknown', color: '#95a5a6', step: 0 };
  };

  const toggleOrderDetails = (orderId) => {
    setExpandedOrder(expandedOrder === orderId ? null : orderId);
  };

  const handleRating = (orderId, rating) => {
    setRatingData(prev => ({
      ...prev,
      [orderId]: rating
    }));
    // Here you would typically send the rating to the backend
    console.log(`Order ${orderId} rated: ${rating} stars`);
  };

  const reorderItems = (order) => {
    console.log('Reordering items from:', order.restaurant);
    // Implement reorder logic
  };

  return (
    <div className="order-tracking-container">
      <div className="order-tracking-wrapper">
        <h1>Track Your Orders</h1>

        {orders.length > 0 ? (
          <div className="orders-list">
            {orders.map(order => {
              const statusInfo = getStatusInfo(order.status);
              const isExpanded = expandedOrder === order.id;
              const isDelivered = order.status === 'delivered';

              return (
                <div key={order.id} className="order-card">
                  {/* Order Header */}
                  <div 
                    className="order-header"
                    onClick={() => toggleOrderDetails(order.id)}
                  >
                    <div className="order-info">
                      <div className="order-number">Order #{order.id}</div>
                      <div className="order-restaurant">{order.restaurant}</div>
                      <div className="order-date">{order.orderDate}</div>
                    </div>

                    <div className="order-status">
                      <span 
                        className="status-badge"
                        style={{ backgroundColor: statusInfo.color }}
                      >
                        {statusInfo.label}
                      </span>
                    </div>

                    <div className="order-total">${order.total.toFixed(2)}</div>

                    <div className={`expand-icon ${isExpanded ? 'expanded' : ''}`}>
                      ▼
                    </div>
                  </div>

                  {/* Order Details */}
                  {isExpanded && (
                    <div className="order-details">
                      {/* Status Timeline */}
                      <div className="status-timeline">
                        <div className={`timeline-step completed`}>
                          <div className="step-dot">✓</div>
                          <div className="step-label">Order Placed</div>
                        </div>
                        <div className={`timeline-step ${statusInfo.step >= 2 ? 'completed' : ''}`}>
                          <div className="step-dot">{statusInfo.step >= 2 ? '✓' : ''}</div>
                          <div className="step-label">Preparing</div>
                        </div>
                        <div className={`timeline-step ${statusInfo.step >= 3 ? 'completed' : ''}`}>
                          <div className="step-dot">{statusInfo.step >= 3 ? '✓' : ''}</div>
                          <div className="step-label">Out for Delivery</div>
                        </div>
                        <div className={`timeline-step ${statusInfo.step >= 4 ? 'completed' : ''}`}>
                          <div className="step-dot">{statusInfo.step >= 4 ? '✓' : ''}</div>
                          <div className="step-label">Delivered</div>
                        </div>
                      </div>

                      {/* Items */}
                      <div className="details-section">
                        <h4>Items</h4>
                        <ul className="items-list">
                          {order.items.map((item, index) => (
                            <li key={index}>{item}</li>
                          ))}
                        </ul>
                      </div>

                      {/* Delivery Details */}
                      {(order.status === 'out_for_delivery' || isDelivered) && (
                        <div className="details-section">
                          <h4>Delivery Information</h4>
                          <div className="info-row">
                            <span className="label">Delivery Person:</span>
                            <span className="value">{order.deliveryPerson}</span>
                          </div>
                          <div className="info-row">
                            <span className="label">Phone:</span>
                            <span className="value">{order.deliveryPhone}</span>
                          </div>
                          <div className="info-row">
                            <span className="label">Delivery Address:</span>
                            <span className="value">{order.address}</span>
                          </div>
                        </div>
                      )}

                      {/* Estimated Time */}
                      {order.status !== 'delivered' && (
                        <div className="details-section">
                          <h4>Estimated Delivery</h4>
                          <div className="estimated-time">{order.estimatedTime}</div>
                        </div>
                      )}

                      {/* Rating Section */}
                      {isDelivered && (
                        <div className="details-section">
                          <h4>Rate Your Order</h4>
                          <div className="rating-section">
                            {ratingData[order.id] ? (
                              <div className="rating-submitted">
                                <p>Thanks for rating! You gave {ratingData[order.id]} star(s).</p>
                              </div>
                            ) : (
                              <div className="star-rating">
                                {[1, 2, 3, 4, 5].map(star => (
                                  <button
                                    key={star}
                                    className="star-btn"
                                    onClick={() => handleRating(order.id, star)}
                                  >
                                    ★
                                  </button>
                                ))}
                              </div>
                            )}
                          </div>
                        </div>
                      )}

                      {/* Action Buttons */}
                      <div className="order-actions">
                        {isDelivered && (
                          <button 
                            className="action-btn reorder-btn"
                            onClick={() => reorderItems(order)}
                          >
                            Reorder
                          </button>
                        )}
                        <button className="action-btn contact-btn">
                          Contact Support
                        </button>
                      </div>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        ) : (
          <div className="empty-state">
            <p>No orders found</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default OrderTrackingPage;
