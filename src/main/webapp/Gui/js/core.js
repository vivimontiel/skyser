(function ($) {
    $.fn.serializeFormJSON = function () {

        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
})(jQuery);


function signUp(){
    var data = JSON.stringify($('#formSignUp').serializeFormJSON());
    console.log(data);
    $.ajax({
        url: "ws/Users/sign_up",
        type: "POST",
        contentType: "application/json",
        data: data,
        dataType: "json",
        success: function (result) {
            $( function() {
                $( "#dialogAdd" ).dialog({
                    height: 200, maxHeight: 200, minHeight: 200,
                    width: 300, maxWidth: 300, minWidth: 300,
                    show: { effect: "scale", duration: 200 },
                    close: function(){
                        window.location.href="index.html"; },
                    dialogClass: "no-close",
                    buttons: {
                        "Back Home": function () {
                            window.location.href="index.html";
                        }
                    }
                });
            } );
        },
        fail: function() {
            $( function() {
                $( "#dialog" ).dialog();
            } );
        }
    });
    return false;
}

function getProfile() {
    console.log("enter getProfile");
    var data = {id: localStorage.getItem("pseudo")};
    console.log(data);
    $.ajax({
        url: "ws/Users/getProfile",
        type: "POST",
        contentType: "application/json",
        data: data.id,
        dataType: "json",
        success: function (result) {
            if (result.response == "success") {
                if (result.passenger != null) {
                    console.log("succes passenger!!!!!");
                    localStorage.setItem("currentUser", JSON.stringify(result.passenger));
                    console.log(result.passenger);
                }
                if (result.pilot != null) {
                    console.log("enter RESULT PILOT")
                    getListPlane();
                    console.log("success pilot !!!");
                    localStorage.setItem("currentPilot", JSON.stringify(result.pilot));
                    console.log(result.pilot);
                }
                window.location.href = "index.html";
            } else if (result.response == "fail") {
                console.log("FAILLLL");
            }

        },
        fail: function () {
            console.log("fail");
        }
    });
    return false;
}

function logIn(form){
    var email = form.email.value;
    var password = form.password.value;
    $.ajax({
    url: "ws/Users/sign_in/" + email + "/" + password, 
    type: "GET",
    contentType: "application/json",
	async: false,
    success: function (result) {
        var res = JSON.parse(JSON.stringify(result));
        if(result.response == "success"){
            console.log("login succes");
            localStorage.setItem('pseudo', res.id);
            getProfile();
            console.log("arriver la")
        }else if(result.response == "fail"){
            alert(result.message);
        }
    },
    fail: function() {
        alert("Server failed to receive request !");
      }
    });
    return false;
 }


 function log_out(){
    localStorage.removeItem('pseudo');
    localStorage.removeItem("currentUser");
    localStorage.removeItem("currentPilot");
    localStorage.removeItem("currentListPlane");
    window.location.href="index.html";
 }


function addFlight(){
    var data = JSON.stringify($('#formAddFlight').serializeFormJSON());
    console.log(data);
    $.ajax({
        url: "ws/home/addflight",
        type: "POST",
        contentType: "application/json",
        data: data,
        dataType: "json",
        success: function (result) {
            $( function() {
                console.log(result.flight);
                $( "#dialogAdd" ).dialog({
                    title: "Youhou !",
                    height: 300, maxHeight: 300, minHeight: 300,
                    width: 300, maxWidth: 300, minWidth: 300,
                    show: { effect: "scale", duration: 200 },
                    close: function(){
                        window.location.reload(true); },
                    dialogClass: "no-close",
                    buttons: {
                        "Back Home": function () {
                            window.location.href="index.html";
                        },
                        "See this flight": function () {
                            //localStorage.removeItem("currentFlight");
                            localStorage.setItem("currentFlight", data);
                            //localStorage.setItem("currentFlight", result.flight);
                            window.location.href="Flight_summary.html";
                        },
                        "Add a new flight": function(){
                            window.location.reload(true);
                        }
                    }
                });
            } );
        },
        fail: function() {
            $( function() {
                $( "#dialog" ).dialog();
            } );
        }
    });
    return false;
}


function listToSummary(div) {
    console.log(div.id);
    var superData = magicParsing(div.id)
    console.log("super DATA :"+superData.id);
    var data = {id: superData.id};
    var obj = JSON.stringify(data);
    localStorage.setItem("summaryUserName", superData.pilote_name);
    localStorage.setItem("summaryUserSurname", superData.pilote_surname);
    console.log("obj :"+obj);
    $.ajax({
        url: "ws/home/getFlightById",
        type: "POST",
        contentType: "application/json",
        data: obj,
        dataType: "json",
        success: function (result) {
            console.log("success");
            console.log(result);
            localStorage.setItem("currentFlight", JSON.stringify(result.flight));
            window.location.href="Flight_summary.html";
            console.log("test final");
        },
        fail: function () {
            console.log("fail");
        }
    });
    return false;
}


function listToBook(div) {
    console.log("enter listToBook");
    data = {id: div.id};
    console.log(data);
    obj = JSON.stringify(data);
    console.log(obj);
    $.ajax({
        url: "ws/home/getFlightById",
        type: "POST",
        contentType: "application/json",
        data: obj,
        dataType: "json",
        success: function (result) {
            console.log("success");
            console.log(result);
            localStorage.setItem("currentFlight", JSON.stringify(result.flight));
            window.location.href="Book_flight.html";
            console.log("test final");
        },
        fail: function () {
            console.log("fail");
        }
    });
    return false;
}


function userFromId(){
    if(localStorage.getItem("pseudo") != null){
        obj = {id: localStorage.getItem("pseudo")}
        $.ajax({
            url: "ws/home/getUserById",
            type: "POST",
            contentType: "application/json",
            data: obj,
            dataType: "json",
            success: function (result) {
                localStorage.removeItem("currentUser");
                localStorage.setItem("currentUser", result.user);
                window.location.replace("Profile.html");
            },
            fail: function() {
                alert("ERROR");
            }
        });
    }
    return false;
}

function myPasteFlight(){
    console.log("My past flight");
    var id = localStorage.getItem("pseudo");
    $.ajax({
        url: "ws/home/getMyPastFlights/" + id,
        type: "GET",
        contentType: "application/json",
        success: function (result) {
            if(result.response == "success"){
                var list = JSON.stringify(result.flightListPassenger);
                var obj = list;
                localStorage.setItem("myflightsPassenger", obj);
                window.location.href="past_flights.html";
            }else if(result.response == "fail"){
                alert("Nothing found !");
            }
        },
        fail: function() {
            alert("ERROR");
        }
    });
    return false;
}

function myUpcomingFlight(){
    console.log("My upcoming flight");
    var id = localStorage.getItem("pseudo");
    $.ajax({
        url: "ws/home/getMyUpcomingFlights/" + id,
        type: "GET",
        contentType: "application/json",
        success: function (result) {
            if(result.response == "success"){
                var list = JSON.stringify(result.flightListPassenger);
                var obj = list;
                localStorage.setItem("myflightsPassenger2", obj);
                window.location.href="Upcoming_flight.html";
            }else if(result.response == "fail"){
                alert("Nothing found !");
            }
        },
        fail: function() {
            alert("ERROR");
        }
    });
    return false;
}

function searchFlight(){
    var data = JSON.stringify($('#formIndex').serializeFormJSON());
    var data2 = JSON.parse(data);
    if(data2.balade == "on") data2.balade = true;
    else data2.balade = false;
    if(data2.departure_location != data2.arrival_location) data2.balade = false;
    if (data2.departure_location == "") data2.departure_location = null;
    if (data2.arrival_location == "") data2.arrival_location = null;
    if (data2.departure_date == "") data2.departure_date = null;
    //var data = JSON.stringify(data2);
    $.ajax({
        url: "ws/home/searchflight/" + data2.departure_location +"/"+ data2.arrival_location +"/"+ data2.departure_date +"/"+ data2.balade,
        type: "GET",
        contentType: "application/json",
        success: function (result) {
            if(result.response == "success"){
                var list = JSON.stringify(result.myflightList);
                var obj = list;
                localStorage.removeItem("listFlight");
                localStorage.setItem("listFlight", obj);
                window.location.href="List_flight.html";
            }
            if(result.response == "fail"){
                alert("Oops ! No flights found !");
            }
        },
        fail: function() {
            alert("ERROR");
        }
    });
    return false;
}


function upgradeToPilot(){
    var data = JSON.stringify($('#formUpgrade').serializeFormJSON());
    obj = JSON.parse(data);
    console.log(data);
    obj.id = localStorage.getItem("pseudo");
    console.log(obj);
    data = JSON.stringify(obj);
    console.log(data);
    $.ajax({
        url: "ws/Users/upgrade_to_pilot",
        type: "POST",
        contentType: "application/json",
        data: data,
        dataType: "json",
        async: false,
        success: function (result) {
            getProfile();
        },
        fail: function() {
        }
    });
    return false;
}


function addPlane(){
    var data = JSON.stringify($('#formAddPlane').serializeFormJSON());
    obj = JSON.parse(data);
    console.log(data);
    obj.id_pilot = localStorage.getItem("pseudo");
    console.log(obj);
    data = JSON.stringify(obj);
    console.log(data);
    $.ajax({
        url: "ws/home/addPlane",
        type: "POST",
        contentType: "application/json",
        data: data,
        dataType: "json",
        success: function (result) {
            if(result.response == "success"){
                console.log("success addPlane enter !!");
                window.location.href = "Profile.html";
            }
            if(result.response == "fail"){
                console.log("fail addPlane enter !!!");
            }
        },
        fail: function() {
            alert("oulala");
        }
    });
    return false;
}


function getListPlane(){
    console.log("enter getListPlane");
    var id = localStorage.getItem("pseudo");
    if(id != null){
        console.log("getListPlane pseudo NOT null");
        $.ajax({
            url: "ws/home/getPlane/" + id,
            type: "GET",
            contentType: "application/json",
            data: id,
            dataType: "json",
            async: false,
            success: function (result) {
                console.log("enter result getListPlane");
                if(result.response == "success"){
                    console.log("success getListPlane");
                    console.log(result.planeList);
                    localStorage.setItem("currentListPlane", JSON.stringify(result.planeList));
                }
                if(result.response == "fail"){
                    console.log("fail getListPlane");
                }
            },
            fail: function() {
                console.log("error");
            }
        });
    }
    else{
        alert("fail");
    }
}


function bookFlight(button){
    /* user id + plane id + nbseats*/
    var data1 = {id: button.id};//id du flight
    var data2 = {id: localStorage.getItem("pseudo")};
    var nbr_seats = document.getElementById("number_seats_chosen").selectedIndex = document.getElementById("number_seats_chosen").value;
    var global = {id_flight: data1.id, id_passenger: data2.id, nb_seats: nbr_seats};

    console.log("nbr_seats = "+nbr_seats);
    console.log("id du flight = "+button.id);
    $.ajax({
        url: "ws/home/bookflight",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(global),
        dataType: "json",
        success: function (result) {
            if(result.response == "success"){
                $( function() {
                    $( "#dialogA" ).dialog({
                        title: "Booking Sucessful !",
                        height: 250, maxHeight: 250, minHeight: 250,
                        width: 300, maxWidth: 300, minWidth: 300,
                        show: { effect: "scale", duration: 200 },
                        close: function(){
                            window.location.reload(true); },
                        dialogClass: "no-close",
                        buttons: {
                            "Back Home": function () {
                                window.location.href="index.html";
                            },
                            "Go to Profile": function () {
                                window.location.href="Profile.html";
                            }
                        }
                    });
                } );
            }else if(result.response = "fail"){
                alert("Book Failed !");
            }
        },
        fail: function() {
            alert("FAIL");
        }
    });
    return false;
}


function getBookingList(){
    console.log("enter getBookingList");
    $.ajax({
        url: "ws/home/getBookingsList",
        type: "POST",
        contentType: "application/json",
        data: localStorage.getItem('pseudo'),
        dataType: "json",
        success: function (result) {
            if(result.response == "success"){
                console.log("success getBookingList");
                localStorage.setItem("bookList", JSON.stringify(result.reservations));
            }else if(result.response == "fail"){
                console.log("fail getBookingList");
                alert(result.message);
            }
        },
        fail: function() {
            console.log("MEGA FAIL");
            alert("FAIL");
        }
    });
    return false;
}


function magicParsing(elt){
    var cpt = 0;
    var pn = "", psn="", fid = "";
    for(var i =0; i<elt.length; i++){
        if(elt[i] == '$'){cpt++;}
        else{ switch(cpt){
                case 0: pn += elt[i]; break;
                case 1: psn += elt[i]; break;
                case 2: fid += elt[i]; break;
                default: break;
            }
        }
    }
    var magicJSON = {pilote_name: pn, pilote_surname: psn, id: fid};
    return magicJSON;
}


function reservationConfirm(button){
    /* user id + plane id + nbseats*/
    console.log(button.id);
    var data1 = {id: button.id};
	console.log(data1);
    $.ajax({
        url: "ws/home/confirmPassenger",
        type: "POST",
        contentType: "application/json",
        data: data1,
        dataType: "json",
        success: function (result) {
            alert("SUCCESS");
        },
        fail: function() {
            alert("FAIL");
        }
    });
    return false;
}


function reservationDecline(button){
    /* user id + plane id + nbseats*/
    var data1 = {id: button.id};
    var data2 = {id: localStorage.getItem("pseudo")};
    console.log(button.id);
    $.ajax({
        url: "ws/home/declinePassenger",
        type: "POST",
        contentType: "application/json",
        data: data,
        dataType: "json",
        success: function (result) {
            alert("SUCCESS");
        },
        fail: function() {
            alert("FAIL");
        }
    });
    return false;
}


function editProfile(){
    console.log("enter editProfile");
    var data = JSON.stringify($('#formEditProfile').serializeFormJSON());
    var obj = JSON.parse(data);
    obj.id = localStorage.getItem("pseudo");
    console.log(obj);
    data = JSON.stringify(obj);
    console.log(data);
    $.ajax({
        url: "ws/Users/editProfile",
        type: "PUT",
        contentType: "application/json",
        data: data,
        dataType: "json",
        async: false,
        success: function (result) {
            if(result.response == "success"){
                console.log("enter editProfile success yeaahh!");
                //getProfile();
                window.location.href = "index.html";
            }
            if(result.response == "fail"){
                console.log("enter editProfile fail bouuhh");

            }
        },
        fail: function() {
            console.log("error enter ws");
        }
    });
    return false;
}


function editFlight(){
    console.log("enter editFlight");
    var data = JSON.stringify($('#formEditFlight').serializeFormJSON());
    $.ajax({
        url: "ws/home/modifyflight",
        type: "PUT",
        contentType: "application/json",
        data: data,
        dataType: "json",
        success: function (result) {
            if(result.response == "success"){
                console.log("enter editFlight success youhou");
            }
            if(result.response == "fail"){
                console.log("enter editFlight then fail bouuhh");
            }
        },
        fail: function() {
            console.log("error");
        }
    });
    return false;
}


function deleteAccount(btn){
    var data = {id: btn.id};
    $.ajax({
        url: "ws/Users/delete_account",
        type: "DELETE",
        contentType: "application/json",
        data: data,
        dataType: "json",
        success: function (result) {
            window.location.href = "index.html";
        },
        fail: function() {
            console.log("error");
        }
    });
    return false;
}
