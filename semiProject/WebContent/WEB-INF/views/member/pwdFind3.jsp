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

.loginBtn{
	width: 125px;
	height: 38px;
}

.button-area{
	width: 410px;
	height: 40px;
	margin-left: 0 auto;
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
						<div class="area text-area">
							<h6>비밀번호가 변경되었습니다.</h6>
						</div>

						<div class="button-area">
							<a class="btn nav-link btn-teal loginBtn float-left ml-1 mr-1" data-toggle="modal" href="#modal-container-1" >로그인 하기</a>
							<button type="button" class="btn btn-darkteal float-left ml-1 mr-1 find">아이디 찾기</button>
							<button type="button" class="btn btn-secondary float-left ml-1 mr-1 find" >메인 페이지로</button>
						</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	<script>
		$(document).ready(function(){
			$(".find").on("click", function(){
				var text = $(this).text();

				var click = "";
				var url = "";

				switch(text){
					case "아이디 찾기": click = "";
										url = "${contextPath}/member/idFind1.do";
										break;
					case "메인 페이지로": click = "";
										 url = "${contextPath}/index.jsp";
										 break;
				}
				location.href = url;
			})
		});

	</script>
</body>

</html>