<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>semi Project</title>

<style>
   
</style>

</head>
<body>

<!-- 공지사항 박스 -->
				<div class=" main-down">
            <div class="col-md-6 content main-left">
            
                <table class="table" id="main-notice">
                <tr>
                    <th class="main-label">
                        <span class="main-title">공지사항</span>
                    </th>
                    <th class="main-label"></th>
                    <th class="main-label"> 
                        <a class="main-href" href="${contextPath}/notice/list.do?tp=b1">
                            <span>+</span>
                        </a>
                    </th>
                </tr>
                
               <%--  <c:choose>
                    <!-- 리스트가 없을 때(조회된 공지사항 글이 없을 때) -->
                    <c:when test="${empty noticeList}"> --%>
                   <%-- <tr>
                            <th class="empty-main" colspan="3">존재하는 공지사항이 없습니다.</th>
                        </tr> --%>
               <%-- </c:when>
                    <!-- 리스트가 있을 때(조회된 공지사항 글이 존재할 때)
                            list의 요소를 하나씩 접근해 한 행으로 만듦(게시글목록)-->
                    <c:otherwise> --%>
                        <c:forEach var="i" begin="1" end="3">
                            <tr>
                            <!-- 게시글로 링크는 수업 후 확정 -->
                                <td class="notice-title">공지사항 ${i}</td> <%-- ${notice.noticeTitle} --%>
                                <td class="notice-date">2021.01.03</td> 
                                <%-- <fmt:formatDate var="createDate" 
																		value="${notice.noticeCreateDate}" pattern="yyyy-MM-dd"/> --%>
                            		<td class="notice-no sr-only">글번호</td> <%-- 안보임 ${notice.noticeNo} --%>
                            </tr>
                        </c:forEach>
                  <%--   </c:otherwise>
                </c:choose> --%>
                </table>

            </div>
            
            <!-- 입양 후기 박스 -->
            <div class="col-md-6 content main-right">
                <table class="table" id="main-review">
                    <tr>
                        <th class="main-label">
                            <span class="main-title">입양 후기</span>
                        </th>
                        <th class="main-label"></th>
                        <th class="main-label"> 
                        	<%-- <c:if test="${!empty loginMember}"> --%>
                            <a class="main-href" href="${contextPath}/review/list.do?tp=b3">
                                <span>+</span>
                            </a>
                          <%-- </c:if> --%>
                        </th>
                    </tr>
                
               <%--  <c:choose>
                    <c:when test="${empty reviewList}">
                    <tr>
                        <th class="empty-main" colspan="3">존재하는 입양후기가 없습니다.</th>
                    </tr>
                    </c:when>

                    <c:otherwise> --%>
                        <c:forEach var="i" begin="0" end="2">
                            <tr>
	                            <td colspan="2" class="review-card">
	                                <div class="card mb-3 row" style="max-width: 1000px;">
                                       <div class="review-thumb">
                                           <img src="https://i2.pickpik.com/photos/714/11/745/golden-retriever-animal-shelter-dog-pension-kennels-preview.jpg"
                                            alt="입양후기 썸네일">
                                       </div>
                                       <div class="review-text">
                                            <h5 class="card-title">뛰어노는 인절미</h5>
                                            	<%--${review.reviewTitle}--%>
                                            <p class="card-date">
                                            		입양 날짜 : 2021.01.04.</p>
                                            	<%--<fmt:formatDate var="createDate" 
																						value="${review.adoptDate}" pattern="yyyy-MM-dd"/> --%>
                                       </div>
	                                </div>
	                            </td>
	                            <td class="sr-only review-no">글번호</td> <%-- 안보임 ${review.reviewNo} --%>
                            </tr>
                        </c:forEach>
                    <%-- </c:otherwise>
                </c:choose>  --%>
                </table>
            
            </div>
				</div>
	<script>
	   /* 메인 공지사항 박스  */
	   
    /* 클릭한 공지사항 글 조회로 이동 */
    $("#main-notice td").on("click", function(){
    	
				var noticeNo = $(this).parent().children().eq(2).text();
				/* 글번호 = 부모의 모든 자식들 중에서 인덱스 2번째의 내용 */
				console.log(noticeNo);
											
				//얻어온 공지사항 글번호를 쿼리스트링으로 작성하여 상세조회 요청
				location.href = "${contextPath}/notice/view.do?tp=b1&no=" + noticeNo;
				/* location : 주소창과 관련된 객체. */
    	
		});
		
		

		/* 메인 입양 후기 박스  */
		
    /* 클릭한 공지사항 글 조회로 이동 */
    $("#main-review td").on("click", function(){
    	
/*     	if(${loginMember} != null){ */
					var	reviewNo = $(this).parent().children().eq(1).text();
					/* 글번호 = 부모의 모든 자식들 중에서 인덱스 1번째의 내용 */
					console.log(reviewNo);
												
					//얻어온 공지사항 글번호를 쿼리스트링으로 작성하여 상세조회 요청
					location.href = "${contextPath}/review/view.do?tp=b3&no=" + reviewNo;
				/* location : 주소창과 관련된 객체. */
/*     	} else {
    		swal({icon : "warning", title:"로그인 후 사용해주세요."});
    	} */
		});
		
		/* 입양 후기 리스트 가져오는 함수 */
		
		function selectReviewList(){
			$.ajax ({
				url: "${contextPath}/mainpage/selectReview.do",
				data: {"brdType" : "b3"}, //입양 후기 게시판 타입을 보내기
				type: "post",
				dataType : "JSON",
				success : function(rList, iList){
					
				},
				error : function(){
					console.log("입양 후기 최신글 조회 실패");
				}
				
			});
		}
		
			
	</script>

</body>
</html>