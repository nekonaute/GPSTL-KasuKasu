<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title >Conversations</title>
<script type="text/javascript" src="/KasuKasu/js/utils.js"></script>
<script type="text/javascript"
	src="/KasuKasu/js/inflator/views/messenger.js"></script>
<script type="text/javascript" src="/KasuKasu/js/inflator/mirror.js"></script>
<script type="text/javascript" src="/KasuKasu/js/jquery-3.1.1.js"></script>
<script type="text/javascript" src="/KasuKasu/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="/KasuKasu/js/translation.js"></script>

<link rel="stylesheet" type="text/css" href="/KasuKasu/css/sidebar.css" />
</head>
<body id="gc-search" onload="trans(); interlocutors()">
	<%@ include file="/fragments/sidebar.jspf"%>

	<h1 id="titre" class="title" align="center">Toutes mes conversations</h1>
	<br>
	<div id="found-speakers" align="center" class="result_writable_zone"></div>
	<br>
</body>
</html>