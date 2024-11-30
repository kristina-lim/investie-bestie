import logo from './logo.svg';
import 'bootstrap/dist/css/bootstrap.min.css';
import StockRow from './components/StockRow.js';

function App() {
  return (
    <div className="App">
      <div className="container">
        <table className="table mt-5">
          <thead>
            <tr>
              <th>Ticker</th>
              <th>Price</th>
              <th>Date</th>
              <th>Time</th>
            </tr>
          </thead>
          {/* top down */}
          <tbody>
            {/* StockRow component and ticker attribute */}
            <StockRow ticker="aapl" />
            <StockRow ticker="goog" />
            <StockRow ticker="msft" />
            <StockRow ticker="tsla" />
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default App;
