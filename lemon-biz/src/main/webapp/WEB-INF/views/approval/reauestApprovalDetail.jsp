<%@page import="com.lemon.lemonbiz.member.model.vo.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:useBean id="now" class="java.util.Date" />
<fmt:requestEncoding value="utf-8" /> <!-- 한글깨짐 방지  -->
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sbHeader.jsp"/>


<div class="container">
		<div class="container-fluid">
		<!-- 게시글 -->
 			<div class="col-lg-12">			
             	<div class="card" >
                	<div class="card-header py-3" align="center">	
						<table class="table table text-center">
					    <tr>
					    	<td><strong>기안담당</strong>
								<c:choose>
								<c:when test="${ loginMember.isManager == 1 }">관리자</c:when>
								<c:otherwise>${ appr.memName }</c:otherwise>
								</c:choose>
							</td>
								
							<td><strong>기안부서</strong>
								<c:choose>
								<c:when test="${ loginMember.isManager == 1 }">관리자</c:when>
								<c:otherwise>${ appr.rankName }</c:otherwise>
								</c:choose>
							</td>
							
							<td>
							<strong>기안일자</strong>
							
							<fmt:formatDate value="${appr.draftDate}"  pattern="yyyy-MM-dd"/>
							
							</td>
						</tr>
					    </table>
					</div>
					<div>
					
					
					    <!-- ================결제칸=============== -->
					
						
					    <table>
						<tr><td width="50%">
						<div class="float-center">
							<!-- Button trigger modal -->
							
						</div>
						</td>
						<td width="50%">
						<div class="float-center">
						<table border="1" style="display: inline-block">
						
						<tr>
							<td></td>
							
							<td id="proNum1">1차 결재자</td>
							<td id="proNum2">2차 결재자</td>
							<td id="proNum3">3차 결재자</td>
						</tr>
						<tr>
						<td class="tt" rowspan='4'>결재</td>
						
						
						<td id="authRank1" class="aa">
						${ apprck1.rankName }
						</td>
						
						<td id="authRank2" class="aa">
						${ apprck2.rankName }
						</td>
						
						<td id="authRank3" class="aa">
						${ apprck3.rankName }
						</td>
						
						
						<%-- <td id="authRank1" class="aa">${ apvReWrite.rank_name1 }</td>
						<td id="authRank2" class="aa">${ apvReWrite.rank_name2 }</td>
						<td id="authRank3" class="aa">${ apvReWrite.rank_name3 }</td> --%>
						</tr>
						
						
						<tr>
						
						
						<td id="authName1">${ apprck1.ckName }</td>
						<td id="authName2">${ apprck2.ckName }</td>
						<td id="authName3">${ apprck3.ckName }</td>
						
						</tr>
						
						<tr>
						
						
						<td id="apv_mem1">${ apprck1.memId }</td>
						<td id="apv_mem2">${ apprck2.memId }</td>
						<td id="apv_mem3">${ apprck3.memId }</td>
	
						</tr>
						
						<tr>
						
						<td>
							<c:choose>
								<c:when test="${ apprck1.status eq null}">
									미결재
								</c:when>
								<c:when test="${ apprck1.status eq 't' }">
									승인
								</c:when>
								<c:when test="${ apprck1.status eq 'f' }">
									반려
								</c:when>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${ apprck2.status eq null }">
									미결재
								</c:when>
								<c:when test="${ apprck2.status eq 't' }">
									승인
								</c:when>
								<c:when test="${ apprck2.status eq 'f' }">
									반려
								</c:when>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${ apprck3.status eq null }">
									미결재
								</c:when>
								<c:when test="${ apprck3.status eq 't' }">
									승인
								</c:when>
								<c:when test="${ apprck3.status eq 'f' }">
									반려
								</c:when>
							</c:choose>
						</td>
						
						</tr>
						
						</table>
						</div>
						
						</table>
						</div>
						<!-- ==============결제칸 끝============== -->
						<!-- 폼 내용 -->
						
						
						
						<form id="sendApv" action="${ pageContext.request.contextPath }/approval/updateApproval.do" method="POST" enctype="multipart/form-data">
							
							
							
							&nbsp;제목 : <input class="form-control" id="title" type="text" name="approval_title" value="${ appr.title }" readonly>
							<br>
							
							<div>
								<div class="form-control" style="height:700px; overflow:auto;" align=left>${ appr.content }</div> <br>
							</div>
							
							
							<div class="container" align="left">
							&nbsp;<label>첨부: ${ attach.originName } </label> <br>
							<button type="button"
									class="btn btn-outline-primary"
									onclick="fileDownload('${attach.key}')">
									다운로드
							</button>
							<hr>
							</div>
						               
						</form>
						
						<!-- 폼 내용 end -->
						
						<div class="container" align="center">
						<input class="btn btn-outline-primary" type="button" value="뒤로가기" onclick="history.back(-1);">
						<button class="btn btn-outline-primary" data-toggle="modal" data-target="#returnA">반려하기</button>
						<button class="btn btn-outline-primary" onclick="approve()">승인하기</button>
						<div><br></div>
						</div>
						
						<form action="${pageContext.request.contextPath}/approval/approve.do"
							  method="POST"
							  id="approve">
						<input type="hidden" name="title" value="${appr.title}"/>
						<input type="hidden" name="apprckKey1" value="${apprck1.key}"/>
						<input type="hidden" name="apprckKey2" value="${apprck2.key}"/>
						<input type="hidden" name="apprckKey3" value="${apprck3.key}"/>
						<input type="hidden" name="key" value="${appr.key}"/>
						<input type="hidden" name="memId" value="${appr.memId}"/>
						
						</form>
						
					</div>
				</div>
			</div>
		</div>
		
	<div class="modal" id="returnA">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">반려사유</h5>
					<button type="button" class="btn btn-outline-primary" data-dismiss="modal">&times;</button>
				</div>

					<div class="form-group">
						<div class="modal-body">

							<textarea id="returnResult" class="form-control" name="approval_content"  cols="40" rows="9" 
		      							  style="width:100%; resize:none"></textarea>
							
						</div>
						<div class="modal-footer float-right">
							<button class="btn btn-outline-primary" type="submit" onclick="returnApprove()" >반려</button>
						</div>
					</div>
			</div>
		</div>
	</div>


<script>

function fileDownload(key) {
	location.href = "${ pageContext.request.contextPath }/approval/fileDownload.do?key=" + key;
}

function approve() {

	$('#approve').submit();
	
}

function returnApprove() {

	$('<form></form>').attr('action',"${pageContext.request.contextPath}/approval/returnApprove.do").attr('method', 'POST').attr('id','returnApprovalForm').appendTo('#body');
	$('<input></input>').attr('type','hidden').attr('value',$('#returnResult').val()).attr('name','opinion').appendTo('#returnApprovalForm');
	$('<input></input>').attr('type','hidden').attr('value','${appr.key}').attr('name','returnApprKey').appendTo('#returnApprovalForm');
	$('<input></input>').attr('type','hidden').attr('value','${appr.title}').attr('name','title').appendTo('#returnApprovalForm');
	$('<input></input>').attr('type','hidden').attr('value','${appr.memId}').attr('name','memId').appendTo('#returnApprovalForm');
	$('<input></input>').attr('type','hidden').attr('value','${appr.key}').attr('name','key').appendTo('#returnApprovalForm');
	
	$('#returnApprovalForm').submit();
}


</script>









<jsp:include page="/WEB-INF/views/common/sbFooter.jsp"/>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>