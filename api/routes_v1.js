var express = require('express');
var routes = express.Router();
var auth = require('../auth/authentication');
var CryptoJS = require('crypto-js');
var db = require('../config/db');


routes.post('/login', function(req, res) {

    // Even kijken wat de inhoud is
    console.dir(req.body);

    // De username en pwd worden meegestuurd in de request body
    var email = req.body.email;
    var nonPassword = req.body.password;
    var password = CryptoJS.MD5(nonPassword);

    // Kijk of de gegevens matchen. Zo ja, dan token genereren en terugsturen.
    db.query('SELECT * FROM customer WHERE email = ?', [email], function (error, results, fields) {
      if (error) {
        console.log("error ocurred",error);
        res.send({
          "code":400,
          "failed":"error ocurred"
        })
      }else{
        //console.log('The solution is: ', results);
        if(results.length > 0){
          if(results[0].password == password){
            var token = auth.encodeToken(email);
            res.status(200).json({
                 "token": token,
            });
          }
          else{
            res.send({
              "code":204,
              "success":"Email and password do not match"
                });
            //console.log(results[0].email, password);
          }
        }
        else{
          res.send({
            "code":204,
            "success":"Email does not exist"
              });
        }
      }
    });

});

// Hiermee maken we onze router zichtbaar voor andere bestanden. 

routes.post('/register', function(req, res){

	console.dir(req.body);

    var password = req.body.password;
    var EncPass = CryptoJS.MD5(password);

    //Create Date
    var currentdate = new Date(); 
    var datetime = currentdate.getFullYear() + "-"
                + (currentdate.getMonth()+1) + "-"
                + currentdate.getDate() + " "
                + currentdate.getHours() + ":" 
                + currentdate.getMinutes() + ":" 
                + currentdate.getSeconds();

    var timestamp = currentdate.getHours() + ":" 
                + currentdate.getMinutes() + ":" 
                + currentdate.getSeconds();

    var email = req.body.email;

    var customer = {
        "first_name": req.body.first_name,
        "last_name": req.body.last_name,
        "email": email,
        "password": EncPass,
        "active": 1,
        "create_date": datetime,
        "last_update": datetime
    }

    db.query('SELECT * FROM customer WHERE email = ?',[email], function (error, results, fields) {
        if (error) {
            res.send({
              "Code":400,
              "Error":"An error has ocurred"
            })
        }else{
            if(results.length > 0){
                res.send({
                    "Code":400,
                    "Error":"Email is already known in out database, use another one"
                })
            }else{
                //create account
                db.query('INSERT INTO customer SET ?', customer, function (error, results, fields) {
                    if (error) {
                        console.log("error occurred",error);
                        res.send({
                            "code":400,
                            "failed":"error occurred"
                        })
                    }else{
                        console.log('The solution is: ', results);
                        res.send({
                            "code":200,
                            "success":"User registered successfully"
                        });
                    }
                });
            }
        }
    });

    




	//Registration
   
    // if (username == (database user) && password == (database pass)) {
        
    // } else {
    //     console.log('Input: username = ' + username + ', password = ' + password);
    //     res.status(401).json({ "error": "Account already exists with that username, cya" })
    // }


});

module.exports = routes;