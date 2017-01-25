bool=0;
if(document.cookie.search("lang=en")!=-1)
	bool=1;
else
	if(document.cookie.search("lang=fr")!=-1)
		bool=0;



function init()
{

	$.ajax({
		type : "POST",
		url : "/KasuKasu/userinfospageaide",
		xhrFields: 
		{
			withCredentials: true
		},
		dataType : "JSON",
		success : function (data)
		{

			act = document.getElementById('act');
			if(bool==0) act.innerHTML=' Actuellement:';
			else if (bool==1) act.innerHTML=' Currently:';

			emprunt=document.getElementById('emprunt');
			if(bool==0) emprunt.innerHTML='- Vous empruntez '+data.emprunt+' objet(s),';
			else if(bool==1) emprunt.innerHTML='- You borrow '+data.emprunt+' object(s),';

			pret=document.getElementById('pret');
			if(bool==0) pret.innerHTML='- Vous prêtez '+data.loaned+' objets parmi vos '+data.pret+' objet(s).';
			else if(bool==1) pret.innerHTML='- You lend '+data.loaned+' objects among your '+data.pret+' object(s).';

			pend=document.getElementById('pend');
			if(bool==0) pend.innerHTML='Vous avez '+data.pending+' demande(s) de prêts en attente'+'<button class=\"btn btn-primary btn-xs\" style=\"margin-left:1px;margin-right:3px;\" onclick= locations(\"/KasuKasu/restricted/applicants.jsp\") >Voir</button>';
			else if(bool==1) pend.innerHTML='You have '+data.pending+' pending loan request(s)'+'<button class=\"btn btn-primary btn-xs\" style=\"margin-left:1px;margin-right:3px;\" onclick=locations(\"/KasuKasu/restricted/applicants.jsp\") >See</button>';
			
			back=document.getElementById('back');
			//if (data.back=="undefined") data.back=0;
			if(bool==0) back.innerHTML='Vous avez '+data.back+' retrour(s) à valider'+'<button class=\"btn btn-primary btn-xs\" style=\"margin-left:1px;margin-right:3px;\"onclick= locations(\"/KasuKasu/restricted/evaluation.jsp\") >Voir</button>';
			else if(bool==1) back.innerHTML='You have '+data.back+' return(s) to validate'+'<button class=\"btn btn-primary btn-xs\" style=\"margin-left:1px;margin-right:3px;\"onclick="locations(\"/KasuKasu/restricted/evaluation.jsp\")">See</button>';


			//populatePre('aide.txt');


		},		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}


	});

}

function locations(href)
{
	window.location.href=href; 
}

function locations2()
{
	window.location.href='/KasuKasu/restricted/evaluation.jsp'; 
}

function populatePre(url) {
	var xhr = new XMLHttpRequest();
	xhr.onload = function () {
		document.getElementById('contents').textContent = this.responseText;
	};
	xhr.open('GET', url);
	xhr.send();
}
