<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>아이디 찾기</title>

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
height:550px;
line-height: 50px;
	background-color: #F6F6F6;
}

h4{
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
padding:5px;
	display: inline-block;
}

.inputText, .inputBotton {
	width: 25%;
}

.inputButton {
	width: 25%;
}

.inputType {
	width: 50%;
}

.inputType1{
	width: 75%
}

#memberName{
	width: 67%
}
</style>


</head>

<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container-fluid wrapper">


		<div class="content-section">
			<div class="col-md-6 py-5" id="content-main">
				<h4 style="padding-left:50px">아이디 찾기</h4>
				<h6 style="padding-left:50px">이메일 인증이 필요합니다.</h6>
				<br>

				<div class="container">
					<form method="POST" action="${contextPath}/member/myIdFind.do"
						onsubmit="return IdFindValidate();" class="form-horizontal"
						role="form">

						<!-- 회원 이름 -->
						<div class="row mb-1 form-row">
							<div class="inputArea inputText">
								<h6>이름:</h6>
							</div>
							<div class="inputArea inputType1">
								<input type="text" class="form-control" id="memberName"
									name="memberName" placeholder="가입자 이름">
							</div>
						</div>

						<!-- 회원 가입시 메일주소 -->
						<div class="row mb-1 form-row">
							<div class="inputArea inputText">
								<h6>메일주소:</h6>
							</div>
							<div class="inputArea inputType">
								<input type="text" class="form-control" id="email" name="email"
									placeholder="인증 받을 메일주소">
							</div>
							<div class="inputArea inputButton">
								<button type="button" class="btn btn-info" onclick="">인증
									하기</button>
							</div>
						</div>

						<!-- 메일 인증하기 -->
						<div class="row mb-4 form-row">
							<div class="inputArea inputText">
								<h6>인증 번호:</h6>
							</div>
							<div class="inputArea inputType">
								<input type="number" class="form-control" id="adduction"
									name="adduction" placeholder="메일로 받은 인증번호">
							</div>
							<div class="inputArea inputButton" style="float: right;">
								<button type="button" class="btn btn-secondary">인증
									확인</button>
							</div>
						</div>
						<br>

						<button type="submit" class="btn btn-teal btn-block btn-lg">아이디
							찾기</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>

	<script>
		
	</script>


</body>

</html>