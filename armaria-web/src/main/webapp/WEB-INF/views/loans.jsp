<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>

<spring:message code="generic.pending"	var="pending" />

<spring:message code="books.available"	var="available" />
<spring:message code="books.borrowed"	var="borrowed" />
<spring:message code="books.waitlist"	var="waitlist" />
<spring:message code="books.author"	var="author" />
<spring:message code="books.title"	var="title" />
<spring:message code="books.subtitle"	var="subtitle" />
<spring:message code="books.tongue"	var="tongue" />
<spring:message code="books.pages"	var="pages" />
<spring:message code="books.year"	var="year" />

<spring:message code="loans.due"	var="due" />
<spring:message code="loans.shelve"	var="shelve" />
<spring:message code="tickets.cancel"	var="cancel" />

<!DOCTYPE html>
<html class="no-js">
	<head>
		<script>
			document.documentElement.className =
				document.documentElement.className.replace(/\bno-js\b/, 'js');
		</script>

		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<spring:message code="view.loans" var="title" />
		<title><spring:message code="generic.title" arguments="${fn:toLowerCase(title)}" /></title>
		<link rel="icon" href="${pageContext.request.contextPath}/resources/css/images/doric_fluted-0-32x32.png"
			type="image/png" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/paging.css"
			type="text/css" />
	</head>

	<body>
		<hr>
		<div id="profile" align="right">
			<p><spring:message code="paging.profile" /> (<a href="../profile">${principal}</a>)</p>
		</div>

		<c:if test="${not empty loans}">
		<table>
			<caption><spring:message code="caption.loans" /></caption>
			<thead>
				<tr>
					<th>#</th>
					<th><span title="${author}">${author}</span></th>
					<th><span title="${title}">${title}</span></th>
					<th><span title="${subtitle}">${subtitle}</span></th>
					<th><span title="${tongue}">${tongue}</span></th>
					<th><span title="${pages}">${pages}</span></th>
					<th><span title="${year}">${year}</span></th>
					<th><span title="${due}">${due}</span></th>
					<th><span title="${shelve}">${shelve}</span></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="loan" items="${loans}" varStatus="pos">
				<c:set var="prefix"
					value="${loan.book.id > 100 ? 'L' : loan.book.id > 10 ? 'L0' : 'L00'}" />
				<c:set var="suffix" value=".pdf" />
				<c:set var="tome" value="${prefix}${loan.book.id}${suffix}" />
				<c:set var="status" value="${loan.book.status.borrowed ? borrowed : available}" />
					<tr>
						<td>${pos.index + 1}</td>
						<td>${loan.book.author}</td>
						<td>
						<form id="fetch_${loan.id}" action="../viewer" method="post">
							<a href="#" id="smart" onclick="event.preventDefault();
							push(this, 'fetch_${loan.id}')">${loan.book.title}</a>
							<input type="hidden" name="tome" value="${tome}" />
							<noscript><button id="dumb" type="submit">
								<span>${loan.book.title}</span>
							</button></noscript>
						</form>
						</td>
						<td>${loan.book.subtitle}</td>
						<td>${loan.book.language}</td>
						<td>${loan.book.pages}</td>
						<td>${loan.book.year}</td>
						<td>${loan.term}</td>
						<td>
						<form id="restore_${loan.id}" action="restore" method="post">
							<a href="#" id="smart" onclick="event.preventDefault();
							push(this, 'restore_${loan.id}')">${status}</a>
							<input type="hidden" name="book" value="${loan.book.id}" />
							<input type="hidden" name="loan" value="${loan.id}" />

							<noscript><button id="dumb" type="submit">
								<span>${status}</span>
							</button></noscript>
						</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</c:if>

		<c:if test="${not empty tickets}">
		<br><br><br><br>

		<table>
			<caption><spring:message code="caption.tickets" /></caption>
			<thead>
				<tr>
					<th>#</th>
					<th><span title="${author}">${author}</span></th>
					<th><span title="${title}">${title}</span></th>
					<th><span title="${subtitle}">${subtitle}</span></th>
					<th><span title="${tongue}">${tongue}</span></th>
					<th><span title="${pages}">${pages}</span></th>
					<th><span title="${year}">${year}</span></th>
					<th><span title="${available}">${available}</span></th>
					<th><span title="${cancel}">${cancel}</span></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="ticket" items="${tickets}" varStatus="pos">
					<tr>
						<td>${pos.index + 1}</td>
						<td>${ticket.book.author}</td>
						<td>${ticket.book.title}</td>
						<td>${ticket.book.subtitle}</td>
						<td>${ticket.book.language}</td>
						<td>${ticket.book.pages}</td>
						<td>${ticket.book.year}</td>
						<td>...</td>
						<td>
						<form id="cancel_${ticket.id}" action="cancel" method="post">
							<a href="#" id="smart" onclick="event.preventDefault();
							push(this, 'cancel_${ticket.id}')"
							style="color: #ff0000">${waitlist}</a>
							<input type="hidden" name="ticket" value="${ticket.id}" />

							<noscript><button id="dumb" type="submit">
								<span>${waitlist}</span>
							</button></noscript>
						</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
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
