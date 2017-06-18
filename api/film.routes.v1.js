//
// ./api/film.routes.v1.js
//
var express = require('express');
var routes = express.Router();
var db = require('../config/db');

//
// Geef een lijst van alle todos. Dat kunnen er veel zijn.
//

routes.get("/films", function(req, res){

    var offset = parseInt(req.query.offset);
    var count = parseInt(req.query.count);

    res.contentType('application/json');

    db.query("SELECT * FROM film LIMIT ? OFFSET ?", [count,offset], function(error, rows, fields){
        if (error) {
            res.status(401).json(error);
        } else {

            res.status(200).json({ result: rows });

        };
    });
});

//
// Retourneer 1 specifieke film.
//
routes.get('/films/:filmid', function(req, res) {

    var filmId = req.params.filmid;

    res.contentType('application/json');

    db.query('SELECT * FROM film WHERE film_id=?', [filmId], function(error, rows, fields) {
        if (error) {
            res.status(401).json(error);
        } else {
            res.status(200).json({ result: rows });
        };
    });
});

routes.get('/rentals/:userid', function(req, res) {

    var userId = req.params.userid;

    res.contentType('application/json');

    db.query('SELECT '  +
        'film.film_id, ' +
        'film.title, ' +
        'film.description, ' +
        'film.release_year, ' +
        'film.length, ' +
        'film.rating, ' +
        'film.special_features, ' +
        'inventory.inventory_id, ' +
        'rental.rental_id, ' +
        'rental.rental_date, ' +
        'rental.return_date, ' +
        'customer.first_name, ' +
        'customer.customer_id, ' +
        'customer.last_name, ' +
        'customer.active ' +
        'FROM film ' +
        'LEFT JOIN inventory USING(film_id) ' +
        'LEFT JOIN rental USING(inventory_id) ' +
        'LEFT JOIN customer USING(customer_id) ' +
        'WHERE customer_id=?;', [userId], function(error, rows, fields) {
        if (error) {
            res.status(401).json(error);
        } else {
            res.status(200).json({ result: rows });
        };
    });
});

routes.put('/rentals/:userid/:inventoryid', function(req, res) {

    // var user = req.params.userid;
    // var inventory = req.params.inventoryid;
    var user = req.params.userid;
    var inventory = req.params.inventoryid;

    res.contentType('application/json');
    db.query('UPDATE rental SET inventory_id=? WHERE customer_id=?', [inventory, user], function(error, rows, fields) {
        if (error) {
            res.status(401).json(error);
        } else {
            res.status(200).json({ result: rows });
        };
    });
});

//
// Voeg een todo toe. De nieuwe info wordt gestuurd via de body van de request message.
//
routes.post('/rentals/:userid/:inventoryid', function(req, res) {

    // var userid = req.body.userid;
    // var inventoryid =req.body.inventoryid;
    var currentDate = new Date();
    var query = {
        sql: 'INSERT INTO `rental`(`rental_date`, `inventory_id`, `customer_id`) VALUES (?, ?, ?)',
        values: [currentDate, req.params.inventoryid, req.params.userid],
        timeout: 2000 // 2secs
    };

    //console.dir(rentals);
    console.log('Onze query: ' + query.sql);

    res.contentType('application/json');
    db.query(query, function(error, rows, fields) {
        if (error) {
            res.status(401).json(error);
        } else {
            res.status(200).json({ result: rows });
        }
    });
});



//
// Verwijder een bestaande todo.
// Er zijn twee manieren om de id van de todos mee te geven: via de request parameters (doen we hier)
// of als property in de request body.
// 
// Vorm van de URL: DELETE http://hostname:3000/api/v1/todos/23
//
routes.delete('/rentals/:userid/:inventoryid', function(req, res) {

    var userId = req.params.userid;
    var inventoryId = req.params.inventoryid;
    var query = {
        sql: 'DELETE FROM `rental` WHERE customer_id=? AND inventory_id=?',
        values: [userId, inventoryId],
        timeout: 2000 // 2secs
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

module.exports = routes;