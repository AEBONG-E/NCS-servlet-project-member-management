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
<jsp:include page="/layout/header.jsp"/>
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
    <script>
        const xhr = new XMLHttpRequest();
        const getRipples = function () {
            /* 리플 목록을 가져옴 */
            let num = document.querySelector('form[name=formUpdate] input[name=num]');
            xhr.open('GET', '/ripple/ripple_list?boardNum=' + num.value);
            xhr.send();
            xhr.onreadystatechange = () => {
                if (xhr.readyState !== XMLHttpRequest.DONE) return;

                if (xhr.status === 200) {   //서버(url)에 문서가 존재함
                    console.log(xhr.response);
                    const json = JSON.parse(xhr.response);
                    console.log("Received response:", xhr.response);
                    for (let data of json) {
                        console.log(data);
                    }
                    insertRippleTag(json);
                } else {  //서버(url)에 문서가 존재하지 않음.
                    console.error('Error', xhr.status, xhr.statusText);
                }
            }
        }
        document.addEventListener("DOMContentLoaded", function () {
            getRipples();
        });
    </script>

    <%-- 댓글 목록 출력 영역 --%>
    <div class="form-group row user-repple-list">
        <ul>

        </ul>

    </div>
    <script>

        const insertRippleTag = function (items) {
            /* 목록에 요소를 추가. 처음 로딩시 목록을 출력하거나, 새로운 글 등록시, 삭제시 사용 */
            let tagUl = document.querySelector('.user-repple-list ul');
            tagUl.innerHTML = '';   //문서가 로딩될 때 ul 안에 무엇인가 있을 경우 요소를 비우는 역할(초기화)
            for (let item of items) {
                let tagNew = document.createElement('li');
                tagNew.innerHTML = item.content + ' | ' + item.name + ' | ' + item.createdAt;
                if (item.isLogin === true) {
                    tagNew.innerHTML +=
                        ' <span class="btn btn-danger" onclick="goRippleDelete(\'' + item.rippleId + '\');"> >삭제</span> ';
                }
                tagNew.setAttribute('class', 'list-group-item');
                tagUl.append(tagNew);
            }

        }


        /* 리플 삭제 하기 */
        const goRippleDelete = function (rippleId) {
            if (confirm("삭제하시겠습니까?")) {
                const xhr = new XMLHttpRequest();  //XMLHttpRequest 객체 생성
                //xhr.open('POST', '../board/ajax_delete.jsp?rippleId=' + ID);
                xhr.open('POST', '/ripple/process_ripple_delete?rippleId=' + rippleId);
                xhr.send();
                xhr.onreadystatechange = () => {
                    if (xhr.readyState !== XMLHttpRequest.DONE) return;

                    if (xhr.status === 200) {   //서버(url)에 문서가 존재함
                        console.log(xhr.response);
                        const json = JSON.parse(xhr.response);
                        console.log("Received response:", xhr.response);
                        if (json.result === 'true') {
                            getRipples();
                        } else {
                            alert("삭제에 실패했습니다.")
                        }
                    } else {  //서버(url)에 문서가 존재하지 않음
                        console.log('Error', xhr.status, xhr.statusText);
                    }
                }
            }
        }
    </script>
    <%--// 리플 목록 출력 영역 --%>

    <c:if test="${sessionMemberId != null}">
        <form name="frmRipple" class="form-horizontal" method="post">
            <c:out value="${board.num}" />
            <input type="hidden" name="num" value="${board.num}">
            <div class="form-group row">
                <label class="col-sm-2 control-label">성명</label>
                <div class="col-sm-3">
                    <input name="name" type="text" class="form-control" value="${sessionMemberName}" readonly>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 control-label">내용</label>
                <div class="col-sm-8" style="word-break: break-all;">
                    <textarea name="content" class="form-control" cols="50" rows="3"></textarea>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 control-label"></label>
                <div class="col-sm-3">
                    <span class="btn btn-primary" name="goRippleRegister">등록</span>
                </div>
            </div>
        </form>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const xhr = new XMLHttpRequest();   //XMLHttpRequest 객체 생성
                document.querySelector('span[name=goRippleRegister]').addEventListener('click', function () {
                    //값 들고옴
                    let num = document.querySelector('input[name=num]');
                    let name = document.querySelector('input[name=name]');
                    let content = document.querySelector('textarea[name=content]');

                    xhr.open('POST', '/ripple/process_ripple_register');
                    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
                    xhr.send('num='+num.value+'&name='+name.value+'&content='+content.value);
                    xhr.onreadystatechange = () => {
                        if (xhr.readyState !== XMLHttpRequest.DONE) return;
                        if (xhr.status === 200) {   //서버(url)에 문서가 존재함
                            const json = JSON.parse(xhr.response);
                            if (json.result === 'true') {
                                alert(json.message);
                                content.value = ''; //input 태그에 입력된 값 삭제.
                                getRipples();
                            } else {
                                alert(json.message);
                                alert("등록에 실패했습니다.");
                            }
                        } else {  //서버(url)에 문서가 존재하지 않음
                            console.log('Error', xhr.status, xhr.statusText);
                        }
                    }
                });
            });
        </script>
    </c:if>

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
    <form name="formUpdate" method="post">
        <input type="hidden" name="num" value="${num}">
        <input type="hidden" name="pageNum" value="${pageNum}">
    </form>
    <script>
        let goUpdate = function () {
            const frm = document.formUpdate;
            frm.action = "board_update";
            frm.submit();
        }

        let goDelete = function () {
            if (confirm("삭제하시겠습니까?")) {
                const frm = document.formUpdate;
                frm.action = "process_board_delete";
                frm.submit();
            }
        }
    </script>
    <hr>
</div>
<jsp:include page="/layout/footer.jsp"/>

</body>
</html>