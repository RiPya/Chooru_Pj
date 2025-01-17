<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입양/분양</title>

		<!-- summernote 라이브러리 연결 -->
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">

		<!-- 이거 없으면 진행이 안됨? -->
 		<script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.9/summernote.js" defer></script>
		
		<!-- include summernote-ko-KR -->
		<script src="${pageContext.request.contextPath}/resources/js/summernote/lang/summernote-ko-KR.js"></script>

<style>
/* 입양/분양 라인 */
.header-info{
   width: 100%;
   margin-bottom: 2rem;
}

.form-info {
	font-size : 22px;
	font-weight: bold;
	font-family: 'TmoneyRoundWindRegular';   
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

/* form 스타일 */
.form-container {
	line-height: 60px;
}

.adtCategory {
	width: 128px !important;
	display: inline-block !important;
}

.adtTitle {
	width: 76.5% !important;
	display: inline-block !important;
}

#address, #adtNote {
	width: 82.5% !important;
}

.adtGender, .adtYn {
	width: 128px !important;
}

.form-btn {
	margin-bottom: 100px
}

.form-btn > .btn{
	margin-right: 5px;	
}

</style>

</head>
<body>
	
	<%-- url 작성 시 붙여야 하는 str --%>
	<!-- tp를 파라미터로 보낼 때 사용하는 변수 (cd X) -->
	<c:set var="tpStr" value="tp=${param.tp}"/>

	<!-- tp와 cd를 파라미터로 동시에 보낼 때 사용하는 변수 : 입양, 자유, 고객센터, 마이페이지는 필요함-->
	<c:set var="tpCpNoStr" value="tp=${param.tp}&cp=${param.cp}&no=${param.no}"/>

	
	<!-- 검색을 통해 온 경우 -->
	<c:if test="${!empty param.sk && !empty param.sv }">
		<c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}"/>
	</c:if>


	<!-- header.jsp -->
	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">
		<div class="header-info">
			<span class="form-info">입양/분양</span>
			<span class="form-hr" style="position:absolute; width:90%;"><hr style="width:1000px;"></span>
		</div>
	</div>
	
	<div class="container my-5">
		<form action="${contextPath}/adoption/update.do?${tpCpNoStr}${searchStr}" 
					method="post" onsubmit="return boardValidate();">
	
			<div class="form-container">
				<label class="form-label mr-3">카테고리</label> 
				<select id="category" name="category" class="form-control mr-3 adtCategory">
					<option value="adtDog">입양 개</option>
					<option value="adtCat">입양 고양이</option>
					<option value="adtEtc">입양 기타</option>
					<option value="temp">임시 보호</option>
				</select>

				<input type="text" name="title" id="title" class="form-control adtTitle" value="${adoption.adtBrdTitle}">

			<div class="form-inline">
				<label for="address" class="form-label mr-5">주소</label> 
				<input type="text" name="address"  id="address" class="form-control ml-1 mr-2 postcodify_address" placeholder="주소를 입력해 주세요(시/군/구)" value="${adoption.address}">
				<button type="button" id="postcodifyBtn" class="btn btn-teal btn-lg">검색</button>
				
			</div>

			<div class="form-inline">
				<label for="adtNote" class="form-label mr-3">특이 사항</label> 
				<input type="text" name="adtNote" id="adtNote" class="form-control" value="${adoption.adtNote}">
			</div>
				
			<div class="form-inline">
				<label for="adtBreed" class="form-label mr-5">품종</label> 
				<input type="text" name="adtBreed" id="adtBreed" class="form-control ml-1 mr-3" size="17" value="${adoption.adtBreed}">
	
				<label for="adtAge" class="form-label mr-5">나이</label> 
				<input type="text" name="adtAge" id="adtAge" class="form-control ml-1 mr-3" size="9" value="${adoption.adtAge}">
	
				<label class="form-label mr-3">성별</label> 
				<select name="adtGender" class="form-control mr-4 adtGender">
					<option value="boy">수컷</option>
					<option value="girl">암컷</option>
					<option value="ntrBoy">수컷 (중성화)</option>
					<option value="ntrGirl">암컷 (중성화)</option>
				</select>				
			</div>
		
				<div class="form-inline">
					<label for="adtDate" class="form-label mr-3">공고 기간</label> 
		 			<input type="date" name="adtDate" id="adtDate" class="form-control mr-3" value="${adoption.adtTime}">				

					<label class="form-label mr-3">입양 여부</label>
					<select name="adtYn" class="form-control mr-4 adtYn">
						<option value="Y">진행 중</option>
						<option value="N">완료</option>
					</select>
				</div>

				<div class="form-inline">
					<label class="form-label mr-3">예방 접종 여부</label> 
					
					<input type="radio" name="adtVaccination" id="vaccYes" class="form-check-input mr-3 adtVaccination" value="Y">
					<label for="vaccYes" class="form-label mr-3">유</label> 		
		
					<input type="radio" name="adtVaccination" id="vaccNo" class="form-check-input mr-3 adtVaccination" value="N">
					<label for="vaccNo" class="form-label mr-3">무</label> 		
				</div>
			</div>
			
			<hr>

			<div class="form-group">
				<textarea name="content" id="summernote">
				${adoption.adtBrdContent}
				</textarea>
				</div>

        <div class="form-btn">
            <button type="submit" class="btn btn-teal float-right btn-lg">수정 완료</button>
            <button type="button" class="btn btn-secondary float-right btn-lg"
            				onclick="location.href='${header.referer}'">취소</button>
        </div>
			</form>
		</div>

		<!-- footer -->
		<jsp:include page="../common/footer.jsp"></jsp:include>
		
		
