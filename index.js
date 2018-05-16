var express = require("express");
var app = express();
var bodyParser = require('body-parser');
var java = require("java");
var fs = require('fs');
/*
var http2 = require('http2');

require('express-http2-workaround')({express:express,http2:http2,app:app});
const httpOptions = {
    'key':fs.readFileSync(__dirname + '/keys/server.key'),
    'cert':fs.readFileSync(__dirname + '/keys/server.crt'),
    'ca':fs.readFileSync(__dirname + '/keys/server.crt')
}



var server = http2.createServer(httpOptions,app);
*/

var server = require('http').Server(app);

var io = require('socket.io')(server);
io.on('connection',function(socket){

    console.log('Socket io');
    
});


server.listen(3000,function(){
  console.log("OK connection!");
});


java.classpath.push("./java/APIClient-0.0.1-SNAPSHOT.jar");
java.callStaticMethodSync("com.huy.APIClient.ClientAPI","connectToRepository",false);

var urlencodedParser = bodyParser.urlencoded({extended:false});

//app.listen(3000);

//app.set("view engine","ejs");
//app.set("views","./views");
app.use(express.static(__dirname + '/public'));

app.use(express.static(__dirname));


app.get('/', function(req,res){
    res.render("home",{a:"Huy",data:"ok"});
});


app.post('/',urlencodedParser, function(req,res){
    var result;
   if(req.body.propertyInstance != "" && req.body.propertyInstance != null)
       // result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getPropertyOfInstance",req.body.propertyInstance);
       result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getPropertyOfInstance",req.body.propertyInstance);
    if(req.body.instanceClass != "" && req.body.instanceClass != null)
        result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getInstanceOfClass",req.body.instanceClass);
    if(req.body.allInstance != "" && req.body.allInstance != null)
        result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getInstance",req.body.allInstance,true);
     //   console.log(result);

     if(req.body.automatic == "yes"){
         result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getInstanceOfClass","owl:Class");
        console.log("Loading Jmeter");
        
        }
       
   // result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getPropertyOfInstance",req.body.propertyInstance);
    
    res.setHeader('Content-Type','text/plain');
    res.send(result);
});

