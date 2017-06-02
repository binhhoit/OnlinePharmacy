const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const userSchema = mongoose.Schema({

    name: String,
    gmail: String,
    number: String,

});

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/angularTest');


var dbMongo = mongoose.connection;
dbMongo.on('err',console.error.bind(console,'connect  err'));
dbMongo.once('open',function () {
	console.log('console connect');
});

module.exports = mongoose.model('user', userSchema);        