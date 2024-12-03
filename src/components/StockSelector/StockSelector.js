import React, { useState, useEffect } from "react";
import axios from "axios";
import "./StockSelector.css"; // Import the CSS file

function StockSelector({ onStockSelect }) {
  const [stocks, setStocks] = useState([]);
  const [selectedStock, setSelectedStock] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch stock list on component mount
  useEffect(() => {
    axios
      .get("https://investiebestie.wl.r.appspot.com/api/stocks/list")
      .then((response) => {
        setStocks(response.data.map(stock => stock.replace(".csv", ""))); // Populate dropdown
        setIsLoading(false); // Data fetched, stop loading
      })
      .catch((error) => {
        console.error("Error fetching stock list:", error);
        setError("Failed to fetch stock list. Please try again later.");
        setIsLoading(false); // Stop loading even on error
      });
  }, []);

  const handleSelectChange = (event) => {
    setSelectedStock(event.target.value);
    onStockSelect(event.target.value); // Notify parent of stock selection
  };

  return (
    <div className="stock-selector-container">
      <h2>Select a Stock</h2>

      {/* Display loader, error, or dropdown */}
      {isLoading ? (
        <p className="stock-selector-info">Loading stock list...</p>
      ) : error ? (
        <p className="stock-selector-error">{error}</p>
      ) : (
        <>
          <select
            className="stock-selector-dropdown"
            onChange={handleSelectChange}
            value={selectedStock}
          >
            <option value="" disabled>
              -- Select a Stock --
            </option>
            {stocks.map((stock) => (
              <option key={stock} value={stock}>
                {stock}
              </option>
            ))}
          </select>

          {selectedStock && (
            <button
              className="receive-analysis-button"
              onClick={() => onStockSelect(selectedStock)}
            >
              Receive Analysis
            </button>
          )}
        </>
      )}

      <div className="stock-selector-info">
        {!isLoading && !error && "Select a stock and click 'Receive Analysis' to view details."}
      </div>
    </div>
  );
}

export default StockSelector;