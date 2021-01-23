<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>

.menu-wrapper {
	top: 1px;
	left: 0;
	height: auto;
	width: 100%;
}

.menu > li {
	display: inline-block;
	margin: 10px;
}

/*메뉴에서 게시판명 폰트 조정*/
.menu li:first-of-type {
	font-size : 20px;
	font-weight : bold;
	font-family: 'TmoneyRoundWindRegular';  
}

#insertBtn{
	margin-top : 5px;
}

.menu > li > button {
	border-radius: 20px;
	text-decoration: none;
	color: teal;
	padding: 6px 12px;
	display: block;
}

.menu > li > button:hover{
	background-color: teal;
	color: white;
}


/*클릭 시 추가해서 사용할 클래스 (다른 것을 클릭하면 제거 : script에서 사용)
 첫 페이지에서는 '전체'에 적용*/
.menu > li > .menu-active {
	background-color: teal !important;
	color: white !important;
	font-weight : bold !important;
}
</style>
</head>
<body>
 <%-- url 작성 시 붙여야 하는 str --%>
   <!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
   <c:set var="tpStr" value="tp=${param.tp}"/>
   <!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
   <c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}"/>
   
   
		<!-- 마이 페이지 > 사이드 메뉴 -->
		<div class="menu-wrapper">
			<ul class="menu">
				<li>마이 페이지</li>
				<li><button class="btn btn-light myPage <c:if test="${empty param.cd || param.cd == 'myActive'}">menu-active</c:if>" type="button">내 활동 조회</button></li>
				<li><button class="btn btn-light myPage <c:if test="${param.cd == 'updateInfo'}">menu-active</c:if>" type="button">내 정보 수정</button></li>
				<li><button class="btn btn-light myPage <c:if test="${param.cd == 'updatePwd'}">menu-active</c:if>" type="button">비밀번호 변경</button></li>
				<li><button class="btn btn-light myPage <c:if test="${param.cd == 'updateStatus'}">menu-active</c:if>" type="button">회원 탈퇴</button></li>
			</ul>
		</div>
		
		
		<script>
		
		$(document).ready(function(){
			
			/* 메뉴 클릭 관련 함수 */
			$(".myPage").on("click", function(){
				//해당 요소의 내용(카테고리 freeCode)을 가져오기
				var category = $(this).text(); 
				
				var myPageCode = "";
				var url = "";
				switch(category){
				case "내 활동 조회" : myPageCode = "myActive";
													url = "${contextPath}/member/myActiveList.do?${tpStr}&cp=1&cd=" + myPageCode;
													break;
				case "내 정보 수정" : myPageCode = "updateInfo";
													url = "${contextPath}/member/updateMyInfoForm.do?${tpStr}&cd=" + myPageCode;
													break;
				case "비밀번호 변경" : myPageCode = "updatePwd";
													url = "${contextPath}/member/changePwdForm.do?${tpStr}&cd=" + myPageCode;
													break;
				case "회원 탈퇴" : myPageCode = "updateStatus";
													url = "${contextPath}/member/updateStatusForm.do?${tpStr}&cd=" + myPageCode;
													break;
				}
				
				//cp(페이지), tp(게시판타입 b1 b2 b3 b4 adminMem b5 mypage), cd(카테고리)
				//tpStr = tp=_
				
				location.href = url;
			});
			
		});
		
		</script>
		
		
		
</body>
</html>