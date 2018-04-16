<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring"	uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"	uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec"		uri="http://www.springframework.org/security/tags" %>

<spring:message code="generic.arrange"	var="arrange" />
<spring:message code="generic.pending"	var="pending" />
<spring:message code="books.available"	var="available" />
<spring:message code="books.borrowed"	var="borrowed" />
<sec:authorize access="isAnonymous()" var="anonymous" />
<sec:authorize access="hasAuthority('patron')" var="patron" />

<%-- The following urls are used in the included paging.jspf page. --%>
<c:url var="firstUrl"	value="/books/1" />
<c:url var="lastUrl"	value="/books/${pages.totalPages}" />
<c:url var="prevUrl"	value="/books/${pages.currentIndex - 1}" />
<c:url var="nextUrl"	value="/books/${pages.currentIndex + 1}" />
<c:url var="itemName"	value="books" />
<c:url var="relProfile"	value="../profile" />
<c:url var="relIndex"	value="../index" />

<!DOCTYPE html>
<html class="no-js">
	<head>
		<script>
			document.documentElement.className =
				document.documentElement.className.replace(/\bno-js\b/, 'js');
		</script>

		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<spring:message code="view.books" var="title" />
		<title><spring:message code="generic.title" arguments="${fn:toLowerCase(title)}" /></title>
		<link rel="icon" href="${pageContext.request.contextPath}/resources/css/images/doric_fluted-0-32x32.png"
			type="image/png" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/paging.css"
			type="text/css" />
	</head>

	<body>
		<hr>
		<%@ include file="/WEB-INF/jspf/paging.jspf" %>

		<div><table>
		<caption><spring:message code="caption.books" /></caption>
		<thead>
			<tr>
			<th>
			<form id="item_0" action="sort">
				<a href="#" id="smart" onclick="order('item_0')"
					title="${arrange}"><spring:message code="books.id" /></a>
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.id" /></span>
				</button></noscript>
			</form>
			</th>

			<th>
			<form id="item_1" action="sort">
				<a href="#" id="smart" onclick="order('item_1')"
					title="${arrange}"><spring:message code="books.author" /></a>
				<input type="hidden" name="orderBy" value="author" />
				<input type="hidden" name="orderBy" value="title" />
				<input type="hidden" name="orderBy" value="subtitle" />
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.author" /></span>
				</button></noscript>
			</form>
			</th>

			<th>
			<form id="item_2" action="sort">
				<a href="#" id="smart" onclick="order('item_2')"
					title="${arrange}"><spring:message code="books.title" /></a>
				<input type="hidden" name="orderBy" value="title" />
				<input type="hidden" name="orderBy" value="subtitle" />
				<input type="hidden" name="orderBy" value="author" />
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.title" /></span>
				</button></noscript>
			</form>
			</th>

			<th>
			<form id="item_3" action="sort">
				<a href="#" id="smart" onclick="order('item_3')"
					title="${arrange}"><spring:message code="books.subtitle" /></a>
				<input type="hidden" name="orderBy" value="subtitle" />
				<input type="hidden" name="orderBy" value="title" />
				<input type="hidden" name="orderBy" value="author" />
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.subtitle" /></span>
				</button></noscript>
			</form>
			</th>

			<th>
			<form id="item_4" action="sort">
				<a href="#" id="smart" onclick="order('item_4')"
					title="${arrange}"><spring:message code="books.tongue" /></a>
				<input type="hidden" name="orderBy" value="language" />
				<input type="hidden" name="orderBy" value="author" />
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.tongue" /></span>
				</button></noscript>
			</form>
			</th>

			<th>
			<form id="item_5" action="sort">
				<a href="#" id="smart" onclick="order('item_5')"
					title="${arrange}"><spring:message code="books.pages" /></a>
				<input type="hidden" name="orderBy" value="pages" />
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.pages" /></span>
				</button></noscript>
			</form>
			</th>

			<th>
			<form id="item_6" action="sort">
				<a href="#" id="smart" onclick="order('item_6')"
					title="${arrange}"><spring:message code="books.year" /></a>
				<input type="hidden" name="orderBy" value="year" />
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.year" /></span>
				</button></noscript>
			</form>
			</th>

			<th>
			<form id="item_7" action="sort">
				<a href="#" id="smart" onclick="order('item_7')"
					title="${arrange}"><spring:message code="books.status" /></a>
				<input type="hidden" name="orderBy" value="status" />
				<input type="hidden" name="orderBy" value="id" />

				<noscript><button id="dumb" type="submit">
					<span><spring:message code="books.status" /></span>
				</button></noscript>
			</form>
			</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${books.content}" var="book">
			<c:set var="status" value="${book.status.borrowed ? borrowed : available}" />
			<tr>
				<td>${book.id}</td>
				<td>${book.author}</td>
				<td>${book.title}</td>
				<td>${book.subtitle}</td>
				<td>${book.language}</td>
				<td>${book.pages}</td>
				<td>${book.year}</td>
				<td>
			<c:choose>
				<c:when test="${anonymous}">${status}</c:when>
				<c:when test="${patron}">
					<c:set var="color" value="${book.status.borrowed ? '#ffff00' : '#0000ff'}" />
					<form id="toggle_${book.id}" action="toggle" method="post">
						<a href="#" id="smart" onclick="event.preventDefault();
						push(this, 'toggle_${book.id}')"
						style="color: ${color}">${status}</a>
						<input type="hidden" name="book" value="${book.id}" />
						<input type="hidden" name="viewPage" value="${viewPage}" />

						<noscript><button id="dumb" type="submit">
							<span>${status}</span>
						</button></noscript>
					</form>
				</c:when>
				<c:when test="${book.status.borrowed}">
					<form:form id="waitlist_${book.id}" action="waitlist"
							method="post" modelAttribute="ticketQuotaUser">
						<c:if test="${not empty bookId and bookId eq book.id}">
							<form:errors path="*" element="div" cssClass="invalid" />
						</c:if>

						<a href="#" id="smart" onclick="event.preventDefault();
						push(this, 'waitlist_${book.id}')"
						style="color: #ffff00"><spring:message code="books.waitlist" /></a>
						<input type="hidden" name="book" value="${book.id}" />
						<input type="hidden" name="viewPage" value="${viewPage}" />

						<noscript><button id="dumb" type="submit">
							<span><spring:message code="books.waitlist" /></span>
						</button></noscript>
					</form:form>
				</c:when>
				<c:otherwise>
					<form:form id="borrow_${book.id}" action="borrow"
							method="post" modelAttribute="bookQuotaUser">
						<c:if test="${not empty bookId and bookId eq book.id}">
							<form:errors path="*" element="div" cssClass="invalid" />
						</c:if>

						<a href="#" id="smart" onclick="event.preventDefault();
						push(this, 'borrow_${book.id}')">${status}</a>
						<input type="hidden" name="book" value="${book.id}" />
						<input type="hidden" name="viewPage" value="${viewPage}" />

						<noscript><button id="dumb" type="submit">
							<span>${status}</span>
						</button></noscript>
					</form:form>
				</c:otherwise>
			</c:choose>
				</td>
			</tr>
			</c:forEach>
		</tbody>
		</table></div>

		<script>
			function push(o, id)
			{
				o.innerHTML	= "${pending}";
				document.getElementById(id).submit();
			}

			function order(id)
			{
				document.getElementById(id).submit();
			}
		</script>
	</body>
</html>
