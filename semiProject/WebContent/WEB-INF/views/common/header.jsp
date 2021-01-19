<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Semi Project</title>
		<!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

    <!-- Bootstrap core JS -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>

    <!-- sweetalert : alert창을 꾸밀 수 있게 해주는 라이브러리 https://sweetalert.js.org/ -->
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    
    <!--font awesome : 아이콘 사용 라이브러리-->
		<script src="https://kit.fontawesome.com/2dd9466b88.js" crossorigin="anonymous"></script>
	
		<!-- header의 css + btn-teal 버튼 -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/headerStyle.css?ver=1.5">

		<!-- Postcodify 오픈 소스 -->
		<script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>

<style>

/* 모달 스타일 */
		.modal-header{
		border-bottom: none;
		}
	
	.modal-content{
		padding: 3rem;
		}
	
	.form-control{
		height: 3rem;
		border-radius: 1rem
		}
	 
	.list-login{
		list-style: none;
		width: 100%;
		}
	
	.list-login > li, .list-area2{
		float: right;
		}

	.list-login a{
		color: #212529;		
		}
	
	.mb-3{
		margin-bottom: 1rem !important;
		}
	
	.btn-block{
		border-radius: 1rem;
		}
	
	.btn-primary{
		margin-bottom: 1rem;
		}
		
	.modal-footer{
		border-top: none;
		}
</style>

