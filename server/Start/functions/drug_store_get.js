'drug_store'
const drugstore = require('../models/DrugStore');


exports.drug_store_get = () => 
    //
    new Promise((resolve, reject) => {

        drugstore.find()
            .then(drugstores => {
                console.log("***" + drugstores + "***");
                resolve(drugstores)
            })
            .catch(err => reject({ status: 500, message: 'Internal Server Error !' }));
    });
    //
    


