<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입양/분양</title>

<style>
/* 마이페이지 버튼 영역 */
/* .myPage-menu {
	top: 0;
	left: 0;
	height: auto;
	width: 100%;
}
 */
.menu-wrapper {
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


/*------목록 스타일---------*/
#adoptionList {
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

#adoptionList .list-card {
	display : inline-block !important;
	width: 30%; 
	height:30%;
	min-width : 200px;
	margin : 10px;
}

#adoptionList > .list-card > .card-image { 
	width : 100%;
	height : 250px;
/* 	margin-bottom : 10px; */
}

#adoptionList .card-img-top {
/* 	width : 100%; */
	height: 100%;
}

#adoptionList > .list-card > .card-body { }
.card-body > p { margin:0; }

.card-body > .adoptTitle,
.card-body > .adoptCode {
	color : teal;
	font-weight : bold;
	text-align : center;
}

.card-body > .adoptCode { font-size : 14px;}
.card-body > .adoptTitle { font-size : 18px;}
.card-body > .card-text { 
	font-size : 14px; 
	color : rgb(51,51,51); 
	text-align : left;
}
/* 입양 여부 완료 span 모양 */
.card-body > .adopt-yn > span {
	display : inline-block;
	font-weight : bold; 
	width : 60px;
	border-radius : 5px;
	text-align : center;
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


</style>

</head>
<body>
	
	<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}"/>

	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<c:if test="${!empty param.cd}">
		<c:set var="tpCpNoStr" value="tp=${param.tp}&cp=${param.cp}&no=${param.no}"/>
	</c:if>

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
			<div class="menu-wrapper">
				<ul class="menu">
					<li>입양/후기</li> <!-- on이벤트로 script에 작성하기 id 구분 필요 myPage는 클래스로-->
					<!-- adAll adDog adCat adEtc adTemp -->
					<li><button class="btn btn-light adoption <c:if test="${empty param.cd || param.cd == 'adtAll'}">menu-active</c:if>"
							 	type="button">전체</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'adtDog'}">menu-active</c:if>" 
								type="button">입양 개</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'adtCat'}">menu-active</c:if>" 
								type="button">입양 고양이</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'adtEtc'}">menu-active</c:if>" 
								type="button">입양 기타</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'temp'}">menu-active</c:if>" 
								type="button">임시 보호</button></li>
				</ul>
			</div>
			
			<div id="adoptionList">
 			<c:choose>
				<c:when test="${empty aList}">  
					<p id="noneList">존재하는 게시글이 없습니다.</p>
				</c:when>
				
				<%-- 게시글이 있을 때 모두 출력 --%>
				<c:otherwise>
					<c:forEach var="adoption" items="${aList}">

						<div class="card list-card">
						  <div class="card-image"> 
						  	<!-- c:forEach 사용해서 해당 게시글 번호에 맞는 첫번째 이미지 붙여넣기 -->
 						  	<c:forEach var="thumbnail" items="${iList}">
							 		<c:if test="${adoption.adtBrdNo == thumbnail.brdNo}">	
							 			<img src="${thumbnail.filePath}${thumbnail.fileName}" 
							  			class="card-img-top" alt="${adoption.adtBrdTitle}}">
									</c:if>
						  	</c:forEach>
						  </div>
						  <div class="card-body">
						  	<p class="card-title adoptCode">[${adoption.adtCode}]</p>
						    <p class="card-title adoptTitle" style="height:60px;">${adoption.adtBrdTitle}</p>
						    <p class="card-text adopt-yn" style="text-align : right;"><span>${adoption.adtYn}</span></p>
						    <p class="card-text adopt-breed">품종 : ${adoption.adtBreed}</p>
						    <p class="card-text adopt-gender">성별 : ${adoption.adtGender}</p>
						    <p class="card-text adopt-age">나이 : ${adoption.adtAge}(추정)</p>
						    <p class="adopt-no sr-only">${adoption.adtBrdNo}</p><!-- sr-only : 안보이게하는 클래스 -->
						  </div>
						</div>
	
					</c:forEach> 
	 		</c:otherwise> 
			</c:choose>
			
			
			</div><!-- 카드형 목록 리스트 div 끝 -->
			
			<%-- 로그인이 되어있는 경우 --%>
			<c:if test="${!empty loginMember}">
				<button type="button" class="btn btn-teal float-right" id="insertBtn" 
						  onclick="location.href='${contextPath}/adoption/insertForm.do?${tpStr}'">글쓰기</button>
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
								
								
			<!-- 검색창 : type:게시판코드(입양/분양은 b2), cd:자유카테고리(검색창에서 설정)-->
			<!-- 게시판코드를 파라미터로 넘겨야 할까? -->
			<div class="my-5">
				<form action="${contextPath}/adoptionSearch.do?${tpStr}" method="GET" class="text-center " 
																																id="searchForm">
					<!-- cd -->
					<select name="cd" class="form-control sf-margin" style="width: 120px; display: inline-block;">
						<option value="adtAll">전체</option>
						<option value="adtDog">입양 개</option>
						<option value="adtCat">입양 고양이</option>
						<option value="adtEtc">입양 기타</option>
						<option value="temp">임시 보호</option>
					</select> 
					
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
			
			/* 메뉴 클릭 관련 함수 */
			/*파라미터에 cd(카테고리 여기서는 text())를 담아서 
				(header.jsp의 게시판메뉴 활성화를 위해 tp(type)도 같이 보냄)
				해당 페이지 목록을 가져올 수 있도록 list.do로 보내기
				controller에서 cd를 꺼내서 쓰고 ★request로 다시 전달 받음★
				→ 메뉴 버튼 활성화(menu-active 추가 요소 태그에서 c:if 사용)*/
			$(".adoption").on("click", function(){
				//해당 요소의 내용(카테고리 freeCode)을 가져오기
				var category = $(this).text(); 
				
				var adtCode = "";
				
				switch(category){
				case "전체" : adtCode = "adtAll"; break;
				case "입양 개" : adtCode = "adtDog"; break;
				case "입양 고양이" : adtCode = "adtCat"; break;
				case "입양 기타" : adtCode = "adtEtc"; break;
				case "임시 보호" : adtCode = "temp"; break;
				}
				
				//해당 카테고리를 가지는 게시글 목록만 다시 출력하도록 요청
				//해당 카테고리의 1페이지로 리셋해야 하기 때문에 cp=1
				var url = "${contextPath}/adoption/list.do?${tpStr}&cp=1&cd=" + adtCode;
								//cp(페이지), tp(게시판타입 b1 b2 b3 b4 adminMem b5 mypage), cd(카테고리)
								//tpStr = tp=_
				location.href = url;
			});

			// 글 목록의 카테고리의 출력 문자를 변환하는 즉시 실행 함수
			(function(){
				$(".adopt-gender").each(function(index, item){
					if($(item).text() == "성별 : boy" ){
						$(item).text("성별 : 수컷");
					} else if($(item).text() == "성별 : girl"){
						$(item).text("성별 : 암컷");
					} else if($(item).text() == "성별 : ntrBoy"){
						$(item).text("성별 : 수컷(중성화)");
					} else if($(item).text() == "성별 : ntrGirl"){
						$(item).text("성별 : 암컷(중성화)");
					}
				});
			})();
			
			//글 목록의 입양 진행 여부 색상 정하는 즉시 실행 함수
			(function(){
				$(".adopt-yn span").each(function(index, item){
					if($(item).text() == "N" ){
						$(item).text("진행 중");
						$(item).css({"background-color":"tomato",  "color":"white"});
					} else if($(item).text() == "Y"){
						$(item).text("완료");
						$(item).css({"background-color":"yellowgreen",  "color":"white"});
					}
				});
			})();
			
			//입양/분양 카드가 클릭되었을 때
			$(".list-card").on("click", function(){
				//글번호를 얻어와 해당 주소로 전달
				var adtBrdNo = $(this).children().children(".adopt-no").text();
				
				var url = "${contextPath}/adoption/view.do?${tpCdStr}&cp=1&no=" + adtBrdNo;
										//tdCdStr == tp=_&cd=_ // cp 추가하기 ${param.cp}
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