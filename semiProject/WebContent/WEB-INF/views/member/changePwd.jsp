<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<!DOCTYPE html>
	<html>

	<head>
		<meta charset="UTF-8">
		<title>비밀번호 변경</title>

		<!-- Bootstrap core CSS -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

		<!-- Bootstrap core JS -->
		<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
			integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
			crossorigin="anonymous"></script>

		<!-- sweetalert : alert창을 꾸밀 수 있게 해주는 라이브러리 https://sweetalert.js.org/ -->
		<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

		<style>
		
			html,
      body {
          height: 100%;
      }

      body {
          margin: 0;
      }

      .wrapper{
          height: 100%;
          min-height: 100%;
          padding : 0;
          margin: 0;
      }

      .header{height: 15%;}
      .content-section{height: 75%; display: flex;}
      .footer{height: 10%;}


      div {
        /*   margin: auto; */
          box-sizing: border-box;
      }
			#content-main div {
				width: 100%;
				height: 100%;
				padding: 2px;
				margin: auto;
				box-sizing: border-box;
			} 

			#content-main {
				width: 60%;
				height: 50%;
				/* position: absolute; */
				top: 0;
				right: 0;
				left: 0;
				bottom: 0;
				margin: auto;
				margin-top: 50px;
				padding: 30px;
				background-color: #F6F6F6;
			}

			.container2 {
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
			
			
			.btn-size {
				width : 70% !important;
				/* margin-left : 50px; */
				margin:0 auto;
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
		
	<div class="wrapper container">
		<jsp:include page="../member/myPageMenu.jsp"></jsp:include>
		
		<div class="container-fluid content-section" id="content-main">

			<div class="row">
				<div class="col-md-12">
					<h4>비밀번호 변경</h4>
					<br>

					<div class="container container2">
						<form method="POST" action="updatePwd.do" onsubmit="return pwdValidate();"
							class="form-horizontal" role="form">

							<!-- 현재 비밀번호 -->
							<div class="row mb-1 form-row">
								<div class="col-md-4">
									<h6>현재 비밀번호:</h6>
								</div>
								<div class="col-md-6">
									<input type="password" class="form-control" id="currentPwd" name="currentPwd"
										placeholder="현재 비밀번호 입력">
								</div>
							</div>

							<!-- 새 비밀번호 -->
							<div class="row mb-1 form-row">
								<div class="col-md-4">
									<h6>새 비밀번호:</h6>
								</div>
								<div class="col-md-6">
									<input type="password" class="form-control" id="newPwd1" name="newPwd1"
										placeholder="새 비밀번호 입력">
								</div>
							</div>

							<!-- 새 비밀번호 확인 -->
							<div class="row mb-4 form-row">
								<div class="col-md-4">
									<h6>새 비밀번호 확인:</h6>
								</div>
								<div class="col-md-6">
									<input type="password" class="form-control" id="newPwd2" name="newPwd2"
										placeholder="새 비밀번호 확인">
								</div>
							</div>
							<br>

							<button type="submit" class="btn btn-teal btn-block btn-size">
								변경하기</button>
							<button type="button" class="btn btn-secondary btn-block btn-size" onclick="location.href='#'">
								돌아가기</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		
	</div>
		<jsp:include page="../common/footer.jsp"></jsp:include>

		<script src="${contextPath}/resources/js/semi_member.js"></script>

	</body>

	</html>