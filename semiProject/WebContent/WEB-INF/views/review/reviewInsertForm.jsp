<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입양 후기 글 작성</title>


<style>
	/* 입양 후기 라인 */
	.header-review{
		width: 100%;
		padding-top : 8px;
		margin-bottom: 2rem;
	}
	.form-review {
	   font-size: 20px;
	   font-weight: bolder;
	   padding-right: 20px;
	} 
	.form-hr > hr{
		height: 8px;
		margin-bottom: .1rem;
		display: inline-block;
		background-color: teal;
		border-radius: 1rem;
	}

</style>



</head>
<body>
<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}"/>
	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<%-- 	<c:if test="${!empty param.cd}">
			<c:set var="tpCdStr" value="tp=${param.tp}&cd=${param.cd}"/>
		</c:if> --%>

	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
				<div class="header-review">
					<span class="form-review">입양 후기</span>
					<span class="form-hr" style="position:absolute; width:90%;"><hr style="width:1000px;"></span>
				</div>
	
			<div>
				
			
			</div>
	
	
	
	
	</div>
	
		<!-- footer -->
	<jsp:include page="../common/footer.jsp"></jsp:include>
</body>
</html>