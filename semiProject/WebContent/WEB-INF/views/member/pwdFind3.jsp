<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>비밀번호 찾기</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

<!-- Bootstrap core JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
	crossorigin="anonymous"></script>

<!-- sweetalert : alert창을 꾸밀 수 있게 해주는 라이브러리 https://sweetalert.js.org/ -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

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
	background-color: #F6F6F6;
}

h4 {
	padding-top: 40px;
}

.container {
	text-align: center;
	margin: 0 auto;
}

.area {
	padding: 10%;
}

.btn-darkteal {
	background-color: #3f6564 !important;
	color: rgb(250, 255, 250) !important;
}

.btn-darkteal:hover {
	background-color: #013838 !important;
	color: rgb(250, 255, 250) !important;
}
</style>


</head>

<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container-fluid wrapper">


		<div class="content-section">
			<div class="col-md-6 py-5" id="content-main">
				<h4 style="padding-left: 50px">비밀번호 찾기</h4>

				<div class="container">
					<form method="POST" action="myIdFind.do"
						onsubmit="return IdFindValidate();" class="form-horizontal"
						role="form">
						<div class="area text-area">
							<h6>비밀번호가 변경되었습니다.</h6>
						</div>

						<div class="area button-area">
							<button type="submit" class="btn btn-teal">로그인 하기</button>
							<button type="button" class="btn btn-darkteal">아이디 찾기</button>
							<button type="menu" class="btn btn-secondary">메인 페이지로</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
</body>

</html>