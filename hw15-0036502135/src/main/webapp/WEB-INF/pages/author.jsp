<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<title>${nick} - entries</title>
		
		<style type="text/css">

		.entries {
			margin-left: 45%;
			font-size: 1.2em;
		}
		.header{
		   font-size: 2em;
		   color: #008abd;
		   padding-left: 30px;
		}
		.userNick{
		   display: inline-block;
		   
		   width: 52%;
                   font-weight: bold;
		   text-align: right;
		}
		.new{
			margin-top: 10%;
			font-size: 1.5em;
			margin-left: 3%;
		}
		</style>
	</head>
	<body>
		<h1><span class="userNick">${nick}'s blogs</span></h1>
	
		<div class="header">
		<c:if test="${currentId != null}">
		 	<c:out value="${currentFn}"/>
		 	<c:out value=" "/>
		 	<c:out value="${currentLn}"/><br>
		</c:if>
		<c:if test="${currentId == null}">
			<c:out value="Not logged in"/>
		</c:if>
		</div>
		
		<div class="header"><a href="../main">Main</a></div>
			
		<div class="entries">
		<c:forEach var="entry" items="${userEntries}">
			<a href="${nick}/${entry.id}">${entry.title}</a><br>
		</c:forEach>
		</div>
		
		<c:if test="${currentNick == nick}">
		<div class="new">
			<a href="${nick}/new">NEW</a>
		</div>
		</c:if>
	</body>
</html>