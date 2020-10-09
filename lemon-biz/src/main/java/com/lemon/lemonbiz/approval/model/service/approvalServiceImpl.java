package com.lemon.lemonbiz.approval.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lemon.lemonbiz.approval.model.dao.approvalDAO;

import com.lemon.lemonbiz.member.model.vo.Member;
import com.lemon.lemonbiz.member.model.vo.Dept1;


@Service
public class approvalServiceImpl implements approvalService {
	
	@Autowired
	private approvalDAO approvalDAO;

	@Override
	public List<Dept1> deptList() {
		
		return approvalDAO.deptList();
	}

	@Override
	public List<Dept1> child() {
		
		return approvalDAO.child();
	}

	@Override
	public List<Dept1> child2() {
		return approvalDAO.child2();
	}

	@Override
	public List<Member> memberList(String node) {
		return approvalDAO.memberList(node);
	}
}
