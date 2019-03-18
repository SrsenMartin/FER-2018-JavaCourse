<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<style type="text/css">
			body{
				font-size: 150%;
			}
		</style>
	</head>
	<body>
		<h1>Available polls to participate in:</h1>
		<ul>
			<c:forEach var="poll" items="${polls}">
				<li><a href="glasanje?pollID=${poll.ID}">${poll.pollTitle}</a></li>
			</c:forEach>
		</ul>
	</body>
</html>