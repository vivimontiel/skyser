jQuery(document).ready(function ($) {
    //move nav element position according to window width
    moveNavigation();
    $(window).on('resize', function () {
        (!window.requestAnimationFrame) ? setTimeout(moveNavigation, 300) : window.requestAnimationFrame(moveNavigation);
    });

    //mobile version - open/close navigation
    $('.cd-nav-trigger').on('click', function (event) {
        event.preventDefault();
        if ($('header').hasClass('nav-is-visible')) $('.moves-out').removeClass('moves-out');

        $('header').toggleClass('nav-is-visible');
        $('.cd-main-nav').toggleClass('nav-is-visible');
        $('.cd-main-content').toggleClass('nav-is-visible');
    });

    //mobile version - go back to main navigation
    $('.go-back').on('click', function (event) {
        event.preventDefault();
        $('.cd-main-nav').removeClass('moves-out');
    });

    //open sub-navigation
    $('.cd-subnav-trigger').on('click', function (event) {
        event.preventDefault();
        $('.cd-main-nav').toggleClass('moves-out');
    });

    function moveNavigation() {
        var navigation = $('.cd-main-nav-wrapper');
        var screenSize = checkWindowWidth();
        if (screenSize) {
            //desktop screen - insert navigation inside header element
            navigation.detach();
            navigation.insertBefore('.cd-nav-trigger');
        } else {
            //mobile screen - insert navigation after .cd-main-content element
            navigation.detach();
            navigation.insertAfter('.cd-main-content');
        }
    }

    function checkWindowWidth() {
        var mq = window.getComputedStyle(document.querySelector('header'), '::before').getPropertyValue('content').replace(/"/g, '').replace(/'/g, "");
        return (mq == 'mobile') ? false : true;
    }


});

//


jQuery(document).ready(function ($) {
    //hide the subtle gradient layer (.cd-pricing-list > li::after) when pricing table has been scrolled to the end (mobile version only)
    checkScrolling($('.cd-pricing-body'));
    $(window).on('resize', function () {
        window.requestAnimationFrame(function () {
            checkScrolling($('.cd-pricing-body'))
        });
    });
    $('.cd-pricing-body').on('scroll', function () {
        var selected = $(this);
        window.requestAnimationFrame(function () {
            checkScrolling(selected)
        });
    });

    function checkScrolling(tables) {
        tables.each(function () {
            var table = $(this),
                totalTableWidth = parseInt(table.children('.cd-pricing-features').width()),
                tableViewport = parseInt(table.width());
            console.log(table.scrollLeft() - totalTableWidth + tableViewport)
            if (table.scrollLeft() >= totalTableWidth - tableViewport - 1) {
                table.parent('li').addClass('is-ended');
            } else {
                table.parent('li').removeClass('is-ended');
            }
        });
    }
});


jQuery(document).ready(function ($) {
    if ($('.floating-labels').length > 0) floatLabels();

    function floatLabels() {
        var inputFields = $('.floating-labels .cd-label').next();
        inputFields.each(function () {
            var singleInput = $(this);
            //check if user is filling one of the form fields
            checkVal(singleInput);
            singleInput.on('change keyup', function () {
                checkVal(singleInput);
            });
        });
    }

    function checkVal(inputField) {
        (inputField.val() == '') ? inputField.prev('.cd-label').removeClass('float') : inputField.prev('.cd-label').addClass('float');
    }
});


function isPilot() {
    var res = null; 
    $.ajax({
        url: "ws/Users/checkPilot",
        type: "POST",
        contentType: "application/json",
        data: localStorage.getItem('pseudo'),
        dataType: "json",
        global: false,
        async:false,
        success: function (result) {
            if(result.response == "success"){
                console.log("la personne connecté est un pilot");
                res = true;
            }else if(result.response == "fail"){
                console.log("la personne connecté est un passenger");
                res = false;
            }
        },
        fail: function() {
           alert("Oops Something went wrong !");
        }
    });
    console.log("c'est = "+res);
    return res;
}


function dynamicHeader() {
    console.log("enter dynamicHeader");
    area = document.getElementById("dynamic-nav-header");
    node = document.createElement("ul");
    node.className = "cd-main-nav"
    if (localStorage.getItem("pseudo") == null) {
        console.log("basic visitor");
        node.innerHTML =
            '<li><a href="About.html">About</a></li>' +
            '<li><a href="index.html">Search a flight</a></li>' +
            '<li><a href="Sign_up.html">Sign up</a></li>' +
            '<li>' +
                '<a href="#2" class="cd-subnav-trigger"><span>Log in</span></a>' +
                '<ul>' +
                    '<a href="Sign_up.html">Don\'t have an account? Click here</a>' +
                    '<li class="go-back"><a href="#0">Menu</a></li>' +
                    '<li><form class="cd-form-login" id="signinForm" method="post" onsubmit="return logIn(this)">' +
                        '<li>' +
                            '<input class="email error" type="email" name="email" placeholder="Email" required>' +
                        '</li>' +
                        '<li>' +
                            '<input class="password" type="password" name="password" placeholder="Password" required>' +
                        '</li>' +
                        '<li><input type="submit" name="connexion" value="Sign in"></li>' +
                    '</form></li>' +
                    '<li><a href="#4" class="placeholder">Placeholder</a></li>' +
                '</ul>' +
            '</li>';
               

        area.appendChild(node);
        console.log("ok");
    } else {
        var ff = isPilot();
        console.log("---------->"+ff);
        if (ff == true) {
            node.innerHTML =
                '<li><a href="Add_flight.html">Add Flight</a></li>' +
                '<li><a href="Profile.html">Profile</a></li>' +
                '<li><a href="add_airplane.html">Add Airplane</a></li>' +
                '<li><a href="Upcoming_flight.html">Upcoming Flights</a></li>' +
                '<li><a href="past_flights.html">Past Flights</a></li>' +
                '<li><a href="Reservations.html">Pending Reservations</a></li>' +
                '<li>'+
                    '<a href="#2" class="cd-subnav-trigger"><span>Log-out</span></a>'+
                    '<ul>'+
                        
                        '<li class="go-back"><a href="#0">Menu</a></li>'+
                        '<li><input type="submit" onclick="log_out()" value="Log out"></li>' +
                        

                        '<li><a href="#4" class="placeholder">Placeholder</a></li>'+
                    '</ul>'+
                '</li>';
            area.appendChild(node);
        } else {
            node.innerHTML =
                '<li><a href="Profile.html">Profile</a></li>' +
                '<li><a href="Upcoming_flight.html">Upcoming Flights</a></li>' +
                '<li><a href="past_flights.html">Past Flights</a></li>' +
                '<li>'+
                    '<a href="#2" class="cd-subnav-trigger"><span>Log-out</span></a>'+
                    '<ul>'+
                        
                        '<li class="go-back"><a href="#0">Menu</a></li>'+
                        '<li><input type="submit" onclick="log_out()" value="Log out"></li>' +
                        

                        '<li><a href="#4" class="placeholder">Placeholder</a></li>'+
                    '</ul>'+
                '</li>';
            area.appendChild(node);
        }
    }
}
