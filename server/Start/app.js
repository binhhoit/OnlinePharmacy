'use strict';

const express    = require('express');        
const app        = express();                
const bodyParser = require('body-parser');
const logger 	   = require('morgan');
const router 	   = express.Router();
const port 	   = process.env.PORT || 8080;

// app.use(bodyParser.json());
app.use(bodyParser.json({limit: '5mb'}));
app.use(bodyParser.urlencoded({limit: '5mb', extended: true}));
app.use(logger('dev'));
app.use('/api/assets',express.static(__dirname+'/public'));
app.set("view engine", "html");

require('./routes')(router);
app.use('/api/', router);

app.listen(port);

console.log(`App Runs on ${port}`);