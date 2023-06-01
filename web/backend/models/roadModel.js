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
	},
	'postedBy' : {
		type: String,
		default:"prazno"
	}
});

var Road = mongoose.model('road', roadSchema);
module.exports = Road;
