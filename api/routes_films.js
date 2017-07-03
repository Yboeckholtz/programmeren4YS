var express = require('express');
var routes = express.Router();
var db = require('../config/db');
var url = require('url');

routes.get('/films', function(req, res){

	var queryData = url.parse(req.url, true).query;
	var offset = parseInt(queryData.offset);
	var count = parseInt(queryData.count);

	//console.log(offset,count);
	//toevoegen van offset en count individueel, ook films individueel nog doen

	db.query('SELECT * FROM film ORDER BY film_id ASC LIMIT ?, ?', [offset, count], function (error, results, fields) {
		if (error) {
			console.log(error, results);
        	res.send({
          		"code":400,
          		"failed":"Error occurred"
        	})
        	console.log(offset, count);
        }else{
        	res.json(results);
        	console.log(offset, count);
        }
	});
});

routes.get('/films/:filmid', function(req, res){

	var filmId = req.params.filmid;
	res.contentType('application/json');

	db.query('SELECT * FROM film WHERE film_id=?', [filmId], function (error, results, fields) {
		if (error) {
        	res.send({
          		"code":400,
          		"failed":"error ocurred"
        	})
        }else{
        	if(results.length > 0){
        		//console.log(results);
        		res.json(results);
        	}else{
        		//console.log(filmId);
        		res.send({
        			"Code": 400,
        			"Error": "This film does not exist"
        		})
        	}
        }
	});
});

module.exports = routes;