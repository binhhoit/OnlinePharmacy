const mongoose = require('../models/MongooDB');

const prescriptionSchema = mongoose.Schema({

	id: String,
	email: String,
	addressReceive: String,
	number_buy: String,
    status:String,
    photo:String,	

});

module.exports = mongoose.model('prescriptionPhoto', prescriptionSchema);