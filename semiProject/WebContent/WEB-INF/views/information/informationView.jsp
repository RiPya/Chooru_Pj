<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객센터 제목</title>

<style>

/* 고객센터 라인 */
.header-info{
   width: 100%;
   margin-bottom: 2rem;
}

.form-info {
   font-size: 20px;
   font-weight: bolder;
   padding-right: 20px;
		font-family: 'TmoneyRoundWindRegular';   
} 

.form-hr > hr {
   width: 85%;
   height: 8px;
   margin-bottom: .1rem;
   display: inline-block;
   background-color: teal;
   border-radius: 1rem;
}

/* board area */
	#board-area{ 
		min-height: 700px; /*반응형*/
		margin-bottom : 100px; 
	}
	#board-content{ 
		padding-top : 50px;
		padding-left : 30px;
		padding-right : 30px;
		padding-bottom:150px;
	}

	/* 카테고리, 제목, 닉네임, 작성일 */	
	.inline-block { display : inline-block;}
	
	#brd-second-area{
		min-height : 50px;
		padding-left : 20px;
		padding-right : 20px;
	}
	
	/* 닉네임, 작성일, 조회, 댓글 */
	.brd-second {
		margin-left : 10px;
		margin-right : 10px;
	}
	
	/* 카테고리 */
	#code {
		min-width : 180px;
		text-align : center;
	}

/*  썸머노트에서 필요함?
	.boardImg{
		width : 100%;
		height: 100%;
		
		max-width : 1280px;
		max-height: 1280px;
		
		margin : auto;
	}
*/	
	
	
/* 댓글 */	
/* 	.replyWrite > table{
		width: 90%;
		align: center;
	} */
	
	#replyContentArea{ 
		width: 100%; 
		min-height : 500px;
	}
	
	#reply-write-area {
		width : 100%;
		height : 240px;
		text-align : center;
		padding : 20px;
		background-color : #F2F2F2;
	}
	
	#replyContentArea textarea{
    resize: none;
   	width: 100%;
   	height: 150px;
   	border : 1px solid #EAEAEA;
   	border-radius : 5px;
	}
	
	.rCount {
		line-height : 50px;
		margin-right : 10px;
		font-size : 14px;
		color : gray;
	}
	.replyBtnArea{
    width: 75px;
    text-align: center;
    margin-top : 5px;
	}
	
	#reply-list {
		width : 100%;
		margin-top : 20px;
	}
	#reply-list td{
		height : 80px;
		border-top : 1px solid #EAEAEA;
		border-bottom : 1px solid #EAEAEA;
		padding : 10px;
	}
	#reply-list .rWriter{ 
		width : 20%;
		font-size : 14px;
	}
	#reply-list .rContent{
		font-size : 14px;
	}
	#reply-list .rDate{
		width : 15%;
		font-size: 0.8em;
		color : gray;
		text-align : right;
	}
	#reply-list .deleteReply {
		width : 10px;
	}
	#reply-list .deleteReply > a {
		color : rgb(51,51,51);
	}
	
	#replyListArea{
		list-style-type: none;
	}
	
	/* 삭제 버튼 간격 조정 */
	#deleteBtn{
		margin-left: .25rem!important;
	}

	
