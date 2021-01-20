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

/* 입양 여부 완료 span 모양 */
.adopt-yn > span {
	display : inline-block;
	width : 60px;
	border-radius : 5px;
	text-align : center;
}	
	
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
	
	.form-adoption {
		padding-right: 20px;
		font-size : 22px;
		font-weight: bold;
		font-family: 'TmoneyRoundWindRegular';
	}
		
	.form-hr>hr {
		height: 8px;
		margin-bottom: .1rem;
		display: inline-block;
		background-color: teal;
		border-radius: 1rem;
	}
</style>
</head>
<body>

	<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}"/>
	
	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<c:set var="tpCpNoStr" value="tp=${param.tp}&cp=${param.cp}&no=${param.no}"/>

	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container  my-5">
		<div class="header-adoption">
			<span class="form-adoption">입양/분양</span> 
			<span class="form-hr" style="position:absolute; width:90%;"><hr style="width:1000px;"></span>
		</div>
	</div>
	
	<div class="container  my-5">

		<div>

			<div id="board-area">

				<h3>
				<span class="mt-4 cd-color inline-block category" id="code" style="color:teal;">${adoption.adtCode}</span>
				
				<span class="mt-4 inline-block">${adoption.adtBrdTitle}</span>
				</h3>
				<br>
				
				<p id="brd-second-area">

					<span class="inline-block brd-second" id="writer">닉네임 : ${adoption.nickName}</span>
					 					
					<!-- float하면 앞의 요소가 먼저 정렬되기 때문에 댓글 요소가 앞에 -->
					<span class="float-right brd-second">댓글 <%-- ${adoption.replyCount} --%></span>
					
					<!-- 조회 -->
			 		<span class="float-right brd-second">조회 ${adoption.readCount}</span>
					<br>

					<!-- Date -->
					<span class="brd-second inline-block">
						작성일 : <fmt:formatDate value="${adoption.adtBrdCrtDt}" pattern="yy-MM-dd HH:mm"/>
					</span>
				</p>

				<hr>

				<div id="brd-third-area">
					<table id="brd-detail">
						<tr>
							<td class="detailMenu">품종</td>
							<td class="detailContent">${adoption.adtBreed} (추정)</td>
							<td class="detailMenu">예방 접종</td>
							<td class="detailContent">${adoption.adtVaccination}</td>
						</tr>
						<tr>
							<td class="detailMenu">성별</td>
							<td class="adtGender">${adoption.adtGender}</td>
							<td class="detailMenu">특이사항</td>
							<td class="detailContent" rowspan="3">${adoption.adtNote}</td>
						</tr>
						<tr>
							<td class="detailMenu">나이</td>
							<td class="detailContent">${adoption.adtAge} (추정)</td>
							<td></td>
						</tr>
						<tr>
							<td class="detailMenu">주소</td>
							<td class="detailContent">${adoption.address}</td>
							<td></td>
						</tr>
						<tr>
							<td class="detailMenu">공고 기간</td>
							<td class="detailContent">${adoption.adtTime}</td>
							<td class="detailMenu">입양 여부</td>
							<td class="detailContent adopt-yn"><span>${adoption.adtYn}</span></td> 
													  
						</tr>
					
					</table>
				</div>
				<hr>
 
				<!-- 썸머노트를 사용하면 content에 img파일에 <img>태그로 연결되기 때문에 
						별도의 이미지 영역 필요 없음 -->
				<div id="board-content">
					${adoption.adtBrdContent}
				</div>
				
				<hr>
				 
				<!-- 목록으로/수정/삭제/블라인드 버튼 -->				
				<div>
					<%-- 로그인된 회원이 관리자인 경우 --%>
					<c:if test="${!empty loginMember && loginMember.grade == '0'.charAt(0)}">
						<button id="blindBtn" class="btn btn-danger float-right ml-1 mr-1">블라인드</button>
					</c:if>
					
					<%-- 로그인된 회원과 해당 글 작성자가 같은 경우--%>
					<c:if test="${!empty loginMember && (adoption.nickName == loginMember.nickName)}">
						<button id="deleteBtn" class="btn btn-secondary float-right" style="width: 75px;">삭제</button> 
						<a href="${contextPath}/adoption/updateForm.do?${tpCpNoStr}${searchStr}" 
							 class="btn btn-secondary float-right ml-1 mr-1" style="width: 75px;">수정</a>
					</c:if>
					
					
					<%-- 파라미터에 sk,sv가 존재한다면 == 이전 목록이 검색 게시글 목록인 경우 --%>
					<c:choose>
						<%-- 이전 목록이 전체 검색일 때 파라미터에 sl 있음 --%>
						<c:when test="${!empty param.from && param.from == 's'}">
							<c:url var="goToList" value="/search/search.do">
								<c:param name="cp">${param.cp}</c:param>
								<c:param name="sk">${param.sk}</c:param>
								<c:param name="sv">${param.sv}</c:param>
								<c:param name="tp">${param.tp}</c:param>
							</c:url>
						</c:when> 
						<c:when test="${empty param.from && !empty param.sk && !empty param.sv }">
							<%-- search.do는 board/view.do에서 마지막 주소만 바뀌면 되는 것이 아니라
								board 위치에서 바뀌어야 됨
								ex) wsp/board/search.do(X), wsp/search.do(O)
								→ ../search.do를 사용하면 한단계 상위 위치에서(board) 주소를 search.do로 바꿈 --%>
							 <c:url var="goToList" value="/search/adoptionSearch.do">
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
									<c:param name="cp">${param.cp}</c:param>
								</c:if>
								<c:param name="cd">${param.cd}</c:param>
								<c:param name="tp">${param.tp}</c:param>
							</c:url>
						</c:otherwise>
					</c:choose>
					
					<a href="${goToList}" class="btn btn-teal float-right" style="width: 75px;">목록</a>
					<button type="button" class="btn btn-dark float-right ml-1 mr-1"
                    	onclick="location.href='${header.referer}'">뒤로가기</button>
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
							<td class="replyNo sr-only"><%-- ${reply.replyNo} --%></td> 
							<td class="rWriter" >댓글을써요써요써요요</td> <%-- ${reply.replyWriter} --%>
							<td class="rContent"><%-- ${reply.replyCotent} --%>
								입양 원합니다.
							</td>
							<td class="rDate">21.01.08. 11:46:28
								<%-- <fmt:formatDate value="${reply.commDate}" pattern="yy-MM-dd HH:mm:ss"/> --%>
							</td>
							<td class="deleteReply">
