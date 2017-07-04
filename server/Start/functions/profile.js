'use strict';

const user = require('../models/User');

exports.getProfile = (id) => 
	
	new Promise((resolve,reject) => {

		user.find({ _id: id }, { name: 1, email: 1, created_at: 1, _id: 0 })

		.then(users => resolve(users[0]))

		.catch(err => reject({ status: 500, message: 'Internal Server Error !' }))

	});