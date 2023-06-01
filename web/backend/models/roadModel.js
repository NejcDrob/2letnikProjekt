var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var roadSchema = new Schema({
	'xStart': Number,
	'yStart': Number,
	'xEnd': Number,
	'yEnd': Number,
	'state': {
		type: Number,
		default: 0
	}
});

var Road = mongoose.model('road', roadSchema);
module.exports = Road;
