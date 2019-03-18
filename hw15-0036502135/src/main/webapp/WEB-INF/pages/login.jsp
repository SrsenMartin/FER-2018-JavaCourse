<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<title>Login</title>
		
		<style type="text/css">
		.error {
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF8888;
		   padding-left: 22%;
		}
		.formLabel {
		   display: inline-block;
		   
		   width: 20%;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
		}
		.formControls {
		  margin-top: 10px;
		  font-size: 1.5em;
		}
		.users {
			margin-left: 60%;
			font-size: 1.2em;
		}
		.reg {
			margin-left: 58%;
			font-size: 1.2em;
		}
		.login{
		   display: inline-block;
		   
		   width: 27%;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
                   padding-bottom: 30px;
                   padding-top: 40px;
		}
		.header{
		   font-size: 2em;
		   color: #008abd;
		   padding-left: 30px;
		   padding-top: 5px;
		}
		</style>
	</head>

	<body>
		<div class="header">
		<c:if test="${currentId != null}">
		 	<c:out value="${currentFn}"/>
		 	<c:out value="${currentLn}"/><br>
		 	<a href="logout">Logout</a>
		</c:if>
		<c:if test="${currentId == null}">
			<c:out value="Not logged in"/>
		</c:if>
		</div>
		
		<c:if test="${currentId == null}">
		<h1><span class="login">LOGIN</span></h1>
	
		<form action="main" method="post">
		<div>
		 <div>
		  <span class="formLabel">Nick:</span><input type="text" name="nick" value="${nick}" maxlength='20' size="20">
		 </div>
		 <c:if test="${nickError != null}">
		 	<div class="error"><c:out value="${nickError}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Password:</span><input type="text" name="password" value='' size="20">
		 </div>
		 <c:if test="${passwordError != null}">
		 	<div class="error"><c:out value="${passwordError}"/></div>
		 </c:if>
		</div>

		 <div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="metoda" value="Login">
		 </div>
		</form>

		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <a href="register">Register</a>
		</div>
		</c:if>
		
		<div class="reg">
		 <h3>Registered users:</h3>
		</div>
		
		<div class="users">
		 <c:forEach var="user" items="${users}">
		 	<a href="author/${user.nick}">${user.nick}</a><br>
		 </c:forEach>
		</div>
	</body>
</html>