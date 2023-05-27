var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var routeSchema = new Schema({
	'name' : String,
    'score' : Int,
    'date': {
        type: Date,
        default: Date.now
    },
    'time' : Time,
    'distance' : Int,
	'postedBy' : {
	 	type: Schema.Types.ObjectId,
	 	ref: 'user'
	}
});

module.exports = mongoose.model('route', routeSchema);