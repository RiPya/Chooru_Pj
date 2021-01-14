<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입양 후기</title>

<style>


/*------목록 스타일---------*/
#reviewList {
	width : 100%;
	min-height : 900px;
	padding : 5px;
	text-align : center;
}

#noneList {
	width:100%; 
	height: 300px; 
	font-size:20px;
	line-height: 260px;
	border-top: 1px solid #EAEAEA;
	border-bottom: 1px solid #EAEAEA;
}

#reviewList .list-card {
	display : inline-block !important;
	width: 30%; 
	height: 30%;
	min-width : 200px;
	margin : 10px;
}

#reviewList > .list-card > .card-image { 
	width : 100%;
	height : 250px;
/* 	margin-bottom : 10px; */
/* 	background-position : center;
	background-size : cover;
	overflow : hidden; */
}

#reviewList .card-img-top {
/* 	width : 100%; */
	height: 100%;
}

.card-body > p { margin:0; }

.card-body > .reviewTitle{
	color : teal;
	font-weight : bold;
	text-align : center;
	font-size : 18px;
	height : 60px;
}
.card-body > .reviewWriter { 
	font-size : 14px; 
	color : rgb(51,51,51); 
	text-align : right;
}


.list-card:hover{
	cursor : pointer;
}



/* 페이징바 */
.pagination {
	justify-content: center;
}
.pagination > li > a {
	background-color : none;
	color : rgb(51,51,51);
	font-size : 12px;
	border : none;
	border-radius : 25%;
	min-width : 35px;
	height : 35px;
	text-align : center;
	line-height : 35px;
	padding : 0;
}

.pagination .page-link:hover{ color : teal !important;}

/*페이징바 클릭 시 추가해서 사용할 클래스
(다른 것을 클릭하면 제거 : script에서 사용)
 첫 페이지에서는 '전체'에 적용*/
.pagination > li > .pag-active {
	color: teal !important;
	font-weight : bold !important;
}

#searchForm {
	position: relative;
}

#searchForm > * {
	top : 0;
	vertical-align:middle; /*인라인 요소 위로 정렬*/
}

/* #searchForm > .sf-margin{
} */

#searchForm > button {
	padding : 0 !important;
}

/* 검색창 검색 아이콘 */
#searchForm > button > #search-in-icon {
	color : white;
	font-size : 28px;
}

/*--------------------------*/
/* 입양 후기 라인 */
.header-review{
	width: 100%;
	padding-top : 8px;
	margin-bottom: 2rem;
}
.form-review {
   font-size: 20px;
   font-weight: bolder;
   padding-right: 20px;
} 
.form-hr > hr{
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
<%-- 	<c:if test="${!empty param.cd}">
		<c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}"/>
	</c:if> --%>

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
				<div class="header-review">
					<span class="form-review">입양 후기</span>
					<span class="form-hr" style="position:absolute; width:90%;"><hr style="width:1000px;"></span>
				</div>
			
			<div id="reviewList">
<%-- 			<c:choose>
				<c:when test="${empty reviewList}">  
					<p id="noneList">존재하는 게시글이 없습니다.</p>
				</c:when>
				<c:otherwise> <!-- 게시글이 있을 때 모두 출력--> --%>
					<c:forEach var="i" begin="0" end="2">
		<%-- 			<c:forEach var="review" items="${reviewList}"> --%>
						<div class="card list-card">
						  <div class="card-image"> <!-- c:forEach 사용해서 해당 게시글 번호에 맞는 첫번째 이미지 붙여넣기 -->
						 		<img src="https://i2.pickpik.com/photos/714/11/745/golden-retriever-animal-shelter-dog-pension-kennels-preview.jpg" 
						  		class="card-img-top" alt="${review.reviewTitle}}">
						  </div>
						  <div class="card-body">
						    <p class="card-title reviewTitle">인절미와 함께 제목 긴 버전 체크합니다아 제목 두줄 스타일 보세요</p> <!-- ${review.reviewTitle}} -->
						    <p class="card-text reviewWriter">인절미누나</p><!-- 품종 : ${review.reviewWriter}} -->
						    <p class="review-no sr-only">123</p><!-- sr-only : 안보이게하는 클래스 --> <!-- ${review.reviewBrdNo}} -->
						  </div>
						</div>
						<div class="card list-card">
						  <div class="card-image"> <!-- c:forEach 사용해서 해당 게시글 번호에 맞는 첫번째 이미지 붙여넣기 -->
						 		<img src="https://cdn.pixabay.com/photo/2014/10/01/10/46/cat-468232_960_720.jpg" 
						  		class="card-img-top" alt="${review.reviewTitle}}">
						  </div>
						  <div class="card-body">
						    <p class="card-title reviewTitle">냥냥냥 행복하자</p> <!-- ${review.reviewTitle}} -->
						    <p class="card-text reviewWriter">무릎냥</p><!-- 품종 : ${review.reviewWriter}} -->
						    <p class="review-no sr-only">123</p><!-- sr-only : 안보이게하는 클래스 --> <!-- ${review.reviewBrdNo}} -->
						  </div>
						</div>
						<div class="card list-card">
						  <div class="card-image"> <!-- c:forEach 사용해서 해당 게시글 번호에 맞는 첫번째 이미지 붙여넣기 -->
						 		<img src="https://cdn.pixabay.com/photo/2020/02/29/18/59/rabbit-4890861_960_720.jpg" 
						  		class="card-img-top" alt="${review.reviewTitle}}">
						  </div>
						  <div class="card-body">
						    <p class="card-title reviewTitle">토끼토끼토끼</p> <!-- ${review.reviewTitle}} -->
						    <p class="card-text reviewWriter">토끼 궁뎅이</p><!-- 품종 : ${review.reviewWriter}} -->
						    <p class="review-no sr-only">123</p><!-- sr-only : 안보이게하는 클래스 --> <!-- ${review.reviewBrdNo}} -->
						  </div>
						</div>
					</c:forEach> 
