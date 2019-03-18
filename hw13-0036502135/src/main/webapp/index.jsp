<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<body bgcolor="${pickedBgCol}">
		<a href="colors.jsp">Background color chooser</a><br><br>
		<a href="trigonometric?a=0&b=90">Trigonometric</a><br>
		
	<form action="trigonometric" method="GET">
		Starting angle:<br>
			<input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Ending angle:<br>
			<input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Create table">
		<input type="reset" value="Reset">
	</form><br><br>
		
		<a href="stories/funny.jsp">Funny story</a><br><br>
		<a href="powers?a=1&b=100&n=3">Powers</a><br><br>
		<a href="appinfo.jsp">Application informations</a><br><br>
		
		<%-- Nije bilo rečeno da se doda, ali za svaki slučaj te zbog jednostavnije recenzije. --%>
		<a href="report.jsp">OS usage survey</a><br>
		<a href="glasanje">Vote for your favourite band</a><br>
		<a href="glasanje-rezultati">Show voting results</a>
	</body>
</html>