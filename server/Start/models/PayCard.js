'use strict';
const mongoose = require('./MongooDB');
const payCardSchema = mongoose.Schema({

	 transId: String,
        transRef:String,
        serial: String,
        status: String,
        amount: String,
        description: String

});
module.exports = mongoose.model('payCard', payCardSchema);

