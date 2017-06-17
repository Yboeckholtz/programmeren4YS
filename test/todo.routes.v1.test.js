//
// Tests voor ToDo routes van de API.
// Deze test is nog niet af. Probleem is namelijk dat 
//
process.env.NODE_ENV = 'test';
process.env.APP_USERNAME = 'username';
process.env.APP_PASSWORD = 'password';

var chai = require('chai');
var chaiHttp = require('chai-http');
var sinon = require('sinon');
var server = require('../server');
var chould = chai.should();
var token;

chai.use(chaiHttp);

describe('GET /api/v1/todos', function() {

    //
    // Before all tests: get a valid JWT token from the server
    //
    before(function(done) {
        var user = {
            username: "username",
            password: "password"
        }
        chai.request(server)
            .post('/api/v1/login')
            .send(user)
            .end(function(err, res) {
                res.body.should.be.an('object');
                res.body.should.have.property('token');
                token = res.body.token;
                done();
            });
    });

    //
    beforeEach(function() {
        // set things we changed for testing
    });

    //
    afterEach(function() {
        // reset things we changed for testing
    });

    // 
    //
    it('should return all ToDos when logged in', function(done) {
        chai.request(server)
            .get('/api/v1/todos')
            .set('Authorization', 'Bearer ' + token)
            .end(function(err, res) {
                // console.dir(err);
                // res.should.have.status(200);
                // res.should.be.json;
                // res.body.should.be.a('object');
                // res.body.should.have.property('result').that.is.an('array');
                // mock.verify();
                done();
            });
    });

    // 
    //

});