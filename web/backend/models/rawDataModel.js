var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var rawDataSchema= new Schema({
    'location': String,
    'speed' : String,
    'accelerometer' : String,
    'user' : String,
    'score' : [Number],
    'date': Date
});

var rawData = mongoose.model('rawData', rawDataSchema);
module.exports = rawData;

