function init() {
	$('.uparrow').hide();
}

$('.uparrow').click(up());
$('.downarrow').click(down());



function freeze() {
	if($("html").css("position") != "fixed") {
		var top = $("html").scrollTop() ? $("html").scrollTop() : $("body").scrollTop();
		if(window.innerWidth > $("html").width()) {
			$("html").css("overflow-y", "scroll");
		}
		$("html").css({"width": "100%", "height": "100%", "position": "fixed", "top": -top});
	}
}

//Unfreeze page content scrolling
function unfreeze() {
	if($("html").css("position") == "fixed") {
		$("html").css("position", "static");
		$("html, body").scrollTop(-parseInt($("html").css("top")));
		$("html").css({"position": "", "width": "", "height": "", "top": "", "overflow-y": ""});
	}
}
var current_elem = "info";
// unfreeze();$('html,body').scrollTop($(".info").offset().top);freeze();
function down() {
	unfreeze();
	switch(current_elem) {
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
	switch(current_elem) {
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

if ('serviceWorker' in navigator) {
	navigator.serviceWorker.register('sw.js')
	.then(function(reg){
	}).catch(function(err) {
		console.log("SW error: ", err)
	});
}
