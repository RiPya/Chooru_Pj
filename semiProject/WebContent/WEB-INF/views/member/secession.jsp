<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>

<head>
<meta charset="UTF-8">
<title>회원탈퇴</title>
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
	/*   margin: auto; */
	box-sizing: border-box;
}

/* 탈퇴 페이지 라인 */
.form-secession {
	font-size: 18px;
	font-weight: bolder;
	padding-right: 20px;
	font-family: 'TmoneyRoundWindRegular';
}

.header-secession {
	width: 100%;
	margin-bottom: 4rem;
}

.form-hr>hr {
	width: 90%;
	height: 8px;
	margin-bottom: .1rem;
	display: inline-block;
	background-color: teal;
	border-radius: 1rem;
}

.btn {
	text-align: center;
}

/* 탈퇴 구문 */
.secession-text {
	top: 100px;
	text-align: center;
}

.secession-area {
	width: 100%;
	height: 100%;
	padding: 2px;
	margin: auto;
	box-sizing: border-box;
}

.secession-area {
	width: 70%;
	height: 84%;
	padding-top: 35px;
	margin: auto;
	background-color: #F6F6F6;
	text-align: center;
}

#newPwd2 {
	margin-bottom: 20px;
}

#btn-area {
	margin : 0 15%;
}

#currentPwd{
	margin-bottom : 10px;
}

</style>
</head>

<body>


	<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}" />
	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}" />

	<jsp:include page="../common/header.jsp"></jsp:include>

	<div class="container wrapper" id="content-main">
		<jsp:include page="../member/myPageMenu.jsp"></jsp:include>

		<div class="row my-5 content-section">

			<br>
			<br>
			<br>
			<br>
			<div class="header-secession">
				<span class="form-secession">회원 탈퇴</span> 
				<span class="form-hr"><hr></span>
			</div>
			<br> <br>

			<div class="secession-area">
				<form method="POST" action="updateStatus.do" class="form-horizontal"
						role="form" onsubmit="return scValidate();">
				<!-- 비밀번호 입력 -->
					<div class="col-md-12">
						<textarea class="form-control" readonly rows="10" cols="100"
							style="resize: none;">
제1조
이 약관은 샘플 약관입니다.

① 약관 내용 1

② 약관 내용 2

③ 약관 내용 3

④ 약관 내용 4


제2조
이 약관은 샘플 약관입니다.

① 약관 내용 1

② 약관 내용 2

③ 약관 내용 3

④ 약관 내용 4
						</textarea>
						<br>
						<h6 style="padding-top: 25px;">탈퇴를 원하시면 비밀번호를 입력해주세요.</h6>
						<br>
					</div>
					<bR>
					<!-- 새 비밀번호 확인 -->
					<div class="row mb-4 form-row" id="btn-area">
						<input type="password" class="form-control" id="currentPwd"
							name="currentPwd" placeholder="비밀번호(password)">
						<button type="submit" class="btn btn-secondary btn-block btn-size">
							탈퇴</button>
						<button type="button" class="btn btn-teal btn-block btn-size"
							onclick="location.href='myActiveList.do';">돌아가기</button>
					</div>
				</form>
			</div>

		</div>
	</div>

	<jsp:include page="../common/footer.jsp"></jsp:include>
	<script src="${contextPath}/resources/js/semi_member.js"></script>
	
	<script>
		function scValidate(){
			if($("#currentPwd").val().trim().length == 0) {
				swal({icon:"warning", title:"비밀번호를 입력해주세요."});
				$("#currentPwd").focus();
				return false;
			}
		};
	</script>

</body>

</html>