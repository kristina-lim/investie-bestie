import React from 'react';
import { useNavigate } from 'react-router-dom';

function Header({ userName, onLogout }) {
  const navigate = useNavigate();

  // function handleLogout() {
  //   onLogout(); // Clear the user state in App
  //   navigate('/login'); // Redirect to login page
  // }

  return (
    <div className="d-flex justify-content-between align-items-center">
      <h2>Welcome, {userName}!</h2>
      <button className="btn btn-danger" onClick={onLogout}>
        Logout
      </button>
    </div>
  );
}

export default Header;
