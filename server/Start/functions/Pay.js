const payCardDB = require('../models/PayCard');
var querystring = require('querystring');
const crypto = require('crypto');
var http = require('http');


exports.PayPost = (body) =>

    new Promise((resolve, reject) => {
        access_key = random(10000000000, 99999999999);
        transRef = random(10000000000, 99999999999);
        // create signature trans to server 
        data = 'access_key=' + access_key
            + '&pin=' + body.pin
            + '&serial=' + body.serial
            + '&transRef=' + transRef
            + '&type=' + body.type;
        console.log("chuoi cần ma hoa:   " + data);
        //hash SHA256 
        signature = crypto.createHash('sha256', 'a secret key').update(data).digest('hex');
        console.log("chuổi được mã hoá:  " + signature);

        data = data + '&signature=' + signature;
        console.log("dữ liệu sẽ đi:   " + data);

        var options = {
            hostname: 'localhost',
            port: 3000,
            path: '/post1pay',
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
        };

        var requesta = http.request(options, function (responsive) {
            console.log('Status: ' + responsive.statusCode);
            console.log('Headers: ' + JSON.stringify(responsive.headers));
            responsive.setEncoding('utf8');
            responsive.on('data', function (body) {
                console.log('Body: ' + body);
                resolve(JSON.parse(body))
                //save data t json
                //     payCardDB = new payCardDB(body);
                //     payCardDB.save(function (error, data) {
                //         console.log(data);
                //         resolve(data);
                //     })
            });
        });

        requesta.on('error', function (e) {
            console.log('problem with request: ' + e.message);
            reject(e.message);
        });

        console.log("gui len server:  " + data);
        requesta.write(data);
        requesta.end();
    });
//

function random(low, high) {
    return Math.floor(Math.random() * (high - low) + low);
}