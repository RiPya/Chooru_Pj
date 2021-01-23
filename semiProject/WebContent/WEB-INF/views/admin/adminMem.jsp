<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 관리</title>

<style>

/* 회원 관리 라인 */
.header-mgt{
   width: 100%;
   margin-bottom: 2rem;
}
.form-mgt {
   font-size: 20px;
   font-weight: bolder;
   padding-right: 20px;
} 

.form-hr > hr{
   width: 85%;
   height: 8px;
   margin-bottom: .1rem;
   display: inline-block;
   background-color: teal;
   border-radius: 1rem;
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

#searchForm > *, #gradeForm > * {
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

/* 테이블 텍스트 중앙 정렬 */
tr, td {
	text-align: center;
}

</style>

</head>
<body>

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
		<div class="header-mgt">
			<span class="form-mgt">회원 관리</span>
			<span class="form-hr" style="position:absolute; width:90%;"><hr style="width:1000px;"></span>
		</div>
		
				<!-- 검색창 : type:게시판코드(자유는 b4), cd:자유카테고리(검색창에서 설정)-->
				<!-- 게시판코드를 파라미터로 넘겨야 할까? -->
			<div class="my-4">
				<form action="${contextPath}/adminMemSearch.do?${tpStr}" method="GET" class="text-right" 
																																id="searchForm">
					<!-- cd -->
					<select name="cd" class="form-control sf-margin" style="width: 110px; display: inline-block;">
						<option value="memAll">전체</option>
						<option value="memId">아이디</option>
						<option value="memNick">닉네임</option>
					</select> 
					
					<input type="text" name="sv" class="form-control sf-margin" 
							placeholder="검색어를 입력하세요." style="width: 25%; display: inline-block;">
							
					<button class="form-control btn btn-teal" style="width: 70px; display: inline-block;">
						<i class="fas fa-search" id="search-in-icon"></i><!--찾기아이콘-->
					</button>

				</form>
			</div>
			
		<div class="list-wrapper">
			<table class="table" id="list-table">
				<thead>
					<tr>
						<th>회원번호</th>
						<th>아이디</th>
						<th>이름</th>
						<th>닉네임</th>
						<th>등급</th>
						<th>가입일</th>
						<th>전화번호</th>
						<th>이메일</th>
						<td>
							<input class="form-check-input" type="checkbox" 
								value="" id="checkAll">
						</td>
					</tr>
				</thead>
				
				<%-- 게시글 목록 출력 --%>
				<tbody>
<%-- 				확인용
					<c:forEach var="info" begin="1" end="3">
								<tr>
									<td>0</td>
									<td>mgr01</td>
									<td>김관리</td>
									<td>관리자</td>
									<td>0</td>
									<td>2021-01-10</td>
									<td>010-1234-5678</td>
									<td>mgr01@naver.com</td>
									<td>
										<input class="form-check-input" type="checkbox" 
										value="" name="check">
									</td>
								</tr>
								<tr>
									<td>0</td>
									<td>mgr01</td>
									<td>김관리</td>
									<td>관리자</td>
									<td>0</td>
									<td>2021-01-10</td>
									<td>010-1234-5678</td>
									<td>mgr01@naver.com</td>
									<td>
										<input class="form-check-input" type="checkbox" 
										value="" name="check">
									</td>
								</tr>
								<tr>
									<td>0</td>
									<td>mgr01</td>
									<td>김관리</td>
									<td>관리자</td>
									<td>0</td>
									<td>2021-01-10</td>
									<td>010-1234-5678</td>
									<td>mgr01@naver.com</td>
									<td>
										<input class="form-check-input" type="checkbox" 
										value="" name="check">
									</td>
								</tr>
					</c:forEach>
					<tr>
						<td>0</td>
						<td>mgr01</td>
						<td>김관리</td>
						<td>관리자</td>
						<td>0</td>
						<td>2021-01-10</td>
						<td>010-1234-5678</td>
						<td>mgr01@naver.com</td>
						
					</tr> --%>
					
				<!-- db연결 후 -->
				<!-- 회원이 없을 때 -->
				<c:choose>
					<c:when test="${empty mList}">  
						<tr>
							<td colspan="6">존재하는 회원이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise> <!-- 회원이 있을 때 모두 출력-->
						<c:forEach var="member" items="${mList}">
							<tr>
								<td>${member.memNo}</td>
								<td>${member.memId}</td>
								<td>${member.nickName}</td>
								<td>${member.memNm}</td>
								<td>${member.grade}</td>
								<td>${member.enrollDate}</td>
								<td>${member.phone}</td>
								<td>${member.email}</td>
								<td>
									<input class="form-check-input" type="checkbox" 
										value="${member.memNo}" name="check">
								</td>
								
								<!-- 날짜 출력 모양 지정 변수 선언 -->
									<!-- *조건 확인용 날짜 -->
									<fmt:formatDate var="today" 
										value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/>
									<!-- *가입한 회원의  날짜 모양-->
								<fmt:formatDate var="enrollDate" 
										value="${member.enrollDate}" pattern="yyyy-MM-dd"/> 
										
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
					
				</tbody>
			</table>
				<div class="my-4"> 				
				<form action="#" method="GET" class="text-right" id="gradeForm">
					<select name="grade" class="form-control sf-margin" id="select-grade" style="width: 80px; display: inline-block;">
						<option value="grade">등급</option>
						<option class="memGrade" value="0">0</option>
						<option class="memGrade" value="1">1</option>
						<option class="memGrade" value="8">8</option>
						<option class="memGrade" value="9">9</option>
					</select> 	
					<button id="changeGradeBtn" class="form-control btn btn-teal" style="width: 80px; display: inline-block;">변경</button>
				</form>
			</div>
		</div>

	
			<%-- 파라미터의 sk(searchKey)와 sv(searchValue)가 비어있지 않을 때  == 검색 후 페이징바 클릭 --%>
			<c:choose>
				<c:when test="${!empty param.sk && !empty param.sv}">
				 	<c:url var="pageUrl" value="search/noticeSearch.do"/>
				 	
				 	<%-- 쿼리스트링 내용을 변수에 저장 --%>
				 	<c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}"/>
				</c:when>
				
					<%-- 비어있을 때 --%>
				<c:otherwise>
					<c:url var="pageUrl" value="/notice/list.do"/>
				</c:otherwise>
			</c:choose>
			
			<c:if test="${!empty param.cd}">
				<c:set var="cdStr" value="&cd=${param.cd}"/>
			</c:if>
			
			
			<!-- <<, >> 화살표에 들어갈 주소를 변수로 생성(쿼리스트링 사용) -->
			<c:set var="firstPage" value="${pageUrl}?${tpStr}&cp=1${cdStr}${searchStr}"/>
			<c:set var="lastPage" value="${pageUrl}?${tpStr}&cp=${pInfo.maxPage}${cdStr}${searchStr}"/>
			
			<%-- EL을 이용한 숫자 연산의 단점 : 연산이 자료형에 영향을 받지 않음
				<fmt:parseNumber> : 숫자 형태를 지정하여 변수 선언
				integerOnly="true" : 정수로만 숫자를 표현(소수점 버림)
			--%>
			<%-- pInfo.pageSize : 10 --%>
			<!-- < 화살표를 눌렀을 때 이전 페이징의 endPage가 prev가 되도록 -->
			<%-- 현재페이지가 29라면 c1==2, prev==20 --%>
			<fmt:parseNumber var="c1" value="${(pInfo.currentPage - 1) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="prev" value="${c1 * pInfo.pageSize}" integerOnly="true"/>
			<c:set var="prevPage" value="${pageUrl}?${tpStr}&cp=${prev}${cdStr}${searchStr}"/>
			
			<!-- > 화살표를 눌렀을 때 다음 페이징의 startPage가 next가 되도록 -->
			<%-- 현재페이지가 23이라면 c2==3, next==31 --%>
			<fmt:parseNumber var="c2" value="${(pInfo.currentPage + 9) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="next" value="${c2 * pInfo.pageSize + 1}" integerOnly="true"/>
			<c:set var="nextPage" value="${pageUrl}?${tpStr}&cp=${next}${cdStr}${searchStr}"/>
						
			
			<div class="my-5">
				<ul class="pagination">
				
					<%-- 현재 페이지가 10페이지 초과인 경우 --%>
					<c:if test="${pInfo.currentPage > pInfo.pageSize}">				
						<li> <!-- 첫 페이지로 이동(<<) -->
							<a class="page-link" href="#">&lt;&lt;</a>
						</li>
						<li> <!-- 이전 페이지로 이동(<) -->
							<a class="page-link" href="#">&lt;</a>
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
								<a class="page-link" href="${pageUrl}?${tpStr}&cp=${page}${cdStr}${searchStr}">${page}</a>
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
	
	<!-- footer -->
	<jsp:include page="../common/footer.jsp"></jsp:include>

	<script>	
	
	$("#changeGradeBtn").on("click", function(){
        
        var list = []; //객체
        var grade = $("select[name=grade] option:selected").val();
        
        $("input:checkbox[name='check']:checked").length
         
        $('input[type="checkbox"]:checked').each(function (index) {
					if($(this).val() != "on"){
          	list.push($(this).val());
          }   
        });
        
        //console.log(list);
        
				$.ajax({
				  url : "${contextPath}/admin/updateAdminMem.do",
				  data : {"numberList" : list.join(),
					  			"grade" : grade },                              
				  type : "post",
					success : function(result){
						if(result > 0){ 
          		swal({"icon" : "success", "title" : "등급이 변경 되었습니다"});
          		
          		location.reload();   
						}
  	      },
         error : function(){
            console.log("등급 변경 실패");
         }
      });   
				
     }); // ajax 끝

//---------------------------------------
	
		$(document).ready(function(){
			$(".memAdmin").on("click", function(){
				var url = "${contextPath}/adminMem/list.do?${tpStr}&cp=1&cd=" + memAdminCode;
								//cp(페이지), tp(게시판타입 b1 b2 b3 b4 adminMem b5 mypage), cd(카테고리)
								//tpStr = tp=_
				
				location.href = url;
			});
		});
	
		// --------------- 체크 박스 함수 ---------------
		// 전체 체크 및 해제
		$("#checkAll").on("click", function(){
	    if($("#checkAll").prop("checked")){
				$("input[name=check]").prop("checked",true);
      }else{
      	$("input[name=check]").prop("checked",false);
	    }
    });
	    
		// 한개의 체크박스 해제시 전체 선택도 해제
 	 	$("input[name=check]").on("click", function(){
				$("#checkAll").prop("checked", false);						
		});
		
		
		// 자유 검색 jsp에서 사용
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
