<%--
  서비스 결과 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>서비스 페이지</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
        integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm2rOTyXmOVZYg5g1NQAp3Edw"
        crossorigin="anonymous">
</head>
<body>
<jsp:include page="/layout/header.jsp" />
<div class="container">
  <h1>환영합니다.</h1>
  <c:choose>
    <c:when test="${param.msg == '1'}">
      <div class="alert alert-success" role="alert">
        회원가입이 성공적으로 완료되었습니다.
      </div>
    </c:when>
    <c:when test="${param.msg == '2'}">
      <div class="alert alert-success" role="alert">
        환영합니다.
      </div>
    </c:when>
  </c:choose>
  <button class="btn btn-primary" onclick="location.href='/main/welcome.jsp'">메인 페이지로 이동</button>
</div>

<jsp:include page="/layout/footer.jsp" />
</body>
</html>
