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

/* 마이페이지 버튼 영역 */
/* .myPage-menu {
	top: 0;
	left: 0;
	height: auto;
	width: 100%;
} */
.menu-wrapper {
	top: 1px;
	left: 0;
	height: auto;
	width: 100%;
}

.menu>li {
	display: inline-block;
	margin: 10px;
}

/*메뉴에서 게시판명 폰트 조정*/
.menu li:first-of-type {
	font-size: 20px;
	font-weight: bold;
}

#insertBtn {
	margin-top: 5px;
}

.menu>li>button {
	border-radius: 20px;
	text-decoration: none;
	color: teal;
	padding: 6px 12px;
	display: block;
}

.menu>li>button:hover {
	background-color: teal;
	color: white;
}

/* 내 활동 조회 버튼 */
.myActiveList>li {
	display: inline-block;
	margin: 10px;
}

.myActiveList li:first-of-type {
	font-weight: bold;
}

#insertBtn {
	margin-top: 5px;
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
							class="btn bg-white myActive <c:if test="${empty param.my || param.cd == 'myActiveList'}">menu-active</c:if>"
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
						<%-- 확인용 --%>
						<c:forEach var="mypage" begin="1" end="3">
							<tr>
								<td>199</td>
								<td class="cd-color">자유게시판</td>
								<td class="mypageTitle">제목입니다 삼십자를 채워서 제일 긴 제목을 출력합니다 하하핫 <%--본문에 img가 있을 때 표시함 --%>
									<i class="fas fa-file-image img-exist" style="color: darkgray;"></i>
									<%--${mypage.replyCount} : 댓글 수 변수 vo에 넣기--%> <span
									class="reply-count">[111]</span>
								</td>
								<td>닉네임도길수도있죠</td>
								<td>2021-01-06</td>
								<td>202</td>
							</tr>
							<tr>
								<td>180</td>
								<td class="cd-color">입양 후기</td>
								<td class="mypageTitle">haha ha님 영상을 보러가자 <%--본문에 img가 있을 때 표시함 --%>
									<i class="fas fa-file-image img-exist" style="color: darkgray;"></i>
									<%--${mypage.replyCount} : 댓글 수 변수 vo에 넣기--%> <span
									class="reply-count">[3]</span>
								</td>
								<td>나</td>
								<td>2021-01-06</td>
								<td>1</td>
							</tr>
							<tr>
								<td>170</td>
								<td class="cd-color">공지사항</td>
								<td class="mypageTitle">아 스타일 맞추는 거 어렵다구요 <%--본문에 img가 있을 때 표시함 --%>
									<i class="fas fa-file-image img-exist" style="color: darkgray;"></i>
									<%--${mypage.replyCount} : 댓글 수 변수 vo에 넣기--%> <span
									class="reply-count">[43]</span>
								</td>
								<td>글쓴이</td>
								<td>2021-01-06</td>
								<td>88</td>
							</tr>
						</c:forEach>
						<tr>
							<td>111</td>
							<td class="cd-color">일상</td>
							<td class="mypageTitle">10번째 줄 그림, 댓글 없는 버전 <%--본문에 img가 있을 때 표시함 --%>
								<!-- <i class="fas fa-file-image img-exist" style="color:darkgray;"></i> -->
								<%--${mypage.replyCount} : 댓글 수 변수 vo에 넣기--%> <!-- <span class="reply-count">[43]</span>	 -->
							</td>
							<td>마지막줄</td>
							<td>2021-01-06</td>
							<td>0</td>
						</tr>

						<!-- db연결 후 -->
						<%-- 					<!-- 자유게시판의 게시글이 없을 때 -->
					<c:choose>
						<c:when test="${empty fList}">  
							<tr>
								<td colspan="6">존재하는 게시글이 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise> <!-- 게시글이 있을 때 모두 출력-->
							<c:forEach var="mypage" items="${fList}">
								<tr>
									<td>${mypage.mypageBrdNo}</td>
									<td class="cd-color">${mypage.mypageCode}</td>
									<td class="mypageTitle">
										${mypage.mypageTitle}
										
										<!-- 본문에 img가 있을 때 표시함 c:if 사용? -->
										<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>		
																			
										<!-- 댓글 수 : vo 필드에 replyCount 넣기 -->
										<span class="reply-count">[${mypage.replyCount}]</span>	
									</td>
									<td class="mypageWriter">${mypage.memId}</td>
									<td> 
									<!-- 날짜 출력 모양 지정 변수 선언 -->
										<!-- *조건 확인용 오늘 날짜 -->
							 			<fmt:formatDate var="today" 
											value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/> 
										<!-- *조회한 글의 작성 날짜 모양-->
									<fmt:formatDate var="createDate" 
											value="${mypage.brdCrtDt}" pattern="yyyy-MM-dd"/>
										
										<c:choose> 	
											작성일과 오늘이 아닐 경우 : yyyy-MM-dd형태의 createDate 출력
											<c:when test="${createDate != today}">
													${createDate}
											</c:when> 
											
											<!-- 작성일이 오늘일 경우 : boardCreateDate를 HH:mm으로 시간만 출력 -->
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
					</c:choose> --%>

					</tbody>
				</table>
			</div>


			<%---------------------- Pagination ----------------------%>
			<%-- boardList.jsp에서 복붙한 거 놔두면 오류나서 메모장 파일에 옮겨놓음 --%>
			<!-- cd가 없다면 href의 url 뒤에 -->


			<!-- 임시 확인용 Pagination -->
			<div class="my-5">
				<ul class="pagination">
					<li>
						<!-- 첫 페이지로 이동(<<) --> <a class="page-link" href="#">&lt;&lt;</a>
					</li>
					<li>
						<!-- 이전 페이지로 이동(<) --> <a class="page-link" href="#">&lt;</a>
					</li>

					<c:forEach var='i' begin="1" end="10">
						<li><a
							class="page-link <c:if test="${param.cp==i}">pag-active</c:if>"
							href="#">${i}</a></li>
					</c:forEach>

					<li>
						<!-- 이전 페이지로 이동(>>) --> <a class="page-link" href="#">&gt;</a>
					</li>
					<li>
						<!-- 마지막 페이지로 이동(>>) --> <a class="page-link" href="#}">&gt;&gt;</a>
					</li>
				</ul>
			</div>
		</div>
	</div>


	<!-- footer -->
	<jsp:include page="../common/footer.jsp"></jsp:include>


	<script>
		$(document).ready(function() {

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
							   break;
				case "내가 쓴 댓글": activeCode = "myActiveReply"; 
								break;
				}

			  url = "${contextPath}/member/myActiveList.do?${tpCdStr}&cp=1&my=" + activeCode;
				//해당 카테고리(activeCode(내가쓴글, 내가쓴댓글))를 가지는 게시글 목록만 다시 출력하도록 요청
				//해당 카테고리의 1페이지로 리셋해야 하기 때문에 cp=1
				//cp(페이지), tp(게시판타입 b1 b2 b3 b4 adminMem b5 mypage), cd(카테고리)
				//tpStr = tp=_
				location.href = url;
			});

			//페이징바 활성화(현재 페이지 부분 색바꾸기) 진행?

		});

		// 게시글 상세보기 기능 (jquery를 통해 작업)
		$("#list-table td").on("click", function() {
			//게시글 번호 얻어오기
			var myPageBrdNo = $(this).parent().children().eq(0).text();

			var url = "${contextPath}/member/view.do?${tpCdStr}&cp=${pInfo.currentPage}&no="+ myPageBrdNo;
			//cp(페이지), tp(게시판타입 b4 자유게시판), no 글번호
			//tpCdStr : "tp=_&cd=_"
			location.href = url;
		});
	</script>
</body>
</html>
