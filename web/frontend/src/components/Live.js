import React, {useEffect, useState} from 'react';

function Live() {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const response = await fetch('http://localhost:3002/messages');  
        const data = await response.json();
        setMessages(data);
      } catch (error) {
        console.error('Error fetching messages:', error);
      }
    };

    fetchMessages();
    const interval = setInterval(fetchMessages, 1000); 

    return () => {
      clearInterval(interval);  
    };
  }, []);

  return (
    <div className='.body-content'>
      {messages.length > 0 ? (
        messages.map((message, index) => (
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
