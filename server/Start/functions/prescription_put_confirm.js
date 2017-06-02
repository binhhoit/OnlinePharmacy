'presciption_put'

const prescription = require('../models/Prescription');

exports.prescriptionPut = (id, body) =>
    //
    new Promise((resolve, reject) => {

        if (id == null) {
            reject({ status: 500, message: 'faile !' });
        } else {
            console.log(body);
            prescription.update({ _id: id }, { status: body.status, price: body.price })
                .then(prescriptions => {
                    console.log(prescriptions);
                    resolve(prescriptions);
                })
                .catch(err => reject({ status: 500, message: 'Internal Server Error !' }));
        }
    });
        //
