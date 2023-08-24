<%--
  로그인 뷰
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<jsp:include page="/layout/header.jsp"/>
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3">로그인</h1>
    </div>
</div>
<div class="container" align="center">
    <div class="col-md-4 col-md-offset-4">
        <h3 class="form-signin-heading">Please sign in</h3>
        <form class="form-signin" action="process_login" method="post">
            <div class="form-group">
                <label for="inputUserName" class="sr-only">User Name</label>
                <label id="inputUserName" class="sr-only">User Name</label>
                <input type="text" class="form-control" placeholder="ID" name="id" required autofocus>
            </div>
            <div class="form-group">
                <label for="inputPassword" class="sr-only">Password</label>
                <label id="inputPassword" class="sr-only">Password</label>
                <input type="password" class="form-control" placeholder="Password" name="password" required>
            </div>
            <button class="btn btn btn-lg btn-success btn-block" type="submit">로그인</button>
        </form>
    </div>
</div>
<jsp:include page="/layout/footer.jsp"/>
</body>
</html>
