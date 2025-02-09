<%@page import="com.lemon.lemonbiz.board.model.vo.BoardComment"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sbHeader.jsp"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <!-- summernote -->
<link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote.css" rel="stylesheet">
<script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote.js"></script>

</head>
<body>
<div class="container">
	<div class="container-fluid text-center">

	<!-- 게시글 -->
 	<div class="col-lg-12">
 			
              <input type="hidden" name="key" value="${board.key}" />
              <input type="hidden" name="id" value="${loginMember.memberId}" />
              <div class="card" >
                <div class="card-header py-3">
                  <div align="right">작성일 :<strong class="m-0 font-weight-bold text-warning"> 
                  <fmt:formatDate  value="${ board.postDate }" pattern="yyyy/MM/dd"/>
                  <br>
                  </strong>작성자  : <strong onclick="memberInfo('${board.memId}');" style="cursor:pointer;" class="m-0 font-weight-bold text-primary">${board.name}</strong>	&nbsp; 
                  	조회수 : <strong class="m-0 font-weight-bold text-warning">${ board.readCount }</strong></div>
                </div>
                <div class="card-body text-center">
                  <div align="left"><strong>제목 : ${board.title}</strong>
                  <button style="float: right;" class="w3-button w3-black w3-round" id="rec_update">
						<i class="fa fa-heart" style="font-size:16px;color:red"></i>
						&nbsp;<span class="rec_count"></span>
					</button> 

					
                  </div>
                  <hr>
                  <div class="form-group" align="left">
                   <div style="height:300px; overflow:auto;" align=left>${ board.content }</div> 
				  </div>
                  <hr>
                  <div><strong>첨부파일</strong><br>
                  <c:forEach items="${ board.attachList }" var="attach">
                  <c:if test="${ attach.originName != null }">
                  <a href="javascript:fileDownload('${ attach.key }');">
                  <img alt="첨부파일" src="${ pageContext.request.contextPath }/resources/images/file.png" width=16px>
					${ attach.originName }
                  </a>
                  </c:if>
				</c:forEach>
				</div><br>
				<div class="container">
				<a class="btn btn-outline-warning" href="${ pageContext.request.contextPath }/board/boardList.do">돌아가기</a>
				<c:if test="${loginMember.name eq board.name or loginMember.isManager eq 1}">
					<a class="btn btn-outline-warning" onclick="deleteBoard('${ board.key}')" href="boardfrmDelete.do?key=<c:out value="${board.key}"/>">삭제</a>
					<a class="btn btn-outline-warning" onclick="updateBoard('${ board.key }')" href="boardUpdate.do?key=<c:out value="${board.key}"/>">수정</a>
				</c:if>
			
					
				</div>
                </div>
              </div>
            </div>
            
				<br>
				
	
		<!-- 댓글 -->
		<div class="card mb-4 py-3 border-bottom-warning">
			<form id="form1" name="boardCommentFrm" action="${pageContext.request.contextPath}/board/boardInsert.do" method="post">
			<div align="center">
				<input type="hidden" name="boardRef" value="${ board.key }" />
				<input type="hidden" name="boardCommentWriter" value= "${loginMember.memberId }" />
				<input type="hidden" name="boardCommentLevel" value="1" />
				<input type="hidden" name="boardCommentRef" value="0" /> 
				<textarea style="width:90% !important;" class="form-control bg-light border-0 small" name="boardCommentContent"  rows="3" cols="60" maxlength="500" placeholder="댓글을 달아주세요."></textarea>
				<br>
				<button  type="submit" id="btn-insert" class="float-right mr-5 btn btn-outline-warning" >등록</button>
				</div>
			</form>
				<hr>
		<!-- 댓글작성창	 -->	
	<table id="tbl-comment">
		 	<c:if test="${ commentList ne null and not empty commentList}">
		 		<c:forEach items="${commentList}" var="BoardComment">
		 			<c:choose>
		 			<c:when test="${ BoardComment.boardCommentLevel eq 1 }">
		 			<tr class="level1">
					<td class="mx-5 border-bottom" style="text-align: left;">
						<div class="ml-4 text-primary comment-writer">
							<a href="#" onclick="memberInfo('${BoardComment.boardCommentWriter}');">${ BoardComment.name }</a>
						</div>
						<div class="ml-4">
							${ BoardComment.boardCommentContent }
						</div>
						<c:if test="${ BoardComment.boardCommentWriter eq (loginMember.memberId)}">			
						<button class="btn-delete float-right mx-2 text-primary" 
							   style="border: none;
									  background-color: inherit;
									  font-size: 11px;
									  cursor: pointer;
									  display: inline-block;"
							   value="${ BoardComment.boardCommentNo }">삭제</button>
						</c:if>										
						<button class="btn-reply float-right mx-2 text-primary"
						  		style="border: none;
									  background-color: inherit;
									  font-size: 11px;
									  cursor: pointer;
									  display: inline-block;"
								value="${ BoardComment.boardCommentNo }">답글</button>
						<small style="font-size:11px;" class="comment-date ml-4 font-weight-bolder">
							<fmt:formatDate value="${ BoardComment.boardCommentDate }" pattern="yyyy/MM/dd"/>
						</small>
					</td>
				</tr>	
		 		</c:when>
				<c:otherwise>
				<tr class="level2">
					<td class="mx-5 border-bottom" style="text-align: left;">
						<div class="ml-5 text-primary comment-writer">
							<a href="#" onclick="memberInfo('${BoardComment.boardCommentWriter}');">${ BoardComment.name }</a>
						</div>
						<div class="ml-5">
							${ BoardComment.boardCommentContent }
						</div>
						<c:if test="${ BoardComment.boardCommentWriter eq (loginMember.memberId)}">			
						<button class="btn-delete float-right mx-2 text-primary" 
							   style="border: none;
									  background-color: inherit;
									  font-size: 11px;
									  cursor: pointer;
									  display: inline-block;"
							   value="${ BoardComment.boardCommentNo }">삭제</button>
						</c:if>
						<small style="font-size:11px;" class="comment-date ml-5 font-weight-bolder">
							<fmt:formatDate value="${ BoardComment.boardCommentDate }" pattern="yyyy/MM/dd"/>
						</small>										
					</td>
				</tr>
				</c:otherwise>
				
				 </c:choose>
			 </c:forEach>
			 </c:if>
				
		</table>	
		</div>
		</div>	
	</div>
		
