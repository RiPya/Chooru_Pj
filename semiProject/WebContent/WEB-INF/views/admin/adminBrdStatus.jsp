<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 관리</title>
<style>



/*------목록 스타일---------*/
/* .brdTitle>img {
	width: 50px;
	height: 50px;
} */

/* 제목, 작성자 목록 구간 크기 지정 */
.brdTitle {
	min-width: 250px;
	text-align : left;
}

.brdWriter {
	min-width: 100px;
}

.brdDt{
	width : 160px;
}

.brdStatus	{
	width : 80px;
}

.brdCheck { width : 50px;}

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
	margin : 0 20%;
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

#searchForm > *, #gradeForm > *, #btn-select-grade, #select-grade {
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

		<div class="container my-5">
		<jsp:include page="../admin/adminMenu.jsp"></jsp:include>

			<div class="my-4">
				<form action="${contextPath}/search/brdStatus.do" method="GET" class="text-right" 
							onsubmit="return brdValidate();" id="searchForm">
					<!-- cd -->
					<select name="sk" id="selectSrch" class="form-control sf-margin" style="width: 90px; display: inline-block;">
						<option value="title">제목</option>
						<option value="memId">아이디</option>
						<option value="writer">닉네임</option>
					</select> 
					<input type="text" name="cd" value="adBrd" class="sr-only">					
					<input type="text" name="tp" value="adminMem" class="sr-only">					
					<input type="text" name="sv" class="form-control sf-margin" id="searchBrd"
							placeholder="검색어를 입력하세요." style="width: 25%; display: inline-block;">
							
					<button class="form-control btn btn-teal" style="width: 70px; display: inline-block;">
						<i class="fas fa-search" id="search-in-icon"></i><!--찾기아이콘-->
					</button>

				</form>
			</div>

			<div class="list-wrapper" id="gradeForm">
<%-- 				<form method="GET" class="text-right" id="gradeForm" 
						action="${contextPath}/admin/updateBrdSt.do"  onsubmit="return brdValidate();"> --%>
				<table class="table table-hover table-striped my-5" id="list-table">
					<thead>
						<tr>
							<th>글번호</th>
							<th>게시판</th>
							<th class="brdTitle">제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>상태</th>
							<td><input class="form-check-input" type="checkbox"	name="checkAll" id="checkAll"></td>
						</tr>
					</thead>

					<%-- 게시글 목록 출력 --%>
					<tbody>
						<!-- db연결 후 -->
						<!-- 자유게시판의 게시글이 없을 때 -->
					<c:choose>
						<c:when test="${empty bList}">  
							<tr>
								<td colspan="6" align="center">삭제 또는 블라인드 된 게시글이 없습니다.</td>
							</tr>
						</c:when>
						<c:otherwise> <!-- 게시글이 있을 때 모두 출력-->
							<c:forEach var="board" items="${bList}">
								<tr>
									<td width="90px">${board.brdNo}</td>
									<td class="brdType">${board.brdType}</td>
									<td class="brdTitle">
										${board.brdTitle}
									</td>
									<td class="brdWriter">${board.nickName}</td>
									<td class="brdDt"> 
											<fmt:formatDate value="${board.brdCrtDt}" pattern="yyyy-MM-dd HH:mm"/>
									</td>
									<td class="brdStatus">${board.brdStatus}</td>
									<td class="brdCheck">
										<input class="form-check-input" type="checkbox"	value="${board.brdNo}" name="check">
									</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>

					</tbody>
				</table>
				
				<div class="my-4 float-right">
					<select class="form-control sf-margin" name="status" id="select-grade" style="width: 130px; display: inline-block;">
						<option value="Y">일반(Y)</option>
						<option value="N">삭제(N)</option>
						<option value="B">블라인드(B)</option>
					</select> 	
					<button id="btn-select-grade" class="form-control btn btn-teal" style="width: 80px; display: inline-block;">변경</button>
				</div>
