<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>비밀번호 찾기</title>

<style>
html, body {
	height: 100%;
}

body {
	margin: 0;
}

.wrapper {
	height: 100%;
	min-height: 100%;
	padding: 0;
	margin: 0;
}

.header {
	height: 15%;
}

.content-section {
	height: 75%;
	display: flex;
}

.footer {
	height: 10%;
}

div {
	margin: auto;
	box-sizing: border-box;
}

#content-main {
	height: 650px;
	line-height: 50px;
	background-color: #F6F6F6;
}

h4 {
	padding-top: 40px;
}

.container {
	text-align: center;
	margin: 0 auto;
}

.btn-teal {
	background-color: teal;
	color: rgb(250, 255, 250);
}

.btn-teal:hover {
	background-color: #006666;
	color: rgb(250, 255, 250);
}

/* input창 number 화살표 없애기 */
input[type="number"]::-webkit-outer-spin-button, input[type="number"]::-webkit-inner-spin-button
	{
	-webkit-appearance: none;
	margin: 0;
}

.inputArea {
padding: 5px;
	display: inline-block;
}

.inputText, .inputBotton {
	width: 25%;
}

.inputButton {
	width: 25%;
}

.inputType {
	width: 75%;
}

</style>


</head>

<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container-fluid wrapper">


		<div class="content-section">
			<div class="col-md-6 py-5" id="content-main">
				<h4 style="padding-left: 50px">비밀번호 찾기</h4>
				<br>

				<div class="container">
					<form method="POST" action="${contextPath}/member/myPwdFind2.do"
						onsubmit="return PwdFindValidate();" class="form-horizontal"
						role="form">

						<!-- 새 비밀번호 -->
						<div class="row mb-1 form-row">
							<div class="inputArea inputText">
								<h6>새 비밀번호:</h6>
							</div>
							<div class="inputArea inputType">
								<input type="password" class="form-control" id="newPwd1"
									name="newPwd1" placeholder="새 비밀번호 입력">
							</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkPwd1">&nbsp;</span>
						</div>							
						</div>

						<!-- 새 비밀번호 확인 -->
						<div class="row mb-1 form-row">
							<div class="inputArea inputText">
								<h6>새 비밀번호 확인:</h6>
							</div>
							<div class="inputArea inputType">
								<input type="password" class="form-control" id="newPwd2"
									name="newPwd2" placeholder="새 비밀번호 확인">
							</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkPwd2">&nbsp;</span>
						</div>							
						<input type = "text" class="sr-only" name="memNo" value="${param.memNo}" id="memNo">
						</div>
						<br>
						<button type="submit" class="btn btn-teal btn-block btn-lg">확인</button>
						<button type="button" class="btn btn-secondary btn-block btn-lg">돌아가기</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	<script src="${contextPath}/resources/js/pwdFind.js"></script>		
	<jsp:include page="../common/footer.jsp"></jsp:include>

	<script>
		
	</script>


</body>

</html>