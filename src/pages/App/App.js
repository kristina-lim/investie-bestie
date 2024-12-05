import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import StockSelector from "../../components/StockSelector/StockSelector.js";
import StockDetails from "../StockDetails/StockDetails.js";
import LoginForm from "../LoginForm/LoginForm";
import { getAuth, signOut } from "firebase/auth"; // Import Firebase Auth

function App() {
  const [selectedStock, setSelectedStock] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Track login state
  const [userName, setUserName] = useState(""); // Store user's name

  const handleLogout = () => {
    const auth = getAuth(); // Initialize Firebase Auth
    signOut(auth)
      .then(() => {
        console.log("User signed out");
        setIsLoggedIn(false); // Reset login state
        setSelectedStock(""); // Clear any selected stock
        setUserName(""); // Clear user name
      })
      .catch((error) => {
        console.error("Error during logout:", error);
      });
  };

  return (
    <Router>
      <Routes>
        {/* Default route redirects to login if not logged in */}
        <Route
          path="/"
          element={
            isLoggedIn ? (
              <Navigate to="/stocks" />
            ) : (
              <LoginForm
                LoginEvent={(user) => {
                  setIsLoggedIn(true);
                  setUserName(user.displayName); // Set user name
                }}
              />
            )
          }
        />

        {/* Stocks page */}
        <Route
          path="/stocks"
          element={
            isLoggedIn ? (
              <div>
                <header
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    padding: "1rem 2rem",
                    backgroundColor: "#f0f8ff",
                    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
                    borderRadius: "0 0 20px 20px",
                    position: "relative",
                    minHeight: "70px",
                  }}
                >
                  <h1
                    style={{
                      margin: 0,
                      fontSize: "1.5rem",
                      color: "#333",
                      textOverflow: "ellipsis",
                      whiteSpace: "nowrap",
                      overflow: "hidden",
                    }}
                  >
                    Welcome, {userName}!
                  </h1>
                  <button
                    onClick={handleLogout}
                    style={{
                      marginRight: "5rem",
                      padding: "0.5rem 1rem",
                      backgroundColor: "#007BFF",
                      color: "white",
                      border: "none",
                      borderRadius: "5px",
                      cursor: "pointer",
                      transition: "background-color 0.3s ease",
                      marginLeft: "1rem", // Move the button closer to the header content
                      position: "static", // Ensure the button stays inline
                    }}
                    onMouseEnter={(e) =>
                      (e.target.style.backgroundColor = "#0056b3") // Hover effect
                    }
                    onMouseLeave={(e) =>
                      (e.target.style.backgroundColor = "#007BFF") // Revert hover effect
                    }
                  >
                    Logout
                  </button>
                </header>
                <StockSelector onStockSelect={setSelectedStock} />
                {selectedStock && <StockDetails stock={selectedStock} />}
              </div>
            ) : (
              <Navigate to="/" />
            )
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
