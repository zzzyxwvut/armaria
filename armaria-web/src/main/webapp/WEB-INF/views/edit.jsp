<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags" %>

<spring:message code="form.newpass"	var="newpass" />
<spring:message code="form.confirm"	var="confirm" />
<spring:message code="generic.submit"	var="submit" />
<spring:message code="tip.password"	var="passwordtip" />
<spring:message code="tip.confirm"	var="confirmtip" />
<spring:message code="tip.email"	var="emailtip" />

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<spring:message code="view.edit" var="title" />
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
			<div id="edit">
			<form:form id="update" action="update" method="post" modelAttribute="dummyUser">
				<form:hidden path="login" />

				<fieldset>
				<p><form:password path="password" autocomplete="off" autofocus="true"
					placeholder="${newpass}" title="${passwordtip}"
					minlength="8" showPassword="true" />
				<p><form:errors path="password" cssClass="invalid" />

				<p><input type="password" autocomplete="off"
					placeholder="${confirm}" title="${confirmtip}"
					minlength="8" name="copyWord" value="${copyWord}" />

				<p><form:input type="email" path="email" autocomplete="off"
					placeholder="info@example.org" title="${emailtip}" minlength="3" />
				<p><form:errors path="email" cssClass="invalid" />

				<p><input name="s" type="submit" value="OK" alt="OK" title="${submit}">
				</fieldset>
			</form:form>

			<br>
			<form id="leave" action="leave" method="post">
				<fieldset>
					<p><input type="checkbox" id="confirm"
						onchange="document.getElementById('remove').disabled = !checked" />
					<p><label for="confirm"><spring:message code="form.expunge" /></label>
					<p><input type="submit" id="remove" value="OK" title="${submit}" disabled />
				</fieldset>
			</form>
			</div>
		</div>
	</body>
</html>
