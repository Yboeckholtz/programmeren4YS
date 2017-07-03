var express = require('express');
var routes = express.Router();
var db = require('../config/db');
var auth = require('../auth/authentication');
var config = require('../config/config.json');

routes.get('/rentals/:userid', function(req, res){

	var userId = req.params.userid;

	db.query('SELECT * FROM rental WHERE customer_id=?', [userId], function (error, results, fields) {
		if (error) {
        	res.send({
          		"code":400,
          		"failed":"error occurred"
        	})
        }else{
        	if(results.length > 0){
        		//console.log(results);
        		res.json(results);
        	}else{
        		//console.log(filmId);
        		res.send({
        			"Code": 400,
        			"Error": "This rental does not exist"
        		})
        	}
        }
	});
});

routes.post('/rentals/:userid/:inventoryid', function(req, res){
	var userID = req.params.userid;
	var inventoryID = req.params.inventoryid;

	//Create Date
    var currentdate = new Date(); 
    var datetime = currentdate.getFullYear() + "-"
                + (currentdate.getMonth()+1) + "-"
                + currentdate.getDate() + " "
                + currentdate.getHours() + ":" 
                + currentdate.getMinutes() + ":" 
                + currentdate.getSeconds();

	var rental_date = datetime;   

    var rental = {
        "rental_date": rental_date,
        "inventory_id": inventoryID,
        "customer_id": userID,
        "staff_id": 1,
        "last_update": datetime
    }

	db.query('SELECT * FROM view_rental WHERE customer_id = ? AND inventory_id = ?', [userID, inventoryID], function (error, results, fields) {
        if (error) {
        	//console.log(error);
            res.send({
              "Code":400,
              "Error":"An error has ocurred"
            })
        }else{
            if(results.length > 0){
                res.send({
                    "Code":400,
                    "Error":"You already rented this film"
                })
            }else{
                //insert rental
                db.query('INSERT INTO rental SET ?', rental, function (error, results, fields) {
                    if (error) {
                        console.log("error occurred",error);
                        res.send({
                            "code":400,
                            "failed":"error occurred"
                        })
                    }else{
                        //console.log('The solution is: ', results);
                        res.send({
                            "code":200,
                            "success":"Film rented successfully"
                        });
                    }
                });
            }
        }
    });
});

routes.put('/rentals/:userid/:inventoryid', function(req, res){
	res.contentType('application/json');
	var userID = req.params.userid;
	var inventoryID = req.params.inventoryid;

	var currentdate = new Date(); 
    var return_date = currentdate.getFullYear() + "-"
                + (currentdate.getMonth()+1) + "-"
                + currentdate.getDate() + " "
                + currentdate.getHours() + ":" 
                + currentdate.getMinutes() + ":" 
                + currentdate.getSeconds();

	db.query('SELECT * FROM rental WHERE customer_id = ? AND inventory_id = ?', [userID, inventoryID], function (error, results, fields) {
        if (error) {
        	//console.log(error);
            res.send({
              "Code":400,
              "Error":"An error has ocurred"
            })
        }else{
            if(results.length > 0){
                //insert rental, moet return_date aanpassen
                db.query('UPDATE rental SET return_date = ? WHERE customer_id = ? AND inventory_id = ?', [return_date, userID, inventoryID], function (error, results, fields) {
                    if (error) {
                        console.log("error occurred",error);
                        res.send({
                            "code":400,
                            "failed":"error occurred"
                        })
                    }else{
                        console.log('The solution is: ', results, return_date);
                        res.send({
                            "code":200,
                            "success":"Film rental edited successfully"
                        });
                    }
                });
            }else{
                res.send({
                    "Code":400,
                    "Error":"No such rental for this user"
                })
            }
        }
    });
});

routes.delete('/rentals/:userid/:inventoryid', function(req, res){
	res.contentType('application/json');
	var userID = req.params.userid;
	var inventoryID = req.params.inventoryid;
	
	db.query('SELECT * FROM rental WHERE customer_id = ? AND inventory_id = ?', [userID, inventoryID], function (error, results, fields) {
        if (error) {
        	//console.log(error);
            res.send({
              "Code":400,
              "Error":"An error has ocurred"
            })
        }else{
            if(results.length > 0){
                //insert rental, moet return_date aanpassen voor de android app
                db.query('DELETE FROM rental WHERE customer_id = ? AND inventory_id = ?', [userID, inventoryID], function (error, results, fields) {
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
                            "success":"Film rental deleted successfully"
                        });
                    }
                });
            }else{
                res.send({
                    "Code":400,
                    "Error":"No such rental for this user"
                })
            }
        }
    });
});

module.exports = routes;