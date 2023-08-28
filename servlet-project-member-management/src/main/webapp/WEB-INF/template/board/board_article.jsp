<%@ page import="com.member.servletprojectmembermanagement.dto.BoardDto" %><%--
  게시글 조회 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>게시글</title>
</head>
<body>
<jsp:include page="/layout/header.jsp" />
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">게시판</h1>
    </div>
</div>
<div class="container">
    <div class="form-group row">
        <label class="col-sm-2 control-label">성명</label>
        <div class="col-sm-3">${board.member.name}</div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 control-label">제목</label>
        <div class="col-sm-5">${board.title}</div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 control-label">내용</label>
        <div class="col-sm-8" style="word-break: break-all;">
            ${board.content}
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 control-label">이미지</label>
        <div class="col-sm-8" style="word-break: break-all;">
            <img src="/upload/${board.fileName}">
        </div>
    </div>

    <%-- 댓글 목록 --%>


    <div class="form-group row">
        <div class="col-sm-offset-2 col-sm-10">
            <%--로그인 한 id와 동일하면 삭제--%>
            <c:if test="${sessionMemberId == board.member.id}">
                <p>
                    <span class="btn btn-danger" onclick="goDelete();">삭제</span>
                    <span class="btn btn-success" onclick="goUpdate();">수정</span>
                </p>
            </c:if>
            <a href="board_list?pageNum=${pageNum}" class="btn btn-primary">목록</a>
        </div>
    </div>
    <form name="fromUpdate" method="post">
        <input type="hidden" name="num" value="${num}">
        <input type="hidden" name="pageNum" value="${pageNum}">
    </form>
    <script>
        let goUpdate = function () {
            const frm = document.fromUpdate;
            frm.action = "board_update";
            frm.submit();
        }

        let goDelete = function () {
            if (confirm("삭제하시겠습니까?")) {
                const frm = document.fromUpdate;
                frm.action = "process_board_delete";
                frm.submit();
            }
        }
    </script>
    <hr>
</div>
<jsp:include page="/layout/footer.jsp" />

</body>
</html>