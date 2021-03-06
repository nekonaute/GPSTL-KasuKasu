/**
 * ANAGBLA Joan & Ouiza B*/
bool=0;
if(readCookie("lang") == "en")
		bool=1;
	else
		if(readCookie("lang") == "fr")
			bool=0;

function Group(id,name,type,owner,date){
	//alert("new Group("+id+","+name+","+type+","+owner+","+date+")");
	this.id=id;
	this.name=name;
	this.type=type;
	this.owner=owner;
	this.date=date;
} 



Group.traiteReponseJSON=function(json){	
	//alert("Group.traiteReponseJSON raw json -> "+JSON.stringify(json));	
	var jsob =JSON.parse(JSON.stringify(json),/*Group.revival*/mirror);
	groups = jsob.groups;
	//alert("Group.traiteReponseJSON cooked jsob -> "+JSON.stringify(jsob));

	if(jsob.error==undefined){
		var fhtm="<br><div id=\"groupsBox\">\n<table>";	
		if(bool==0)
			fhtm+="<tr><th class=\"th\"><span style=\"margin-left:45%\">Mes Groupes</span></th></tr>\n";
		if(bool==1)
			fhtm+="<tr><th class=\"th\"><span style=\"margin-left:45%\">My Groups</span></th></tr>\n";

		if(groups .length==0 && bool==0)
			fhtm+="<h3>Il n'y a aucun groupe pour le moment.</h3>";
		if(groups .length==0 && bool==1)
			fhtm+="<h3>No group for the moment.</h3>";

		for(var i in groups){
			//alert(JSON.stringify(groups[i]));
			fhtm+=(groups[i]).getHTML();
			//alert("JSOB.htmling : "+groups[i].getHTML());
		}		
		fhtm+="</table></div>\n"; 
		//alert("Groups.html = "+fhtm);  
		printHTML("#found-groups",fhtm); 
	}else
		console.log("server error ! : " +jsob.error+"\n");
};

Group.prototype.getHTML=function(){  
	//alert("Group ->getHtml ");
	var s;
	s="<tr><td class=\"groupBox\" id=\"groupBox"+this.id+"\">";

	s+="<span  style=\"float:left;margin-left:1%;margin-right:3%;\" class=\"group-name\" id=\"group-name"+this.id+"\">" +
	"<a href=\"" + groupmembers_jsp + "?gid="+this.id+"&gname="+this.name+" \"><b>"+this.name+"</b></a>" +
	"</span>\n";
	
	if(bool==0)
		s+=" <input style=\"float:right;margin-right:1%;\" type=\"button\" value=\"Supprimer\" class=\"btn btn-primary btn-xs\" " +
		"id=\"del_grp"+this.id+"\" OnClick=\"deleteGroup('"+this.id+"');\"/>\n";
	if(bool==1)
		s+=" <input style=\"float:right;margin-right:1%;\" type=\"button\" value=\"Delete\" class=\"btn btn-primary btn-xs\" " +
		"id=\"del_grp"+this.id+"\" OnClick=\"deleteGroup('"+this.id+"');\"/>\n";
	
	s+="<span style=\"float:right;margin-right:3%;\" class=\"group-infos\">";
	s+="<span style=\"display:none;\" class=\"hiden-group-info\" id=\"group-group-info"+this.id+"\">"+this.group+"</span>";
	s+="<span class=\"group-date\" id=\"group-date"+this.id+"\">"+this.date+"</span>\n";
	s+="</span> ";


	s+="</td></tr>\n";
	return s;
};


function userGroups(){
	$.ajax({
		type : "GET",
		url : UserGroupsServlet,
		data : "",
		dataType : "JSON",
		success : Group.traiteReponseJSON,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}


function createGroup(name){
	if(name.value.length==0)
		return;
	$.ajax({
		type : "POST",
		url : CreateGroupServlet,
		data : "name="+name.value,
		dataType : "JSON",
		success : refresh,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}


function deleteGroup(gid){
	$.ajax({
		type : "POST",
		url : DeleteGroupServlet,
		data : "gid="+gid,
		dataType : "JSON",
		success : refresh,
		error : function(xhr,status,errorthrown){
			console.log(JSON.stringify(xhr + " " + status + " " + errorthrown));
		}
	});
}




function refresh(result){
	if(result.error!=undefined)
		openJModal(2000,result.error);	
	else
		window.location.href = groups_jsp;
}


/**Only for first debugs*/ 
function printJSONGroups(rep){printHTML("#found-Groups", JSON.stringify(rep));}