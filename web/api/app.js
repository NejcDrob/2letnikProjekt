const express = require('express');
const http = require('http');
const socketIO = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIO(server);
let sensorData = { message: 'Some data from the Sensor' };

io.on('connection', (socket) => {
  console.log('A user connected');

  socket.on('sensorData', (data) => {
    console.log('Received SensorData:', data);
    sensorData = data;  
  });

 
  socket.emit('sensorData', sensorData);

 
  socket.on('disconnect', () => {
    console.log('A user disconnected');
  });
});

const port = 8080;
server.listen(port, () => {
  console.log(`Server started on port ${port}`);
});

app.get('/api/data', (req, res) => {
  
  res.json(sensorData);
});