<%-- 		</c:otherwise> 
			</c:choose> --%>
			
			
			
			</div><!-- 카드형 목록 리스트 div 끝 -->
			
						<%-- 로그인이 되어있는 경우 --%>
	<%--c:if test="${!empty loginMember}" --%>
				<button type="button" class="btn btn-teal float-right" id="insertBtn" 
							style="margin-top: 10px;"
							onclick="location.href='${contextPath}/review/insertForm.do?${tpStr}'">글쓰기</button>
	<%--/c:if --%>
			
			
			<%---------------------- Pagination ----------------------%>
			<%-- boardList.jsp에서 복붙한 거 놔두면 오류나서 메모장 파일에 옮겨놓음 --%>
			<!-- cd가 없다면 href의 url 뒤에 -->
			
			
			<!-- 임시 확인용 Pagination -->
			<div class="my-5">
				<ul class="pagination">
						<li> <!-- 첫 페이지로 이동(<<) -->
							<a class="page-link" href="#">&lt;&lt;</a>
						</li>
						<li> <!-- 이전 페이지로 이동(<) -->
							<a class="page-link" href="#">&lt;</a>
						</li>
						
						<c:forEach var='i' begin="1" end="10">
							<li>
								<a class="page-link <c:if test="${param.cp==i}">pag-active</c:if>" 
									href="#">${i}</a>
							</li>
						</c:forEach>
						
						<li> <!-- 이전 페이지로 이동(>>) -->
							<a class="page-link" href="#">&gt;</a>
						</li>
						<li> <!-- 마지막 페이지로 이동(>>) -->
							<a class="page-link" href="#}">&gt;&gt;</a>
						</li>
				</ul>
			</div>
						
		
				<!-- 검색창 : type:게시판코드(자유는 b4), cd:자유카테고리(검색창에서 설정)-->
				<!-- 게시판코드를 파라미터로 넘겨야 할까? -->
			<div class="my-5">
				<form action="${contextPath}/reviewSearch.do?${tpStr}" method="GET" class="text-center " 
																																id="searchForm">

					<select name="sk" class="form-control sf-margin" style="width: 110px; display: inline-block;">
						<option value="title">제목</option>
						<option value="titcont">제목+내용</option>
						<option value="writer">글쓴이</option>
					</select>
					
					<input type="text" name="sv" class="form-control sf-margin" 
							placeholder="검색어를 입력하세요." style="width: 25%; display: inline-block;">
							
					<button class="form-control btn btn-teal" style="width: 70px; display: inline-block;">
						<i class="fas fa-search" id="search-in-icon"></i><!--찾기아이콘-->
					</button>

				</form>
			</div>
			
			
	</div>
	
		<!-- footer -->
	<jsp:include page="../common/footer.jsp"></jsp:include>

	<script>
		$(document).ready(function(){
			
			//페이징바 활성화(현재 페이지 부분 색바꾸기) 진행?
			
					
			//입양/분양 카드가 클릭되었을 때
			$(".list-card").on("click", function(){
				//글번호를 얻어와 해당 주소로 전달
				var reviewNo = $(this).children().children(".review-no").text();
				

				var url = "${contextPath}/review/view.do?${tpStr}&cp=1&no="  + reviewNo;
										//tdCdStr == tp=_&cd=_ / cp 추가하기 ${param.cp}
				location.href = url;
			});		
					
		
		});
	

	
	
	</script>





</body>
</html>