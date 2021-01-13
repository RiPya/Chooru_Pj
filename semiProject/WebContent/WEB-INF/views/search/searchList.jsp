<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>전체 검색</title>

<style>
/* 검색 라인 */
.header-search{
	width: 100%;
	padding-top : 8px;
	margin-bottom: 2rem;
}
.form-search {
   font-size: 20px;
   font-weight: bolder;
   padding-right: 20px;
   padding-left : 50px;
} 
.form-hr > hr{
	width: 85%;
	height: 8px;
	margin-bottom: .1rem;
	display: inline-block;
	background-color: teal;
	border-radius: 1rem;
}
	


/*------목록 스타일---------*/
.searchTitle>img {
	width: 50px;
	height: 50px;
}

/* 제목, 작성자 목록 구간 크기 지정 */
.searchTitle {
	min-width : 300px;
	max-width : 400px;
}
.searchWriter{
	min-width : 80px;
}

/*제목 부분을 제외한 부분 가운데 정렬*/
#list-table th:not(:nth-of-type(2)),
#list-table td:not(:nth-of-type(2)){
	text-align: center;
}

.list-wrapper{
	min-height: 540px;
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



/*댓글 수*/
.reply-count {
	font-size : 12px;
	color : teal;
	font-weight : bold;
}

/* 목록의 td에 마우스를 올릴 때 마우스 손가락 */
#list-table td:hover{
	cursor : pointer;
}

</style>

</head>
<body>
	
	<!-- tp 값 제거 (view에서 목록 돌아오기 했을 때 tp 값 없애야 header에 표시 안됨??) -->
<%-- 	<c:if test="${!empty param.tp}">
		<script>
			param.set('tp', '');
		</script>
	</c:if> --%>
	

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
			<div class="header-search">
				<span class="form-search">전체 검색</span>
				<span class="form-hr"><hr></span>
			</div>
			
			<div class="list-wrapper">
				<table class="table table-hover table-striped my-5" id="list-table">
					<thead>
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>조회수</th>
						</tr>
					</thead>
					
					<%-- 게시글 목록 출력 --%>
					<tbody>
					<%-- 확인용 --%>
						<c:forEach var="search" begin="1" end="3">
									<tr>
										<td>199</td>
										<td class="searchTitle">
											안녕하세요! 만나서 반갑습니다.
											<%--본문에 img가 있을 때 표시함 --%>
											<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>
											<%--${search.replyCount} : 댓글 수 변수 vo에 넣기--%>
											<span class="reply-count">[111]</span>	
										</td>
										<td>닉네임도길수도있죠</td>
										<td>2021-01-06</td>
										<td>202</td>
										<td class="sr-only">b4</td> <%-- ${search.brdType} --%>
									</tr>
									<tr>
										<td>180</td>
										<td class="searchTitle">
											안녕! haha ha님 영상을 보러가자 
											<%--본문에 img가 있을 때 표시함 --%>
											<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>
											<%--${search.replyCount} : 댓글 수 변수 vo에 넣기--%>
											<span class="reply-count">[3]</span>	
										</td>
										<td>나</td>
										<td>2021-01-06</td>
										<td>1</td>
										<td class="sr-only">b3</td> <%-- ${search.brdType} --%>
									</tr>
									<tr>
										<td>170</td>
										<td class="searchTitle">
											안녕 친구들
											<%--본문에 img가 있을 때 표시함 --%>
											<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>
											<%--${search.replyCount} : 댓글 수 변수 vo에 넣기--%>
											<span class="reply-count">[43]</span>	
										</td>
										<td>글쓴이</td>
										<td>2021-01-06</td>
										<td>88</td>
										<td class="sr-only">b2</td> <%-- ${search.brdType} --%>
									</tr>
						</c:forEach>
								<tr>
										<td>111</td>
										<td class="searchTitle">
											10번째 줄 그림, 댓글 없는 버전
											<%--본문에 img가 있을 때 표시함 --%>
											<!-- <i class="fas fa-file-image img-exist" style="color:darkgray;"></i> -->
											<%--${search.replyCount} : 댓글 수 변수 vo에 넣기--%>
											<!-- <span class="reply-count">[43]</span>	 -->
										</td>
										<td>마지막줄</td>
										<td>2021-01-06</td>
										<td>0</td>
										<td class="sr-only">b4</td> <%-- ${search.brdType} --%>
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
							<c:forEach var="search" items="${fList}">
								<tr>
									<td>${search.searchBrdNo}</td>
									<td class="searchTitle">
										${search.searchTitle}
										
										<!-- 본문에 img가 있을 때 표시함 c:if 사용? -->
										<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>		
																			
										<!-- 댓글 수 : vo 필드에 replyCount 넣기 -->
										<span class="reply-count">[${search.replyCount}]</span>	
									</td>
									<td class="searchWriter">${search.memId}</td>
									<td> 
									<!-- 날짜 출력 모양 지정 변수 선언 -->
										<!-- *조건 확인용 오늘 날짜 -->
							 			<fmt:formatDate var="today" 
											value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/> 
										<!-- *조회한 글의 작성 날짜 모양-->
									<fmt:formatDate var="createDate" 
											value="${search.brdCrtDt}" pattern="yyyy-MM-dd"/>
										
										<c:choose> 	
											작성일과 오늘이 아닐 경우 : yyyy-MM-dd형태의 createDate 출력
											<c:when test="${createDate != today}">
													${createDate}
											</c:when> 
											
											<!-- 작성일이 오늘일 경우 : boardCreateDate를 HH:mm으로 시간만 출력 -->
										<c:otherwise>
												<fmt:formatDate 
													value="${search.brdCrtDt}" pattern="HH:mm"/>
											</c:otherwise>
										</c:choose>	
									</td>
									<td>${search.readCount}</td>
									<td class="sr-only">${search.brdType}</td>
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




			<!-- 게시판명에 따라서 해당 검색 list로 가는 방식이면 sk select에서 전체를 빼야 함 -->
