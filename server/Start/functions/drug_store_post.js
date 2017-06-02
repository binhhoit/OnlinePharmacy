'drug_store'
const drugstore = require('../models/DrugStore');


exports.drug_store_post = (body /*name, address, numberphone, datefound, type*/) =>

    new Promise((resolve, reject) => {

        var drugstores = new drugstore(
            body/*{
            name: name,
            address: address,
            numberphone: numberphone,
            datefound: datefound,
            type: type,
        }*/)

        drugstores.save(function (error, data) {

            console.log(data);
            resolve(data);

        })
        /*   .then(() => {
               resolve({ status: 201, message: 'User Registered Sucessfully !' })
           })
           .catch(err => {
               if (err.code == 11000) {

                   reject({ status: 409, message: 'User Already Registered !' });

               } else {

                   reject({ status: 500, message: 'Internal Server Error !' });
               }

           })*/

    })
