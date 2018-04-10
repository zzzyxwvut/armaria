<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page trimDirectiveWhitespaces="false" %>

<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<title><spring:message code="reject.title" /></title>
		<link rel="icon" href="${pageContext.request.contextPath}/resources/css/images/doric_fluted-0-32x32.png"
			type="image/png" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/page.css"
			type="text/css" />
	</head>

	<body>
		<br><br><br><br>
		<div id="reject" align="center"><pre><spring:message code="reject.error" /></pre></div>
		<br><br><br><br>
		<hr>
		<p><spring:message code="error.source" /></p>

	<!--
		Failed (${code}) URL: ${url}
		Exception: ${exception.message}
		<c:forEach items="${exception.stackTrace}" var="trace">
			<c:out value="${trace}" />
		</c:forEach>
	-->
	</body>
</html>
