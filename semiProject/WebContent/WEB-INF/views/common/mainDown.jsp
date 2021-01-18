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
                		<!-- 입양 후기 최신 글 3개 추가됨 (ajax) -->
                </table>
            
            </div>
				</div>
	<script>
	   /* 메인 공지사항 박스  */
	   
    /* 클릭한 공지사항 글 조회로 이동 */
    function clickNotice(brdNo){
    	
				console.log(brdNo);
											
				//얻어온 공지사항 글번호를 쿼리스트링으로 작성하여 상세조회 요청
				location.href = "${contextPath}/notice/view.do?tp=b1&cp=1&no=" + brdNo;
				/* location : 주소창과 관련된 객체. */
    	
		}
		
		

		/* 메인 입양 후기 박스  */
		
    /* 클릭한 입양 후기 글 조회로 이동하게 하는 함수 */
 		function clickReview(brdNo){
					console.log(brdNo);
												
					//얻어온 공지사항 글번호를 쿼리스트링으로 작성하여 상세조회 요청
					location.href = "${contextPath}/review/view.do?tp=b3&cp=1&no=" + brdNo;
		}
		
		/* 입양 후기 리스트 가져오는 함수 */
		
		function selectReviewList(){
			$.ajax ({
				url: "${contextPath}/main/selectReview.do",
				/* data: {"brdType" : "b3"}, */ //입양 후기 게시판 타입을 보내기
				type: "post",
				dataType : "JSON",
				success : function(map){
					
					var rList = map.rList;
					var iList = map.iList;
					
					if(rList == null){ //rList가 없을 때
					/* <tr>
                   <th class="empty-main" colspan="3">존재하는 입양후기가 없습니다.</th>
               </tr> */
						var tr = $("<tr>");
            var th = $("<th>").addClass("empty-main").attr("colspan", "3").text("존재하는 입양 후기가 없습니다.");
            tr.append(th);
            $("#main-review").append(tr);//추가
					}
					
					else { //rList가 있을 때 == 입양 후기 게시글이 있을 때
						$.each(rList, function(index, item){
								<%--<tr>
									 		<td colspan="3" class="review-card">
	                        <div class="card mb-3 row" style="max-width: 1000px;">
	                           <div class="review-thumb">
	                               <img src="" alt="입양 후기">
	                           </div>
	                           <div class="review-text">
	                                <h5 class="card-title">item.brdTitle</h5>
	                                <p class="card-date">item.adtDate</p>
	                           </div>
	                        </div>
	                        <div class="sr-only review-no">item.brdNo</div>
	                    </td> 
	                   </tr>--%>
	              
	            var tr = $("<tr>");
							
							var td = $("<td>").addClass("review-card").attr("colspan", "3")
										.attr("onclick", "clickReview("+item.brdNo+")" );//td
							
							var card = $("<div>").addClass("card mb-3 row").css("max-width", "1000px");
												
							
							var cardThumb = $("<div>").addClass("review-thumb");
							
							var flag = true;//로고 이미지 삽입 필요 여부 확인용(true:이미지 없음으로 로고 삽입, false:이미지 있음)
							
							$.each(iList, function(i, image){
								if(item.brdNo == image.brdNo){
									var url;
									
									if(image.filePath == "../resources/uploadImages/"){
										url = "resources/uploadImages/" + image.fileName;
									} else {
										url = image.filePath + image.fileName;
									}
										console.log(url);
									var thumbnail = $("<img>").attr("alt", "입양 후기").attr("src", url);
									cardThumb.append(thumbnail);	
									flag = false;//이미지 있음 → 로고 대체 필요 없음
								} 
							});
							
							if(flag){ //이미지 없으면 로고 삽입
								var url = "${pageContext.request.contextPath}/css/yamung_logo_2.png";
								var thumbnail = $("<img>").attr("alt", "입양 후기").attr("src", url);
								cardThumb.append(thumbnail);	
								flag = false;//이미지 있음 → 로고 대체 필요 없음
							}
							
							var cardText = $("<div>").addClass("review-text");
							
							var cardTitle = $("<h5>").addClass("card-title").text(item.brdTitle);
							var cardDate = $("<p>").addClass("card-date").text("입양 날짜: "+ item.adtDate);
							
							cardText.append(cardTitle).append(cardDate);
							
							card.append(cardThumb).append(cardText);
							
							var reviewNo = $("<div>").addClass("sr-only review-no").text(item.brdNo);
							
							td.append(card).append(reviewNo);
							
							tr.append(td)
							
							//입양 게시글 추가
							$("#main-review").append(tr);
						});
					}
				},
				error : function(){
					console.log("입양 후기 최신글 조회 실패");
				}
				
			});
		}
		
		//ready 함수
		$(document).ready(function(){
			//입양 후기 가져오는 함수
			selectReviewList();
			
			
		});
		
			
	</script>

</body>
</html>