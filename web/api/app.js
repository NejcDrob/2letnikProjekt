const express = require('express');
const bodyParser = require('body-parser');
const os = require('os');

const app = express();
app.use(bodyParser.json());

const messageList = [];  
const clearMessages = () => {
  messageList = [];
};
setInterval(clearMessages, 5 * 60 * 1000);

app.post('/rawData', (req, res) => {
  const sensorData = req.body;
  
  const { location, speed, accelerometer, user, score } = sensorData;

  
  const existingIndex = messageList.findIndex((message) => message.user == user);
  if (existingIndex !== -1) {
   
    messageList.splice(existingIndex, 1);
  }
 
  const message = { location, speed, accelerometer, user, score };
  messageList.push(message);
 
  res.status(200).send('Data received successfully');
});
app.get('/messages', (req, res) => {
  res.json(messageList);
});
const server = app.listen(3002, "164.8.162.186" ,() => {
  const host = server.address().address;
  const port = server.address().port;
  console.log('Server is running on', host, 'port', port);
  console.log('Server is running on port 3002');
});
