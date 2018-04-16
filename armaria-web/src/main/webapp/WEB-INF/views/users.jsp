<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>

<%-- The following urls are used in the included paging.jspf page. --%>
<c:url var="firstUrl"	value="/users/1" />
<c:url var="lastUrl"	value="/users/${pages.totalPages}" />
<c:url var="prevUrl"	value="/users/${pages.currentIndex - 1}" />
<c:url var="nextUrl"	value="/users/${pages.currentIndex + 1}" />
<c:url var="itemName"	value="users" />
<c:url var="relProfile"	value="../profile" />
<c:url var="relIndex"	value="../index" />

<spring:message code="generic.pending"	var="pending" />
<spring:message code="users.confirm"	var="confirm" />
<spring:message code="users.delete"	var="delete" />
<spring:message code="users.valid"	var="valid" />
<spring:message code="users.invalid"	var="invalid" />

<!DOCTYPE html>
<html class="no-js">
	<head>
		<script>
			document.documentElement.className =
				document.documentElement.className.replace(/\bno-js\b/, 'js');
		</script>

		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<spring:message code="view.users" var="title" />
		<title><spring:message code="generic.title" arguments="${fn:toLowerCase(title)}" /></title>
		<link rel="icon" href="${pageContext.request.contextPath}/resources/css/images/doric_fluted-0-32x32.png"
			type="image/png" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/paging.css"
			type="text/css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/users.css"
			type="text/css" />
	</head>

	<body>
		<hr>
		<%@ include file="/WEB-INF/jspf/paging.jspf" %>

		<c:if test="${not empty users}">
		<div><table>
		<caption><spring:message code="caption.users" /></caption>
		<thead>
			<tr>
				<th><spring:message code="users.id" /></th>
				<th><spring:message code="users.login" /></th>
				<th><spring:message code="users.email" /></th>
				<th><spring:message code="users.entry" /></th>
				<th><spring:message code="users.loans" /></th>
				<th><spring:message code="users.tickets" /></th>
				<th><spring:message code="users.status" /></th>
				<th><spring:message code="users.expunge" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users.content}" var="user" begin="0">
			<tr>
				<td>${user.id}</td>
				<td>
				<c:choose>
				<c:when test="${user.role.isPatron()}">...</c:when>
				<c:otherwise>
				<c:choose>
				<c:when test="${not empty user.loans or not empty user.tickets}">
				<form id="user_${user.id}" action="patronize" method="post">
					<a href="#" id="smart" onclick="event.preventDefault();
					push(this, 'user_${user.id}')">${user.login}</a>
					<input type="hidden" name="patron" value="${user.id}" />

					<noscript><button id="dumb" type="submit">
						<span>${user.login}</span>
					</button></noscript>
				</form>
				</c:when>
				<c:otherwise>${user.login}</c:otherwise>
				</c:choose>
				</c:otherwise>
				</c:choose>
				<td>
				<c:choose>
				<c:when test="${user.role.isPatron()}">...</c:when>
				<c:otherwise><span title="${user.email}">${user.email}</span></c:otherwise>
				</c:choose>
				</td>
				<td>
				<span title="${user.tessera.getEntry()}">${user.tessera.getEntry()}</span>
				</td>
				<td>
				<c:choose>
				<c:when test="${user.role.isPatron()}">...</c:when>
				<c:otherwise>
				<c:if test="${not empty user.loans}">
					<c:set var="loans" value="[" />
					<c:forEach items="${user.loans}" var="loan">
						<c:set var="loans" value="${loans}, ${loan.bookId}" />
					</c:forEach>
					<c:set var="allLoans" value="${fn:replace(loans, '[, ', '')}" />
					<c:out value="${allLoans}" />
					<c:set var="allLoans" value="" />
					<c:set var="loans" value="" />
				</c:if>
				</c:otherwise>
				</c:choose>
				</td>
				<td>
				<c:choose>
				<c:when test="${user.role.isPatron()}">...</c:when>
				<c:otherwise>
				<c:if test="${not empty user.tickets}">
					<c:set var="tickets" value="[" />
					<c:forEach items="${user.tickets}" var="ticket">
						<c:set var="tickets" value="${tickets}, ${ticket.bookId}" />
					</c:forEach>
					<c:set var="allTickets" value="${fn:replace(tickets, '[, ', '')}" />
					<c:out value="${allTickets}" />
					<c:set var="allTickets" value="" />
					<c:set var="tickets" value="" />
				</c:if>
				</c:otherwise>
				</c:choose>
				</td>
				<td>
				<c:choose>
				<c:when test="${user.role.isPatron()}">${user.role}</c:when>
				<c:otherwise>
				<form id="toggle_${user.id}" action="toggle" method="post">
					<c:choose>
					<c:when test="${user.status.isValid()}">
						<c:set var="state" value="${valid}" />
					</c:when>
					<c:otherwise>
						<c:set var="state" value="${invalid}" />
					</c:otherwise>
					</c:choose>

					<a href="#" id="smart" onclick="event.preventDefault();
					push(this, 'toggle_${user.id}')">${state}</a>
					<input type="hidden" name="user" value="${user.id}" />
					<input type="hidden" name="viewPage" value="${viewPage}" />

					<noscript><button id="dumb" type="submit">
						<span>${state}</span>
					</button></noscript>
				</form>
				</c:otherwise>
				</c:choose>
				</td>
				<td>
				<c:choose>
				<c:when test="${user.role.isPatron()}">...</c:when>
				<c:otherwise>
				<form id="expunge_${user.id}" action="expunge" method="post">
					<c:choose>
					<c:when test="${user.status.isValid()}">
						<c:set var="state" value="${confirm}" />
					</c:when>
					<c:otherwise>
						<c:set var="state" value="${delete}" />
					</c:otherwise>
					</c:choose>

					<a href="#" id="smart" onclick="event.preventDefault();
					push(this, 'expunge_${user.id}')">${state}</a>
					<input type="hidden" name="user" value="${user.id}" />
					<input type="hidden" name="viewPage" value="${viewPage}" />

					<noscript><button id="dumb" type="submit">
						<span>${state}</span>
					</button></noscript>
				</form>
				</c:otherwise>
				</c:choose>
				</td>
			</tr>
			</c:forEach>
		</tbody>
		</table></div>
		</c:if>

		<script>
			function push(o, id)
			{
				o.innerHTML	= "${pending}";
				document.getElementById(id).submit();
			}
		</script>
	</body>
</html>
