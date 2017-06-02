'presciption_post'

const prescription = require('../models/Prescription');

exports.prescriptionGet = (id, boolean) =>
    //
    new Promise((resolve, reject) => {
        if (id == 'all') {
            prescription.find({ status: boolean })
                .then(prescriptions => {
                    resolve(prescriptions);
                })
                .catch(err => reject({ status: 500, message: 'Internal Server Error !' }));

        }
        else {
            prescription.find({ id: id, status: boolean })
                .then(prescriptions => {
                    resolve(prescriptions);
                })
                .catch(err => reject({ status: 500, message: 'Internal Server Error !' }));

        }

    });
        //
