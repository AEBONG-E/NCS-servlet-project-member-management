<%--
  헤더 레이아웃
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String sessionMemberId = (String) session.getAttribute("sessionMemberId");
    String sessionMemberName = (String) session.getAttribute("sessionMemberName");
%>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<nav class="navbar navbar-expand  navbar-dark bg-dark">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="/main/welcome.jsp">Home</a>
        </div>
        <div>
            <ul class="navbar-nav mr-auto">
                <c:choose>
                    <c:when test="${empty sessionMemberId}">
                        <li class="nav-item"><a class="nav-link" href="<c:url value="/member/login"/>">로그인 </a>
                        </li>
                        <li class="nav-item"><a class="nav-link" href='<c:url value="/member/member_register"/>'>회원 가입</a>
                        </li>
                        <li class="nav-item"><a class="nav-link" href='<c:url value="/board/board_list"/>'>게시판</a></li>
                    </c:when>
                    <c:otherwise>
                        <li style="padding-top: 7px; color: white">[<%=sessionMemberName%>님]</li>
                        <li class="nav-item"><a class="nav-link" href="<c:url value="/member/process_logout"/>">로그아웃 </a>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="<c:url value="/member/member_update"/>">회원 수정</a>
                        </li>
                        <li class="nav-item"><a class="nav-link" href='<c:url value="/board/board_list"/>'>게시판</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>