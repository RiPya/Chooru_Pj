<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

<!-- Bootstrap core JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>

<style>
	.header-signUp{
		width: 100%;
		margin-bottom: 4rem;
	}

	.form-signUp{
		font-size: 18px;
		font-weight: bolder;
		padding-right: 20px;
	}

	.form-hr > hr{
		width: 90%;
		height: 8px;
		margin-bottom: .1rem;
		display: inline-block;
		background-color: teal;
		border-radius: 1rem;
	}
	
</style>
</head>
<body>
		<!-- header -->
		<jsp:include page="../common/header.jsp"></jsp:include>

	<div class="container">
		
		<br><br><br><br>
		<div class="header-signUp">
			<span class="form-signUp">회원가입</span>
			<span class="form-hr"><hr></span>
		</div>
		<div class="row">
			<div class="col-md-6 offset-md-3">
				<form method="POST" action="signUp.do" class="needs-validation" name="signUpForm" onsubmit="return validate();">
				
					<!-- 아이디 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="id">* 아이디</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" name="id" id="id"
								maxlength="12" placeholder="아이디" autocomplete="off" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkId">&nbsp;</span>
						</div> 
					</div>

					<!-- 비밀번호 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="pwd1">* 비밀번호</label>
						</div>
						<div class="col-md-6">
							<input type="password" class="form-control" id="pwd1" name="pwd1" maxlength="12" placeholder="비밀번호" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkPwd1">&nbsp;</span>
						</div>
					</div>

					<!-- 비밀번호 확인 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="pwd2">* 비밀번호 확인</label>
						</div>
						<div class="col-md-6">
							<input type="password" class="form-control" id="pwd2" maxlength="12" placeholder="비밀번호 확인" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkPwd2">&nbsp;</span>
						</div>
					</div>

					<!-- 이름 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="name">* 이름</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" id="name" name="name" placeholder="이름" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkName">&nbsp;</span>
						</div>
					</div>

					<!-- 전화번호 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="phone">* 전화번호</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control phone" id="phone" name="phone" placeholder="전화번호 '-' 제외" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkPhone">&nbsp;</span>
						</div>
					</div>

					<!-- 이메일 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="email">* 이메일</label>
						</div>
						<div class="col-md-6">
							<input type="email" class="form-control" id="email" name="email" placeholder="이메일" autocomplete="off" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkEmail">&nbsp;</span>
						</div>
					</div>			
					
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="nickName">닉네임</label>
						</div>
						<div class="col-md-6">
							<input type="nickName" class="form-control" id="nickName" name="nickName" autocomplete="off" placeholder="닉네임" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkNickName">&nbsp;</span>
						</div>
					</div>

					<div class="row">
						<div class="col-md-4">
							<label>반려동물 유무</label>
						</div>
					<div class="form-check">
						<input class="form-check-input" type="radio" name="petYn" id="y" value = 'Y'>
						<label class="form-check-label" for="flexRadioDefault1">
							유 &nbsp;&nbsp;&nbsp;&nbsp;
						</label>
					</div>
					<div class="form-check">
						<input class="form-check-input" type="radio" name="petYn" id="n"  value='N' checked>
						<label class="form-check-label" for="flexRadioDefault2">
							무
						</label>
						</div>
					</div>
					<br>
		
					<button class="btn btn-teal btn-lg btn-block" type="submit">가입하기</button>
				</form>
			</div>
		</div>
		<br><br>
		<br><br>		
		
		<!-- 회원 관련 Javascript 코드를 모아둘 wsp_member.js 파일을 작성 -->
		<script src="${contextPath}/resources/js/semi_member.js?ver=350"></script>
	</div>
		<!-- footer -->
		<jsp:include page="../common/footer.jsp"></jsp:include>
	
</body>
</html>