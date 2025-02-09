package com.lemon.lemonbiz.approval.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


import com.lemon.lemonbiz.member.model.vo.Member;
import com.lemon.lemonbiz.approval.model.vo.Appr;
import com.lemon.lemonbiz.approval.model.vo.ApprCheck;
import com.lemon.lemonbiz.approval.model.vo.Approval;
import com.lemon.lemonbiz.approval.model.vo.DocType;
import com.lemon.lemonbiz.common.vo.Attachment;
import com.lemon.lemonbiz.member.model.vo.Dept;



public interface ApprovalService {

	List<Dept> deptList();

	List<Dept> child();

	List<Dept> child2();

	List<Member> memberList(String node);

	List<Member> selectMember(String param);

	List<Member> joinMemberlist(String param);

	String SeqApprKey();

	int insertSaveApproval(Appr appr);

	List<Appr> ApprovalList(String memberId);

	Appr reWriteAppr(String key);

	List<ApprCheck> reWriteApprck(String key);

	Attachment reWriteAttach(String key);

	int updateApproval(Appr appr);

	int insertApproval(Appr appr);

	List<ApprCheck> apprckList(String memberId);

	List<Appr> apprAndCkList(Member loginMember);

	Appr apprckDetail(int ckKey);

	Attachment selectOneAttachment(String key);

	ApprCheck selectcApprck(Map<String, String> map);

	int changeApprck(int key, Appr appr);

	int compliteApprck(int key, String apprKey, Appr appr);

	List<Appr> myApprovalList(String memberId);

	DocType selectOneDocTypeAjax(DocType docType);

	List<DocType> selectDocTypeTitleList();

	List<Appr> compliteApprList(String memberId);

	List<Appr> returnApprList(String memberId);

	Appr returnApprovalDetail(String key);

	Appr compliteApprDetail(String key);

	int returnApproval(Map<String, String> map, Appr appr);

	int getCountApproval(HashMap<Object, Object> params);

	
}
