'use strict';

const mongoose = require('./MongooDB');
const drugStoreSchema = mongoose.Schema({

	name: String,
	address: String,
	numberphone: String,
	datefound: String,
	type: String,

});
module.exports = mongoose.model('drugstore', drugStoreSchema);

