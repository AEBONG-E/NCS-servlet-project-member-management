<%--
  메인 페이지 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date" %>
<html>
<head>
    <title>AeBong's Mall</title>
    <link rel="stylesheet"	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>
<body>
<%@ include file="../layout/header.jsp"%>
<%!
    String greeting = "AeBong's Mall";
    String tagline = "Welcome to Web Market!";
%>
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">
            <%=greeting%>
        </h1>
        <c:if test="${sessionScope.sessionMemberName != null && sessionScope.sessionMemberName != ''}">
            <p>${sessionScope.sessionMemberName} 님 환영합니다.</p>
        </c:if>
    </div>
</div>
<div class="container">
    <div class="text-center">
        <h3>
            <%=tagline%>
        </h3>
        <c:set var="currentDate" value="<%= new Date() %>"/>
        <fmt:formatDate value="${currentDate}" pattern="hh:mm:ss a" var="formattedDate"/>
        <c:out value="현재 접속 시각: ${formattedDate}"/>
    </div>
    <hr>
</div>
<%@ include file="../layout/footer.jsp"%>
</body>
</html>