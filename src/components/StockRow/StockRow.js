import React, { Component } from "react";
import axios from "axios";

const changeHighStyle = {
  color: '#4caf50',
  fontSize: '0.8rem',
  marginLeft: 5
}

const changeLowStyle = {
  color: '#ff0000',
  fontSize: '0.8rem',
  marginLeft: 5
}

class StockRow extends Component {

  // Call in props
  constructor(props) {
    super(props)
    // Object
    this.state = {
      data: {
        price: [],
        volume: null,
        high: null,
        low: null
      }
    }
  }

  componentDidMount() {
    // Fetch beta data
    // URL FOR NOW, CHANGE LATER CANT SIGN OUT BECAUSE OF URL
    axios.get(`http://localhost:3000/`)
      .then(response => {
        this.setState({
          data: {
            ...this.state.data,
            beta: response.data
          }
        });
      })
      .catch(error => {
        console.error("There was an error fetching the data.", error);
      });
  }

  render() {
    const { price, volume, high, low } = this.state.data;
    return (
      <li className='list-group-item'>
        <b>{this.props.ticker.toUpperCase()}</b><br />
        <span>Last Price: ${price.length > 0 ? price[price.length - 1].toFixed(2) : "Loading..."}</span><br />
        <span>Volume: {volume || "Loading..."}</span><br />
        <span className='highChange' style={changeHighStyle}>High: ${high || "Loading..."}</span><br />
        <span className='lowChange' style={changeLowStyle}>Low: ${low || "Loading..."}</span>
      </li>
    )
  }
}

export default StockRow;