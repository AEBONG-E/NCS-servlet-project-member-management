<%--
  회원 수정 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String sessionMemberId = (String) session.getAttribute("sessionMemberId");
    String sessionMemberName = (String) session.getAttribute("sessionMemberName");
%>
<html>
<head>
    <title>게시판 수정</title>
</head>
<script type="text/javascript">
    /*
    session 의 값이 없을 경우 로그인 페이지로 이동
     */
    if (${sessionMemberId == null}) {
        alert("로그인 해주세요");
        location.href = "/member/login";
    } else if (${sessionMemberId != board.member.id}) {
        alert('본인이 작성한 글만 수정할 수 있습니다.');
        history.back();
    }

    /*
    게시판 글쓰기 입력 폼에서 submit 할 때 검증
     */
    function checkForm() {
        if (!document.fromUpdate.name.value) {
            alert("성명을 입력하세요");
            return false;
        }
        if (!document.fromUpdate.title.value) {
            alert("제목을 입력하세요");
            return false;
        }
        if (!document.fromUpdate.content.value) {
            alert("내용을 입력하세요");
            return false;
        }
    }


</script>
<body>
<jsp:include page="/layout/header.jsp"/>
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">게시판</h1>
    </div>
</div>

<div class="container">
    <form name="fromUpdate" action="process_board_update" class="form-horizontal" method="post" onsubmit="return checkForm()"
          enctype="multipart/form-data">
        <input type="hidden" name="num" value="${board.num}">
        <input type="hidden" name="pageNum" value="${pageNum}">
        <div class="form-group row">
            <label class="col-sm-2 control-label">성명</label>
            <div class="col-sm-3">
                <input name="name" type="text" class="form-control" value="${sessionMemberName}" readonly>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 control-label">제목</label>
            <div class="col-sm-5">
                <input name="title" type="text" class="form-control" value="${board.title}">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 control-label">내용</label>
            <div class="col-sm-8" style="word-break: break-all;">
                <textarea name="content" cols="50" rows="5" class="form-control">${board.content}</textarea>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 control-label">이미지</label>
            <div class="col-sm-8">
                <input name="addImage" type="file" class="form-control" value="${board.fileName}" required>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-offset-2 col-sm-10">
                <c:if test="${sessionMemberId == board.member.id}">
                    <p><input type="submit" class="btn btn-success" value="수정"></p>
                </c:if>
                <a href="board_list?pageNum=${pageNum}" class="btn btn-primary">목록</a>
            </div>
        </div>
    </form>
    <hr>
</div>
<jsp:include page="/layout/footer.jsp"/>
</body>
</html>
