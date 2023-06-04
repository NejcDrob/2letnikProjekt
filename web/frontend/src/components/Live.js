import React, {useEffect, useState} from 'react';

function Live() {
  const [messages, setMessages] = useState({});

  useEffect(() => {
    const fetchLatestMessages = async () => {
      const response = await fetch('http://localhost:8080/latest-messages');
      const data = await response.json();
      setMessages(data);
    };

    const intervalId = setInterval(fetchLatestMessages, 1000);

    return () => {
      clearInterval(intervalId);
    };
  }, []);

  return (
    <div>
      {Object.keys(messages).map(user => (
        <div key={user} style={{ border: "1px solid black", margin: "10px", padding: "10px" }}>
          <h2>{user}</h2>
          {messages[user].map((message, index) => (
            <p key={index}>{message.text}</p>
          ))}
        </div>
      ))}
    </div>
  );
};
export default Live;