<%-- 				</form> --%>
			</div>


			
						
			<%---------------------- Pagination ----------------------%>
			<%-- boardList.jsp에서 복붙한 거 놔두면 오류나서 메모장 파일에 옮겨놓음 --%>
			<!-- cd가 없다면 href의 url 뒤에 -->
			
			
			<%-- 파라미터의 sk(searchKey)와 sv(searchValue)가 비어있지 않을 때
					 == 검색 후 페이징바 클릭 --%>
			<c:choose>
				<c:when test="${!empty param.sk && !empty param.sv}">
				 	<c:url var="pageUrl" value="search/brdStatus.do"/>
				 	
				 	<%-- 쿼리스트링 내용을 변수에 저장 --%>
				 	<c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}"/>
				</c:when>
				
					<%-- 비어있을 때 --%>
				<c:otherwise>
					<c:url var="pageUrl" value="/admin/adminBrd.do"/>
				</c:otherwise>
			</c:choose>
			
			<c:set var="cdStr" value="&cd=${param.cd}"/>
			
			
			
			<!-- <<, >> 화살표에 들어갈 주소를 변수로 생성(쿼리스트링 사용) -->
			<c:set var="firstPage" value="${pageUrl}?${tpCdStr}&cp=1${searchStr}"/>
			<c:set var="lastPage" value="${pageUrl}?${tpCdStr}&cp=${pInfo.maxPage}${searchStr}"/>
			
			<%-- EL을 이용한 숫자 연산의 단점 : 연산이 자료형에 영향을 받지 않음
				<fmt:parseNumber> : 숫자 형태를 지정하여 변수 선언
				integerOnly="true" : 정수로만 숫자를 표현(소수점 버림)
			--%>
			<%-- pInfo.pageSize : 10 --%>
			<!-- < 화살표를 눌렀을 때 이전 페이징의 endPage가 prev가 되도록 -->
			<%-- 현재페이지가 29라면 c1==2, prev==20 --%>
			<fmt:parseNumber var="c1" value="${(pInfo.currentPage - 1) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="prev" value="${c1 * pInfo.pageSize}" integerOnly="true"/>
			<c:set var="prevPage" value="${pageUrl}?${tpCdStr}&cp=${prev}${searchStr}"/>
			
			<!-- > 화살표를 눌렀을 때 다음 페이징의 startPage가 next가 되도록 -->
			<%-- 현재페이지가 23이라면 c2==3, next==31 --%>
			<fmt:parseNumber var="c2" value="${(pInfo.currentPage + 9) / pInfo.pageSize}" integerOnly="true"/>
			<fmt:parseNumber var="next" value="${c2 * pInfo.pageSize + 1}" integerOnly="true"/>
			<c:set var="nextPage" value="${pageUrl}?${tpCdStr}&cp=${next}${searchStr}"/>
						
			
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
								<a class="page-link" href="${pageUrl}?${tpCdStr}&cp=${page}${searchStr}">${page}</a>
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
	
			
			//게시글 상태 바꾸기
			$("#btn-select-grade").on("click", function(){
				
	        var check = []; //객체
	        var status = $("select[name=status] option:selected").val();
	        
	        $("input:checkbox[name=check]:checked").length
	         
	        $('input[type=checkbox]:checked').each(function (index, item) {
						if($(item).val() != "on"){
							check.push($(item).val());
	          }   
	        });
	        
	        //console.log("check: "+check);
	        //console.log("status: "+status);
	        
	        var code = "adBrd";
	        
					if( check.length < 1 ){
						alert("선택된 글이 없습니다.");
						return false;
					} else {
						if(confirm("글 " + check.length +"개를 상태를 변경하시겠습니까?")){
							
				    	$.ajax({
							  url : "${contextPath}/admin/updateBrdSt.do",
							  data : {"checkList" : check.join(),
								  			"status" : status,
								  			"cd" : code},                             
							  type : "post",
								success : function(result){
									if(result > 0){ 
			          		location.reload(); 
			          		alert("게시글 상태 변경이 성공했습니다.");			          		
							// swal({"icon" : "success", "title" : "게시글 상태 변경이 성공했습니다."});
									}
			  	      },
			        	error : function(){
			        	    console.log("게시글 상태 변경 실패");
			         	}
			      	});   
						} else {
							$("#btn-select-grade").blur();
							return;
						}
					}
			});
			
		//검색 시 게시글 검색 내용 유지
		//검색 내용이 있을 경우 검색창에 해당 내용을 작성해두는 즉시 실행 함수
		(function(){
			var searchKey = "${param.sk}";
			var searchValue = "${param.sv}";
			
			//.each문 반복 접근문
			//검색창 select의 option을 반복 접근
			$("select[name=sk] > option").each(function(index, item){
				//검색조건일 경우 selected 추가
				if($(item).val() == searchKey){
					$(item).prop("selected", true);
				}
			});
			//검색창에 검색어 출력
			$("#searchBrd").val(searchValue);
		})();
		
		
		//검색 validate
		function brdValidate(){
			if($("#searchBrd").val().trim().length == 0){
				alert("검색어를 입력하세요.");
				$("#searchBrd").focus();
				return false;
			}
		}
		
	</script>

</body>
</html>