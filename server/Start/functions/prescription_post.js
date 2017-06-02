'presciption_post'

const prescription = require('../models/Prescription');


exports.prescriptionPost = (body   /*id,addressReceive,number_buy,nameMedical,number*/) =>
    //
    new Promise((resolve, reject) => {

        var updatePrescription = body.prescription;
        /* var updatePrescription[] = {
             nameMedical: body.prescription.nameMedical,
             number: body.prescription.number,
         }*/
        console.log("Toa Thuốc : ");
        console.log(body.prescription);
        prescription.findOneAndUpdate({ id: body.id, number_buy: body.number_buy }, {
            $push: {prescription : updatePrescription }
        }, function (err, result) {
            console.log("/update/" + result);
            if (result == null) {
                console.log("/NEW/ ")
                var prescriptions = new prescription(
                    body
                    /*  id: String,
                        addressReceive: String,
                        number_buy: String,
                        prescription: [
                            {
                                nameMedical: String,
                                number: String,
                            }
                        ]*/
                )

                prescriptions.save(function (error, data) {

                    if (error != null) {
                        console.log(error);
                        reject({ status: 404, message: 'Sever do not save data' });
                    } else {
                        console.log("thêm mới :" + data);
                        resolve({ status: 200, message: 'success' });

                    }
                })
            } else {
                resolve(result);
            }
        });
        //


    });
