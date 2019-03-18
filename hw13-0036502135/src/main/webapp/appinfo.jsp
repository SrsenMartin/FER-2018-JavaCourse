<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%!
	String timeElapsed(){
		long start = (Long) getServletContext().getAttribute("timeStarted");
		long now = System.currentTimeMillis();
		
		int elapsedSec = (int) (now - start)/1000;
		int miliseconds = (int) (now - start) % 1000;
		
		int days = elapsedSec / 86400;
		int hours = (elapsedSec % 86400) / 3600;
		int minutes = (elapsedSec % 3600) / 60;
		int seconds = elapsedSec % 60;
		
		StringBuilder sb = new StringBuilder();
		if(days != 0)	sb.append(days + " days ");
		if(hours != 0)	sb.append(hours + " hours ");
		if(minutes != 0)	sb.append(minutes + " minutes ");
		sb.append(seconds + " seconds ");
		sb.append(miliseconds + " miliseconds.");
		
		return sb.toString();
	}
%>

<!DOCTYPE html>
<html>	
	<body bgcolor="${pickedBgCol}">		
		<h2>Time elapsed since application started:</h2>
		<h4><%=timeElapsed()%></h4>
	</body>
</html>