</body>
<script type="text/javascript">
$(document).ready(function(){

	$('#brdmemo').summernote({
		  lang: 'ko-KR',
	      height: 500,
	      popover: {
	    	  image:[],
	    	  link:[],
	    	  air:[]
	      } ,
	      toolbar: [
	    	    // [groupName, [list of button]]
	    	    ['style', ['bold', 'italic', 'underline', 'clear']],
	    	    ['font', ['strikethrough', 'superscript', 'subscript']],
	    	    ['fontsize', ['fontsize']],
	    	    ['color', ['color']],
	    	    ['para', ['ul', 'ol', 'paragraph']],
	    	    ['table', ['table']],
	    	    ['insert', ['link', 'picture', 'hr']],
	    	    ['height', ['height']]
	    	  ],
	    	  focus: true,
				callbacks: {
					onImageUpload: function(files, editor, welEditable) {
			            for (var i = files.length - 1; i >= 0; i--) {
			            	sendFile(files[i], this);
			            }
			        }
				}
	  });	  
}) 

function fileDownload(key){
	location.href = "${ pageContext.request.contextPath }/board/fileDownload.do?key=" + key;
}

function deleteBoard(key){
	if(!confirm("정말 삭제하시겠습니까?")) return;
	location.href = "${ pageContext.request.contextPath }/board/boardfrmDelete.do?key=" + key;
}

function updateBoard(key){
	location.href = "${ pageContext.request.contextPath }/board/boardUpdate.do?key=" + key;
}

 /* 댓글입력창 */
$("[name=boardCommentFrm]").submit(function(){

	var $textarea = $("#boardCommentContent");
	if(/^(.|\n)+$/.test($textarea.val()) == false){
		alert("댓글 내용을 입력하세요.");
		return false;
	}
	
	return true;
});
 /* 댓글 삭제 */
 $(".btn-delete").click(function(){
	
	if(!confirm("정말 삭제하시겠습니까?")) return;
	
	location.href = "${ pageContext.request.contextPath }/board/boardDelete.do?key=" + ${ board.key } + "&boardCommentNo="+$(this).val();
	
});

/*  대댓글 */
	$(".btn-reply").click(function(){
		var html = "<tr>"
			 + "<td style='display:none; text-align:left' colspan='2'>"
			 + "<form action='${pageContext.request.contextPath}/board/boardInsert.do' method='POST'>"
			 + '<input type="hidden" name="boardRef" value="${ board.key }" />'
			 + '<input type="hidden" name="boardCommentWriter" value="${loginMember.memberId }" />'
			 + '<input type="hidden" name="boardCommentLevel" value="2" />'
			 + '<input type="hidden" name="boardCommentRef" value="' + $(this).val() + '" />' 
			 + '<textarea name="boardCommentContent" rows="3" class="ml-5" style="width: 80%;"></textarea>'
			 + '<button type="submit" class="btn-insert2 btn btn-outline-warning ml-2" style="width:60px;">등록</button>'
			 + "</form>"
			 + "</td>"
			 + "</tr>"
		  var $tr = $(html);
		  var $trFromBtn = $(this).parent().parent();
		  $tr.insertAfter($trFromBtn)
		  	 .children("td")
		  	 .slideDown(800)
		  	 .children("form")
		  	 .submit(function(){
		  		var $textarea = $(this).children("textarea");
		  		console.log($textarea);
		  		if(/^(.|\n)+$/.test($textarea.val()) == false){
		  			alert("댓글 내용을 입력하세요.");
		  			return false;
		  		}
		  		return true;
		  	 })
		  	 .children("textarea")
		  	 .focus();
		  $(this).off('click');

});

/*  추천 */
	
    $(function(){
		// 추천버튼 클릭시(추천 추가 또는 추천 제거)
		$("#rec_update").click(function(){	
			var path ='${ pageContext.request.contextPath }/board/RecUpdate.do';
			var id = '${loginMember.memberId}';
			var msg = '${obj.msg}';
			 $.ajax({
				url : path,
                type : "POST",
                dataType: "json",
                data : {
                    key: ${board.key},
                    id
                },
                success: function (result) {
          			if(result == 0){
							alert("추천되었습니다!");
              		}
          			else{
						alert("추천 취소!");
              		}
                   
			       recCount(); 
                },
               
			})
		})
    })
		// 게시글 추천수
	    function recCount() {
		     console.log("작성완료");
				var path2 ='${ pageContext.request.contextPath }/board/RecCount.do';
				var key = '${board.key}';
			$.ajax({
				url: path2,
                type: "POST",
                data: {
                    key
                },
                success: function (count) {
                	$(".rec_count").html(count);
                },
                
			});
	    };
	    recCount(); // 처음 시작했을 때 실행되도록 해당 함수 호출

	
</script>

</html>

<jsp:include page="/WEB-INF/views/common/showMemberInfo.jsp"/>
<jsp:include page="/WEB-INF/views/common/sbFooter.jsp"/>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
