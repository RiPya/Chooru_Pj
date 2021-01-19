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
	min-width : 120px;
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
	<c:if test="${!empty param.tp}">
		<script>
			
		</script>
	</c:if>
	

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
					<!-- db연결 후 -->
 					<!-- 자유게시판의 게시글이 없을 때 -->
					<c:choose>
						<c:when test="${empty sList}">  
							<tr>
								<td colspan="6" height="140px" style="line-height : 100px;">존재하는 게시글이 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise> <!-- 게시글이 있을 때 모두 출력-->
							<c:forEach var="search" items="${sList}">
								<tr>
									<td>${search.brdNo}</td>
									<td class="searchTitle">
										${search.brdTitle}
										
										<c:forEach var="img" items="${iList}">
							 				<c:if test="${search.brdNo == img.brdNo}">	
							 					<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>	
											</c:if>
								 		</c:forEach>			
																			
										<!-- 글번호가 같을 때 댓글 수 추가 -->
										<c:forEach var="comm" items="${commCounts}">
											<c:if test='${comm.brdNo == search.brdNo}'>
												<span class="reply-count">[${comm.count}]</span>
											</c:if>	
										</c:forEach>
									</td>
									<td class="searchWriter">${search.nickName}</td>
									<td width="140px"> 
									<!-- 날짜 출력 모양 지정 변수 선언 -->
										<!-- *조건 확인용 오늘 날짜 -->
							 			<fmt:formatDate var="today" 
											value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/> 
										<!-- *조회한 글의 작성 날짜 모양-->
									<fmt:formatDate var="createDate" 
											value="${search.brdCrtDt}" pattern="yyyy-MM-dd"/>
										
										<c:choose> 	
											<c:when test="${createDate != today}">
													${createDate}
											</c:when> 
											
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
					</c:choose> 
					</tbody>
				</table>
			</div>


			
			
			<%---------------------- Pagination ----------------------%>
			<%-- boardList.jsp에서 복붙한 거 놔두면 오류나서 메모장 파일에 옮겨놓음 --%>
			<!-- cd가 없다면 href의 url 뒤에 -->
			
			

			
			<%-- 파라미터의 sk(searchKey)와 sv(searchValue)가 비어있지 않을 때
					 == 검색 후 페이징바 클릭 --%>
			<c:choose>
				<c:when test="${!empty param.sk && !empty param.sv}">
				 	<c:url var="pageUrl" value="/search/search.do"/>
				 	
				 	<%-- 쿼리스트링 내용을 변수에 저장 --%>
				 	<c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}"/>
				</c:when>
				
					<%-- 비어있을 때 --%>
				<c:otherwise>
					<c:url var="pageUrl" value="/search/search.do"/>
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





			<div class="my-5">
				<form action="${contextPath}/search/search.do" method="GET" class="text-center " 
																				onsubmit="return allValidate();"	id="searchForm">
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
					
					<input type="text" name="sv" class="form-control sf-margin" id="searchAll"
							placeholder="검색어를 입력하세요." style="width: 25%; display: inline-block;">
							
					<button class="form-control btn btn-teal" style="width: 70px; display: inline-block;">
						<i class="fas fa-search" id="search-in-icon"></i><!--찾기아이콘-->
					</button>

				</form>
			</div>

	</div>
	
	
	<!-- footer -->
	<jsp:include page="../common/footer.jsp"></jsp:include>

	
	<c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}"/>


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
				$("#searchAll").val(searchValue);
			})(); 
					
		});
		
		
		function allValidate() {
			if ($("#searchAll").val().trim().length == 0) {
				swal({icon:"warning", title:"검색어를 입력해 주세요."});
				$("#searchAll").focus();
				return false;
			}
		}
		

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
								"&from=s&cp=${pInfo.currentPage}${searchStr}&no=" + searchBrdNo; 
									//cp(페이지), tp(게시판타입 b4 자유게시판), no 글번호
									//type : 각 목록의 게시판 type
									//sl(전체 검색 목록에서 왔는지 아닌지 판단) : y/n
			location.href = url;     
		});
		
			
		
	</script>
</body>
</html>
