<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="false" %>

<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags" %>

<spring:message code="encrypt.plain"	var="plaintext" />
<spring:message code="encrypt.cipher"	var="encryptedtext" />
<spring:message code="encrypt.submit"	var="submit" />

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<spring:message code="view.encrypt" var="title" />
		<title><spring:message code="generic.title" arguments="${fn:toLowerCase(title)}" /></title>
		<link rel="icon" href="${pageContext.request.contextPath}/resources/css/images/doric_fluted-0-32x32.png"
			type="image/png" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/basic.css"
			type="text/css" />
	</head>

	<body>
		<hr>
		<div id="profile" align="right">
			<p><spring:message code="paging.profile" /> (<a href="profile"><sec:authentication property="principal.username" /></a>)</p>
		</div>

		<br><br><br><br>
		<div align="center">
			<form id="encrypt" action="encryptor" method="post">
				<p><textarea rows="4" cols="26" placeholder="${plaintext}"
					name="plain" autofocus autocomplete="off"
					style="text-align: center">${plain}</textarea></p>
				<p><textarea rows="4" cols="26" readonly placeholder="${encryptedtext}"
					style="text-align: center">${encrypted}</textarea></p>

				<p><input type="submit" value="${submit}" /></p>
			</form>
		</div>
	</body>
</html>
