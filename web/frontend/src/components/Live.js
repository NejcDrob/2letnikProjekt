import React, { useEffect, useState } from 'react';

const Live = () => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      // Your code to fetch and set the messages goes here
      // For example:
      const response = await fetch('/rawdata');
      const data = await response.json();
      setMessages(data);
    };

    // Fetch data initially
    fetchData();

    // Refresh data every 1 second
    const interval = setInterval(fetchData, 1000);

    // Clean up the interval on component unmount
    return () => {
      clearInterval(interval);
    };
  }, []);

  const filteredMessages = messages.reduce((accumulator, message) => {
    const existingMessage = accumulator.find(
      (m) => m.user === message.user && m.date > message.date
    );

    if (!existingMessage) {
      accumulator.push(message);
    }

    return accumulator;
  }, []);

  return (
    <div className='.body-content'>
      {filteredMessages.length > 0 ? (
        filteredMessages.map((message, index) => (
          <div key={index} className="message-box">
            <h4>User: {message.user}</h4>
            <p>Location: {message.location}</p>
            <p>Speed: {message.speed}</p>
            <p>Accelerometer: {message.accelerometer}</p>
            <p>Score: {message.score}</p>
          </div>
        ))
      ) : (
        <p>No one is online</p>
      )}
    </div>
  );
};

export default Live;
