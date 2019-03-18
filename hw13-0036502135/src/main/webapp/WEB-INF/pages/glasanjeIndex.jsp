<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<body bgcolor="${pickedBgCol}">
		<h1>Voting for favourite band:</h1>
		<p>Of the following bands, which is your favorite one? Click on
			link to vote!</p>
		<ol>
			<c:forEach var="band" items="${bandsList}">
				<li><a href="glasanje-glasaj?id=${band.id}">${band.name}</a></li>
			</c:forEach>
		</ol>
	</body>
</html>