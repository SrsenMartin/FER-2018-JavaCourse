<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>	
	<head>
		<style type="text/css">
			table.rez td {
				text-align: center;
			}
			td{
				padding: 15px;
			}
		</style>	
	</head>
	<body bgcolor="${pickedBgCol}">		
		<table border="1">
			<thead>
				<tr><th>Degree</th><th>Sin</th><th>Cos</th></tr>
			</thead>
			<tbody>
				<c:forEach var="value" items="${trigonometricValues}">
					<tr><td>${value.degree}</td><td>${value.sin}</td><td>${value.cos}</td></tr>
				</c:forEach>
			</tbody>
		</table>
	</body>
</html>