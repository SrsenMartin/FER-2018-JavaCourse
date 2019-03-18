<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%! 
	public String randomColor(){
		char[] color = new char[6];
		
		for(int i = 0;i < 6;i++){
			char value = (char) Math.floor(Math.random()*16);
			if(value < 10)	value = (char) ('0' + value);
			else	value = (char) ('A' + (value - 10));
			color[i] = value;
		}
		
		return "#" + new String(color);
	}
%>

<!DOCTYPE html>
<html>	
	<head>
		<style type="text/css">
		  body {
    		color: <%=randomColor()%>;
    		font-size: 200%;
    		}
  		</style>
	</head>
	<body bgcolor="${pickedBgCol}">
		<p>A curious child asked his mother: “Mommy, why are some of your hairs turning grey? <br>
		The mother tried to use this occasion to teach her child: <br>
		“It is because of you, dear. Every bad action of yours will turn one of my hairs grey!”<br>
		The child replied innocently: “Now I know why grandmother has only grey hairs on her head.”</p>
	</body>
</html>