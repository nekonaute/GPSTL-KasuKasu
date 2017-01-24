/**
 * ANAGBLA Joan*/
bool=0;
applicants_shared_div_state_opened=false;
if(document.cookie.search("lang=en")!=-1)
	bool=1;
else
	if(document.cookie.search("lang=fr")!=-1)
		bool=0;
function Item(id,owner,group,date,longitude,latitude,title,description,permission){
	//alert("new Item("+id+","+title+","+group+","+longitude+","+latitude+","+date+","+description+","+permission+")");
	this.id=id;
	this.owner=owner;
	this.group=group;
	this.date=date;
	this.longitude=longitude;
	this.latitude=latitude;
	this.title=title;
	this.description=description;
	this.permission=permission;
} 


Item.getItemTraiteReponseJSON=function(json){	
	//alert("Item.getItemTraiteReponseJSON raw json -> "+JSON.stringify(json));	
	var jsob =JSON.parse(JSON.stringify(json),/*Item.revival*/mirror);
	items = jsob.items;
//	alert("Item.getItemTraiteReponseJSON cooked jsob -> "+JSON.stringify(jsob));

	if(jsob.error==undefined){
		var fhtm="<br><div id=\"itemsBox\">";	

		if(items.length==0 && bool==0)
			fhtm+="<h3>Il n'y a rien à afficher.</h3>";
		if(items.length==0 && bool==1)
			fhtm+="<h3>Nothing to display.</h3>";
		for(var i in items){
			//alert(JSON.stringify(items[i]));
			if(items[i].permission==1){
				fhtm+=(items[i]).getHTML2();
				//alert("JSOB.htmling : "+items[i].getHTML2());
			}
			else if(items[i].permission === 0){
				fhtm+=(items[i]).getHTML();
				//alert("JSOB.htmling : "+items[i].getHTML());
			}
		}		

		// DatePickerDialog
		fhtm+="<div id='datePickerDialog'></div>"; 
		fhtm+="</div>\n"; 
		//alert("items.html = "+fhtm);  
		printHTML("#found-items",fhtm); 
	}else
		console.log("server error ! : " +jsob.error+"\n");
};


Item.listItemTraiteReponseJSON=function(json){	
	//alert("Item.getItemTraiteReponseJSON raw json -> "+JSON.stringify(json));	
	var jsob =JSON.parse(JSON.stringify(json),/*Item.revival*/mirror);
	items = jsob.items;
//	alert("Item.getItemTraiteReponseJSON cooked jsob -> "+JSON.stringify(jsob));

	if(jsob.error==undefined){
		var fhtm="<br><div id=\"itemsBox\">";	

		if(items.length==0 && bool==0)
			fhtm+="<h3>Il n'y a rien à afficher.</h3>";
		if(items.length==0 && bool==1)
			fhtm+="<h3>Nothing to display.</h3>";
		for(var i in items){
			//alert(JSON.stringify(items[i]));
			if(items[i].permission==1){
				fhtm+=(items[i]).getHTML0();
				//alert("JSOB.htmling : "+items[i].getHTML2());
			}
			else if(items[i].permission === 0){
				fhtm+=(items[i]).getHTML();
				//alert("JSOB.htmling : "+items[i].getHTML());
			}
		}		

		// DatePickerDialog
		fhtm+="<div id='datePickerDialog'></div>"; 
		fhtm+="</div>\n"; 
		//alert("items.html = "+fhtm);  
		printHTML("#found-items",fhtm); 

	}else
		console.log("server error ! : " +jsob.error+"\n");
};

Item.prototype.getHTML0=function(){  
	//alert("Item ->getHtml ");
	var s;
	s="<div class=\"itemBox\" id=\"itemBox"+this.id+"\">";
	s+="<div class=\"item-title\" id=\"item-title"+this.id+"\">";
	s+="<a href=" + item_jsp + "?id="+this.id+"&title="+this.title+">";//TODO PREG REPLACE TITLE IF IS SENT BY URL 
	s+="<b>"+this.title+"</b>";
	s+="</a>";
	s+="</div>\n";	
	s+="<div class=\"item-infos\">";
	s+="<span style=\"display:none;\" class=\"hiden-item-info\" id=\"item-owner-info"+this.id+"\">"+this.owner+"</span>";
	s+="<span style=\"display:none;\" class=\"hiden-item-info\" id=\"item-group-info"+this.id+"\">"+this.group+"</span>";
	s+="</div> ";
	s+="<div class=\"item-desc\" id=\"item-desc"+this.id+"\">"+this.description+"</div><br>\n";
	s+="<div class=\"item-more\">";
	s+="<span class=\"item-date\" id=\"item-date"+this.id+"\">"+this.date+"</span>\n";
	s+="</div>";
	s+="</div><hr><br>\n";
	return s;
};


