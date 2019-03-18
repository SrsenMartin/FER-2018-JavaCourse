<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<body>
		<h1>${poll.pollTitle}</h1>
		<p>${poll.pollMessage}</p>
		<ol>
			<c:forEach var="entry" items="${pollData}">
				<li><a href="glasanje-glasaj?id=${entry.id}">${entry.name}</a></li>
			</c:forEach>
		</ol>
	</body>
</html>