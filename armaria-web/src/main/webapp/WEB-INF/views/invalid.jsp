<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>

	<body style="background: #bebebe">
		<br><br><br><br>
		<h1 align="center">${code}. ${exception.message}</h1>
		<br><br><br><br>
		<hr>
		<p>View the stack trace in the page source.</p>
		
	<!--
		Failed (${code}) URL: ${url}
		Exception: ${exception.message}
		<c:forEach items="${exception.stackTrace}" var="trace">
			${trace} 
		</c:forEach>
	-->
	</body>
</html>
