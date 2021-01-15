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
	<c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}"/>

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
			<div class="menu-wrapper">
				<ul class="menu">
					<li>입양/후기</li> <!-- on이벤트로 script에 작성하기 id 구분 필요 myPage는 클래스로-->
					<!-- adAll adDog adCat adEtc adTemp -->
					<li><button class="btn btn-light adoption <c:if test="${empty param.cd || param.cd == 'adAll'}">menu-active</c:if>"
							 	type="button">전체</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'adDog'}">menu-active</c:if>" 
								type="button">입양 개</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'adCat'}">menu-active</c:if>" 
								type="button">입양 고양이</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'adEtc'}">menu-active</c:if>" 
								type="button">입양 기타</button></li>
					<li><button class="btn btn-light adoption <c:if test="${param.cd == 'adTemp'}">menu-active</c:if>" 
								type="button">임시보호</button></li>
				</ul>
			</div>
			
			<div id="adoptionList">
<%-- 			<c:choose>
				<c:when test="${empty fList}">  
					<p id="noneList">존재하는 게시글이 없습니다.</p>
				</c:when>
				<c:otherwise> <!-- 게시글이 있을 때 모두 출력--> --%>
					<c:forEach var="i" begin="0" end="2">
		<%-- 			<c:forEach var="adoption" items="${adoptList}"> --%>
						<div class="card list-card">
						  <div class="card-image"> <!-- c:forEach 사용해서 해당 게시글 번호에 맞는 첫번째 이미지 붙여넣기 -->
						 		<img src="https://i1.pickpik.com/photos/135/49/188/cat-cute-yellow-animal-preview.jpg" 
						  		class="card-img-top" alt="${adoption.adoptTitle}}">
						  </div>
						  <div class="card-body">
						  	<p class="card-title adoptCode">[입양 고양이]</p> <!-- ${adoption.adoptCode}} -->
						    <p class="card-title adoptTitle" style="height:60px;">치즈냥 가족 찾아요</p> <!-- ${adoption.adoptTitle}} -->
						    <p class="card-text adopt-yn" style="text-align : right;"><span>N</span></p><!-- ${adoption.adoptYn}} -->
						    <p class="card-text adopt-breed">품종 : 코숏</p><!-- 품종 : ${adoption.adoptBreed}} -->
						    <p class="card-text adopt-gender">성별 : 수컷</p><!-- 성별 : ${adoption.adoptGender}} -->
						    <p class="card-text adopt-age">나이 : 6개월(추정)</p><!-- 나이 : ${adoption.adoptAge}} -->
						    <p class="adopt-no sr-only">123</p><!-- sr-only : 안보이게하는 클래스 --> <!-- ${adoption.adoptBrdNo}} -->
						  </div>
						</div>
						<div class="card list-card">
						  <div class="card-image">
						  	<img src="https://i2.pickpik.com/photos/743/719/777/dogs-sun-summer-cute-preview.jpg" 
						  		class="card-img-top" alt="${adoption.adoptBrdTitle}}">
						  </div>
						  <div class="card-body">
						  	<p class="card-title adoptCode">[입양 개]</p>
						    <p class="card-title adoptTitle" style="height:60px;">부산 하양이</p>
						    <p class="card-text adopt-yn" style="text-align : right;"><span>N</span></p>
						    <p class="card-text adopt-breed">품종 : 믹스견</p>
						    <p class="card-text adopt-gender">성별 : 암컷</p>
						    <p class="card-text adopt-age">나이 : 1살</p>
						    <p class="adopt-no sr-only">123</p>
						  </div>
						</div>
						<div class="card list-card">
						  <div class="card-image">
						  	<img src="https://i2.pickpik.com/photos/714/11/745/golden-retriever-animal-shelter-dog-pension-kennels-preview.jpg" 
						  		class="card-img-top" alt="${adoption.adoptBrdTitle}}">
						  </div>
						  <div class="card-body">
						  	<p class="card-title adoptCode">[입양 개]</p>
						    <p class="card-title adoptTitle" style="height:60px;">활발한 성격인 인절미가 가족을 기다려요</p>
						    <p class="card-text adopt-yn" style="text-align : right;"><span>Y</span></p>
						    <p class="card-text adopt-breed">품종 : 리트리버믹스(추정)</p>
						    <p class="card-text adopt-gender">성별 : 수컷(중성화)</p>
						    <p class="card-text adopt-age">나이 : 2살</p>
						    <p class="adopt-no sr-only">123</p>
						  </div>
						</div>
					</c:forEach> 
<%-- 		</c:otherwise> 
			</c:choose> --%>
			
			
			
			</div><!-- 카드형 목록 리스트 div 끝 -->
			
						<%-- 로그인이 되어있는 경우 --%>
	<%--c:if test="${!empty loginMember}" --%>
				<button type="button" class="btn btn-teal float-right" id="insertBtn" 
						  onclick="location.href='${contextPath}/adoption/insertForm.do?${tpStr}'">글쓰기</button>
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
				<form action="${contextPath}/adoptionSearch.do?${tpStr}" method="GET" class="text-center " 
																																id="searchForm">
					<!-- cd -->
					<select name="cd" class="form-control sf-margin" style="width: 120px; display: inline-block;">
						<option value="adAll">전체</option>
						<option value="adDog">입양 개</option>
						<option value="adCat">입양 고양이</option>
						<option value="adEtc">입양 기타</option>
						<option value="adTemp">임시보호</option>
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
				
				var adoptionCode = "";
				
				switch(category){
				case "전체" : adoptionCode = "adAll"; break;
				case "입양 개" : adoptionCode = "adDog"; break;
				case "입양 고양이" : adoptionCode = "adCat"; break;
				case "입양 기타" : adoptionCode = "adEtc"; break;
				case "임시보호" : adoptionCode = "adTemp"; break;
				}
				
				//해당 카테고리(freeCode)를 가지는 게시글 목록만 다시 출력하도록 요청
				//해당 카테고리의 1페이지로 리셋해야 하기 때문에 cp=1
				var url = "${contextPath}/adoption/list.do?${tpStr}&cp=1&cd=" + adoptionCode;
								//cp(페이지), tp(게시판타입 b1 b2 b3 b4 adminMem b5 mypage), cd(카테고리)
								//tpStr = tp=_
				location.href = url;
			});
			
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
			
			//페이징바 활성화(현재 페이지 부분 색바꾸기) 진행?
			
					
			//입양/분양 카드가 클릭되었을 때
			$(".list-card").on("click", function(){
				//글번호를 얻어와 해당 주소로 전달
				var adoptNo = $(this).children().children(".adopt-no").text();
				
				var url = "${contextPath}/adoption/view.do?${tpCdStr}&cp=1&no=" + adoptNo;
										//tdCdStr == tp=_&cd=_ // cp 추가하기 ${param.cp}
				location.href = url;
			});		
					
					
		
		});
	

/* 		// 게시글 상세보기 기능 (jquery를 통해 작업)
		$("#list-table td").on("click", function(){
			//게시글 번호 얻어오기
			var freeBrdNo = $(this).parent().children().eq(0).text();
			
			var url = "${contextPath}/adoption/view.do?${tpCdStr}&cp=${pInfo.currentPage}&no=" + freeBrdNo; 
																	//cp(페이지), tp(게시판타입 b4 자유게시판), no 글번호
																	//tpCdStr : "tp=_&cd=_"
			location.href = url;     
		});
	 */
	
	
	</script>





</body>
</html>