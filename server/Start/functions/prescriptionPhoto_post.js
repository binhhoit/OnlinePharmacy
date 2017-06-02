'presciptionPhoto_post'

const prescription = require('../models/PrescriptionPhoto');


exports.prescriptionPhotoPost = (body  ) =>
    //
    new Promise((resolve, reject) => {
        console.log("Toa Thuốc : ");
        var prescriptions = new prescription(body)
        prescriptions.save(function (error, data) {

            if (error != null) {
                console.log(error);
                reject({ status: 404, message: 'Sever do not save data' });
            } else {
                console.log("thêm mới :" + data);
                resolve({ status: 200, message: 'success' });

            }
        })

    });
        //



