'connect'

const mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/OnlinePharmacy');
const Schema = mongoose.Schema;
mongoose.Promise = global.Promise;

module.exports = mongoose;