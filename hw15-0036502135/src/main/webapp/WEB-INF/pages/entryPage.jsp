<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>Entry page</title>

<style type="text/css">
.header {
	font-size: 2em;
	color: #008abd;
	padding-left: 30px;
}

.titleText {
	display: inline-block;
	width: 49%;
	font-weight: bold;
	text-align: right;
	font-size: 1.6em;
}

.edit {
	margin-top: 2%;
	font-size: 1.5em;
	margin-left: 30%;
}

.error {
	font-weight: bold;
	font-size: 0.9em;
	color: #FF8888;
	padding-left: 42%;
}

.formLabel {
	display: inline-block;
	width: 3%;
	font-weight: bold;
	text-align: right;
	padding-right: 10px;
}

.formControls {
	margin-top: 10px;
	font-size: 1.5em;
}

</style>
</head>

<body>
	<h1>
		<span class="titleText">${entry.title}</span>
	</h1>

	<div class="header">
		<c:if test="${currentId != null}">
			<c:out value="${currentFn}" />
			<c:out value=" " />
			<c:out value="${currentLn}" />
			<br>
		</c:if>
		<c:if test="${currentId == null}">
			<c:out value="Not logged in" />
		</c:if>
	</div>

	<div class="header">
		<a href="../${nick}">Back</a>
	</div>

	<p>
		<span class="titleText">${entry.text}</span>
	</p>

	<c:if test="${currentNick == nick}">
		<div class="edit">
			<a href="edit?entryId=${entry.id}">EDIT</a>
		</div>
	</c:if>

	<c:if test="${entryComments != null && !entryComments.isEmpty()}">
		<ul>
			<c:forEach var="comment" items="${entryComments}">
				<li><div style="font-weight: bold">[User=<c:out value="${comment.usersEMail}"/>] <c:out value="${comment.postedOn}"/></div><div style="padding-left: 10px;"><c:out value="${comment.message}"/></div></li>
			</c:forEach>
		</ul>
	</c:if>

	<form action="../../comment?entryId=${entry.id}" method="post">
		<output></output>
		<div>
				<span class="formLabel">Text:</span><input size="200" type="text"
					name="text" value="${text}" maxlength='4096'>
		</div>
		
		<c:if test="${currentId == null}">
		<div>
				<span class="formLabel">Email:</span><input type="text" name="email"
					value="${email}" maxlength='100' size="50">
		</div>
		</c:if>
		
		<div class="formControls">
			<span class="formLabel">&nbsp;</span> <input type="submit"
				name="metoda" value="Add comment">
		</div>
	</form>
</body>
</html>