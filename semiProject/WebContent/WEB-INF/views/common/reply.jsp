<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
	#replyContentArea{ 
		width: 100%; 
		min-height : 500px;
	}
	
	#reply-write-area {
		width : 100%;
		height : 240px;
		text-align : center;
		padding : 20px;
		background-color : #F2F2F2;
	}
	
	#replyContentArea textarea{
    resize: none;
   	width: 100%;
   	height: 150px;
   	border : 1px solid #EAEAEA;
   	border-radius : 5px;
	}
	
	.rCount {
		line-height : 50px;
		margin-right : 10px;
		font-size : 14px;
		color : gray;
	}
	.replyBtnArea{
    width: 75px;
    text-align: center;
    margin-top : 5px;
	}
	
	#reply-list {
		width : 100%;
		margin-top : 20px;
	}
	#reply-list td{
		height : 80px;
		border-top : 1px solid #EAEAEA;
		border-bottom : 1px solid #EAEAEA;
		padding : 10px;
	}
	#reply-list .rWriter{ 
		width : 20%;
		font-size : 14px;
	}
	#reply-list .rContent{
		font-size : 14px;
	}
	#reply-list .rDate{
		width : 15%;
		font-size: 0.8em;
		color : gray;
		text-align : right;
	}
	#reply-list .deleteReply {
		width : 10px;
	}
	#reply-list .deleteReply > a {
		color : rgb(51,51,51);
	}
	
	#replyListArea{
		list-style-type: none;
	}
	
</style>

<%-- include를 이용해 공용으로 사용해서 head, body가 없음 --%>

<!-- 댓글 영역 -->
<div id="replyContentArea">
	<div id="reply-write-area">
		<form action="#" method="post" onsubmit="">
			<textarea id="reply-textarea" name="reply">
			</textarea>
			<button id="addReply" class="btn btn-teal float-right replyBtnArea" type="submit">등록</button>
		</form>
	</div>
			
<%-- 			<c:if test="${!empty replyList}"> --%>
			<table id="reply-list">				
<%-- 					<c:forEach var="reply" items="${replyList}" > --%>
						<tr class="reply-row">
							<td class="replyNo sr-only"><%-- ${reply.replyNo} --%></td> 
							<td class="rWriter" ></td> <%-- ${reply.replyWriter} --%>
							<td class="rContent"><%-- ${reply.replyCotent} --%>
		
							</td>
							<td class="rDate">
								<%-- <fmt:formatDate value="${reply.commDate}" pattern="yy-MM-dd HH:mm:ss"/> --%>
							</td>
							<td class="deleteReply">
<%-- 								<c:if test="${!empty loginMember && loginMember.nNm == reply.replyWriter }"> --%>
									<a id="deleteBtn" onclick="delete(2)">
										<i class="fas fa-times"></i>
									</a>
<%-- 								</c:if> --%>
							</td>
						</tr>
				</table>
<%-- 			</c:if> --%>
		</div>
		
		
<script>

var loginNickName = "${loginMember.memNm}";


//페이지 로딩 완료 시 댓글 목록 호출
$(function(){
	selectReplyList();
});

//해당 게시글 댓글 목록 조회 함수(ajax)
function selectReplyList(){
	$.ajax({
		url : "${contextPath}/reply/selectList.do",
		data : {"brdNo" : brdNo},
		type : "post",
		dataType : "JSON",
		success : function(rpList){
			//console.log(rpList);
			
			$("#reply-list").html("");
			
			$.each(rpList, function(index, item){
				
				var tr = $("<tr>").addClass("reply-row");
				var rWriter = $("<td>").addClass("rWriter").text(item.nickName);
				var rContent = $("<td>").addClass("rContent").text(item.replyContent);
				var rDate = $("<td>").addClass("rDate").text(item.replyDate);
					
				// 댓글 삭제 
				if(item.nickName == loginNickName){
					var deleteBtn = $("<a>").addClass("deleteBtn");
					var i = $("<i>").addClass("fas fa-times").attr("onclick", "deleteReply("+item.replyNo+")")

					i.append(deleteBtn);
					
					var td = $("<td>").addClass("deleteReply").attr("style", "cursor: pointer");
	
					td.append(i);
				}
				
				tr.append(rWriter).append(rContent).append(rDate).append(td)
				$("#reply-list").append(tr);
			
			});
		},
		error : function(){
			console.log("댓글 목록 조회 실패");
			}
	})
}
	
	
// 댓글 등록 (ajax) ---------------------
$("#addReply").on("click", function(){
	
 	var replyContent = $("#reply-textarea").val().trim();
	
	// 댓글 내용이 없는 경우
	if(replyContent.length == 0){
		alert("댓글을 입력해주세요");
	} 
	
	else { // 댓글이 작성되어 있는 경우
		
		var replyWriter = "${loginMember.memNo}";
		
		$.ajax({
			url : "${contextPath}/reply/insertReply.do",
			data : { "replyWriter" : replyWriter,
								"replyContent" : replyContent,
								"brdNo" : brdNo },
			type : "post",
			success : function(result){		
				if(result > 0){
					
					// 댓글 작성 내용 삭제
					$("#replyContent").val("");
					
					swal({"icon" : "success", "title" : "댓글 등록 성공"});
					
					selectReplyList();
				
				}
				
			}, error : function(){
				console.log("댓글 등록 실패");
			}
		});
	} 
	
});


// 댓글 삭제 함수 ---------------
function deleteReply(replyNo){

	if(confirm("정말로 삭제하시겠습니까?")){
		var url = "${contextPath}/reply/deleteReply.do";
		// key값에는 변수 적용 x
		
		$.ajax({
			url : url,
			data : {"replyNo" : replyNo},
			success : function(result){
				if(result > 0){
					selectReplyList(brdNo);
										
					swal({"icon" : "success" , "title" : "댓글 삭제 성공"});
				}
				
			}, error : function(){
				console.log("ajax 통신 실패");
			}
		});
	}
}

</script>