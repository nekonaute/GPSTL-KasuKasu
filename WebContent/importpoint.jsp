<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title id='titre'>Lieux d'échange</title>
<link type="text/css" rel="stylesheet" href="/KasuKasu/css/style.css" />
<link type="text/css" rel="stylesheet"
	href="/KasuKasu/css/bootstrap.min.css">
<script type="text/javascript" src="/KasuKasu/js/jquery-3.1.1.js"></script>
<script src="http://www.openlayers.org/api/OpenLayers.js"></script>
<script type="text/javascript" src="/KasuKasu/js/tether.min.js"></script>
<script type="text/javascript" src="/KasuKasu/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/KasuKasu/js/importpoint.js"
	charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="/KasuKasu/css/sidebar.css" />

<style type="text/css">
html, body, #mapdiv {
	width: 100%;
	height: 100%;
	margin: 0;
}

.olImageLoadError {
	display: none;
}
</style>
</head>
<body onload="javascript:init()">
	<%@ include file="/fragments/sidebar.jspf"%>
<!-- 	<div id="mapdiv"></div> -->
	<div id="page">

		<div class='col-md-6'>
			<div id="mapdiv" style="width: 500px; height: 500px"></div>
		</div>
		
	<div class='modal-body row'>

			<div class='col-md-6'>
				<p>
				<img alt="" src="data/marker-red.png" width="10" height="10">
				Vos points d'échange
                <br></br>				
				<img alt="" src="data/marker.png" width="10" height="10">
				Points d'échange de vos amis
				<br></br>	
				Double-cliquez sur la carte pour ajouter un lieu
				</p>
			</div>

			
		</div>
		

	</div>
	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Ajouter le lieu</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<div class="form-group row">
							<label for="old_email_input" class="col-xs-3 col-form-label">Nom</label>
							<div class="col-xs-8">
								<input class="form-control" value="" id="nom_input"
									name="nom_input" placeholder="Nom (obligatoire)"
									required="required">
							</div>
						</div>
						<div class="form-group row">
							<label for="old_password_input" class="col-xs-3 col-form-label">Portée</label>
							<div class="col-xs-8">
								<input class="form-control" value="" id="radius_input"
									type="number" name="radius_input"
									placeholder="Portée (obligatoire)" required="required">
							</div>
						</div>
					</form>






				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">	Fermer</button>
					<button type="button" id="save" class="btn btn-primary">Ajouter</button>
				</div>
			</div>
		</div>
	</div>


	<div id="myModal2" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- dialog body -->
				<div id='comment' class="modal-body">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					Le lieu a bien été ajouté
				</div>
			</div>
		</div>
	</div>
	
<div id="myModal3" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <!-- dialog body -->
      <div id="comment" class="modal-body">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        Les changements ont bien été pris en compte
      </div>
    </div>
  </div>
</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal4" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span id="close" class="sr-only">Fermer</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Modifier le lieu</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<div class="form-group row">
							<label id="nom" for="old_email_input" class="col-xs-3 col-form-label">Nom</label>
							<div class="col-xs-8">
								<input class="form-control" value="" id="nom_input"
									name="nom_input" >
<!-- 									placeholder="Nom" -->
									
							</div>
						</div>
						<div class="form-group row">
							<label id="range" for="old_password_input" class="col-xs-3 col-form-label">Portée</label>
							<div class="col-xs-8">
								<input class="form-control" value="" id="radius_input"
									type="number" name="radius_input"
									>
<!-- 									placeholder="Portée" -->
							</div>
						</div>
					</form>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">
					<button id="ferm" type="button" class="btn btn-default" data-dismiss="modal">Fermer</button>
					<button id="enreg" type="button" class="btn btn-primary">Enregistrer</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>