<%-- 								<c:if test="${!empty loginMember && loginMember.nNm == reply.replyWriter }"> --%>
									<a href="${contentPath}/reply/delete.do?${tpCpNoStr}&cp=${param.cp}&no=${param.no}">
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

			// 글 목록 카테고리 출력 문자를 변화하는 즉시 실행 함수
			(function(){
				$(".category").each(function(index, item){
					if($(item).text() == "adtDog"){
						$(item).text("입양 개");
					} else if($(item).text() == "adtCat"){
						$(item).text("입양 고양이");
					} else if($(item).text() == "adtEtc"){
						$(item).text("입양 기타");
					} else if($(item).text() == "temp"){
						$(item).text("임시 보호");
					}
				});
			})();
			
			// 글 목록의 성별 출력 문자를 변환하는 즉시 실행 함수
			(function(){
				$(".adtGender").each(function(index, item){
					if($(item).text() == "boy"){
						$(item).text("수컷");
					} else if($(item).text() == "girl"){
						$(item).text("암컷");
					} else if($(item).text() == "ntrBoy"){
						$(item).text("수컷(중성화)");
					} else if($(item).text() == "ntrGirl"){
						$(item).text("암컷(중성화)");
					}
				});
			})();
			
			// 글 목록의 입양 진행 여부 글+색상 정하는 즉시 실행 함수
			(function(){
				$(".adopt-yn span").each(function(index, item){
					if($(item).text() == "Y" ){
						$(item).text("진행 중");
						$(item).css({"background-color":"tomato",  "color":"white"});
					} else if($(item).text() == "N"){
						$(item).text("완료");
						$(item).css({"background-color":"yellowgreen",  "color":"white"});
					}
				});
			})();
			
			//삭제 버튼 클릭
			$("#deleteBtn").on("click", function(){
				if(confirm("정말 삭제하시겠습니까?")) {
					location.href = "${contextPath}/adoption/delete.do?${tpCpNoStr}";
				}
			});
			
			//블라인드 클릭
			$("#blindBtn").on("click", function(){
				if(confirm("해당 글을 블라인드하시겠습니까?")){
					location.href="${contextPath}/admin/blindBrd.do?${tpCpNoStr}";
				}
			});
			
		});
	</script>
</body>
</html>
