package com.lemon.lemonbiz.approver.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lemon.lemonbiz.approver.model.service.ApproverService;
import com.lemon.lemonbiz.approver.model.vo.dept;

@Controller
@RequestMapping("/approver")
public class ApproverController {
	
	private static Logger log = LoggerFactory.getLogger(ApproverController.class);

	@Autowired
	private ApproverService approverService;
	
	@RequestMapping("/writeForm.html")
	public String writeForm(Model model) {
		
		List<dept> dept = approverService.deptList();
		List<dept> child = approverService.child();
		List<dept> child2 = approverService.child2();
		log.debug("dept = {}",dept);
		log.debug("child = {}",child);
		log.debug("child2 = {}",child2);
		
		model.addAttribute("dept",dept);
		model.addAttribute("child",child);
		model.addAttribute("child2",child2);
		
		return "approver/writeForm";
	}
	
	//@RequestMapping("/tree.do")
	public String approval(Model model) {
		
		List<dept> dept = approverService.deptList();
		
		
		log.debug("dept = {}",dept);
		
		return "";
	}
	
	
}
