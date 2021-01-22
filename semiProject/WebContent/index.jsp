<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Semi Project</title>
    
    <%-- header에 넣으면 안됨 / css파일은 WEB-INF 밖에? --%>
		<link rel="stylesheet" href="css/mainStyle.css?ver=2.6" type="text/css">
</head>
<body>
    <!-- header.jsp include -->
    <jsp:include page="WEB-INF/views/common/header.jsp"></jsp:include>

    <!-- main-up : 입양/분양 carousel -->
    <div class="container-fluid">


        <div class="row">

            <div class="col-md-12 main-up">
                <div class="carousel slide" id="carouselBox">
                    <ol class="carousel-indicators">
                        <li data-slide-to="0" data-target="#carouselBox" class="active">
                        </li>
                        <li data-slide-to="1" data-target="#carouselBox">
                        </li>
                        <li data-slide-to="2" data-target="#carouselBox">
                        </li>
                    </ol>
                    <div class="carousel-inner">
                    <!-- 여기에 adoption 목록 carousel 넣기 -->
                    	<div class="carousel-item main-adopt active" id="carousel1"></div>
                    	<div class="carousel-item main-adopt" id="carousel2"></div>
                    	<div class="carousel-item main-adopt" id="carousel3"></div>
                    
                    </div> 
                    <a class="carousel-control-prev" href="#carouselBox" data-slide="prev">
                        <span class="carousel-control-prev-icon" ></span> 
                        <span class="sr-only">Previous</span></a> 
                    
                    <a class="carousel-control-next" href="#carouselBox" data-slide="next">
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
        $("#carouselBox").carousel({
            interval : 4000,
            pause : "hover",
            wrap : true
            /* keyboard : true */
        });
    });
    
    /* adoption 이미지를 클릭시 상세 조회로 연결하는 함수*/
 		function clickAdopt(brdNo){
	    	location.href = "${contextPath}/adoption/view.do?tp=b2&cp=1&no=" + brdNo;
    };
    
    
		/* adoption 목록을 가져오는 함수 */
		function selectAdoptList(){
			$.ajax ({
				url: "${contextPath}/main/selectAdoption.do",
				/* data: {"brdType" : "b2"}, */ //입양/분양 게시판 타입을 보내기
				type: "post",
				dataType : "JSON",
				
				success : function(map){
					
					var aList = map.aList;
					var iList = map.iList;
					
					//carousel div 내부 리셋
					for(var i=1; i<=3; i++){
						$("#carousel"+i).html();
					}
					
					if(aList.length == 0){ //aList가 없을 때
						
						for(var i=1; i<=3; i++){
								var noneAdopt = $("<div>").addClass("noneAdopt").text("아직 입양/분양 게시글이 없습니다.");
								$("#carousel"+i).append(noneAdopt);
						} 
						
					}//aList가 없을 때 if문 끝
					
					else {//aList가 있을 때
						<%-- 아래 div(main-adopt)
									<div class="carousel-item main-adopt <c:if test="${i == 0}">active</c:if>">
											<div class="main-adopt-div">
	                       <img class="d-block w-100" alt="${adoption.adoptBrdTitle}" src="#" />
	                   	</div>
											<div class="main-adopt-div">
	                       <img class="d-block w-100" alt="${adoption.adoptBrdTitle}" src="#" />
	                   	</div>
	                   	<div class="main-adopt-div">
	                       <img class="d-block w-100" alt="${adoption.adoptBrdTitle}" src="#" />
	                   	</div>
									</div>
							 --%>	
							 
							//carousel div 번호 추가를 위한 변수 
 							var count = 1;
							
							$.each(aList, function(index, item){
								
								var adopt = $("<div>").addClass("main-adopt-div")
															.attr("onclick", "clickAdopt("+ item.brdNo +")");

									var flag = true;//로고 이미지 삽입 필요 여부 확인용
									
									$.each(iList, function(i, image){//이미지 있는지 확인하는 each함수
											if(item.brdNo == image.brdNo){
												var url;
												var img = $("<img>").addClass("d-block w-100 adoptThumb");
												
												if(image.filePath == "../resources/uploadImages/"){
													url = "${pageContext.request.contextPath}/resources/uploadImages/" + image.fileName;
												} else {
													url = image.filePath + image.fileName;
												}
												console.log(url);
												
												img.attr("src", url).attr("alt", "[입양/분양] " + item.brdTitle);
												adopt.append(img);	
												
												flag = false;//이미지 있음 → 로고 대체 필요 없음
											}
									});
								
									if(flag){ //이미지 없으면 로고 삽입
										var url = "${pageContext.request.contextPath}/css/yamung_logo.png";
										var img = $("<img>").addClass("d-block w-100").attr("src", url);
										
										adopt.append(img);	
									}
									
									$("#carousel"+count).append(adopt);
									
									if(index == 3*count-1) count++;									
									
							}); //carousel 만드는 for문 끝
							
							for(var i=1; i<=3; i++){
								if($("#carousel"+i).html().trim().length == 0){
									var noneAdopt = $("<div>").addClass("noneAdopt").text("입양/분양 글 보러 오세요.");
									$("#carousel"+i).append(noneAdopt);
								}
							} 
							
						}//else문 끝(list 있을 때)
					
					},//success 끝
					
					
					error : function(){
						console.log("입양/분양 최신글 조회 실패")
					}
				});//aJax끝
			};


			
			//ready 함수
			$(document).ready(function(){
				//입양/분양 가져오는 함수
				selectAdoptList();
			});

			
			$(window).resize(function(){
	    		if($(window).width() < 992) {
	    			$(".carousel-control-next-icon").addClass("sr-only");
	    			$(".carousel-control-prev-icon").addClass("sr-only");
	    		}
	    });
			$(window).resize(function(){
	    		if($(window).width() > 992) {
	    			$(".carousel-control-next-icon").removeClass("sr-only");
	    			$(".carousel-control-prev-icon").removeClass("sr-only");
	    		}
	    });

    
</script>



</body>
</html>







