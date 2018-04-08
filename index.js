var express = require("express");
var app = express();
var bodyParser = require('body-parser');
var java = require("java");
java.classpath.push("./java/APIClient-0.0.1-SNAPSHOT.jar");
java.callStaticMethodSync("com.huy.APIClient.ClientAPI","connectToRepository",false);

var urlencodedParser = bodyParser.urlencoded({extended:false});

app.listen(3000);

app.set("view engine","ejs");
app.set("views","./views");
app.use(express.static(__dirname + '/public'));

app.get('/', function(req,res){
    res.render("home",{a:"Huy",data:"ok"});
});


app.post('/',urlencodedParser, function(req,res){
    var result;
    if(req.body.propertyInstance != "")
       // result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getPropertyOfInstance",req.body.propertyInstance);
       result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getPropertyOfInstance","<http://dbpedia.org/ontology/Province>");
    if(req.body.instanceClass != "")
        result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getInstanceOfClass",req.body.instanceClass);
    if(req.body.allInstance != "")
        result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getInstance",req.body.allInstance,true);
     //   console.log(result);
       
   // result = java.callStaticMethodSync("com.huy.APIClient.ClientAPI","getPropertyOfInstance",req.body.propertyInstance);
    
    res.setHeader('Content-Type','text/plain');
    res.send(result);
});

