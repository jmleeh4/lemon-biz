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
                	<div class="card-header py-2" align="center">
                	<br />
                	<h2>반려 문서</h2>
                	<br />	
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
					
					
					    <!-- ================결재칸=============== -->
					
						
					      <div class="float-left col-md-5" >
						<div class="float-left col-md-4">
						
						</div>
						
						
						<div class="float-left col-md-10" style="padding:15px">
							
					
							
						</div>
						</div>
						
						
					    <table>
						<tr>
						<td width="40%">
						<div class="float-center"  style="padding:15px">
						<table class="table table-hover text-center" >
						<tr>
							
							<td>분류</td>
							<td id="proNum1">1차 결재자</td>
							<td id="proNum2">2차 결재자</td>
							<td id="proNum3">3차 결재자</td>
						</tr>
						<tr>
						<td class="tt">직급</td>
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
						
						<td>성명</td>
						<td id="authName1">${ apprck1.ckName }</td>
						<td id="authName2">${ apprck2.ckName }</td>
						<td id="authName3">${ apprck3.ckName }</td>
						
						</tr>
						
						<tr>
						
						<td>사원번호</td>
						<td id="apv_mem1">${ apprck1.memId }</td>
						<td id="apv_mem2">${ apprck2.memId }</td>
						<td id="apv_mem3">${ apprck3.memId }</td>
	
						</tr>
						
						<tr>
						<td>승인여부</td>
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
						<!-- ==============결재칸 끝============== -->
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
						
						<div>
							<h5>반려사유</h5>
							<c:if test="${ apprck1.opinion != null }">
							<textarea name="opinion" class="form-control" cols="90" rows="5" readonly>${ apprck1.opinion }</textarea>
							</c:if>
							<c:if test="${ apprck2.opinion != null }">
							<textarea name="opinion" class="form-control" cols="90" rows="5" readonly>${ apprck2.opinion }</textarea>
							</c:if>
							<c:if test="${ apprck3.opinion != null }">
							<textarea name="opinion" class="form-control" cols="90" rows="5" readonly>${ apprck3.opinion }</textarea>
							</c:if>
						</div>
						<hr />
						
						<!-- 폼 내용 end -->
						
						<div class="container" align="center">
						<input class="btn btn-outline-primary" type="button" value="뒤로가기" onclick="history.back(-1);">
						<button class="btn btn-outline-primary" onclick="rewrite()">재작성하기</button>
						<div><br></div>
						</div>
						
						<form action="${pageContext.request.contextPath}/approval/writeForm.do"
							  method="POST"
							  id="writeForm">
						
						</form>
						
					</div>
				</div>
			</div>
		</div>
		


<script>

function fileDownload(key) {
	location.href = "${ pageContext.request.contextPath }/approval/fileDownload.do?key=" + key;
}


function rewrite() {
	$('#writeForm').submit();
}

</script>









<jsp:include page="/WEB-INF/views/common/sbFooter.jsp"/>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>