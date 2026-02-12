import React, { useState } from 'react';
import './PaymentPage.css';

const PaymentPage = () => {
  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState('credit-card');
  const [orderTotal] = useState(49.99);
  const [deliveryFee] = useState(5.00);
  const [cardDetails, setCardDetails] = useState({
    cardName: '',
    cardNumber: '',
    expiryDate: '',
    cvv: ''
  });
  const [address, setAddress] = useState({
    street: '',
    city: '',
    zipCode: '',
    phone: ''
  });
  const [loading, setLoading] = useState(false);
  const [paymentStatus, setPaymentStatus] = useState(null);

  const handleCardChange = (e) => {
    const { name, value } = e.target;
    setCardDetails(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setAddress(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handlePayment = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      setPaymentStatus({
        success: true,
        message: 'Payment successful! Your order has been placed.'
      });
      
      // Reset form after success
      setTimeout(() => {
        setCardDetails({ cardName: '', cardNumber: '', expiryDate: '', cvv: '' });
        setAddress({ street: '', city: '', zipCode: '', phone: '' });
        setPaymentStatus(null);
      }, 3000);
    } catch (error) {
      setPaymentStatus({
        success: false,
        message: 'Payment failed. Please try again.'
      });
    } finally {
      setLoading(false);
    }
  };

  const total = orderTotal + deliveryFee;

  return (
    <div className="payment-container">
      <div className="payment-wrapper">
        <h1>Checkout</h1>
        
        <div className="payment-content">
          {/* Order Summary */}
          <div className="order-summary">
            <h2>Order Summary</h2>
            <div className="summary-item">
              <span>Subtotal:</span>
              <span>${orderTotal.toFixed(2)}</span>
            </div>
            <div className="summary-item">
              <span>Delivery Fee:</span>
              <span>${deliveryFee.toFixed(2)}</span>
            </div>
            <div className="summary-total">
              <span>Total:</span>
              <span>${total.toFixed(2)}</span>
            </div>
          </div>

          {/* Payment Form */}
          <form className="payment-form" onSubmit={handlePayment}>
            {/* Delivery Address */}
            <div className="form-section">
              <h2>Delivery Address</h2>
              <div className="form-group">
                <label htmlFor="street">Street Address</label>
                <input
                  type="text"
                  id="street"
                  name="street"
                  value={address.street}
                  onChange={handleAddressChange}
                  placeholder="123 Main Street"
                  required
                />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="city">City</label>
                  <input
                    type="text"
                    id="city"
                    name="city"
                    value={address.city}
                    onChange={handleAddressChange}
                    placeholder="New York"
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="zipCode">ZIP Code</label>
                  <input
                    type="text"
                    id="zipCode"
                    name="zipCode"
                    value={address.zipCode}
                    onChange={handleAddressChange}
                    placeholder="10001"
                    required
                  />
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="phone">Phone Number</label>
                <input
                  type="tel"
                  id="phone"
                  name="phone"
                  value={address.phone}
                  onChange={handleAddressChange}
                  placeholder="+1 (555) 000-0000"
                  required
                />
              </div>
            </div>

            {/* Payment Method Selection */}
            <div className="form-section">
              <h2>Payment Method</h2>
              <div className="payment-methods">
                <label className="payment-method">
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="credit-card"
                    checked={selectedPaymentMethod === 'credit-card'}
                    onChange={(e) => setSelectedPaymentMethod(e.target.value)}
                  />
                  <span>Credit Card</span>
                </label>
                <label className="payment-method">
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="debit-card"
                    checked={selectedPaymentMethod === 'debit-card'}
                    onChange={(e) => setSelectedPaymentMethod(e.target.value)}
                  />
                  <span>Debit Card</span>
                </label>
                <label className="payment-method">
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="paypal"
                    checked={selectedPaymentMethod === 'paypal'}
                    onChange={(e) => setSelectedPaymentMethod(e.target.value)}
                  />
                  <span>PayPal</span>
                </label>
                <label className="payment-method">
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="wallet"
                    checked={selectedPaymentMethod === 'wallet'}
                    onChange={(e) => setSelectedPaymentMethod(e.target.value)}
                  />
                  <span>Digital Wallet</span>
                </label>
              </div>
            </div>

            {/* Card Details - Show only for credit/debit card */}
            {(selectedPaymentMethod === 'credit-card' || selectedPaymentMethod === 'debit-card') && (
              <div className="form-section">
                <h2>Card Details</h2>
                <div className="form-group">
                  <label htmlFor="cardName">Cardholder Name</label>
                  <input
                    type="text"
                    id="cardName"
                    name="cardName"
                    value={cardDetails.cardName}
                    onChange={handleCardChange}
                    placeholder="John Doe"
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="cardNumber">Card Number</label>
                  <input
                    type="text"
                    id="cardNumber"
                    name="cardNumber"
                    value={cardDetails.cardNumber}
                    onChange={handleCardChange}
                    placeholder="1234 5678 9012 3456"
                    maxLength="19"
                    required
                  />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="expiryDate">Expiry Date</label>
                    <input
                      type="text"
                      id="expiryDate"
                      name="expiryDate"
                      value={cardDetails.expiryDate}
                      onChange={handleCardChange}
                      placeholder="MM/YY"
                      maxLength="5"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="cvv">CVV</label>
                    <input
                      type="text"
                      id="cvv"
                      name="cvv"
                      value={cardDetails.cvv}
                      onChange={handleCardChange}
                      placeholder="123"
                      maxLength="4"
                      required
                    />
                  </div>
                </div>
              </div>
            )}

            {paymentStatus && (
              <div className={`payment-status ${paymentStatus.success ? 'success' : 'error'}`}>
                {paymentStatus.message}
              </div>
            )}

            <button 
              type="submit" 
              className="submit-btn" 
              disabled={loading}
            >
              {loading ? 'Processing...' : `Pay $${total.toFixed(2)}`}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PaymentPage;
