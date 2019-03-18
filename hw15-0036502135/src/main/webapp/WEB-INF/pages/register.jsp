<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<title>Register</title>
		
		<style type="text/css">
		.error {
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF8888;
		   padding-left: 42%;
		}
		.formLabel {
		   display: inline-block;
		   
		   width: 40%;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
           
		}
		.formControls {
		  margin-top: 10px;
		  font-size: 1.5em;
		}
		.register{
		   display: inline-block;
		   
		   width: 49%;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
                   padding-bottom: 40px;
                   padding-top: 50px;
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
		 	<c:out value=" "/>
		 	<c:out value="${currentLn}"/><br>
		</c:if>
		<c:if test="${currentId == null}">
			<c:out value="Not logged in"/>
		</c:if>
		</div>
		
		<div class="header"><a href="main">Main</a></div>
	
		<h1><span class="register">REGISTER</span></h1>
		
		<form action="register" method="post">
		<div>
		 <div>
		  <span class="formLabel">First name:</span><input type="text" name="firstName" value="${form.firstName}" maxlength='30' size="20">
		 </div>
		 <c:if test="${form.hasMistake('firstNameError')}">
		 <div class="error"><c:out value="${form.getMistake('firstNameError')}"/></div>
		 </c:if>
		</div>
		
		<div>
		 <div>
		  <span class="formLabel">Last name:</span><input type="text" name="lastName" value="${form.lastName}" maxlength='50' size="20">
		 </div>
		 <c:if test="${form.hasMistake('lastNameError')}">
		 <div class="error"><c:out value="${form.getMistake('lastNameError')}"/></div>
		 </c:if>
		</div>
		 
		<div>
		 <div>
		  <span class="formLabel">Email:</span><input type="text" name="email" value="${form.email}" maxlength='50' size="20">
		 </div>
		 <c:if test="${form.hasMistake('emailError')}">
		 <div class="error"><c:out value="${form.getMistake('emailError')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Nick:</span><input type="text" name="nick" value="${form.nick}" maxlength='20' size="20">
		 </div>
		 <c:if test="${form.hasMistake('nickError')}">
		 <div class="error"><c:out value="${form.getMistake('nickError')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Password:</span><input type="text" name="password" value="${form.password}" size="20">
		 </div>
		 <c:if test="${form.hasMistake('passwordError')}">
		 <div class="error"><c:out value="${form.getMistake('passwordError')}"/></div>
		 </c:if>
		</div>

		 <div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="metoda" value="Register">
		 </div>
		</form>
	</body>
</html>