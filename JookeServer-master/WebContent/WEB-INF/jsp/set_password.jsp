<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Jooke reset the password</title>
</head>
<body>

	<h1>Input the new password</h1>

	<form name="newPwd" action="/RichServer/set_password" method="post">
		New Password: <input type="text" name="password">
	    Re-enter Password: <input type="text" name="re_password"> 
	    <input type="submit" value="submit">
	    
	     <input type="hidden" name="case" value="<%= (String) request.getAttribute("caseKey")%>">  
	</form>

</body>
</html>