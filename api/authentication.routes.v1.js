
//
// ./api/authentication.routes.v1.js
//
var express = require('express');
var router = express.Router();



var auth2 = require('../auth/authentication');
var db = require('../config/db');

//
// Hier gaat de gebruiker inloggen
// Input: username en wachtwoord 
// ToDo: 
//	 - zoek de username in de database, en vind het password wat opgeslagen is. 
// 	 - als user gevonden en password matcht, dan return valide token.
//   - anders is de inlogpoging gefaald - geef foutmelding terug.
//

router.post('/register', function (req,res) {

    var username = req.body.username;
    var password = req.body.password;

    var todos = req.body;
    var query = {
        sql: 'INSERT INTO `customer`(`first_name`, `last_name`) VALUES (?, ?)',
        values: [username, password],
        timeout: 2000 // 2sec
    };


    console.log('Onze query: ' + query.sql);

    res.contentType('application/json');
    db.query(query, function(error, rows, fields) {
        if (error) {
            res.status(401).json(error);
        } else {
            res.status(200).json({ result: rows });
        };
    });
});




router.post('/login', function(req, res) {

    // Even bekijken wat de inhoud is..
    // console.dir(req.body);

    var username = req.body.username;
    var password = req.body.password;

    // var credentials = auth.parse(req.headers['Proxy-Authorisation']);
    //
    // if (!credentials) {
    //
    //     res.status(400).json({message: 'Invalid Request !'});
    // } else {


    db.query('SELECT * FROM customer WHERE first_name = ?', [username], function (error, results, fields) {
        if (error) {
            // console.log("error ocurred",error);
            res.send({
                "code": 400,
                "failed": "error ocurred"
            })
        } else {
            // console.log('The solution is: ', results);
            if (results.length > 0) {
                if (results[0].last_name == password) {
                    // res.send({
                    //     "code": 200,
                    //     "success": "login sucessfull"
                    // });

                    var token = auth2.encodeToken(username);
                    var customerId = results[0].customer_id;
                    res.status(200).json({
                        "token": token,
                        "customer_id": customerId
                    });
                }
                else {
                    res.send({
                        "code": 204,
                        "success": "Email and password does not match"
                    });
                }
            }
            else {
                res.send({
                    "code": 204,
                    "success": "Email does not exits"
                });
            }
        }
    });

});



//     // Kijk of de gegevens matchen. Zo ja, dan token genereren en terugsturen.
//     if (username == _dummy_username && password == _dummy_password) {
//         var token = auth.encodeToken(username);
//         res.status(200).json({
//             "token": token,
//         });
//     } else {
//         console.log('Input: username = ' + username + ', password = ' + password);
//         res.status(401).json({ "error": "Invalid credentials, bye" })
//     }
//
// });

// Hiermee wordt router zichtbaar gemaakt voor andere bestanden.
    module.exports = router;
