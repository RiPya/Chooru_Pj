<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객센터</title>

<style>

/* 고객센터 라인 */
.header-info{
   width: 100%;
   margin-bottom: 2rem;
}

.form-info {
   font-size: 20px;
   font-weight: bolder;
   padding-right: 20px;
   
} 

.form-hr > hr {
   width: 85%;
   height: 8px;
   margin-bottom: .1rem;
   display: inline-block;
   background-color: teal;
   border-radius: 1rem;
}


/*------목록 스타일---------*/

/*제목 부분을 제외한 부분 가운데 정렬*/
#list-table th:not(:nth-of-type(3)),
#list-table td:not(:nth-of-type(3)){
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
	
	 <%-- url 작성 시 붙여야 하는 str --%>
   <!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
   <c:set var="tpStr" value="tp=${param.tp}"/>
   <!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
   <c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}"/>
   
	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
		<div class="header-info">
			<span class="form-info">고객 센터</span>
			<span class="form-hr" ><hr></span>
		</div>
			
		<div class="list-wrapper">
			<table class="table table-hover table-striped my-5" id="list-table">
				<thead>
					<tr>
						<th>번호</th>
						<th>카테고리</th>
						<th>제목</th>
						<th>작성자</th>
						<th>작성일</th>
						<th>조회수</th>
					</tr>
				</thead>
				
				<%-- 게시글 목록 출력 --%>
				<tbody>
				<%-- 확인용 --%>
					<c:forEach var="info" begin="1" end="3">
						<tr>
							<td>302</td>
							<td class="cd-color">신고</td>
							<td class="info">
								광고 도배글 신고 
								<%--본문에 img가 있을 때 표시함 --%>
								<!-- <i class="fas fa-file-image img-exist" style="color:darkgray;"></i> -->
								<%--${info.replyCount} : 댓글 수 변수 vo에 넣기--%>
								<span class="reply-count">(2)</span>	
							</td>
							<td>애옹이</td>
							<td>2021-01-10</td>
							<td>12</td>
						</tr>
						<tr>
							<td>274</td>
							<td class="cd-color">문의</td>
							<td class="infoTitle">
								입양 글 작성 관련 문의 
								<%--본문에 img가 있을 때 표시함 --%>
								<!-- <i class="fas fa-file-image img-exist" style="color:darkgray;"></i> -->
								<%--${info.replyCount} : 댓글 수 변수 vo에 넣기--%>
								<span class="reply-count">(2)</span>	
							</td>
							<td>애옹이</td>
							<td>2021-01-09</td>
							<td>4</td>
						</tr>
						<tr>
							<td>240</td>
							<td class="cd-color">문의</td>
							<td class="infoTitle">
								공지 이벤트 문의
								<%--본문에 img가 있을 때 표시함 --%>
								<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>
								<%--${info.replyCount} : 댓글 수 변수 vo에 넣기--%>
								<span class="reply-count">(1)</span>	
							</td>
							<td>애옹이</td>
							<td>2021-01-08</td>
							<td>11</td>
						</tr>
					</c:forEach>
							<tr>
									<td>230</td>
									<td class="cd-color">신고</td>
									<td class="infoTitle">
										불량 회원 신고
										<%--본문에 img가 있을 때 표시함 --%>
										<!-- <i class="fas fa-file-image img-exist" style="color:darkgray;"></i> -->
										<%--${info.replyCount} : 댓글 수 변수 vo에 넣기--%>
										<!-- <span class="reply-count">[43]</span>	 -->
									</td>
									<td>애옹이</td>
									<td>2021-01-01</td>
									<td>9</td>
							</tr>
				
				<!-- db연결 후 -->
				<!-- 고객센터 게시판에 게시글이 없을 때 -->
				<%-- <c:choose> 
					<c:when test="${empty fList}">  
						<tr>
							<td colspan="6">존재하는 게시글이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise> --%>  <!-- 게시글이 있을 때 모두 출력--> 
					
						<%-- 본인 글만 조회 (이렇게 하는거 맞나?) --%>
						<%-- <c:if test="${!empty loginMember && (board.memberId == loginMember.memberId)}"> --%>		
							<%-- <c:forEach var="info" items="${fList}">
								<tr>
									<td>${info.infoBrdNo}</td>
									<td class="cd-color">${info.infoCode}</td>
									<td class="infoTitle">
										${info.infoTitle}
										
										<!-- 본문에 img가 있을 때 표시함 c:if 사용? -->
										<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>		
																			
										<!-- 댓글 수 : vo 필드에 replyCount 넣기 -->
										<span class="reply-count">[${info.replyCount}]</span>	
									</td>
									<td class="infoWriter">${info.memId}</td>
									<td> 
									<!-- 날짜 출력 모양 지정 변수 선언 -->
										<!-- *조건 확인용 오늘 날짜 -->
										<fmt:formatDate var="today" 
											value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/> 
										<!-- *조회한 글의 작성 날짜 모양-->
									<fmt:formatDate var="createDate" 
											value="${info.brdCrtDt}" pattern="yyyy-MM-dd"/>
										
										<c:choose> 	
											작성일과 오늘이 아닐 경우 : yyyy-MM-dd형태의 createDate 출력
											<c:when test="${createDate != today}">
													${createDate}
											</c:when> 
											
											<!-- 작성일이 오늘일 경우 : boardCreateDate를 HH:mm으로 시간만 출력 -->
										<c:otherwise>
												<fmt:formatDate 
													value="${info.brdCrtDt}" pattern="HH:mm"/>
											</c:otherwise>
										</c:choose>	
									</td>
									<td>${info.readCount}</td>
								</tr>
							</c:forEach>
							</c:if>	
						
						<%-- 로그인된 계정이 관리자 등급인 경우 --%>	
						<%-- <c:if test="${!empty loginMember && loginMember.memberGrade == 0}"> --%>		
							<%-- <c:forEach var="info" items="${fList}">
								<tr>
									<td>${info.infoBrdNo}</td>
									<td class="cd-color">${info.infoCode}</td>
									<td class="infoTitle">
										${info.infoTitle}
										
										<!-- 본문에 img가 있을 때 표시함 c:if 사용? -->
										<i class="fas fa-file-image img-exist" style="color:darkgray;"></i>		
																			
										<!-- 댓글 수 : vo 필드에 replyCount 넣기 -->
										<span class="reply-count">[${info.replyCount}]</span>	
									</td>
									<td class="infoWriter">${info.memId}</td>
									<td> 
									<!-- 날짜 출력 모양 지정 변수 선언 -->
										<!-- *조건 확인용 오늘 날짜 -->
										<fmt:formatDate var="today" 
											value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/> 
										<!-- *조회한 글의 작성 날짜 모양-->
									<fmt:formatDate var="createDate" 
											value="${info.brdCrtDt}" pattern="yyyy-MM-dd"/>
										
										<c:choose> 	
											작성일과 오늘이 아닐 경우 : yyyy-MM-dd형태의 createDate 출력
											<c:when test="${createDate != today}">
													${createDate}
											</c:when> 
											
											<!-- 작성일이 오늘일 경우 : boardCreateDate를 HH:mm으로 시간만 출력 -->
										<c:otherwise>
												<fmt:formatDate 
													value="${info.brdCrtDt}" pattern="HH:mm"/>
											</c:otherwise>
										</c:choose>	
									</td>
									<td>${info.readCount}</td>
								</tr>
							</c:forEach>
							</c:if>					 	
					 </c:otherwise>
					</c:choose> --%>
					
				</tbody>
			</table>
		</div>


			<%-- 로그인이 되어있는 경우 --%>
	<%--c:if test="${!empty loginMember}" --%>
				<button type="button" class="btn btn-teal float-right" id="insertBtn" 
							onclick="location.href='${contextPath}/information/informationInsertForm.do?${tpStr}'">글쓰기</button>
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
				<form action="${contextPath}/informationSearch.do?${tpStr}" method="GET" class="text-center " 
																																id="searchForm">
					<!-- cd -->
					<select name="cd" class="form-control sf-margin" style="width: 110px; display: inline-block;">
						<option value="infoQuest">문의</option>
						<option value="infoReport">신고</option>
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
			
				$(".info").on("click", function(){
					
					var url = "${contextPath}/information/list.do?${tpStr}&cp=1&cd=" + infoCode;
									//cp(페이지), tp(게시판타입 b1 b2 b3 b4 adminMem b5 mypage), cd(카테고리)
									//tpStr = tp=_
					
					location.href = url;
				});

					//글 목록의 카테고리 색상 정하는 즉시 실행 함수: 해당 td의 클래스 ="cd-color 
				(function(){
					$(".cd-color").each(function(index, item){
						if($(item).text() == "문의" ){
							$(item).css("color", "green");
						} else if($(item).text() == "신고"){
							$(item).css("color", "blue");
						}
					});
				})();
		});
		

		// 게시글 상세보기 기능 (jquery를 통해 작업)
		$("#list-table td").on("click", function(){
			//게시글 번호 얻어오기
			var infoBrdNo = $(this).parent().children().eq(0).text();
			
			var url = "${contextPath}/information/view.do?${tpCdStr}&cp=${pInfo.currentPage}&no=" + infoBrdNo; 
																	//cp(페이지), tp(게시판타입 b4 자유게시판), no 글번호
																	//tpCdStr : "tp=_&cd=_"
			location.href = url;     
		});
		
		// 고객센터 검색 jsp에서 사용
		/* 		//검색 내용이 있을 경우 검색창에 해당 내용을 작성해두는 즉시 실행 함수
				(function(){
					//cd:자유카테고리(검색창에서 설정): INFO_CODE
					//파라미터 중 cd, sk, sv가 있을 경우 변수가 저장됨 → 출력
					//파라미터 중 cd, sk, sv가 없을 경우 빈문자열로 출력됨(el은 null을 인식 안함)
					var infoCode = "${param.cd}";
					var searchKey = "${param.sk}";
					var searchValue = "${param.sv}";
					
					//검색창 select 카테고리에 검색한 카테고리로 selected하기
					$("select[name=cd] > option").each(function(index, item){
						//index : 현재 접근 중인 요소의 인덱스
						//item : 현재 접근 중인 요소
						
						//검색조건일 경우 selected 추가
						if($(item).val() == infoCode){
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
				})(); */
		
	</script>
</body>
</html>
