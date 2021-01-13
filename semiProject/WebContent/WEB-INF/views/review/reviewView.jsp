<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입양/분양 게시글 제목</title>
<style>
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
		min-height : 500px;
	}

	/* 제목, 닉네임, 작성일 */	
	.inline-block { display : inline-block;}
	
	.brd-title { 
		margin-left : 30px;
	}
	
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
	

/* 상세 내용 */
	#brd-third-area{
		width : 100%;
		padding : 20px;
	}
	#brd-detail {
		width : 90%;
		height : 100%;
		margin : 0 auto;
	}
	#brd-detail td {
		padding-left : 10px;
		padding-right : 10px;
		padding-bottom : 5px;
	}
	#brd-detail .detailMenu {
		width : 10%;
		font-weight: bold;
	}
	#brd-detail .detailContent { 
		width : 40% !important;
	}

	.adoptUrl { color : rgb(51,51,51);}
	.adoptUrl:hover { color : teal; }



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
	
	

	
</style>
</head>
<body>

		<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}"/>


	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container  my-5">

		<div>

			<div id="board-area">

				<h3>
				<!--Title ${review.reviewTitle} class에 inline-block이 있어야 다른 게시글과 위치가 맞음-->
				<span class="mt-4 brd-title inline-block">인절미와 함께 제목 긴 버전 체크합니다아 제목 두줄 스타일 보세요</span>
				</h3>
				<br>
				
				<p id="brd-second-area">
				<!-- Writer ${review.nickName}  --> 
					<span class="inline-block brd-second" id="writer">인절미누나</span>
					
					<!-- Date -->
					<span class="brd-second inline-block">
						2021-01-06 23:41:75
						
						<%-- <fmt:formatDate value="${review.brdCrtDt}" 
														pattern="yyyy년 MM월 dd일 HH:mm:ss"/> --%>
						<!-- 수정일을 굳이 상세 조회 때 출력해야하나? -->
						<%--<br>
						 마지막 수정일 : <fmt:formatDate value="${review.brdModify}" 
																									pattern="yyyy년 MM월 dd일 HH:mm:ss"/> --%>
					</span>
					
					<!-- float하면 앞의 요소가 먼저 정렬되기 때문에 댓글 요소가 앞에 -->
					<!-- 댓글 ${review.replyCount} -->
					<span class="float-right brd-second">댓글 2</span>
					
					<!-- 조회 ${review.readCount}-->
			 		<span class="float-right brd-second">조회 202</span>
				</p>

				<hr>

				
				<div id="brd-third-area">
					<table id="brd-detail">
						<tr>
							<td class="detailMenu">입양 날짜</td>
							<td class="detailContent">2020.10.20.</td> <!-- ${review.adoptDate} -->
						</tr>
						<tr>
							<td class="detailMenu">입양/분양 url</td>
							<td class="detailContent">
								<a class="adoptUrl" target="_blank">https://mungnyang.com/123256243</a>
							</td>
							<!-- ${review.adoptLink} script에서 해당 주소가 a href 속성 추가로 + 클릭 시 새창 열기 (즉시함수) -->
						</tr>
					</table>
				</div>
				<hr>
 
				<!-- 썸머노트를 사용하면 content에 img파일에 <img>태그로 연결되기 때문에 
						별도의 이미지 영역 필요 없음 -->
				<!-- Content ${free.freeContent} -->
				<div id="board-content">
					
					<img src="https://i2.pickpik.com/photos/714/11/745/golden-retriever-animal-shelter-dog-pension-kennels-preview.jpg" 
						width="60%"/>
					<p>이제 우리랑 행복하게 살자~~~~~</p> <!-- ${review.reviewContent} -->
					
				</div>
				

				<hr>
				 
				<!-- 목록으로/수정/삭제/블라인드 버튼 -->				
				<div>
					<%-- 로그인된 회원이 관리자인 경우 --%>
					<%-- <c:if test="${!empty loginMember && (loginMember.grade == 0)}"> --%>
						<a href="#블라인드처리.do?${tpStr}" class="btn btn-danger float-right ml-1 mr-1">블라인드</a>
					<%-- </c:if> --%>
					<%-- 로그인된 회원과 해당 글 작성자가 같은 경우--%>
					<%-- <c:if test="${!empty loginMember && (board.memberId == loginMember.memberId)}"> --%>
						<button id="deleteBtn" class="btn btn-secondary float-right" style="width: 75px;">삭제</button> 
						<a href="#" class="btn btn-secondary float-right ml-1 mr-1" style="width: 75px;">수정</a>
					<%-- </c:if> --%>
					
					
					<%-- 파라미터에 sk,sv가 존재한다면 == 이전 목록이 검색 게시글 목록인 경우 --%>
					<%-- <c:choose> 
						<c:when test="${!empty param.sk && !empty param.sv }"> --%>
							<%-- search.do는 board/view.do에서 마지막 주소만 바뀌면 되는 것이 아니라
								board 위치에서 바뀌어야 됨
								ex) wsp/board/search.do(X), wsp/search.do(O)
								→ ../search.do를 사용하면 한단계 상위 위치에서(board) 주소를 search.do로 바꿈 --%>
							<%-- <c:url var="goToList" value="../search.do">
								<c:param name="cp">${param.cp}</c:param>
								<c:param name="sk">${param.sk}</c:param>
								<c:param name="sv">${param.sv}</c:param>
								<c:param name="cd">${param.cd}</c:param>
								<c:param name="tp">${param.tp}</c:param>
							</c:url> --%>
						<%-- </c:when> --%>
						
						<%-- 이전 목록이 일반 게시글 목록일 때 --%>
						<%-- c:url를 통해 목록으로 돌아가는 주소를 만들고 그 안에 파라미터 cp(현재페이지)에 지정하면 
									목록으로 돌아갈 때 cp가 같이 전달됨 --%>
