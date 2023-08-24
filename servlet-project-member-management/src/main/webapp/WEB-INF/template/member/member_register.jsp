<%--
  회원 등록 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>회원 가입</title>
    <script type="text/javascript">
        function checkForm() {
            if (!document.formMemberInsert.id.value) {
                alert("아이디를 입력하세요.");
                return false;
            }

            if (!document.formMemberInsert.password.value) {
                alert("비밀번호를 입력하세요.");
                return false;
            }

            if (document.formMemberInsert.password.value !== document.formMemberInsert.password_confirm.value) {
                alert("비밀번호를 동일하게 입력하세요.");
                return false;
            }
        }
    </script>
</head>
<body>
<jsp:include page="/layout/header.jsp"/>
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">회원 가입</h1>
    </div>
</div>

<div class="container">
    <form name="formMemberInsert" class="form-horizontal" action="process_member_register" method="post"
          onsubmit="return checkForm()">
        <div class="form-group row">
            <label class="col-sm-2 ">아이디</label>
            <div class="col-sm-3">
                <input name="id" type="text" class="form-control" placeholder="id" required>
                <span class="idCheck"></span>
                <br><input type="button" name="btnIsDuplication" value="alert 아이디 중복 확인">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2">비밀번호</label>
            <div class="col-sm-3">
                <input name="password" type="text" class="form-control" placeholder="password" required>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2">비밀번호확인</label>
            <div class="col-sm-3">
                <input name="password_confirm" type="text" class="form-control" placeholder="password confirm" required>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2">성명</label>
            <div class="col-sm-3">
                <input name="name" type="text" class="form-control" placeholder="name" required>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2">성별</label>
            <div class="col-sm-10">
                <input name="gender" type="radio" value="남" required> 남
                <input name="gender" type="radio" value="여" required> 여
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2">생일</label>
            <div class="col-sm-4  ">
                <input type="text" name="birthyy" maxlength="4" placeholder="년(4자)" size="6" required>
                <select name="birthmm" required>
                    <option value="">월</option>
                    <option value="01">1</option>
                    <option value="02">2</option>
                    <option value="03">3</option>
                    <option value="04">4</option>
                    <option value="05">5</option>
                    <option value="06">6</option>
                    <option value="07">7</option>
                    <option value="08">8</option>
                    <option value="09">9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                </select> <input type="text" name="birthdd" maxlength="2" placeholder="일" size="4">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2">이메일</label>
            <div class="col-sm-10">
                <input type="text" name="mail1" maxlength="50" required>@
                <select name="mail2" required>
                    <option>naver.com</option>
                    <option>daum.net</option>
                    <option>gmail.com</option>
                    <option>nate.com</option>
                </select>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2">전화번호</label>
            <div class="col-sm-3">
                <input name="phone" type="text" class="form-control" placeholder="phone" required>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 ">우편번호</label>
            <div class="col-sm-2">
                <input name="zipcode" type="text" class="form-control" placeholder="zipcode" readonly required>
                <input type="button" value="우편번호 검색" onclick="execDaumPostcode();">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 ">주소1</label>
            <div class="col-sm-5">
                <input name="addr1" type="text" class="form-control" placeholder="addr1" readonly required>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 ">주소2</label>
            <div class="col-sm-5">
                <input name="addr2" type="text" class="form-control" placeholder="addr2" required>
            </div>
        </div>
        <div class="form-group  row">
            <label class="col-sm-2 ">생성일시</label>
            <div class="col-sm-5">
                <input name="createdAt" type="text" class="form-control" hidden readonly>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-offset-2 col-sm-10 ">
                <input type="submit" class="btn btn-primary " value="등록 ">
                <input type="reset" class="btn btn-primary " value="취소 " onclick="reset()">
            </div>
        </div>
    </form>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            // const inputID = document.querySelector('input[name=id]');
            const formMemberInsert = document.formMemberInsert; //폼을 들고옴

            //ajax를 이용한 alert 방식의 ID 중복확인
            const xhr = new XMLHttpRequest();	//XMLHttpRequest 객체 생성
            const btnIsDuplication = document.querySelector('input[name=btnIsDuplication]');
            btnIsDuplication.addEventListener('click', function () {
                const id = formMemberInsert.id.value;	//아이디 input에 있는 값
                xhr.open('POST', 'ajax_id_check?id=' + id);	//HTTP 요청 초기화. 통신 방식과 url 설정
                xhr.send();	//url에 요청을 보냄.
                //이벤트 등록. XMLHttpRequest 객체의 readyState 프로퍼티 값이 변할 때마다 자동으로 호출.
                xhr.onreadystatechange = () => {
                    //readyState 프로퍼티의 값이 DONE : 요청한 데이터의 처리가 완료되어 응답할 준비가 완료됨.
                    if (xhr.readyState !== XMLHttpRequest.DONE) return;

                    if (xhr.status === 200) {	//서버(url) 에 문서가 존재함
                        console.log(xhr.response);
                        const json = JSON.parse(xhr.response);
                        if (json.result === 'true') {
                            alert('동일한 아이디가 있습니다.')
                        } else {
                            alert('동일한 아이디가 없습니다.')
                        }
                    } else {	//서버(url)에 문서가 존재하지 않음.
                        console.error('Error', xhr.status, xhr.statusText);
                    }
                }
            });

            //ajax를 이용한 실시간 ID 중복확인
            const inputId = document.querySelector('input[name=id]');
            inputId.addEventListener('keyup', function () {
                const id = formMemberInsert.id.value;	//아이디 input에 있는 값
                const idCheck = document.querySelector('.idCheck');	//결과 문자열이 표현될 영역
                xhr.open('POST', 'ajax_id_check?id=' + id);	//HTTP 요청 초기화. 통신 방식과 url 설정
                xhr.send();	//url에 요청을 보냄.
                //이벤트 등록. XMLHttpRequest 객체의 readyState 프로퍼티 값이 변할 때마다 자동으로 호출.
                xhr.onreadystatechange = () => {
                    //readyState 프로퍼티의 값이 DONE : 요청한 데이터의 처리가 완료되어 응답할 준비가 완료됨.
                    if (xhr.readyState !== XMLHttpRequest.DONE) return;

                    if (xhr.status === 200) {	//서버(url) 에 문서가 존재함
                        const json = JSON.parse(xhr.response);
                        if (json.result === 'true') {
                            idCheck.style.color = 'red';
                            idCheck.innerHTML = '동일한 아이디가 있습니다.';
                        } else {
                            idCheck.style.color = 'gray';
                            idCheck.innerHTML = '동일한 아이디가 없습니다';
                        }
                    } else {	//서버(url)에 문서가 존재하지 않음.
                        console.error('Error', xhr.status, xhr.statusText);
                    }
                }
            });
        });
    </script>
    <%-- 우편번호 검색 API (카카오) --%>
    <script src="https://spi.maps.daum.net/imap/map_js_init/postcode.v2.js"></script>
    <script>
        function execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var fullAddr = ''; // 최종 주소 변수
                    var extraAddr = ''; // 조합형 주소 변수

                    // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        fullAddr = data.roadAddress;
                    }
                    else { // 사용자가 지번 주소를 선택했을 경우(J)
                        fullAddr = data.jibunAddress;
                    }

                    // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                    if(data.userSelectedType === 'R'){
                        //법정동명이 있을 경우 추가한다.
                        if(data.bname !== ''){
                            extraAddr += data.bname;
                        }
                        // 건물명이 있을 경우 추가한다.
                        if(data.buildingName !== ''){
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                        fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                    }

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.querySelector('input[name=zipcode]').value = data.zonecode; //5자리 새우편번호 사용
                    document.querySelector('input[name=addr1]').value = fullAddr;

                    // 커서를 상세주소 필드로 이동한다.
                    document.querySelector('input[name=addr2]').focus();
                }
            }).open();
        }
    </script>
</div>
<jsp:include page="/layout/footer.jsp"/>
</body>
</html>
