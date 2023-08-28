<%--
  게시글 생성 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String sessionMemberId = (String) session.getAttribute("sessionMemberId");
    String sessionMemberName = (String) session.getAttribute("sessionMemberName");
%>
<html>
<head>
    <title>게시판 등록</title>
</head>
<script type="text/javascript">
    /*
    session 의 값이 없을 경우 로그인 페이지로 이동
     */
    if (${sessionMemberId == null}) {
        alert("로그인 해주세요");
        location.href = "/member/login";
    }

    /*
    게시판 글쓰기 입력 폼에서 submit 할 때 검증
     */
    function checkForm() {
        if (!document.form_register.name.value) {
            alert("성명을 입력하세요");
            return false;
        }
        if (!document.form_register.subject.value) {
            alert("제목을 입력하세요");
            return false;
        }
        if (!document.form_register.content.value) {
            alert("내용을 입력하세요");
            return false;
        }
    }


</script>
<body>
<jsp:include page="/layout/header.jsp" />
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">게시판</h1>
    </div>
</div>

<div class="container">
    <form name="form_register" action="process_board_register" class="form-horizontal" method="post" onsubmit="return checkForm()" enctype="multipart/form-data">
        <div class="form-group row">
            <label class="col-sm-2 control-label">작성자</label>
            <div class="col-sm-3">
                <input name="name" type="text" class="form-control" value="${sessionMemberName}" placeholder="name" required readonly>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 control-label">제목</label>
            <div class="col-sm-5">
                <input name="title" type="text" class="form-control" placeholder="제목 입력" required>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 control-label">내용</label>
            <div class="col-sm-8">
                <textarea name="content" cols="50" rows="5" class="form-control" placeholder="내용 입력" required></textarea>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 control-label">이미지</label>
            <div class="col-sm-8">
                <input type="file" name="addImage" class="form-control" required>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="submit" class="btn btn-primary" value="등록">
                <input type="reset" class="btn btn-primary" value="취소">
            </div>
        </div>
    </form>
    <hr>
</div>
<jsp:include page="/layout/footer.jsp" />
</body>
</html>