</style>
</head>
<body>

			<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}"/>
	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<c:set var="tpCpNoStr" value="tp=${param.tp}&cp=${param.cp}&no=${param.no}"></c:set>

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
		<div class="header-info">
			<span class="form-info">고객 센터</span>
			<span class="form-hr" style="position:absolute; width:90%;"><hr style="width:1000px;"></span>
		</div>
	<div>
			<div id="board-area">

				<h3>
				<span class="mt-4 cd-color inline-block" id="code">${info.code}</span>
				
				<span class="mt-4 inline-block">${info.brdTitle}</span>
				</h3>
				<br>
				
				<p id="brd-second-area">
					<span class="inline-block brd-second" id="writer">${info.nickName}</span>
					
					<!-- Date -->
					<span class="brd-second inline-block">
						작성일 <fmt:formatDate value="${info.brdCrtDt}" pattern="yyyy년 MM월 dd일 HH:mm"/>
					</span>
					
					<!-- float하면 앞의 요소가 먼저 정렬되기 때문에 댓글 요소가 앞에 -->
					<span class="float-right brd-second">댓글 추가</span>
					
			 		<span class="float-right brd-second">조회 ${info.readCount}</span>
				</p>

				<hr>

				<!-- 썸머노트를 사용하면 content에 img파일에 <img>태그로 연결되기 때문에 
						별도의 이미지 영역 필요 없음 -->
				<div id="board-content">
					${info.brdContent}
				</div>
				
				<hr>
				 
				<!-- 목록으로/수정/삭제/블라인드 버튼 -->				
				<div>
					<%-- 로그인된 회원이 관리자인 경우 --%>
				<%-- 	<c:if test="${!empty loginMember && loginMember.grade == '0'.charAt(0)}">
						<button id="deleteBtn" class="btn btn-secondary float-right" style="width: 75px;">블라인드</button>
					</c:if> --%>
					<%-- 로그인된 회원과 해당 글 작성자가 같은 경우--%>
					<c:if test="${!empty loginMember && (info.nickName == loginMember.nickName) || loginMember.grade == '0'.charAt(0)}">
						<button id="deleteBtn" class="btn btn-secondary float-right" style="width: 75px;">삭제</button> 
					</c:if>
					
					
					<%-- 파라미터에 sk,sv가 존재한다면 == 이전 목록이 검색 게시글 목록인 경우 --%>
					 <c:choose> 
					 <%-- 마이페이지(내글 목록)에서 왔을 때 --%>
					 	<c:when test="${!empty param.from && param.from == 'myL'}">
					 		<c:url var="goToList" value="/member/myActiveList.do">
					 			<c:param name="cp">${param.cp}</c:param>
					 			<c:param name="tp">${param.tp}</c:param>
					 			<c:param name="my">${param.my}</c:param>
					 		</c:url>
					 	</c:when>
					 <%-- 마이페이지(내 댓글 목록)에서 왔을 때 --%>
					 	<c:when test="${!empty param.from && param.from == 'myR'}">
					 		<c:url var="goToList" value="/member/myActiveReply.do">
					 			<c:param name="cp">${param.cp}</c:param>
					 			<c:param name="tp">${param.tp}</c:param>
					 			<c:param name="my">${param.my}</c:param>
					 		</c:url>
					 	</c:when>
						<%-- 이전 목록이 전체 검색일 때 파라미터에 sl 있음 --%>
						<c:when test="${!empty param.from && param.from == 's'}">
							<c:url var="goToList" value="/search/search.do">
								<c:param name="cp">${param.cp}</c:param>
								<c:param name="sk">${param.sk}</c:param>
								<c:param name="sv">${param.sv}</c:param>
								<c:param name="tp">${param.tp}</c:param>
							</c:url>
						</c:when>
						<c:when test="${empty param.from && !empty param.sv && !empty param.sk}">
							<%-- search.do는 board/view.do에서 마지막 주소만 바뀌면 되는 것이 아니라
								board 위치에서 바뀌어야 됨
								ex) wsp/board/search.do(X), wsp/search.do(O)
								→ ../search.do를 사용하면 한단계 상위 위치에서(board) 주소를 search.do로 바꿈 --%>
							 <c:url var="goToList" value="/search/infoSearch.do">
								<c:param name="cp">${param.cp}</c:param>
								<c:param name="sk">${param.sk}</c:param>
								<c:param name="sv">${param.sv}</c:param>
								<c:if test="${!empty param.cd}">
									<c:param name="cd">${param.cd}</c:param>
								</c:if>
								<c:param name="tp">${param.tp}</c:param>
							</c:url>
					 	</c:when> 
						
						<%-- 이전 목록이 일반 게시글 목록일 때 --%>
						<%-- c:url를 통해 목록으로 돌아가는 주소를 만들고 그 안에 파라미터 cp(현재페이지)에 지정하면 
									목록으로 돌아갈 때 cp가 같이 전달됨 --%>
						<c:otherwise>
							<c:url var="goToList" value="list.do">
								<c:if test="${!empty param.cd}">
									<c:param name="cd">${param.cd}</c:param>
								</c:if>
								<c:param name="cp">${param.cp}</c:param>
								<c:param name="tp">${param.tp}</c:param>
							</c:url>
						</c:otherwise>
					</c:choose> 
					
					<a href="${goToList}" class="btn btn-teal float-right" style="width: 75px;">목록</a>
				</div>
			</div>
		</div>

 	<%-- Reply --%>
	<script>
		var brdNo = ${info.brdNo};
	</script>
	
	<c:if test="${!empty loginMember && loginMember.grade == '0'.charAt(0)}">
		<jsp:include page="../common/reply.jsp"></jsp:include>
	</c:if>
	
	<%-- footer --%>
	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	
	<script>
	$(document).ready(function(){
		// 삭제 버튼 클릭
		$("#deleteBtn").on("click", function(){
			if(confirm("정말 삭제하시겠습니까?")) {
				location.href = "${contextPath}/information/delete.do?${tpCpNoStr}";
			}
		});
	
		//게시글의 카테고리 색상 정하는 즉시 실행 함수: 해당 td의 클래스 ="cd-color 
		(function(){	
			$(".cd-color").each(function(index, item){
				if($(item).text() == "문의" ){
					$(item).css("color", "green");
				} else if($(item).text() == "신고"){
					$(item).css("color", "blue");
				}
			});
		})();
	});
	</script>
</body>
</html>
