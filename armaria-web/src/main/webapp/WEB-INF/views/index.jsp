<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form" %>

<spring:message code="index.motto"	var="motto" />
<spring:message code="generic.submit"	var="submit" />
<spring:message code="form.username"	var="username" />
<spring:message code="form.password"	var="password" />
<spring:message code="form.confirm"	var="confirm" />
<spring:message code="tip.username"	var="usernametip" />
<spring:message code="tip.password"	var="passwordtip" />
<spring:message code="tip.confirm"	var="confirmtip" />
<spring:message code="tip.email"	var="emailtip" />
<spring:message code="tip.or.username"	var="nameortip" />
<spring:message code="tip.or.password"	var="wordortip" />

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<spring:message code="view.main" var="title" />
		<title><spring:message code="generic.title" arguments="${fn:toLowerCase(title)}" /></title>
		<link rel="icon" href="${pageContext.request.contextPath}/resources/css/images/doric_fluted-0-32x32.png"
			type="image/png" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/index.css"
			type="text/css" />
	</head>

	<body>
		<hr>
		<div id="menu">
		<ul>
		<li>
		<c:choose>
			<c:when test="${forgotten}">
			<spring:message code="index.recall" />
			<form:form id="signin" action="remember" method="post" modelAttribute="forgetfulUser">
				<fieldset>
				<p><form:input path="login" name="login" placeholder="${username}"
					autocomplete="off" required="true" autofocus="false"
					title="${nameortip}" />
				<p><form:errors path="login" />

				<p><form:input type="email" path="email" autocomplete="off" required="true"
					placeholder="info@example.org" title="${emailtip}" minlength="3" />
				<p><form:errors path="email" />

				<p><input name="s0" type="submit" value="OK" alt="OK" title="${submit}">
				</fieldset>
			</form:form>
			</c:when>
			<c:otherwise>
			<spring:message code="index.signin" />
			<form:form id="signin" action="signin" method="post" modelAttribute="oldUser">
				<fieldset>
				<p><form:input path="login" name="login" placeholder="${username}"
					autocomplete="off" required="true" autofocus="false"
					title="${nameortip}" />
				<p><form:password path="password" name="password" placeholder="${password}"
					autocomplete="off" required="true" showPassword="true"
					title="${wordortip}" />
				<p><a id="help" href="forgotten"><spring:message code="index.forgotten" /></a>
				<p><input name="s1" type="submit" value="OK" alt="OK" title="${submit}">
				</fieldset>
			</form:form>
			</c:otherwise>
		</c:choose>
		</li>

		<li><spring:message code="index.signup" />
			<form:form id="signup" action="signup" method="post" modelAttribute="newUser">
				<fieldset>
				<p><form:input path="login" placeholder="${username}"
					title="${usernametip}" pattern="[a-zA-Z_]{4,}"
					autocomplete="off" required="true" minlength="4" />
				<p><form:errors path="login" />

				<p><form:password path="password" autocomplete="off" required="true"
					placeholder="${password}" title="${passwordtip}"
					minlength="8" showPassword="true" />
				<p><form:errors path="password" />

				<p><input type="password" autocomplete="off" required
					placeholder="${confirm}" title="${confirmtip}"
					minlength="8" name="copyWord" value="${copyWord}" />

				<p><form:input type="email" path="email" autocomplete="off" required="true"
					placeholder="info@example.org" title="${emailtip}" minlength="3" />
				<p><form:errors path="email" />

				<p><input name="s2" type="submit" value="OK" alt="OK" title="${submit}">
				</fieldset>
			</form:form>
		</li>
		</ul>
		</div>

		<c:if test="${not empty salute}">
			<div id="salute" align="center">
				<pre><spring:message code="index.salute" arguments="${salute.login}" /></pre>
			</div>
		</c:if>

		<div id="tongue">
		<ul><li>
			<form id="translate" action="translate" method="post">
			<fieldset>
				<p><input name="locale" type="radio" value="en_US" checked>English
				<p><input name="locale" type="radio" value="ru_RU">Русский
				<p><input name="s3" type="submit" value="OK" alt="OK" title="${submit}">
			</fieldset>
			</form>
		</li></ul>
		</div>

		<div id="footer" align="right">
			<p><span title="${motto}">
				<i>... brevis esse laboro,<br>obscurus fio.</i><br>
			</span>
			<pre><spring:message code="index.welcome" /></pre>
		</div>
	</body>
</html>
