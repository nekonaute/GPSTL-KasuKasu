bool=0;
if(readCookie("lang") == "en")
			bool=1;
		else
			if(readCookie("lang") == "fr")
				bool=0;
function connexion(form) 
{
	var mail = form.mail.value;
	var pass = form.pass.value;

	var ok = checkInput(mail, pass);
	if (ok) 
	{
		printHTML("#notifier","");
		checkConnexion(mail, pass);
	}
}



function checkInput(mail, pass) 
{
	if(mail.length==0)
	{
		//func_error_login("Email missing");
		if(bool==0)
			func_error_login("Email manquant");
		if(bool==1)
			func_error_login("Email missing");
		return false;
	}

	if(pass.length==0)
	{
		//func_error_login("Password missing");
		if(bool==0)
			func_error_login("Mot de passe manquant");
		if(bool==1)
			func_error_login("Password missing");
		return false;
	}
	
	if(pass.length<8)
	{
		//func_error_login("Password too short");
		if(bool==0)
			func_error_login("Mot de passe trop court");
		if(bool==1)
			func_error_login("Password too short");
		return false;
	}
	else 
	{
		return true;
	}
}


function checkConnexion(mailv, passv) 
{
	$.ajax({
		type : "POST",
		url : ConnectUserServlet,
		data : {mail : mailv, pass : passv},
		dataType : "json",
		success : ProcessLoginRequest,
		error : function(xhr,status,errorthrown){
			if(xhr.warning!= undefined)
			alert(xhr.warning);
			if(xhr.error!= undefined)
				alert(xhr.error);
			else alert(JSON.stringify(xhr))
		}
	});
}




function ProcessLoginRequest(rep) 
{
	if (rep.error != undefined)  
	{
		func_error_login(rep.error);
	}
	else 
	{
		window.location.href = kasukasu.private.dashboard;
	}
}


function func_error_login(msg)
{
		printHTML("#notifier",msg);
		$("#notifier").css({
			"color":"red",
			"font-size": "80%"
		})
}


function printHTML(dom,htm)
{ 
	$(dom).html(htm);
}