<%-- 						<c:otherwise>--%>
							<c:url var="goToList" value="list.do">
								<c:param name="cp">${param.cp}</c:param>
								<c:param name="cd">${param.cd}</c:param>
								<c:param name="tp">${param.tp}</c:param>
							</c:url>
						<%--</c:otherwise>
					</c:choose> --%>
					
					<!-- 전체 검색 후 view로 연결했을 때 목록 돌아가기는 전체 검색 목록으로..? 그냥 이전으로 처리? -->
					
					<a href="${goToList}" class="btn btn-teal float-right" style="width: 75px;">목록</a>
				</div>
			</div>


		</div>
		
			<!-- 댓글 영역 -->
		<div id="replyContentArea">
			<div id="reply-write-area">
				<form action="#" method="post" onsubmit="">
					<textarea id="reply-textarea" name="reply">
					</textarea>
					<button class="btn btn-teal float-right replyBtnArea" type="submit">등록</button>
					<span class="float-right rCount">0/600</span>
				</form>
			</div>
			
<%-- 			<c:if test="${!empty replyList}"> --%>
			<table id="reply-list">
				
<%-- 					<c:forEach var="reply" items="${replyList}" > --%>
				<c:forEach var="r" begin="0" end="1">
						<tr>
							<td class="replyNo sr-only">${reply.replyNo}</td> 
							<td class="rWriter" >댓글을써요써요써요요</td> <%-- ${reply.replyWriter} --%>
							<td class="rContent"><%-- ${reply.replyCotent} --%>
								너무 귀여워요ㅠㅠㅠㅠㅠ.
							</td>
							<td class="rDate">21.01.08. 11:46:28
								<%-- <fmt:formatDate value="${reply.commDate}" pattern="yy-MM-dd HH:mm:ss"/> --%>
							</td>
							<td class="deleteReply">
<%-- 								<c:if test="${!empty loginMember && loginMember.nNm == reply.replyWriter }"> --%>
									<a href="${contentPath}/reply/delete.do?${tpStr}&cp=${param.cp}&no=${param.no}">
										<i class="fas fa-times"></i></a>
<%-- 								</c:if> --%>
							</td>
						</tr>
					</c:forEach>
				
				</table>
<%-- 			</c:if> --%>
		
		</div>



	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	
	<script>
		$(function(){
			
			(function(){
				var url = $(".adoptUrl").text();
				$(".adoptUrl").attr("href", url);
			})();
			
			
			
			
		});
	
	
	
	</script>
</body>
</html>
