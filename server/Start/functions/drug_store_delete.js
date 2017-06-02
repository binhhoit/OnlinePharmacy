'drug_store'
const drugstore = require('../models/DrugStore');


exports.drug_store_delete = (id) =>

    new Promise((resolve, reject) => {
        drugstore.remove({ _id: id }, function (err, doc) {
            if (!err) {
                console.log(doc+"Đã xoá còn lại")
                resolve(doc);
            }
            reject(err);
        })
            .then(drugstores => {
                console.log("Đã xoá " + drugstores + "***");
            })
            .catch(err => reject({ status: 500, message: 'Internal Server Error !' }));
    });
