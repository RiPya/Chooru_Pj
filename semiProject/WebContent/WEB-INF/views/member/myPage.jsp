<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 활동 조회</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

<!-- Bootstrap core JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
	crossorigin="anonymous"></script>

<!-- sweetalert : alert창을 꾸밀 수 있게 해주는 라이브러리 https://sweetalert.js.org/ -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<!--font awesome : 아이콘 사용 라이브러리-->
<script src="https://kit.fontawesome.com/2dd9466b88.js"
	crossorigin="anonymous"></script>

<!-- header의 css + btn-teal 버튼 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/headerStyle.css?ver=1.4">

<style>

/* 내 활동 조회 버튼 */

.myActiveList>li {
	display: inline-block;
	margin: 10px;
	font-family: 'TmoneyRoundWindRegular'; 
}

.myActiveList li:first-of-type {
	font-weight: bold; 
	font-size: 18px;
}

.myActiveList>li>button{
	padding-top :0;
	padding-button:0;
}

.myActiveList>li>button:hover {
	color: teal;
}

/*클릭 시 추가해서 사용할 클래스 (다른 것을 클릭하면 제거 : script에서 사용)
 첫 페이지에서는 '전체'에 적용*/
.myActiveList>li>.menu-active {
	color: teal !important;
	font-weight: bold !important;
}

/*------목록 스타일---------*/
.myPageTitle>img {
	width: 50px;
	height: 50px;
}

/* 제목, 작성자 목록 구간 크기 지정 */
.mypageTitle {
	min-width: 400px;
}

.myPageWriter {
	min-width: 80px;
}

/*제목 부분을 제외한 부분 가운데 정렬*/
#list-table th:not(:nth-of-type(3)), #list-table td:not(:nth-of-type(3))
	{
	text-align: center;
}

.list-wrapper {
	min-height: 540px;
}

/* 페이징바 */
.pagination {
	justify-content: center;
}

.pagination>li>a {
	background-color: none;
	color: rgb(51, 51, 51);
	font-size: 12px;
	border: none;
	border-radius: 25%;
	min-width: 35px;
	height: 35px;
	text-align: center;
	line-height: 35px;
	padding: 0;
}

.pagination .page-link:hover {
	color: teal !important;
}

/*페이징바 클릭 시 추가해서 사용할 클래스
(다른 것을 클릭하면 제거 : script에서 사용)
 첫 페이지에서는 '전체'에 적용*/
.pagination>li>.pag-active {
	color: teal !important;
	font-weight: bold !important;
}

#searchForm {
	position: relative;
}

#searchForm>* {
	top: 0;
	vertical-align: middle; /*인라인 요소 위로 정렬*/
}

/*댓글 수*/
.reply-count {
	font-size: 12px;
	color: teal;
	font-weight: bold;
}

/* 목록의 td에 마우스를 올릴 때 마우스 손가락 */
#list-table td:hover {
	cursor: pointer;
}
</style>

