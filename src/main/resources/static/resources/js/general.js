

function popup(mylink, windowname) {
	if (!window.focus)
		return true;


	var href;
	if (typeof (mylink) == 'string')
		href = mylink;
	else {
		href = mylink.href;
		var IE6 = (navigator.userAgent.indexOf("MSIE 6") >= 0) ? true : false;
		if (IE6) {
			window.showModalDialog(href, windowname, 'dialogWidth:730px;dialogHeight:620px;dialogTop:190px;dialogLeft:190px;status:no;scroll:no;');
		}
		else {
			window.showModalDialog(href, windowname, 'dialogWidth:730px;dialogHeight:620px;dialogTop:190px;dialogLeft:190px;status:no;scroll:no;');
		}
		return false;
	}
}

function popupUser(mylink, windowname) {
	if (!window.focus)
		return true;


	var href;
	if (typeof (mylink) == 'string')
		href = mylink;
	else {
		href = mylink.href;
		var IE6 = (navigator.userAgent.indexOf("MSIE 6") >= 0) ? true : false;
		if (IE6) {
			window.showModalDialog(href, windowname, 'dialogWidth:730px;dialogHeight:350px;dialogTop:190px;dialogLeft:190px;status:no;scroll:no;');
		}
		else {
			window.showModalDialog(href, windowname, 'dialogWidth:730px;dialogHeight:350px;dialogTop:190px;dialogLeft:190px;status:no;scroll:no;');
		}
		return false;
	}
}

function getPara(name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if (results == null)
		return "";
	else
		return results[1];
}

function menu() {
	//left side veritcal menu

	var html = '';

	html = html + '<div id="accordion">';
	html = html + '<ul>';
	html = html + '<li>';
	html = html + '<div>Class Management</div>';
	html = html + '<ul>'
	html = html + '<li><a href="/user/setupClass">Class Registration</a></li>';
	html = html + '<li><a href="/user/setupStudentSearch">Student Search</a></li>';
	html = html + '<li><a href="/user/setupStudentAdd">Student Registration</a></li>';
	html = html + '</ul>';
	html = html + '</li>';
	html = html + '<li id="no-child">';
	html = html + '<a href="/user/setupUser" style="font-weight:bold;">User Management</a>';
	html = html + '</li>';
	html = html + '</ul>';
	html = html + '</div>';

	document.getElementById("left_menu").innerHTML = html;

}

function isNumber(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
}