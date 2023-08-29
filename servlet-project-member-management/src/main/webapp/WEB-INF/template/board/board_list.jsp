<%--
  게시판 목록 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="com.member.servletprojectmembermanagement.dto.BoardDto" %>


<%
    String sessionMemberId = (String) session.getAttribute("sessionMemberId");
    List boardList = (List) request.getAttribute("boardList");
    int totalPage = ((Integer) request.getAttribute("totalPage")).intValue();
    int totalRecord = ((Integer) request.getAttribute("totalRecord")).intValue();
%>
<html>
<head>
    <title>게시판 목록</title>
    <script type="text/javascript">
        function checkForm() {
            if (${sessionMemberId == null}) {
                alert("로그인 해주세요.");
                location.href = "/member/login";
            }
            location.href = "/board/board_register";
        }
    </script>
</head>
<body>
<jsp:include page="/layout/header.jsp" />
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">게시판</h1>
    </div>
</div>
<div class="container">
    <form action="board_list" method="post">
        <div>
            <div class="text-right">
                <span class="badge badge-success">전체 <%=totalRecord%>건 </span>
            </div>
        </div>
        <div style="padding-top: 50px">
            <table class="table table-hover">
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성일</th>
                    <th>수정일</th>
                    <th>조회</th>
                    <th>글쓴이</th>
                </tr>
                <c:set var="no" value="${totalRecord -((pageNum - 1) * limit)}" />
                <c:forEach var="board" items="${boardList}">
                    <tr>
                        <td>${no}</td>
                        <td><a href="board_article?num=${board.num}&pageNum=${pageNum}">${board.title}</a>
                            <c:if test="${board.rippleCnt > 0}">(${board.rippleCnt})</c:if>
                        </td>
                        <td>${board.createdAt}</td>
                        <td>${board.updatedAt}</td>
                        <td>${board.hit}</td>
                        <td>${board.member.name}</td>
                    </tr>
                    <c:set var="no" value="${no -1}" />
                </c:forEach>
            </table>
        </div>
        <%-- 페이징 처리 --%>
        <div align="center">
            <a href="<c:url value="board_list?pageNum=1" />">첫 페이지</a>
            <c:if test="${thisBlock > 1}">
                <a href="<c:url value="board_list?pageNum=${firstPage - 1}" />">이전</a>
            </c:if>
            <c:forEach var="i" begin="${firstPage}" end="${lastPage}">
                <c:choose>
                    <c:when test="${pageNum == i}">
                        <a href="<c:url value="board_list?pageNum=${i}" /> ">
                            <span style="color: #4C5317; font-weight: bold;">[${i}]</span></a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="board_list?pageNum=${i}" /> "><span style="color: #4C5317;">[${i}]</span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${thisBlock < totalBlock}">
                <a href="<c:url value="board_list?pageNum=${lastPage + 1}" />">다음</a>
            </c:if>
            <a href="<c:url value="board_list?pageNum=${totalPage}" />">끝 페이지</a>
        </div>
        <%--// 페이징 처리 --%>
        <div align="left">
            <table>
                <tr>
                    <td width="100%" align="left">&nbsp;&nbsp;
                        <select name="items" class="txt">
                            <option value="title">제목에서</option>
                            <option value="content">본문에서</option>
                            <option value="name">글쓴이에서</option>
                        </select> <input name="text" type="text" /> <input type="submit" id="btnAdd" class="btn btn-primary" value="검색 " />
                    </td>
                    <td width="100%" align="right">
                        <a href="#" onclick="checkForm(); return false;" class="btn btn-primary">&laquo;글쓰기</a>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <hr>
</div>
<jsp:include page="/layout/footer.jsp" />
</body>
</html>