</head>
<body>

	<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}" />
	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}" />

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>

	<div class="container">
		<jsp:include page="../member/myPageMenu.jsp"></jsp:include>

		<div class="container my-5">
			<div class="menu-wrapper">

				<ul class="myActiveList">
					<li>내 활동 조회</li>
					<li><button
							class="btn bg-white myActive <c:if test="${empty param.my || param.my == 'myActiveList'}">menu-active</c:if>"
							type="button">내가 쓴 글</button></li>
					<li>|</li>
					<li><button
							class="btn bg-white myActive <c:if test="${param.my == 'myActiveReply'}">menu-active</c:if>"
							type="button">내가 쓴 댓글</button></li>
				</ul>
			</div>

			<div class="list-wrapper">
				<table class="table table-hover table-striped my-5" id="list-table">
					<thead>
						<tr>
							<th>번호</th>
							<th>게시판</th>
							<th>제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>조회수</th>
						</tr>
					</thead>

					<%-- 게시글 목록 출력 --%>
					<tbody>
						<!-- db연결 후 -->
						<!-- 자유게시판의 게시글이 없을 때 -->
					<c:choose>
						<c:when test="${empty bList}">  
							<tr>
								<td colspan="6" align="center">존재하는 게시글이 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise> <!-- 게시글이 있을 때 모두 출력-->
							<c:forEach var="mypage" items="${bList}">
								<tr>
									<td>${mypage.brdNo}</td>
									<td class="typeTd">${mypage.brdType}</td>
									<td class="mypageTitle">
										${mypage.brdTitle}
										
										<c:forEach var="img" items="${iList}">
							 				<c:if test="${mypage.brdNo == img.brdNo}">	
							 					<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>	
											</c:if>
								 		</c:forEach>																			
									</td>
									<td class="mypageWriter">${mypage.nickName}</td>
									<td> 
									<!-- 날짜 출력 모양 지정 변수 선언 -->
										<!-- *조건 확인용 오늘 날짜 -->
							 			<fmt:formatDate var="today" 
											value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/> 
										<!-- *조회한 글의 작성 날짜 모양-->
									<fmt:formatDate var="createDate" 
											value="${mypage.brdCrtDt}" pattern="yyyy-MM-dd"/>
										
										<c:choose> 	
											<%-- 작성일과 오늘이 아닐 경우 : yyyy-MM-dd형태의 createDate 출력 --%>
											<c:when test="${createDate != today}">
													${createDate}
											</c:when> 
											
											<%-- 작성일이 오늘일 경우 : boardCreateDate를 HH:mm으로 시간만 출력 --%>
										<c:otherwise>
												<fmt:formatDate 
													value="${mypage.brdCrtDt}" pattern="HH:mm"/>
											</c:otherwise>
										</c:choose>	
									</td>
									<td>${mypage.readCount}</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>

					</tbody>
				</table>
			</div>


			<%---------------------- Pagination ----------------------%>
			<%-- boardList.jsp에서 복붙한 거 놔두면 오류나서 메모장 파일에 옮겨놓음 --%>
			
		 	<c:url var="pageUrl" value="member/myActiveList.do"/>
		 	<c:set var="cdMyStr" value="&cd=${param.cd}&my=${param.my}"/>
			
			<!-- <<, >> 화살표에 들어갈 주소를 변수로 생성(쿼리스트링 사용) -->
			<c:set var="firstPage" value="${pageUrl}?${tpStr}&cp=1${cdMyStr}"/>
			<c:set var="lastPage" value="${pageUrl}?${tpStr}&cp=${pInfo.maxPage}${cdMyStr}"/>
			
			<%-- EL을 이용한 숫자 연산의 단점 : 연산이 자료형에 영향을 받지 않음
				<fmt:parseNumber> : 숫자 형태를 지정하여 변수 선언
				integerOnly="true" : 정수로만 숫자를 표현(소수점 버림)
			--%>
			<%-- pInfo.pageSize : 10 --%>
			<!-- < 화살표를 눌렀을 때 이전 페이징의 endPage가 prev가 되도록 -->
			<%-- 현재페이지가 29라면 c1==2, prev==20 --%>
			<fmt:parseNumber var="c1" value="${(pInfo.currentPage - 1) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="prev" value="${c1 * pInfo.pageSize}" integerOnly="true"/>
			<c:set var="prevPage" value="${pageUrl}?${tpStr}&cp=${prev}${cdMyStr}"/>
			
			<!-- > 화살표를 눌렀을 때 다음 페이징의 startPage가 next가 되도록 -->
			<%-- 현재페이지가 23이라면 c2==3, next==31 --%>
			<fmt:parseNumber var="c2" value="${(pInfo.currentPage + 9) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="next" value="${c2 * pInfo.pageSize + 1}" integerOnly="true"/>
			<c:set var="nextPage" value="${pageUrl}?${tpStr}&cp=${next}${cdMyStr}"/>
						
			
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
								<a class="page-link" href="${pageUrl}?${tpStr}&cp=${page}${cdMyStr}">${page}</a>
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
		</div>
	</div>


	<!-- footer -->
	<jsp:include page="../common/footer.jsp"></jsp:include>


	<script>
		$(document).ready(function() {
			// console.log("${param.tp}");
			/* 메뉴 클릭 관련 함수 */
			/*파라미터에 cd(카테고리 여기서는 text())를 담아서 
			(header.jsp의 게시판메뉴 활성화를 위해 tp(type)도 같이 보냄)
			해당 페이지 목록을 가져올 수 있도록 list.do로 보내기
			controller에서 cd를 꺼내서 쓰고 ★request로 다시 전달 받음★
			→ 메뉴 버튼 활성화(menu-active 추가 요소 태그에서 c:if 사용)*/
			$(".myActive").on("click", function() {
				//해당 요소의 내용을 가져오기
				var category = $(this).text();

				var activeCode = "";
				var url = "";

				switch (category) {
				case "내가 쓴 글": activeCode = "myActiveList"; 
			 				   url = "${contextPath}/member/myActiveList.do?${tpCdStr}&cp=1&my=" + activeCode;
							   break;
				case "내가 쓴 댓글": activeCode = "myActiveReply"; 
								url = "${contextPath}/member/myActiveReply.do?${tpCdStr}&cp=1&my=" + activeCode;
								break;
				}

				//해당 카테고리(activeCode(내가쓴글, 내가쓴댓글))를 가지는 게시글 목록만 다시 출력하도록 요청
				//해당 카테고리의 1페이지로 리셋해야 하기 때문에 cp=1
				//cp(페이지), tp(게시판타입 b1 b2 b3 b4 adminMem b5 mypage), cd(카테고리)
				//tpStr = tp=_
				location.href = url;
			});

			//페이징바 활성화(현재 페이지 부분 색바꾸기) 진행?

		});

		// 게시글 상세보기 기능 (jquery를 통해 작업)
		$("#list-table td").on("click", function(){
			//boardType 얻어오기 (파라미터 보내기와 상세 조회를 위해 필요함)
			var type = $(this).parent().children().eq(1).text();
			
			//게시글 번호 얻어오기
			var searchBrdNo = $(this).parent().children().eq(0).text();

			var brdType = ""; /*주소용 게시판  */
			
			switch(type) {
			case "공지사항" : brdType = "notice"; break;
			case "입양/분양" : brdType = "adoption"; break;
			case "입양 후기" : brdType = "review"; break;
			case "자유게시판" : brdType = "free"; break;
			case "고객센터" : brdType = "information"; break;
			}
			
			
			var url = "${contextPath}/" + brdType + "/view.do?tp=" + type + 
					  "&from=myL&cp=${pInfo.currentPage}&no=" + searchBrdNo; 
						//cp(페이지), tp(게시판타입 b4 자유게시판), no 글번호
						//type : 각 목록의 게시판 type
			location.href = url;
		});
		
		
		(function(){
			$(".typeTd").each(function(index, item){
				var brdType = $(item).text();
				
				switch(brdType){
				case "b1" : $(item).text("공지사항"); break;
				case "b2" : $(item).text("입양/분양"); break;
				case "b3" : $(item).text("입양 후기"); break;
				case "b4" : $(item).text("자유게시판"); break;
				case "b5" : $(item).text("고객센터"); break;
				}
			});
		})();
		
		
		
		
		
	</script>
</body>
</html>
