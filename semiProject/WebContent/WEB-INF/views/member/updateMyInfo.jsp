<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

<!-- Bootstrap core JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>

<style>
#email-div, #certifyBtn { display : inline-block !important;}
#certifyBtn { border-radius : 15px; }
	
</style>
</head>
<body>

<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}"/>
	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<c:if test="${!empty param.cd}">
		<c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}"/>
	</c:if>


	<!-- header -->
	<jsp:include page="../common/header.jsp"></jsp:include>

	<div class="container">
		<jsp:include page="../member/myPageMenu.jsp"></jsp:include>


		
		<!-- 회원 정보 수정 폼 -->
			<br><br>
		<div class="row">
			<div class="col-md-6 offset-md-3">
				<form method="POST" action="updateMyInfo.do" class="needs-validation" name="updateMyInfoForm" onsubmit="return updateMyInfoValidate();">
				
					<!-- 아이디 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="id">아이디</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" name="id" id="id" maxlength="12" autocomplete="off" value="${loginMember.memId}" readOnly>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkId">&nbsp;</span>
						</div> 
					</div>

					<!-- 이름 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="name">이름</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" id="name" name="name" value="${loginMember.memNm}" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkName">&nbsp;</span>
						</div>
					</div>

					<!-- 전화번호 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="phone">전화번호</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control phone" id="phone" name="phone" placeholder="전화번호 '-' 제외" value="${loginMember.phone}" required>
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
						<div class="col-md-6" id="email-div">
							<input type="email" class="form-control" id="email" name="email" placeholder="이메일" autocomplete="off" required>
						</div>
						<button class="btn btn-secondary" type="button" id="certifyBtn">인증</button>
						<div class="col-md-6 offset-md-3">
							<span id="checkEmail">&nbsp;</span>
						</div>
					</div>			
					<!-- 이메일 인증 -->
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="email">* 이메일 인증</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" id="certify" name="certify" placeholder="이메일 인증번호" autocomplete="off" required>
						</div>
						<div class="col-md-6 offset-md-3">
							<span id="checkCertify">&nbsp;</span>
						</div>
					</div>		
					
					<div class="row mb-3 form-row">
						<div class="col-md-4">
							<label for="nickName">닉네임</label>
						</div>
						<div class="col-md-6">
							<input type="text" class="form-control" id="nickName" name="nickName" autocomplete="off" value="${loginMember.nickName}" required>
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
						<input class="form-check-input petYn" type="radio" name="petYn" id="petY" value="Y">
						<label class="form-check-label" for="petY">
							유 &nbsp;&nbsp;&nbsp;&nbsp;
						</label>
					</div>
					<div class="form-check">
						<input class="form-check-input petYn" type="radio" name="petYn" id="petN" value="N">
						<label class="form-check-label" for="petN">
							무
						</label>
						</div>
					</div>
					<br>
		
					<button class="btn btn-teal btn-lg btn-block" type="submit">확인</button>
				</form>
			</div>
		</div>
		<br><br>
		<br><br>		

		
	</div>
	<script src="${contextPath}/resources/js/semi_member.js"></script>
		<!-- footer -->
		<jsp:include page="../common/footer.jsp"></jsp:include>
	
	<script>
	// 반려동물 유무
	(function(){
		$(".petYn").each(function(index, item){
			if($(item).val() == "${loginMember.petYn}") {
				$(item).prop("checked", true);
			}
		});
	})();
	
	
	</script>
	
</body>
</html>