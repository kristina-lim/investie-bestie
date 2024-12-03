import React, { useState, useEffect } from "react";
import axios from "axios";
import "./StockDetails.css";

function StockDetails({ stock }) {
  const [beta, setBeta] = useState(null);
  const [summary, setSummary] = useState("");
  const [chartUrl, setChartUrl] = useState("");

  useEffect(() => {
    if (!stock) return;

    // Fetch Beta
    axios
      .get(`https://investiebestie.wl.r.appspot.com/api/stocks/${stock}/beta`)
      .then((response) => {
        setBeta(response.data);
      })
      .catch((error) => {
        console.error("Error fetching beta:", error);
      });

    // Fetch AI Summary
    axios
      .get(`https://investiebestie.wl.r.appspot.com/api/stocks/${stock}/summary`)
      .then((response) => {
        setSummary(response.data);
      })
      .catch((error) => {
        console.error("Error fetching summary:", error);
      });

    // Set Chart URL
    setChartUrl(`https://investiebestie.wl.r.appspot.com/api/stocks/${stock}/chart`);
  }, [stock]);

  return (
    <div className="stock-details">
      <h2>Analysis for {stock}</h2>

      {beta !== null && (
        <div className="analysis-section">
          <h3>Beta Rating</h3>
          <p className="beta-rating">{beta.toFixed(2)}</p>
        </div>
      )}

      {summary && (
        <div className="analysis-section">
          <h3>AI Summary</h3>
          <div className="ai-summary">
            {summary.split("\n").map((line, index) =>
              line.startsWith("-") ? (
                <li key={index} className="ai-bullet-point">
                  {line.slice(1).trim()}
                </li>
              ) : (
                <p key={index}>{line}</p>
              )
            )}
          </div>
        </div>
      )}

      {chartUrl && (
        <div className="analysis-section">
          <h3>Stock Chart</h3>
          <img src={chartUrl} alt={`Chart for ${stock}`} className="stock-chart" />
        </div>
      )}
    </div>
  );
}

export default StockDetails;