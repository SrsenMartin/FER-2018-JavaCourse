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
	<body>
		<h1>Voting results</h1>
		<p>These are the voting results:</p>
		<table border="1" class="rez">
			<thead>
				<tr>
					<th>Name</th>
					<th>Number of votes</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="result" items="${results}">
					<tr>
						<td>${result.name}</td>
						<td>${result.noVotes}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<h2>Results graphical view.</h2>
		<img alt="Pie-chart" src="glasanje-grafika?pollId=${results[0].pollId}" width="400" height="400" />

		<h2>Results in XLS format</h2>
		<p>Results in XLS format available at <a href="glasanje-xls?pollId=${results[0].pollId}">here</a></p>

		<h2>Various</h2>
		<p>Examples of links by winners:</p>
		<ul>
			<c:forEach var="result" items="${results}">
				<c:if test="${result.noVotes == results[0].noVotes}">
					<li><a href="${result.link}" target="_blank">${result.name}</a></li>
				</c:if>
			</c:forEach>
		</ul>
	</body>
</html>