<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags" %>

<spring:eval expression="T(org.example.zzzyxwvut.armaria.domain.naming.Constants$QUOTA).LOANS.toString()" var="loans" />
<spring:eval expression="T(org.example.zzzyxwvut.armaria.domain.naming.Constants$QUOTA).DAYS.toString()" var="days" />
<spring:eval expression="T(org.example.zzzyxwvut.armaria.domain.naming.Constants$QUOTA).TICKETS.toString()" var="tickets" />

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<spring:message code="view.profile" var="title" />
		<title><spring:message code="generic.title" arguments="${fn:toLowerCase(title)}" /></title>
		<link rel="icon" href="${pageContext.request.contextPath}/resources/css/images/doric_fluted-0-32x32.png"
			type="image/png" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/profile.css"
			type="text/css" />
	</head>

	<body>
		<hr>
		<div id="logout" align="right">
			<p><spring:message code="generic.logout" /> (<a href="logout"><sec:authentication property="principal.username" /></a>)</p>
		</div>

		<div id="menu" align="center">
			<ul>
				<li><a href="index"><spring:message code="view.main" /></a></li>
				<li><a href="edit"><spring:message code="view.edit" /></a></li>
				<li><a href="books/1"><spring:message code="view.books" /></a></li>

				<sec:authorize access="!hasAuthority('patron')">
					<c:choose>
						<c:when test="${qualified}">
							<li><a href="loans/items">
								<spring:message code="view.loans" />
							</a></li>
						</c:when>
						<c:otherwise>
							<li><spring:message code="view.loans" /></li>
						</c:otherwise>
					</c:choose>
				</sec:authorize>

				<sec:authorize access="hasAuthority('patron')">
					<li><a href="users/1"><spring:message code="view.users" /></a></li>
					<li><a href="encrypt"><spring:message code="view.encrypt" /></a></li>
				</sec:authorize>
			</ul>
		</div>
		<br><br>

		<div id="policy" align="center">
			<pre><spring:message code="profile.welcome"
				arguments="${loans}, ${days}, ${tickets}" /></pre>
		</div>
	</body>
</html>
