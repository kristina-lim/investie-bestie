import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import StockRow from '../../components/StockRow/StockRow.js';
import LoginForm from '../LoginForm/LoginForm.js';
import Header from '../../components/Header/Header.js';

function App() {
  // User is the currently logged in user
  const [user, setUser] = useState(null);
  // Title is just a sample value entered in form
  const [title, setTitle] = useState('');
  const navigate = useNavigate();

  // When the user submits form...don't really do anything in this sample
  async function handleSubmit(event) {
    event.tpreventDefault();

    // Do whatever on submit
    console.log("submitted");
  };

  // This will be called by the LoginForm
  function handleLogin(user) {
    setUser(user);
    navigate('/stocks');
  }

  function handleLogout() {
    console.log("Logging out...");
    setUser(null);
    navigate('/login');
  }

  // We have a subcomponent LoginForm and we pass it the function to call when login happens
  return (
    <>
      <Routes>
        <Route path="/login" element={<LoginForm LoginEvent={handleLogin} />} />
        <Route
          path="/stocks"
          element={
            user ? (
              <>
                <Header userName={user.displayName} onLogout={handleLogout} />
                <div className="container">
                  <div className="col-md-5 mt-5">
                    <div className="card">
                      <ul className="list-group list-group-flush">
                        <StockRow ticker="aapl" />
                        <StockRow ticker="aabv" />
                        <StockRow ticker="adbe" />
                        <StockRow ticker='aig' />
                        <StockRow ticker='amd' />
                        <StockRow ticker='amzn' />
                        <StockRow ticker='axp' />
                        <StockRow ticker='ba' />
                        <StockRow ticker='bac' />
                        <StockRow ticker='cat' />
                        <StockRow ticker='tsla' />
                      </ul>
                    </div>
                  </div>
                </div>
              </>
            ) : (
              <Navigate to="/login" replace /> // Redirect if not logged in
            )
          }
        />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </>
  );
}

export default App;