Item.prototype.getHTML=function(){  
	//alert("Item ->getHtml ");
	var s;
	s="<div class=\"itemBox\" id=\"itemBox"+this.id+"\">";
	s+="<div class=\"item-title\" id=\"item-title"+this.id+"\">";
	s+="<a href=" + item_jsp + "?id="+this.id+"&title="+this.title+">";//TODO PREG REPLACE TITLE IF IS SENT BY URL 
	s+="<b>"+this.title+"</b>";
	s+="</a>";
	s+="</div>\n";	
	s+="<div class=\"item-infos\">";
	s+="<span style=\"display:none;\" class=\"hiden-item-info\" id=\"item-owner-info"+this.id+"\">"+this.owner+"</span>";
	s+="<span style=\"display:none;\" class=\"hiden-item-info\" id=\"item-group-info"+this.id+"\">"+this.group+"</span>";
	s+="</div> ";
	s+="<div class=\"item-desc\" id=\"item-desc"+this.id+"\">"+this.description+"</div><br>\n";
	s+="<div class=\"item-more\">";
	s+="<span class=\"item-date\" id=\"item-date"+this.id+"\">"+this.date+"</span>\n";
	if(bool==0)
		s+=" <input style=\"margin-left:50%;\" type=\"button\" value=\"Je le veux\" class=\"btn btn-primary btn-xs\" " +
		"id=\"iwantit_btn"+this.id+"\" OnClick=\"request_item('"+this.id+"');\"/>\n";
	if(bool==1)
		s+=" <input style=\"margin-left:50%;\" type=\"button\" value=\"I want it\" class=\"btn btn-primary btn-xs\" " +
		"id=\"iwantit_btn"+this.id+"\" OnClick=\"request_item('"+this.id+"');\"/>\n";
	s+="</div>";
	s+="</div><hr><br>\n";
	return s;
};


Item.prototype.getHTML2=function(){  
	//alert("Item ->getHtml ");
	var s;
	s="<div class=\"itemBox\" id=\"itemBox"+this.id+"\">";
	s+="<div class=\"item-title\" id=\"item-title"+this.id+"\">";
	s+="<a href=" + item_jsp + "?id="+this.id+"&title="+this.title+">";
	s+="<b>"+this.title+"</b>";
	s+="</a>";
	s+="</div>\n";		
	s+="<div class=\"item-infos\">";
	s+="<span style=\"display:none;\" class=\"hiden-item-info\" id=\"item-group-info"+this.id+"\">"+this.group+"</span>";
	s+="</div> ";
	s+="<div class=\"item-desc\" id=\"item-desc"+this.id+"\">"+this.description+"</div><br>\n";
	s+="<div class=\"item-more\">";
	s+=" <input style=\"float:left;margin-right:5%;\" type=\"image\" " +
	" alt=\"Submit\" width=\"30\" type=\"image\" height=\"30\" " +
	" src=\"icons/Feedback_Filled_50.png\" class=\"iwantit_btn\" " +
	"id=\"unwrap_applicants_btn"+this.id+"\" OnClick=\"javascript:item_applicants('"+this.id+"')\"/>\n";
	s+=" <input style=\"float:left;margin-right:75%;\" type=\"image\" " +
	" alt=\"Submit\" width=\"30\" type=\"image\" height=\"30\" " +
	" src=\"icons/Empty_Trash_Filled-50.png\" class=\"iwantit_btn\" " +
	"id=\"remove_item_btn"+this.id+"\" OnClick=\"javascript:removeItem('"+this.id+"')\"/>\n";
	if(bool==0)
		s+="<input style=\"float:left;\" " +
		"onclick=\"window.location.href='" + ObjectManagementServlet + "?objectId="+this.id+"&data=null'\"" +
		" type=\"button\" class=\"btn btn-primary btn-xs\" name=\"modify\" value=\"modifier\">";
	if(bool==1)
		s+="<input style=\"float:left;\"" +
		" onclick=\"window.location.href='" + ObjectManagementServlet + "?objectId="+this.id+"&data=null'\"" +
		" type=\"button\" class=\"btn btn-primary btn-xs\" name=\"modify\" value=\"modify\">";

	s+="<span  class=\"item-date\" id=\"item-date"+this.id+"\">"+this.date+"</span>\n";
	s+="</div>";
	s+="</div><hr><br>\n";
	return s;
};


