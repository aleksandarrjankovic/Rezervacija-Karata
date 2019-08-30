var mongodb = require('mongodb');
var crypto = require('crypto');
var body_prarser = require('body-parser');
var express = require('express');
var nodemailer = require('nodemailer');

//Express service
var app = express();
app.use(body_prarser.json());
app.use(body_prarser.urlencoded({ extended: true }));

//Promenjive
global.brojKupljenihKarata = 0
global.str = ""

//MongoDB
//Kreiranje klijenta
var MongoClient = mongodb.MongoClient;
//Konekcija
var url = 'mongodb://localhost:27017'
MongoClient.connect(url, { useNewUrlParser: true }, (err, client) => {
    if (err) {
        console.log("Konekcija sa bazom nije uspostavljena.");
    } 
    else {
        //Register
        app.post('/register', (request, res, next) => {
                var postData = request.body;
                var password = postData.password;
                var encrypted_password = enkripcija(password);
                var username = postData.username;
                var email = postData.email;
                var utakmica = ""
                var brojKupljenihKarata = 0;
                var price = 0
                var tribina = ""
                var inserJson = {
                    'email': email,
                    'password': password,
                    'encrypted_password': encrypted_password,
                    'username': username,
                    'brKupljenihKarata': brojKupljenihKarata,
                    'utakmica' : utakmica,
                    'price' : price,
                    'tribina' : tribina

                };
                var db = client.db('olsrbije');

                //provera email adrese
                db.collection('user')
                    .find({ 'email': email }).count((err, number) => {
                        if (number != 0) {
                            res.json("Email adresa vec postoji");
                            console.log("Email adresa vec postoji");
                        } else {
                            db.collection('user')
                                .insertOne(inserJson, (err, result) => {
                                    res.json("Registracija je bila uspesna");
                                    console.log("Registracija je bila uspesna");
                                    uspesna_registracija(email);
                                })
                        }
                    })
            })
        //Login
        app.post('/login', (request, res, next) => {
                var postData = request.body;
                var userPassword = postData.password;
                var email = postData.email;
                var db = client.db('olsrbije');

                //provera email adrese
                db.collection('user')
                    .find({ 'email': email }).count((err, number) => {
                        if (number == 0) {
                            res.json("Email ne postoji");
                            console.log("Email ne postoji");
                        } else {
                            db.collection('user')
                                .findOne({ 'email': email }, (err, user) => {
                                    var encrypted_password = user.encrypted_password;
                                    var password = enkripcija(userPassword);
                                    if (encrypted_password == password) {
                                        console.log('Uspesno ste se ulogovali.');
                                        res.json('Uspesno ste se ulogovali.');
                                    } else {
                                        console.log('Pogresili ste sifru.');
                                        res.json('Pogresili ste sifru.');
                                    }
                                })
                        }
                    })
            })
        //rezrvacija karata
        app.post('/rezervacija', (request, res, next) => {
                var postData = request.body;
                var br_karata = postData.br_karata;
                var email = postData.email;
                var username = postData.username;
                var price = postData.price;
                var tribina = postData.tribina;
                var id = postData.id;
                if (id == 0) {
                    var str = "FK Rudar - FK Ozren"
                }
                if (id == 1) {
                    var str = "FK Timok - Fk Kablovi"
                }
                var kod = randomInt(100000, 999999)
                var inserJson = {
                    'email': email,
                    'username': username,
                    'br_karata': br_karata,
                    'price': price,
                    'tribina': tribina,
                    'utakmica': str,
                    'kod': kod
                };
                var db = client.db('olsrbije');

                //provera email adrese
                db.collection('user')
                    .find({ 'kod': kod }).count((err, number) => {
                        if (number != 0) {
                            kod = randomInt(100000, 999999);
                        } else {
                            db.collection('user')
                                .insertOne(inserJson, (err, result) => {
                                    res.json("Rezervacija je bila uspesna!");
                                    console.log("Rezervacija je bila uspesna!");
                                    brojKupljenihKarata++;
                                    posalji_email(email, tribina, br_karata, kod, price, id)
                                })
                        }
                    })
            })
        //brisanje korisnika
        app.post('/karte/:kod', (request, res, next) => {
            var k = request.params.kod;
            var broj = parseInt(k, 10);
            var db = client.db('olsrbije');
            console.log(k);
            db.collection('user').findOneAndDelete({ "kod": broj }, (err, result) => {
                if (err) throw err;
                else {
                    console.log(result)
                    res.send({ type: 'Korisnik je obrisan.' })
                    console.log("Korisnik je obrisan!")
                }
            })
        })
        //rezervacija karata sa nalogom
        app.post('/rezervacijaLogin', (request, res, next) => {
                var postData = request.body;
                var br_karata = postData.br_karata;
                var email = postData.email;
                var price = postData.price;
                var tribina = postData.tribina;
                var id = postData.id;
                var br = parseInt(br_karata, 10)
                var brKupljenihKarata = 0

                if (id == 0) {
                    str = "FK Rudar - FK Ozren"
                }
                if (id == 1) {
                    str = "FK Timok - Fk Kablovi"
                }

                var db = client.db('olsrbije');
    
                db.collection('user')
                    .find({ 'email': email }).toArray((err, result) => {
                        if(err) throw err;
                        else {
                            //console.log(result);
                            while(brKupljenihKarata<br){
                                brKupljenihKarata++;
                            }
                            brKupljenihKarata = brKupljenihKarata + result[0].brKupljenihKarata;
                            console.log(brKupljenihKarata);
                            //console.log(result[0].username);//izvlacenje iz dokumenta
                            db.collection('user')
                                .updateMany({'email' : email}, {$set: {'utakmica' : str , 'price' : price, 'tribina' : tribina, 'brKupljenihKarata' : brKupljenihKarata}}, (err, result)=>{
                                    if(err) throw err;

                                    else{
                                        res.json("Rezervacija je bila uspesna!");
                                        console.log("Rezervacija je bila uspesna!");
                                        posalji_email_login(email, tribina, br_karata, price, id);
                                    }
                                })
                            }
                    })
            })
        //web server
        app.listen(3000, () => {
            console.log("Konekcija sa bazom je uspostavljena , Web Servis radi na portu 3000");
        })
    }
});