<%--	<c:choose>
				<c:when test="${param.tp == 'b1'}">
					<c:set var="goSearch" value="noticeSearch.do"/>
				</c:when>
				<c:when test="${param.tp == 'b2'}">
					<c:set var="goSearch" value="adoptSearch.do"/>
				</c:when>
				<c:when test="${param.tp == 'b3'}">
					<c:set var="goSearch" value="reviewSearch.do"/>
				</c:when>
				<c:when test="${param.tp == 'b4'}">
					<c:set var="goSearch" value="freeSearch.do"/>
				</c:when>
				<c:otherwise>
					<c:set var="goSearch" value="search.do"/>
				</c:otherwise>
			</c:choose>  --%>

			<div class="my-5">
				<form action="${contextPath}/search/search.do" method="GET" class="text-center " 
																																id="searchForm">
					<select name="tp" class="form-control sf-margin" style="width: 125px; display: inline-block;">
						<option value="all">전체</option>
						<option value="b1">공지사항</option>
						<option value="b2">입양/분양</option>
						<option value="b3">입양 후기</option>
						<option value="b4">자유 게시판</option>
					</select> 
					<select name="sk" class="form-control sf-margin" style="width: 120px; display: inline-block;">
						<option value="allKey">전체</option>
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
			
					
					
			//검색 내용이 있을 경우 검색창에 해당 내용을 작성해두는 즉시 실행 함수
			(function(){
				//검색 선택 파라미터 중
				//파라미터 중 tp, sk, sv가 있을 경우 변수가 저장됨 → 출력
				//파라미터 중 tp, sk, sv가 없을 경우 빈문자열로 출력됨(el은 null을 인식 안함)
				var searchType = "${param.tp}";
				var searchKey = "${param.sk}";
				var searchValue = "${param.sv}";
				
				//검색창 select 카테고리에 검색한 카테고리로 selected하기
				
				if(searchType == null) searchType = "all";
				if(searchKey == null) searchKey = "allKey";
				
				$("select[name=tp] > option").each(function(index, item){
						//index : 현재 접근 중인 요소의 인덱스
						//item : 현재 접근 중인 요소
						
						//검색조건일 경우 selected 추가
						if($(item).val() == searchType){
							$(item).prop("selected", true);
						}
				});
							
				//.each문 반복 접근문
				//검색창 select의 option을 반복 접근
				$("select[name=sk] > option").each(function(index, item){
					//index : 현재 접근 중인 요소의 인덱스
					//item : 현재 접근 중인 요소
					
					//검색조건일 경우 selected 추가
					if($(item).val() == searchKey){
						$(item).prop("selected", true);
					}
				});
				//검색창에 검색어 출력
				$("input[name=sv]").val(searchValue);
			})(); 
					
		});
		

		// 게시글 상세보기 기능 (jquery를 통해 작업)
		$("#list-table td").on("click", function(){
			//boardType 얻어오기 (파라미터 보내기와 상세 조회를 위해 필요함)
			var type = $(this).parent().children().eq(5).text();
			
			//게시글 번호 얻어오기
			var searchBrdNo = $(this).parent().children().eq(0).text();

			var brdType = ""; /*주소용 게시판  */
			
			switch(type) {
			case "b1" : brdType = "notice"; break;
			case "b2" : brdType = "adoption"; break;
			case "b3" : brdType = "review"; break;
			case "b4" : brdType = "free"; break;
			/* case "b5" : brdType = "information"; //고객센터도  */
			}
			
			
			var url = "${contextPath}/" + brdType + "/view.do?tp=" + type + 
								"&cp=${pInfo.currentPage}&no=" + searchBrdNo; 
									//cp(페이지), tp(게시판타입 b4 자유게시판), no 글번호
									//type : 각 목록의 게시판 type
			location.href = url;     
		});
		
			
		
	</script>
</body>
</html>
