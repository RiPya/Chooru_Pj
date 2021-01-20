<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Semi Project</title>
    
    <%-- header에 넣으면 안됨 / css파일은 WEB-INF 밖에? --%>
		<link rel="stylesheet" href="css/mainStyle.css?ver=2.5" type="text/css">
</head>
<body>
    <!-- header.jsp include -->
    <jsp:include page="WEB-INF/views/common/header.jsp"></jsp:include>

    <!-- main-up : 입양/분양 carousel -->
    <div class="container-fluid">


        <div class="row">

            <div class="col-md-12 main-up">
                <div class="carousel slide" id="carousel-476497">
                    <ol class="carousel-indicators">
                        <li data-slide-to="0" data-target="#carousel-476497" class="active">
                        </li>
                        <li data-slide-to="1" data-target="#carousel-476497">
                        </li>
                        <li data-slide-to="2" data-target="#carousel-476497">
                        </li>
                    </ol>
                    <div class="carousel-inner">
                    <%-- 데이터 완성 시 이중 forEach 사용 방법....???????????
                    	<c:forEach var="i" begin="0" end="8" step="3">
													<div class="carousel-item main-adopt <c:if test="${i == 0}">active</c:if>">
														
														<c:forEach var="adoption" items="${adoptList}" begin="i" end="i+3">
															<div class="main-adopt-div">
                                <img class="d-block w-100" alt="${adoption.adoptBrdTitle}" 
                                															src="#" />
                                													<!--이미지 파일 리스트 가져와서 경로+이름 넣기-->
                                <span class="sr-only">${adoption.adoptBrdNo }</span>
                            	</div>
														</c:forEach>
														
													</div>
											</c:forEach> --%>
                    
                    		<!-- 예시용 -->
                    	<c:forEach var="i" begin="0" end="2">
                    	
                        <div class="carousel-item main-adopt active">
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="글제목" src="https://i1.pickpik.com/photos/135/49/188/cat-cute-yellow-animal-preview.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="글제목" src="https://i2.pickpik.com/photos/743/719/777/dogs-sun-summer-cute-preview.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="글제목" src="https://i2.pickpik.com/photos/714/11/745/golden-retriever-animal-shelter-dog-pension-kennels-preview.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                        </div>
                        
                      </c:forEach>
                        <!-- <div class="carousel-item main-adopt">
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="Carousel Bootstrap First" src="https://www.layoutit.com/img/sports-q-c-1600-500-2.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="Carousel Bootstrap First" src="https://www.layoutit.com/img/sports-q-c-1600-500-2.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="Carousel Bootstrap First" src="https://www.layoutit.com/img/sports-q-c-1600-500-2.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                        </div>
                        <div class="carousel-item main-adopt">
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="Carousel Bootstrap First" src="https://www.layoutit.com/img/sports-q-c-1600-500-3.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="Carousel Bootstrap First" src="https://www.layoutit.com/img/sports-q-c-1600-500-3.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>
                            <div class="main-adopt-div">
                                <img class="d-block w-100" alt="Carousel Bootstrap First" src="https://www.layoutit.com/img/sports-q-c-1600-500-3.jpg" />
                                <span class="sr-only">글번호</span>
                            </div>  
                        </div> -->
                       
                    </div> 
                    <a class="carousel-control-prev" href="#carousel-476497" data-slide="prev">
                        <span class="carousel-control-prev-icon" ></span> 
                        <span class="sr-only">Previous</span></a> 
                    
                    <a class="carousel-control-next" href="#carousel-476497" data-slide="next">
                        <span class="carousel-control-next-icon" ></span> 
                        <span class="sr-only">Next</span></a>
                </div>
                
            </div>

        </div>


        <div class="row">

            <!-- mainDown.jsp include : 공지사항, 입양 후기 박스 -->
            <jsp:include page="WEB-INF/views/common/mainDown.jsp"></jsp:include>

        </div>
 </div>
 

    

<!-- footer.jsp include -->
    <jsp:include page="WEB-INF/views/common/footer.jsp"></jsp:include>

<script>

    /* 메인 위 : 자동 carousel 움직임 + 클릭 */
    $(function(){
        $("#carousel-476497").carousel({
            interval : 4000,
            pause : "hover",
            wrap : true
            /* keyboard : true */
        });
    })
    
    /* adoption 이미지를 클릭시 상세 조회로 연결 */
    $(".main-adopt-div").on("click", function(){
    	
    /* 	if(${loginMember} != null) { */
	    	var adoptionNo = $(this).children().eq(1).text();
	    	/*글번호 : img의 다음 요소 <span>*/
	    	console.log(adoptionNo);
	    	//얻어온 공지사항 글번호를 쿼리스트링으로 작성하여 상세조회 요청
	    	location.href = "${contextPath}/adoption/view.do?tp=b2&no=" + adoptionNo;
    	/* } else {
				swal({icon : "warning", title:"로그인 후 사용해주세요."});
			} */
    });
    
    

    
</script>





</body>
</html>