//Funkcije
function enkripcija(p) {
    const e = crypto.createHmac('sha256', p).update('nodejs', 'utf-8').digest('hex');
    return e;
}

function posalji_email(email, tribina, br_karata, kod, price, id) {
    if (id == 0) {
        var string = "FK Rudar - FK Ozren"
    }
    if (id == 1) {
        var string = "FK Timok - Fk Kablovi"
    }
    let transporter = nodemailer.createTransport({
        service: 'gmail',
        secure: 'false',
        //port: 587,
        auth: {
            user: 'okruznaligasrbije19@gmail.com',
            pass: 'srbija19'
        },
        tls: {
            rejectUnauthorized: false
        }
    });

    let help = {
        from: '<okruznaligasrbije19@gmail.com>',
        to: email,
        subject: 'Okruzna fudbalska liga Srbije',
        text: ' Uspešno ste rezervisali ' + br_karata + ' karte za utakmicu ' + string + ' za tribinu ' + tribina + '. \n Cena karata je ' + price + ' dinara.\n Vaša rezervacija važi do 30min pre početka utakmice.\n \n Broj vaše rezervacije:' + kod
    };

    transporter.sendMail(help, (error, info) => {
        if (error) {
            console.log(error);
        } else {
            console.log("Poruka je poslata");
            console.log(info);
        }
    });
}

function posalji_email_login(email, tribina, br_karata, price, id) {
    if (id == 0) {
        var string = "FK Rudar - FK Ozren"
    }
    if (id == 1) {
        var string = "FK Timok - Fk Kablovi"
    }
    let transporter = nodemailer.createTransport({
        service: 'gmail',
        secure: 'false',
        //port: 587,
        auth: {
            user: 'okruznaligasrbije19@gmail.com',
            pass: 'srbija19'
        },
        tls: {
            rejectUnauthorized: false
        }
    });

    let help = {
        from: '<okruznaligasrbije19@gmail.com>',
        to: email,
        subject: 'Okruzna fudbalska liga Srbije',
        text: ' Uspešno ste rezervisali ' + br_karata + ' karte za utakmicu ' + string + ' za tribinu ' + tribina + '. \n Cena karata je ' + price + ' dinara.\n Vaša rezervacija važi do 30min pre početka utakmice.\n \n '
    };

    transporter.sendMail(help, (error, info) => {
        if (error) {
            console.log(error);
        } else {
            console.log("Poruka je poslata");
            console.log(info);
        }
    });
}

function uspesna_registracija(email) {
    let transporter = nodemailer.createTransport({
        service: 'gmail',
        secure: 'false',
        //port: 587,
        auth: {
            user: 'okruznaligasrbije19@gmail.com',
            pass: 'srbija19'
        },
        tls: {
            rejectUnauthorized: false
        }
    });

    let help = {
        from: '<okruznaligasrbije19@gmail.com>',
        to: email,
        subject: 'Okružna fudbalska liga Srbije',
        text: 'Hvala vam sto šte se registrovali na našoj zvaničnoj aplikaciji okružne fudbalske lige Srbije. Očekuju vas razne nagrade i popusti.'
    };

    transporter.sendMail(help, (error, info) => {
        if (error) {
            console.log(error);
        } else {
            console.log("Poruka je poslata");
            console.log(info);
        }
    });
}

function randomInt(low, high) {
    return Math.floor(Math.random() * (high - low) + low)
}