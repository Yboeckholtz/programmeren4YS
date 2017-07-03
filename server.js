var http = require('http');
var express = require('express');
var routes_v1 = require('./api/routes_v1');
var routes_films = require('./api/routes_films');
var routes_rentals = require('./api/routes_rentals');
var bodyParser =  require('body-parser');
var config = require('./config/config');
var db = require('./config/db');
var expressJWT = require('express-jwt');
var app = express();


app.use(bodyParser.urlencoded({ 'extended': 'true' })); // parse application/x-www-form-urlencoded
app.use(bodyParser.json()); // parse application/json
app.use(bodyParser.json({ type: 'application/vnd.api+json' })); // parse application/vnd.api+json as json

app.use('/api/v1/rentals', expressJWT({
    secret: config.secretkey
}));

app.use('/api/v1', routes_v1);
app.use('/api/v1', routes_films);
app.use('/api/v1', routes_rentals);

app.use(function(err, req, res, next) {
    // console.dir(err);
    var error = {
        message: err.message,
        code: err.code,
        name: err.name,
        status: err.status
    }
    res.status(401).send(error);
});

app.use('*', function(req, res) {
    res.status(400);
    res.json({
        'error': 'Deze URL is niet beschikbaar.'
    });
});

app.listen(process.env.PORT || 3000, function(){
	console.log('Server is listening on port 3000');	
});

module.exports = app;