function searchMRItems(query){
	$.ajax({
		type : "GET",
		url : SearchItemsServlet,
		data : "query=" +query,
		dataType : "JSON",
		success : Item.listItemTraiteReponseJSON,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}
function searchItems(query){
	console.log("query="+query);
	searchMRItems(query);
}


function filterUserItems(query){
	console.log("query="+query);	
	userItems(query);
}

function userItems(query){
	$.ajax({
		type : "GET",
		url : UserItemsServlet,
		data : "query=" +query,
		dataType : "JSON",
		success : Item.listItemTraiteReponseJSON,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}

function item_applicants(id) {
	reset_applicants_shared_div(id);

	$.ajax({
		type : "GET",
		url : ItemApplicantsListServlet,
		data : "id=" +id,
		dataType : "JSON",
		success : ProcessFindApplicants,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}

function user_items_applicants() {
	$.ajax({
		type : "GET",
		url : UserItemsApplicantsServlet,
		data : "",
		dataType : "JSON",
		success : ProcessFindApplicants2,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}


function removeItem(id) {
	reset_applicants_shared_div(id);

	$.ajax({
		type : "POST",
		url : RemoveItemServlet,
		data : "id=" +id,
		dataType : "JSON",
		success : refresh,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}

function refresh(result){
	if(result.error!=undefined)
		fillNOTIFIER(result.error);
	else
		window.location.reload();
}


function reset_applicants_shared_div(id){
	if(applicants_shared_div_state_opened)
		removeElt("#item-applicants");
	else{
		removeElt("#item-applicants");
		printHTMLSup ("#itemBox"+id,"<div id=\"item-applicants\">" +
				"<div id=\"current_item\" style=\"display:none;\">"+id+"<div>" +
		"<br></div>");
	}
	applicants_shared_div_state_opened=!applicants_shared_div_state_opened;
}



function getItem(id){
	$.ajax({
		type : "GET",
		url : GetItemServlet,
		data : "id=" +id,
		dataType : "JSON",
		success : Item.getItemTraiteReponseJSON,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}



function ProcessFindApplicants(rep) {
	var message;
	if(bool==0)
		message = "<table class=\"table\">" +
		"<tr>" +
		"<th>Nom</th><th>Prenom</th><th>Profil</th>" +
		"</tr>";

	if(bool==1)
		message = "<table class=\"table\">" +
		"<tr>" +
		"<th>Last name</th><th>First name</th><th>Profile</th>" +
		"</tr>";
	var endmessage ="</table>";

	var bodymessage ="";
	var nb=0;
	if(rep.users != undefined)
		$.each(rep.users, function(user, profile) {
			var x,y,z;
			if(user !='warning'){
				$.each(profile, function(field, value) {
					//console.log(field); console.log(value);
					if(field=='name')
						x=value;
					if(field=='firstname')
						y=value;
					if(field=='id')
						z=value;
				});

				if(z==rep.id)return;//Skip if the user is yourself
				nb++;
				if(bool==0)
					bodymessage +=
						"<tr>" +
						"<td>"+x+"</td>" +
						"<td>"+y+"</td>"+
						"<td><a href=\"" + memberprofile_jsp + "?id="+z+"\"> Afficher le profil </a></td>"+
						"<td>" +
						"<input style=\"margin-left:5%;\" type=\"button\" value=\"Ignorer\" class=\"btn btn-primary btn-xs\" " +
						"id=\"refuse_item_request_btn"+this.id+"\" OnClick=\"refuse_item_request('"+z+"','"+$("#current_item").text()+"');\"/>\n"+
						"<input style=\"float:right;\" type=\"button\" value=\"Valider\" class=\"btn btn-primary btn-xs\" " +
						"id=\"accept_item_request_btn"+this.id+"\" OnClick=\"accept_item_request('"+z+"','"+$("#current_item").text()+"');\"/>\n";
				"</tr>";
				if(bool==1)
					bodymessage +=
						"<tr>" +
						"<td>"+x+"</td>" +
						"<td>"+y+"</td>"+
						"<td><a href=\"" + memberprofile_jsp + "?id="+z+"\"> Show profile </a></td>"+
						"<td>" +
						"<input style=\"margin-left:5%;\" type=\"button\" value=\"Ignore\" class=\"accept_request_btn\" " +
						"id=\"refuse_item_request_btn"+this.id+"\" OnClick=\"refuse_item_request('"+z+"','"+$("#current_item").text()+"');\"/>\n"+
						"<input style=\"float:right;\" type=\"button\" value=\"Validate\" class=\"accept_request_btn\" " +
						"id=\"accept_item_request_btn"+this.id+"\" OnClick=\"accept_item_request('"+z+"','"+$("#current_item").text()+"');\"/>\n";
				"</tr>";

			} 
		});
	if(nb==0){
		if(bool==0)
			message="<br>Aucune demande sur cet objet pour le moment.";
		if(bool==1)
			message="<br>No request on this item for the moment.";

		bodymessage="";
		endmessage="";
	}
	endmessage+="<br>";

	var iahtm=(message+bodymessage+endmessage);
	printHTML("#item-applicants",iahtm);
}






function ProcessFindApplicants2(rep) {
	var message;
	if(bool==0)
		message = "<table class=\"table\">" +
		"<tr>" +
		"<th>Objet</th><th>Nom</th><th>Prenom</th><th>Profil</th>" +
		"</tr>";

	if(bool==1)
		message = "<table class=\"table\">" +
		"<tr>" +
		"<th>Item</th><th>Last name</th><th>First name</th><th>Profile</th>" +
		"</tr>";
	var endmessage ="</table>";

	var bodymessage ="";
	var nb=0;
	if(rep.users != undefined)
		$.each(rep.users, function(user, profile) {
			var x,y,z,iid,ititle;
			if(user !='warning'){
				$.each(profile, function(field, value) {
					//console.log(field); console.log(value);
					if(field=='name')
						x=value;
					if(field=='firstname')
						y=value;
					if(field=='id')
						z=value;
					if(field=='itemid')
						iid=value;
					if(field=='itemtitle')
						ititle=value;
				});

				if(z==rep.id)return;//Skip if the user is yourself
				nb++;
				if(bool==0)
					bodymessage +=
						"<tr>" +
						"<td><b><a href="+item_jsp+"?id="+iid+"&title="+ititle+">"+ititle+"</a></b></td>" +
						"<td>"+x+"</td>" +
						"<td>"+y+"</td>"+
						"<td><a href=\"" + memberprofile_jsp + "?id="+z+"\"> Afficher le profil </a></td>"+
						"<td>" +
						"<input style=\"margin-left:5%;\" type=\"button\" value=\"Ignorer\" class=\"btn btn-primary btn-xs\" " +
						"id=\"refuse_item_request_btn"+this.id+"\" OnClick=\"refuse_item_request('"+z+"','"+iid+"');\"/>\n"+
						"<input style=\"float:right;\" type=\"button\" value=\"Valider\" class=\"btn btn-primary btn-xs\" " +
						"id=\"accept_item_request_btn"+this.id+"\" OnClick=\"accept_item_request('"+z+"','"+iid+"');\"/>\n";
				"</tr>";
				if(bool==1)
					bodymessage +=
						"<tr>" +
						"<td><b><a href="+item_jsp+"?id="+iid+"&title="+ititle+">"+ititle+"</a></b></td>" +
						"<td>"+ititle+"</td>" +
						"<td>"+x+"</td>" +
						"<td>"+y+"</td>"+
						"<td><a href=\"" + memberprofile_jsp + "?id="+z+"\"> Show profile </a></td>"+
						"<td>" +
						"<input style=\"margin-left:5%;\" type=\"button\" value=\"Ignore\" class=\"accept_request_btn\" " +
						"id=\"refuse_item_request_btn"+this.id+"\" OnClick=\"refuse_item_request('"+z+"','"+iid+"');\"/>\n"+
						"<input style=\"float:right;\" type=\"button\" value=\"Validate\" class=\"accept_request_btn\" " +
						"id=\"accept_item_request_btn"+this.id+"\" OnClick=\"accept_item_request('"+z+"','"+iid+"');\"/>\n";
				"</tr>";

			} 
		});
	if(nb==0){
		if(bool==0)
			message="<br>Aucune demande pour le moment.";
		if(bool==1)
			message="<br>No request for the moment.";

		bodymessage="";
		endmessage="";
	}
	endmessage+="<br>";

	var iahtm=(message+bodymessage+endmessage);
	printHTML("#item-applicants",iahtm);
}