</head>
<body>
	<!-- 프로젝트의 시작주소(context root)를 얻어와 간단하게 사용할 수 있도록 별도의 변수를 생성 -->
	<!-- 해당 변수를 프로젝트 전체에서 사용할 수 있도록 scope=application 지정 -->
	<c:set var="contextPath" value="${pageContext.servletContext.contextPath}" 
		scope="application"></c:set>
		
	<!-- 
		로그인 실패 등의 서버로부터 전달받은 메시지를 경고창으로 출력하기
		
		1) 서버로부터 전달받은 메시지가 있는지 검사
		session에 text라는 게 있다면 -->
  <c:if test="${!empty sessionScope.swalTitle || !empty sessionScope.swalText}"> 
	     <!-- 임시로 swal 사용 안함 -->
		 	 <script>
		 		swal({	icon : "${swalIcon}", 
		 				title : "${swalTitle}", 
		 				text : "${swalText}"	});
		 	</script>
		 	
		 	<!-- 한번 출력한 메시지를 session에서 삭제 -->
			<c:remove var="text"/>
		 	<c:remove var="swalIcon"/>
		 	<c:remove var="swalTitle"/>
		 	<c:remove var="swalText"/>
    </c:if>

    
      
     
	<div class="container-fluid" >
	      <nav class="header navbar navbar-expand-lg navbar-light bg-light sticky-top">
	          <!--줄어들었을 때 버거 버튼--> 
	          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
	              <span class="navbar-toggler-icon"></span>
	          </button> 
	          <a class="navbar-brand header-left" href="${contextPath}">
	              <img src="${pageContext.request.contextPath}/css/yamung_logo_3.png" 
	              	alt="brand" id="header-brand"/>
	          </a>
	          <div class="collapse navbar-collapse header-left" id="bs-example-navbar-collapse-1">
	              <ul class="navbar-nav header-nav"> 
	             														 <!--active : 현재 활성화 버튼 → 현재 페이지 게시판<a>-->
	                  <li class="nav-item">  <!-- active 클래스를 주기 위해 현재 활성화된 게시판 타입(type)을 Controller에서 
	                  																								출력화면으로 요청 위임할 때 request에 저장하기  -->
	                          <a class="nav-link <c:if test="${param.tp == 'b1'}"> active </c:if>" 
	                          		id="notice" href="${contextPath}/notice/list.do?tp=b1">공지사항</a>
	                  </li>
	                  <li class="nav-item">
	                          <a class="nav-link <c:if test="${param.tp == 'b2'}"> active </c:if>" 
	                          		id="adoption" href="${contextPath}/adoption/list.do?tp=b2">입양/분양</a>
	                  </li>
	                  <li class="nav-item">
	                          <a class="nav-link <c:if test="${param.tp == 'b3'}"> active </c:if>" 
	                          		id="review" href="${contextPath}/review/list.do?tp=b3">입양 후기</a>
	                  </li>
	                  <li class="nav-item">
	                          <a class="nav-link <c:if test="${param.tp == 'b4'}"> active </c:if>" 
	                          		id="free" href="${contextPath}/free/list.do?tp=b4">자유게시판</a>
	                  </li>
	                  
	                  <c:if test="${!empty loginMember && loginMember.grade == '0'.charAt(0) }"> 
	                   <li class="nav-item"> <!-- 관리인 로그인시에만 보임 -->
	                           <a class="nav-link <c:if test="${param.tp == 'adminMem'}"> active </c:if>" 
	                           	id="admin-mem" href="${contextPath}/admin/adminMem.do?tp=adminMem">회원 관리</a>
	                   </li>
	                  </c:if>
	              </ul>
	          </div>
	          <div class="header-right" id="searchInput">
	              <form class="form-inline" action="${contextPath}/search/search.do" method="GET" onsubmit="return searchValidate();" >
	                  <input class="search-input" id="search-input" type="text" name="sv" /> <!-- searchValue -->
	                  <button class="btn search-btn" type="submit">
	                      <i class="fas fa-search search-icon"></i><!--찾기아이콘-->
	                  </button>
	              </form>
	          </div>
	          <div class="header-right" id="bs-example-navbar-collapse-3">                        
	               <c:choose>    
	                  <c:when test="${empty sessionScope.loginMember}"> 
	                  <!--로그인이 되어 있지 않을 때-->
	               <ul class="navbar-nav">
	                          <li class="nav-item">
	                              <a class="nav-link" data-toggle="modal" href="#modal-container-1">
	                              	<div class="icon-text" id="login">
	                                   <i class="fas fa-user-alt" id="user-icon"></i>
	                                  <span class="under-icon">로그인</span>
	                               	</div>
	                              </a><!-- user아이콘 -->
	                          </li>
	                      </ul>
	                 </c:when>
	                   <c:otherwise> <!--active : 현재 활성화 버튼 → 현재 페이지<i>-->  
	                  <!--로그인이 되어 있을 때-->
	                	 <ul class="navbar-nav">
	                          <li class="nav-item header-right-users">
	                             <a class="nav-link" data-toggle="modal" href="#modal-checkPwd">
	                               <div class="icon-text" id="mypage">
	                                   <i class="fas fa-user-alt 
	                                   			<c:if test="${param.tp == 'mypage'}"> active </c:if>"
	                                  			id="user-icon"></i><!--user아이콘-->
	                                   <span class="under-icon" id="nickname" <c:if test="${loginMember.grade == '0'.charAt(0)}">style="color:red"</c:if>>
	                                   	 	${loginMember.nickName}</span> 
	                                   <!--관리자 로그인 시 color 추가 / ${loginMember.memNm} -->
	                               </div>
	                             </a>
	                          </li>
	                          <li class="nav-item header-right-users">
	                              <a class="nav-link" id="cs-page" href="${contextPath}/information/list.do?tp=b5">
	                                  <i class="fas fa-question-circle <c:if test="${param.tp == 'b5'}"> active </c:if>" 
	                                  	id="cs-icon"></i><!--고객센터 아이콘-->
	                              </a>
	                          </li>
	                          <li class="nav-item header-right-users">
	                              <a class="nav-link" id="logout" href="${contextPath}/member/logout.do">
	                                  <i class="fas fa-sign-out-alt" id="logout-icon"></i> <!--로그아웃 아이콘-->
	                              </a>
	                          </li>
	                      </ul>
	               	</c:otherwise>
	              </c:choose>
	          </div>
	      </nav>
	  </div>
	
    <!--로그인 모달창------------------------------------------------------------->
    <%-- Login Modal --%>
	<div class="modal fade" id="modal-container-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
						
				<div class="modal-header">
				<h3>로그인</h3>
					<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
					</button>
				</div>

				<div class="modal-body">
					<form class="form-signin" method="POST" action="${contextPath}/member/login.do">
						<input type="text" class="form-control" id="memberId" name="memberId" placeholder="아이디" value="${cookie.saveId.value}">
						<br>
						<input type="password" class="form-control" id="memberPwd" name="memberPwd" placeholder="비밀번호">
						<br>
						<div class="checkbox mb-3">
							<span class="list-area1">
								<label> 
									<input type="checkbox" name="save" id="save"
										<c:if test="${!empty cookie.saveId.value}">
											checked
										</c:if>
									> 아이디 저장
								</label>							
							</span>
							<span class="list-area2">
								<ul class="list-login">
									<li>
										<a href="${contextPath}/member/pwdFind1.do">비밀번호 찾기</a>
									</li>
									<li>ㅣ</li>
									<li>
										<a href="${contextPath}/member/idFind1.do">아이디</a>									
									</li>
								</ul>							
							</span>
						</div>
						<button class="btn btn-lg btn-teal btn-block" type="submit">로그인</button>
						<a class="btn btn-lg btn-dark btn-block" href="${contextPath}/member/signUpForm.do">회원가입</a>
					</form>
				</div>
				
				<div class="modal-footer">
				</div>
			</div>
		</div>
	</div>
    
    <!--마이페이지 연결 전 비밀번호 찾기 모달창-----------------------------------------  -->
    <!-- 비밀번호 확인 모달창 --> 
	<div class="modal fade" id="modal-checkPwd" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<h4 class="modal-header-text">본인 인증을 위해 <br> 비밀번호를 입력하세요</h4>
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				
				<div class="modal-body">								<!-- 임시로 연결 내글 목록 만든 후에는 비번 확인 → 성공 시 mypage로 -->
					<form class="modal-checkPwd" method="POST" action="${contextPath}/member/myActiveList.do?tp=mypage">
						<div class="col-md-6 offset-md-3">
							<!-- 비밀번호 유효성 체크 영역 -->
							<span id="checkPwd">&nbsp;</span>
						</div>
						<input type="password" class="form-control" id="checkPwd" name="checkPwd" placeholder="비밀번호">
						<br>
						<div class="modal-body-text">
							<a href="#">비밀번호를 잊어버리셨나요?</a>
						</div>
						<br>
						<button class="btn btn-lg btn-teal btn-block" type="submit">확인</button>
					</form>
				</div>
			</div>
		</div>
	</div>
    
    
    <script>
    //ready함수
    $(function(){
		 
    });
		
/*-------------------------------------------------------------------------------*/

    	$(".navbar-toggler").on("click", function(){
    		$(".header-right").slideToggle(200);
    	});
    	
    	$(window).resize(function(){
    		if($(window).width() > 990) {
    			$(".header-right").removeAttr("style");
    		}
    	});
    	
 /*--------------------------------------------------------*/
 			function searchValidate() {
				if ($("#search-input").val().trim().length == 0) {
					swal({icon:"warning", title:"검색어를 입력해 주세요."});
					$("#search-input").focus();
					return false;
				}
			}
    
    </script>

</body>
</html>