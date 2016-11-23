<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link type="text/css" rel="stylesheet" href="/KasuKasu/css/style.css" />
<link type="text/css" rel="stylesheet" href="/KasuKasu/css/bootstrap.min.css">
<script type="text/javascript" src="/KasuKasu/js/utils.js"></script>
<script type="text/javascript" src="/KasuKasu/js/items.js"></script>
<script type="text/javascript" src="/KasuKasu/js/jquery-3.1.1.js"></script>

<link rel="stylesheet" type="text/css" href="/KasuKasu/css/sidebar.css" />

<title>Recherche d'objets</title>
</head>
<body>

	<%@ include file="/fragments/sidebar.jspf"%>

	<div id="page">
		<div class='layer-center'>
		
			<p class='capital'>Trouvez des Objets � proximit� de vos points d'emprunt</p>
	
			<div id='notifier'></div>
			
			<form action="javascript:(function(){return;})()" method="get"
				OnSubmit="javascript:searchItems(this)">
	
				<div class="form-group row">
					<div class="col-xs-10">
						<input type="text" class="form-control" name="iquery" value="" id="iquery">
					</div>
				</div>
	
				<input type="submit" class="btn btn-primary btn-block"
					value="Rechercher"> <br>
					
				<div id="found-items" class="abootsraper"></div>
			</form>
		</div>
		
	</div>
</body>
</html>

