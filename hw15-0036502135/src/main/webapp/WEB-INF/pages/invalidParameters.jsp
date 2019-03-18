<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>	
	<head>
		<style type="text/css">
		.header{
		   font-size: 2em;
		   color: #008abd;
		   padding-left: 30px;
		   padding-top: 5px;
		}
		.invalid{
		   font-weight: bold;
		   font-size: 2.2em;
		   color: #FF8888;
		   padding-left: 25%;
		   padding-top: 10%
		}
		</style>
	</head>
	<body>		
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
		
		<div class="invalid"><h1>${error}</h1></div>
	</body>
</html>