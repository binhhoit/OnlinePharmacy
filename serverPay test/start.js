var express = require('express');


var app = express();
//var data_x = null;
app.listen(3000);
//day trang web di
var bodyParser = require('body-parser')

var urlencodedParser = bodyParser.urlencoded({ extended: false });
app.use("/js", express.static(__dirname + '/js'));
app.use("/Views", express.static(__dirname + '/Views'));
app.use(bodyParser.json());
app.set("view engine", "ejs");



app.get('/'/*, urlencodedParser*/, function (req, res) {
    // body...
    res.sendFile(__dirname + "/index.html");
});

// cơ sỡ dữ liệu lấy từ mongodb


app.get('/contactlist', function (rep, res) {
    console.log("có kết nối")
    user.find(function (err, doc) {
        res.json(doc);
    })

});

app.post('/post1pay',urlencodedParser, function (rep, res) {
    const k = rep.body;
    console.log(k);
   
    var result = {
        transId: random(1000000000, 9999999999) + "",
        transRef: k.transRef,
        serial: k.serial,
        status: "[200]",
        amount: "[50k]",
        description: "[google.com]"
    }
    console.log("phản hồi :");
    console.log(result);
    res.json(result);
});
function random(low, high) {
    return Math.floor(Math.random() * (high - low) + low);
}