<script>

// 카테고리 초기값 지정
(function(){
	$("#category > option").each(function(index, item){
		if($(item).val() == "${adoption.adtCode}"){
			$(item).prop("selected", true);
		}
	})
	
	// 성별  초기값 지정
	$(".adtGender > option").each(function(index, item){
		if($(item).val() == "${adoption.adtGender}"){
			$(item).prop("selected", true);
		}
	})
	
	// 입양 여부  초기값 지정
	$(".adtYn > option").each(function(index, item){
		if($(item).val() == "${adoption.adtYn}"){
			$(item).prop("selected", true);
		}
	});
	
	// 예방 접종 여부  초기값 지정
	$(".adtVaccination").each(function(index, item){
		   if( $(item).val() == "${adoption.adtVaccination}" )	
		     $(item).prop("checked", true); 
		});
	
})();


/* Postcodify 주소창  */	
	$(function(){
    $("#postcodifyBtn").postcodifyPopUp();
});

$(document).ready(function() {
	/* 써머노트 스타일 지정 */
	$("#summernote").summernote({
   	minHeight: 500, //최소높이
   	maxHeight: null, //최대높이
   	lang: "ko-KR",
   	
		toolbar: [
    // [groupName, [list of button]]
    ['fontname', ['fontname']],
    ['fontsize', ['fontsize']],
    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
    ['color', ['forecolor','color']],
    ['table', ['table']],
    ['para', ['ul', 'ol', 'paragraph']],
    ['height', ['height']],
    ['insert',['picture','link','video']],
    ['view', ['fullscreen', 'help']]
	  ],
		fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
		fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],

    /* 이미지 삽입 후 서버에 저장을 위한 callback */
   	callbacks: {
   			onImageUpload : function(files, editor, welEditable) {
         for (var i = files.length - 1; i >= 0; i--) {
         	sendFile(files[i], this);
         }
			} 
   	}
	});
        
  /* 이미지 서버 저장 후 url 반환 받는 함수 */  
	function sendFile(file, el) {
		var form_data = new FormData();
		form_data.append('file', file);
    
		$.ajax({
       	data: form_data,
       	type: "POST",
       	url: '${contextPath}/file/uploadFile.do',
       	cache: false,
       	contentType: false,
       	enctype: 'multipart/form-data',
       	dataType : "json",
       	processData: false,
       	success: function(file) {
   	  		//filePath == url : 서버에 업로드된 url을 반환받아 <img> 태그 src에 저장
 	    		$(el).summernote('editor.insertImage', file.filePath, file.fileName);
   	  		console.log("서버 업로드 성공");
       	}
     	});
  	}  
	});//ready 함수 끝

	// 유효성 검사 
	function boardValidate() {
		if ($("#title").val().trim().length == 0) {
			swal({icon:"warning", title:"제목을 입력해 주세요."});
			$("#title").focus();
			return false;
		}

		if ($("#address").val().trim().length == 0) {
			swal({icon:"warning", title:"주소를 입력해 주세요."});
			$("#address").focus();
			return false;
		}
		
		if ($("#adtBreed").val().trim().length == 0) {
			swal({icon:"warning", title:"품종을 입력해 주세요."});
			$("#adtBreed").focus();
			return false;
		}
		
		if ($("#adtAge").val().trim().length == 0) {
			swal({icon:"warning", title:"나이 입력해 주세요."});
			$("#adtAge").focus();
			return false;
		}

		if ($("#adtDate").val().trim().length == 0) {
			swal({icon:"warning", title:"공고 기간을 입력해 주세요."});
			$("#adtDate").focus();
			return false;
		}
		
		if ($("#summernote").val().trim().length == 0) {
			swal({icon:"warning", title:"내용을 입력해 주세요."});
			$("#summernote").focus();
			return false;
		}
			
			/* img태그가 있는지 확인 후 없으면 img 추가하는 경고창 필요 → 썸네일이 필요하기 때문 */
		}
	
	</script>
</body>
</html>