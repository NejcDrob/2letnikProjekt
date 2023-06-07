const express = require('express');
const bodyParser = require('body-parser');

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
app.listen(3002, () => {
  console.log('Server is running on port 3002');
});
