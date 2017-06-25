'presciption_post'

const prescription = require('../models/Prescription');
const user = require('../models/User');

exports.prescriptionPost = (body) =>
    //
    new Promise((resolve, reject) => {

        console.log("Toa Thuốc : ");
        console.log(body.prescription);
        if (body.id != null) {
            user.find({ _id: body.id })
                .then(result => {

                    var prescriptionAddNew = new prescription(body)

                    prescriptionAddNew.save(function (error, data) {

                        if (error != null) {

                            console.log(error);
                            reject({ status: 404, message: 'Sever do not save data' });
                        } else {

                            console.log("thêm mới :" + data);
                            resolve({ status: 200, message: 'success' });
                        }
                    })

                })
                .catch(err => reject({ status: 500, message: 'Internal Server Error !' }));
        } else {
            reject({ status: 500, message: 'Internal Server Error !' })
        }
    });
