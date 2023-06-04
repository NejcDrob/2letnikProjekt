var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var rawDataSchema= new Schema({
    'speed': [Number],
    'location' : [Number],
    'Xpos' : [Number],
    'Ypos' : [Number],
    'gyroX' : [Number],
    'gyroY' : [Number],
    'gyroZ' : [Number],
    'postedBy' : {
		type: String,
		default:"prazno"
	}
});
var rawData = mongoose.model('rawData', rawDataSchema);
module.exports = rawData;

