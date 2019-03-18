<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<title>Edit/New</title>
		
		<style type="text/css">
		.error {
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF8888;
		   padding-left: 42%;
		}
		.formLabel {
		   display: inline-block;
		   
		   width: 30%;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
           
		}
		.formControls {
		  margin-top: 10px;
		  font-size: 1.5em;
		}
		.save{
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
		.editUpdate{
		   display: inline-block;
		   
		   width: 45%;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
                   padding-bottom: 40px;
                   padding-top: 50px;
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
		
		<div class="header"><a href="../${currentNick}">Back</a></div>
	
		<h1><span class="editUpdate">EDIT/UPDATE</span></h1>
		
		<form action="?entryId=${entryId}" method="post">
		 <div>
		  <span class="formLabel">Title:</span><input size="40" type="text" name="title" value="${form.title}" maxlength='200' >
		</div>
		
		<div>
		  <span class="formLabel">Text:</span><input size="150" type="text" name="text" value="${form.text}" maxlength='4096'>
		</div>

		 <div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="metoda" value="Save">
		 </div>
		</form>
	</body>
</html>