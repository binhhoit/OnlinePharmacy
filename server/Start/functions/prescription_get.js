'presciption_post'

const prescription = require('../models/Prescription');

exports.prescriptionGet = (id) =>
    //
    new Promise((resolve, reject) => {

        prescription.find({ id: id})
            .then(prescriptions => {
                resolve(prescriptions);
            })
            .catch(err => reject({ status: 500, message: 'Internal Server Error !' }));
    });
        //
