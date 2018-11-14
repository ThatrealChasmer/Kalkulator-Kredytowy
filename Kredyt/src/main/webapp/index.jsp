<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
	<body>
		<form action="/kalk" method="post">
		<b>Wysokosc kredytu </b><input type="text" id="kwota" name="kwota" required/><br>
		<b>Ilosc rat </b><input type="text" id="ilosc" name="ilosc" required/><br>
		<b>Oprocentowanie </b><input type="text" id="proc" name="proc" required/><br>
		<b>Oplata stala </b><input type="text" id="oplata" name="oplata" required/><br>
		<b>Raty</b>
		<input type="radio" id="rata" name="rata" value="stala" checked/><b>stale</b>
		<input type="radio" id="rata" name="rata" value="malejaca"/><b>malejace</b><br>
		<input type="submit" name="submit" value="Oblicz"/>
		</form> 
	</body>
</html>