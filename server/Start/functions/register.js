'use strict';

const user = require('../models/User');
const bcrypt = require('bcryptjs');

exports.registerUser = (name, email, password) =>

	new Promise((resolve, reject) => {

		const salt = bcrypt.genSaltSync(10);
		const hash = bcrypt.hashSync(password, salt);
		console.log("đăng ký mới " + name + "  " + email + "   " + password + "   " + hash)
		const newUser = new user({
			name: name,
			email: email,
			hashed_password: hash,
			created_at: new Date()
		});

		newUser.save(function(err,doc){
			console.log(err);
			console.log(doc);
		})

			.then(() => resolve({ status: 201, message: 'User Registered Sucessfully !' }))

			.catch(err => {

				if (err.code == 11000) {

					reject({ status: 409, message: 'User Already Registered !' });

				} else {

					reject({ status: 500, message: 'Internal Server Error !' });
				}
			});
	});


