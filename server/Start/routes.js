'use strict';
//id = email 
const auth = require('basic-auth');
const jwt = require('jsonwebtoken');

const register = require('./functions/register');
const login = require('./functions/login');
const profile = require('./functions/profile');
const password = require('./functions/password');
const config = require('./config/config.json');
const drugstore_get = require('./functions/drug_store_get');
const drugstore_post = require('./functions/drug_store_post');
const drugstore_delete = require('./functions/drug_store_delete');
const prescription_post = require('./functions/prescription_post');
const prescriptionPhoto_post = require('./functions/prescriptionPhoto_post');
const prescription_get = require('./functions/prescription_get');
const prescription_get_confirm = require('./functions/prescription_get_confirm');
const prescription_put_confirm = require('./functions/prescription_put_confirm');

const pay = require('./functions/Pay');

module.exports = router => {

	router.get('/', (req, res) => res.end('Welcome to Thanh Binh!'));

	router.post('/authenticate', (req, res) => {
		//get request lấy giá trị thông tin user
		const credentials = auth(req);
		console.log(credentials.name + "     " + credentials.pass + "         co ket noi login");
		if (!credentials) {

			res.status(400).json({ message: 'Invalid Request !' });

		} else {
			// gọi bên login.js loginUser
			login.loginUser(credentials.name, credentials.pass)
				.then(result => {

					const token = jwt.sign(result, config.secret, { expiresIn: 1440 });
					res.status(result.status).json({ message: result.message, token: token, id: result.id });

				})

				.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	router.post('/users', (req, res) => {

		const name = req.body.name;
		const email = req.body.email;
		const password = req.body.password;
		//hàm trim() loại bỏ khảng trắng
		if (!name || !email || !password || !name.trim() || !email.trim() || !password.trim()) {

			res.status(400).json({ message: 'Invalid Request !' });

		} else {

			register.registerUser(name, email, password)

				.then(result => {

					res.setHeader('Location', '/users/' + email);
					res.status(result.status).json({ message: result.message })
				})

				.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	router.get('/users/:id', (req, res) => {

		if (checkToken(req)) {

			profile.getProfile(req.params.id)

				.then(result => {
					console.log(result)
					res.json(result)
				})

				.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.put('/users/:id', (req, res) => {

		if (checkToken(req)) {

			const oldPassword = req.body.password;
			const newPassword = req.body.newPassword;

			if (!oldPassword || !newPassword || !oldPassword.trim() || !newPassword.trim()) {

				res.status(400).json({ message: 'Invalid Request !' });

			} else {

				password.changePassword(req.params.id, oldPassword, newPassword)

					.then(result => res.status(result.status).json({ message: result.message }))

					.catch(err => res.status(err.status).json({ message: err.message }));

			}
		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.post('/users/:id/password', (req, res) => {

		const email = req.params.id;
		const token = req.body.token;
		const newPassword = req.body.password;

		if (!token || !newPassword || !token.trim() || !newPassword.trim()) {

			password.resetPasswordInit(email)

				.then(result => res.status(result.status).json({ message: result.message }))

				.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			password.resetPasswordFinish(email, token, newPassword)

				.then(result => res.status(result.status).json({ message: result.message }))

				.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	function checkToken(req) {

		const token = req.headers['x-access-token'];

		if (token) {

			try {

				var decoded = jwt.verify(token, config.secret);

				return decoded.message === req.params.id;

			} catch (err) {

				return false;
			}

		} else {

			return false;
		}
	}
	//--------------------------------------Web---------------------------------------------------
	router.get('/drugstore', (req, res) => {

		res.render(__dirname + ("/view/drugstore.ejs"))
	});

	router.get('/drugstorelist', (req, res) => {

		drugstore_get.drug_store_get()

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));


	});
	router.post('/drugstorelist', (req, res) => {

		drugstore_post.drug_store_post(req.body)
			.then(result => {
				res.json(result);
			})

	});
	router.delete('/drugstorelist/:id', (req, res) => {
		var id = req.params.id;
		drugstore_delete.drug_store_delete(id)
			.then(result => {
				res.json(result);
			})
	});

	//------------------------------prescription (đơn thuốc)----------------------------------
	router.get('/prescription/:id', (req, res) => {
		prescription_get.prescriptionGet(req.params.id)
			.then(result => {
				// console.log("***" + result + "***")
				res.json(result);
			})
	});
	// lấy đơn thuốc xác thực trên di động.
	router.get('/prescription/:id/:boolean', (req, res) => {
		prescription_get_confirm.prescriptionGet(req.params.id,req.params.boolean)
			.then(result => {
				res.json(result);
			})
	});

	router.post('/prescription', (req, res) => {
		var prescription = req.body/*= {
			id: req.body.id,
			addressReceive: req.body.addressReceive,
			number_buy: req.body.number_buy,
			prescription: [
				{
					nameMedical: req.body.nameMedical,
					number: req.body.number,
				}
			]
		}*/
		//console.log(prescription);
		prescription_post.prescriptionPost(prescription)
			.then(result => {
				// console.log(result.status + "   " + result.message);
				res.json({ status: result.status, message: result.message });
			})
	});
	router.post('/prescription/photo', (req, res) => {
		var prescription = req.body;
		prescriptionPhoto_post.prescriptionPhotoPost(prescription)
			.then(result => {
				// console.log(result.status + "   " + result.message);
				res.json({ status: result.status, message: result.message });
			})
	});
	//------------------------------1Pay (thanh toán trực tuyến)----------------------------------

	router.post('/1pay', (req, res) => {
		var data = req.body;
		console.log(data);
		pay.PayPost(data)
			.then(result => {
				// console.log("****Mã Thanh Toán Card")
				// console.log(result.status + "   " + result.amount);
				res.json({ status: result.status, message: result.amount });
			})

	});

	//------------------------------confirm (kiểm duyệt đơn thuốc)----------------------------------
	router.get('/confirm', (req, res) => {
		res.render(__dirname +"/view/confirmPresciption.ejs");
	});
	router.get('/viewModal', (req, res) => {
		res.render(__dirname +"/view/viewModal.ejs");
	});
	router.get('/confirmList/:boolean', (req, res) => {
		var id = 'all';
		prescription_get_confirm.prescriptionGet(id,req.params.boolean)
			.then(result => {
				res.json(result);
			})
	});
	router.put('/confirmList/:id', (req, res) => {
		prescription_put_confirm.prescriptionPut(req.params.id,req.body)
			.then(result => {
				res.json(result);
			})
	});
}