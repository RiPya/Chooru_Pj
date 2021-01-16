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
/* 	text-align : center; 한 페이지에 게시글이 9개 미만일 때 하면 이상해짐*/
}

#noneList {
	width:100%; 
	height: 300px; 
	font-size:20px;
	line-height: 260px;
	border-top: 1px solid #EAEAEA;
	border-bottom: 1px solid #EAEAEA;
	text-align: center; /*추가*/
}

#reviewList .list-card:nth-of-type(3n+1){ margin-left:30px;}
/* 한 페이지에 게시글이 9개 미만일 때 리스트가 가운데에 있는 것처럼 하기 위해
 맨 왼쪽에 있는 list-card에 추가 margin 주기 */

#reviewList .list-card {
	display : inline-block !important;
	width: 30%; 
	height: 30%;
	min-width : 200px;
	margin : 10px;
}

#reviewList > .list-card > .card-image { 
	position: relative;
	width : 100%;
	height : 250px;
	overflow : hidden; /* img 가운데 오게 하기 위해 필요함 */
}

#reviewList .card-image > .card-img-top { 
	/*img 태그: 사진 가운데에 오게 하기! 세로는 div 크기 만큼만 보이게 하기*/
	position: absolute;
	min-width : 115%;
	top: 50%;
	left: 50%;
	transform : translate(-50%, -50%);
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
			<c:choose>
				<c:when test="${empty rList}">  
					<p id="noneList">존재하는 게시글이 없습니다.</p>
				</c:when>
				
				<%-- 게시글이 있을 때 모두 출력--%>
				<c:otherwise>
					<c:forEach var="review" items="${rList}">
					
						<div class="card list-card">
						  <div class="card-image"> 
						  <%-- c:forEach 사용해서 해당 게시글 번호에 맞는 첫번째 이미지 붙여넣기 --%>
						  	<c:forEach var="thumbnail" items="${iList}">
							 		<c:if test="${review.brdNo == thumbnail.brdNo}">
							 			<img src="${thumbnail.filePath}${thumbnail.fileName}"
							 				 class="card-img-top" alt="${review.brdTitle}">
							 		</c:if>
						 		</c:forEach>
						  </div>
						  <div class="card-body">
						    <p class="card-title reviewTitle">${review.brdTitle}</p>
						    <p class="card-text reviewWriter">${review.nickName}</p>
						    <p class="review-no sr-only">${review.brdNo}</p><!-- sr-only : 안보이게하는 클래스 -->
						  </div>
						</div>
						
					</c:forEach>
				</c:otherwise> 
			</c:choose>
			
			
			</div><!-- 카드형 목록 리스트 div 끝 -->
			
						<%-- 로그인이 되어있는 경우 --%>
			<c:if test="${!empty loginMember}">
				<button type="button" class="btn btn-teal float-right" id="insertBtn" 
							style="margin-top: 10px;"
							onclick="location.href='${contextPath}/review/insertForm.do?${tpStr}'">글쓰기</button>
			</c:if>
			
			
			<%---------------------- Pagination ----------------------%>
			<%-- boardList.jsp에서 복붙한 거 놔두면 오류나서 메모장 파일에 옮겨놓음 --%>
			<!-- cd가 없다면 href의 url 뒤에 -->
			
			<%-- 파라미터의 sk(searchKey)와 sv(searchValue)가 비어있지 않을 때
					 == 검색 후 페이징바 클릭 --%>
			<c:choose>
				<c:when test="${!empty param.sk && !empty param.sv}">
				 	<c:url var="pageUrl" value="/search.do"/>
				 	
				 	<%-- 쿼리스트링 내용을 변수에 저장 --%>
				 	<c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}"/>
				</c:when>
				
					<%-- 비어있을 때 --%>
				<c:otherwise>
					<c:url var="pageUrl" value="/review/list.do"/>
				</c:otherwise>
			</c:choose>
			
			<!-- <<, >> 화살표에 들어갈 주소를 변수로 생성(쿼리스트링 사용) -->
			<c:set var="firstPage" value="${pageUrl}?${tpStr}&cp=1${searchStr}"/>
			<c:set var="lastPage" value="${pageUrl}?${tpStr}&cp=${pInfo.maxPage}${searchStr}"/>
			
			<%-- EL을 이용한 숫자 연산의 단점 : 연산이 자료형에 영향을 받지 않음
				<fmt:parseNumber> : 숫자 형태를 지정하여 변수 선언
				integerOnly="true" : 정수로만 숫자를 표현(소수점 버림)
			--%>
			<%-- pInfo.pageSize : 10 --%>
			<!-- < 화살표를 눌렀을 때 이전 페이징의 endPage가 prev가 되도록 -->
			<%-- 현재페이지가 29라면 c1==2, prev==20 --%>
			<fmt:parseNumber var="c1" value="${(pInfo.currentPage - 1) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="prev" value="${c1 * pInfo.pageSize}" integerOnly="true"/>
			<c:set var="prevPage" value="${pageUrl}?${tpStr}&cp=${prev}${searchStr}"/>
			
			<!-- > 화살표를 눌렀을 때 다음 페이징의 startPage가 next가 되도록 -->
			<%-- 현재페이지가 23이라면 c2==3, next==31 --%>
			<fmt:parseNumber var="c2" value="${(pInfo.currentPage + 9) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="next" value="${c2 * pInfo.pageSize + 1}" integerOnly="true"/>
			<c:set var="nextPage" value="${pageUrl}?${tpStr}&cp=${next}${searchStr}"/>
			
			
			
			<div class="my-5">
				<ul class="pagination">
				
					<%-- 현재 페이지가 10페이지 초과인 경우 --%>
					<c:if test="${pInfo.currentPage > pInfo.pageSize}">
						<li> <!-- 첫 페이지로 이동(<<) -->
							<a class="page-link" href="${firstPage}">&lt;&lt;</a>
						</li>
						<li> <!-- 이전 페이지로 이동(<) -->
							<a class="page-link" href="${prevPage}">&lt;</a>
						</li>
					</c:if>
					
					<!-- 페이지 목록 -->
					<c:forEach var='page' begin="${pInfo.startPage}" end="${pInfo.endPage}">
						<c:choose>
						<c:when test="${pInfo.currentPage == page}">
							<li> <!-- 현재 페이지인 경우 활성화 -->
								<a class="page-link pag-active">${page}</a>
							</li>
						</c:when>
						<c:otherwise>
							<li>
								<a class="page-link" href="${pageUrl}?${tpStr}&cp=${page}${searchStr}">${page}</a>
							</li>
						</c:otherwise>
						</c:choose>
					</c:forEach>
						
					<%-- 다음 페이징의 첫번째 페이지가 마지막 페이지 미만일 경우 --%>	
					<c:if test="${next <= pInfo.maxPage}">
						<li> <!-- 이전 페이지로 이동(>>) -->
							<a class="page-link" href="${nextPage}">&gt;</a>
						</li>
						<li> <!-- 마지막 페이지로 이동(>>) -->
							<a class="page-link" href="${lastPage}">&gt;&gt;</a>
						</li>
					</c:if>
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
					
			//입양 후기 카드가 클릭되었을 때
			$(".list-card").on("click", function(){
				//글번호를 얻어와 해당 주소로 전달
				var reviewNo = $(this).children().children(".review-no").text();
				
				var url = "${contextPath}/review/view.do?${tpStr}&cp=${pInfo.currentPage}&no="  + reviewNo;
										//tdCdStr == tp=_&cd=_ / cp 추가하기 ${param.cp}
				location.href = url;
			});		
			
			//입양 후기 썸네일이 없을 때 야멍 로고 넣기 즉시 함수
			(function(){
				//카드 이미지에 반복 접근해 해당 카드 이미지 자식 요소 중 <img>가 없을 때 <img>태그 추가
				$(".card-image").each(function(index, item){
					if($(item).children("img").length == 0){
						var img = $("<img>").addClass("card-img-top").attr("src", "${pageContext.request.contextPath}/css/yamung_logo.png");
						
						$(item).html(img);
				}
				});
				
			})();//썸네일 추가 즉시 함수 끝
					
		});
	
	</script>





</body>
</html>