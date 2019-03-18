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
		<h1>Voting results</h1>
		<p>These are the voting results.</p>
		<table border="1" class="rez">
			<thead>
				<tr>
					<th>Band</th>
					<th>Number of votes</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="result" items="${results}">
					<tr>
						<td>${result.name}</td>
						<td>${result.numberOfVotes}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<h2>Results graphical view.</h2>
		<img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />

		<h2>Results in XLS format</h2>
		<p>Results in XLS format available at <a href="glasanje-xls">here</a></p>

		<h2>Various</h2>
		<p>Examples of songs by winning bands:</p>
		<ul>
			<c:forEach var="result" items="${results}">
				<c:if test="${result.numberOfVotes == results[0].numberOfVotes}">
					<li><a href="${result.song}" target="_blank">${result.name}</a></li>
				</c:if>
			</c:forEach>
		</ul>
	</body>
</html>