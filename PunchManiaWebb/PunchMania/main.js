// Author Marcus Nordström

function init() {
  $('.uparrow').hide();
  navigator.vibrate = navigator.vibrate || navigator.webkitVibrate || navigator.mozVibrate || navigator.msVibrate;
}

$('.uparrow').click(up());
$('.downarrow').click(down());

function freeze() {
  if ($("html").css("position") != "fixed") {
    var top = $("html").scrollTop() ? $("html").scrollTop() : $("body").scrollTop();
    if (window.innerWidth > $("html").width()) {
      $("html").css("overflow-y", "scroll");
    }
    $("html").css({
      "width": "100%",
      "height": "100%",
      "position": "fixed",
      "top": -top
    });
  }
}

function unfreeze() {
  if ($("html").css("position") == "fixed") {
    $("html").css("position", "static");
    $("html, body").scrollTop(-parseInt($("html").css("top")));
    $("html").css({
      "position": "",
      "width": "",
      "height": "",
      "top": "",
      "overflow-y": ""
    });
  }
}
var current_elem = "info";
function down() {
  unfreeze();
  switch (current_elem) {
    case "info":
      $('html,body').scrollTop($(".hs").offset().top);
      $('.uparrow').show();
      current_elem = "hs";
      if (!($('.q').length)) {
        $('.downarrow').hide();
      }
      $('.info img').hide();
      break;
    case "hs":
      $('html,body').scrollTop($(".q").offset().top);
      $('.downarrow').hide();
      current_elem = "q";
      break;
  }
  freeze();
}

function up() {
  unfreeze();
  switch (current_elem) {
    case "hs":
      $('html,body').scrollTop($(".info").offset().top);
      $('.uparrow').hide();
      $('.downarrow').show();
      current_elem = "info";
      $('.info img').show();
      break;
    case "q":
      $('html,body').scrollTop($(".hs").offset().top);
      $('.downarrow').show();
      current_elem = "hs";
      break;
  }
  freeze();
}

if (!navigator.serviceWorker.controller) {
  navigator.serviceWorker.register('sw_offline.js', {
    scope: './'
  }).then(function(reg) {
    console.log('sw_offline.js @ ' + reg.scope);
  });
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

if (localStorage !== "undefined") {
  if (getCookie('uuid') != "" && localStorage.getItem('uuid') == null) {
    localStorage.setItem('uuid', getCookie('uuid'));
  } else if (getCookie('uuid') == "NaN" && localStorage.getItem('uuid') != null) {
    localStorage.removeItem('uuid');
  } else if (getCookie('uuid') == "" && localStorage.getItem('uuid') != null) {
    setCookie('uuid', localStorage.getItem('uuid'), 365);
  }
}
var PrevTime = "";
var Startup = false;
function recentColor() {
    var text = document.getElementById("recentText").getAttribute("data-unix-time");
    if (text != "") {
      var CurrentDate = Date.now()/1000.0;
      var PunchDate = Date.parse(text)/1000.0;
      var DateDiff = CurrentDate - PunchDate;
      if(DateDiff < 60) {
        var red = parseInt(DateDiff*4.25);
        red = 255 - red;
        if (red < 63) {
          red = 63;
        }
        $('#recentText').css("color", "rgb("+ red +", 63, 63)");
      }
      if (PrevTime != PunchDate) {
        PrevTime = PunchDate;
        if (Startup) {
          var hs = document.getElementById("recentText").getAttribute("data-highScore");
          if (hs != "") {
            switch (hs) {
              case 'global':
                navigator.vibrate([400, 300, 400]);
                break;
              case 'personal':
                navigator.vibrate([300, 200, 300]);
                break;
              case 'no':
                break;
              default:

              }
          }
        } else {
          Startup = true;
        }
      }
    }
}
