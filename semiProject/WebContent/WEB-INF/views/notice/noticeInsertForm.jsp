<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지 글 작성</title>

		<!-- summernote 라이브러리 연결 -->
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">

		<!-- 이거 없으면 진행이 안됨? -->
 		<script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.9/summernote.js" defer></script>
		
		<!-- include summernote-ko-KR -->
		<script src="${pageContext.request.contextPath}/resources/js/summernote/lang/summernote-ko-KR.js"></script>

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
	   font-family: 'TmoneyRoundWindRegular';
	} 
	.form-hr > hr{
		height: 8px;
		margin-bottom: .1rem;
		display: inline-block;
		background-color: teal;
		border-radius: 1rem;
	}
	
	/*------------------------------------*/
   div, input, lable {box-sizing: border-box;}

   /*form-wrapper 크기 */
   #form-wrapper {
       width : 100%;
       min-height : 1000px;
       margin: 0 auto;
       padding: 30px 0px;
       box-sizing: border-box;
   }

   .form-control{
 			height: 3rem;
			border-radius: 1rem;
      box-sizing: border-box;
      display: inline-block !important;
		}

		#titleInput{
			width : 99.5%;
		}
   .inputLabel {
       height : 3rem;
       width: 16% !important;
       min-width : 130px;
       line-height: 1.3;
       padding: .75rem .75rem;
       margin : 0;
       font-size : 18px;
   }
   .inputContent{
       width: 83% !important;
   }
   .form-size{
       width : 100%;
       padding: 10px;
       margin : 0;
   }
	
	.btn-size{
		width : 75px !important;
		border-radius: 1rem;
		margin-left : 10px;
		margin-top : 20px;
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
					<span class="form-review">공지 사항</span>
					<span class="form-hr" style="position:absolute; width:90%;"><hr style="width:1000px;"></span>
				</div>
	
	        <div id="form-wrapper">
            <form action="${contextPath}/notice/noticeInsert.do?${tpStr}&cp=1" method="post" onsubmit="return boardValidate()">
                <div class="form-size">
                    <input type="text" name="title" id="titleInput" class="form-control"
                         placeholder="제목을 입력해 주세요.">
                </div>
            
                <div class="form-size">
                    <textarea name="content" id="summernote"></textarea>
                </div>
                <div id="form-btn" class="form-size">
                    <button type="submit" class="btn btn-teal float-right btn-size">등록</button>
                    <button type="button" class="btn btn-secondary float-right btn-size"
                    	onclick="location.href='${header.referer}'">취소</button>
                </div>
            </form>
        </div>
	
	
	</div>
	
		<!-- footer -->
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	<script>
	 $(document).ready(function() {
	/* 써머노트 스타일 지정 */
        $("#summernote").summernote({
        	minHeight: 500, //최소높이
        	maxHeight: null, //최대높이
        	lang: "ko-KR",
        	placeholder: "사진과 함께 입양 후기를 작성해 주세요.",
        	
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
		       	url: '${contextPath}/image/uploadImage.do',
		       	cache: false,
		       	contentType: false,
		       	enctype: 'multipart/form-data',
		       	dataType : "json",
		       	processData: false,
		       	success: function(image) {
		      		//filePath == url : 서버에 업로드된 url을 반환받아 <img> 태그 src에 저장
		      			var imageUrl = image.filePath + image.fileName
		         		$(el).summernote('editor.insertImage', imageUrl);
		      			console.log("서버 업로드 성공");
		      			/* console.log(image);
		      			console.log(image.filePath);
		      			console.log(image.fileName); */
		       	}
		     	});
  			} 
        
        
    });//ready 함수 끝
	
	
	/* 유효성 검사 */
		function boardValidate() {
			if ($("#titleInput").val().trim().length == 0) {
				swal({icon:"warning", title:"제목을 입력해 주세요."});
				$("#titleInput").focus();
				return false;
			}
			
			if ($("#summernote").val().trim().length == 0) {
				swal({icon:"warning", title:"내용을 입력해 주세요."});
				$("#summernote").focus();
				return false;
			}
			//이미지 없을 때 검사...?
			
		}
    
    
    
	</script>
	
	
	
</body>
</html>
