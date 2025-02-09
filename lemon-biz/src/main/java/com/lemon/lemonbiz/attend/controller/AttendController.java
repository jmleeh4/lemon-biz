package com.lemon.lemonbiz.attend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lemon.lemonbiz.attend.model.service.AttendService;
import com.lemon.lemonbiz.attend.model.vo.Attend;
import com.lemon.lemonbiz.common.vo.Paging;
import com.lemon.lemonbiz.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/attend")
public class AttendController {

	@Autowired
	private AttendService attendService;

		//내 근태 페이지
		@RequestMapping("/attend.do")
		public ModelAndView attend(ModelAndView mav, Model model, HttpServletRequest request,Attend attend) {
		
			HttpSession session = request.getSession();
			Member loginMember = (Member)session.getAttribute("loginMember");
			attend.setMemId(loginMember.getMemberId());
			
			//내 총 정보
			Attend attendInfo =attendService.selectAttendInfo(attend);
			model.addAttribute("sumArr",attendInfo.getKey());
			model.addAttribute("sumTime",attendInfo.getMemId());
			model.addAttribute("avgTime",attendInfo.getTime());
			
			//마지막 기록 정보
			Attend lastAttend = attendService.selectLastOne(attend);
			model.addAttribute("lastAttend", lastAttend);
			
			//페이징 처리 코드
			int numPerPage = 5;
			int cPage = 1;
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch (NumberFormatException e) {		}
			int totalContents;
			try {
				totalContents= attendInfo.getKey();
			}catch(Exception e){
				totalContents=0;
			}

			String url = request.getRequestURI();
			
			String pageBar = Paging.getPageBarHtml(cPage, numPerPage, totalContents, url);
			Map<String,Object> map = new HashMap<String, Object>();
		
			String memId =attend.getMemId();
			List<Map<String, Object>> list = attendService.selectAttendList(cPage,numPerPage,map,memId);
			mav.addObject("list", list);
			mav.addObject("pagebar",pageBar);

			mav.setViewName("attend/attend");
			return mav;
		}

		//출근
		@RequestMapping("/attendArrive.do")
		public String attendArrive( RedirectAttributes redirectAttr,
									HttpServletRequest request, Attend attend) {

			HttpSession session = request.getSession();
			Member loginMember = (Member)session.getAttribute("loginMember");
			attend.setMemId(loginMember.getMemberId());
			
			try {
				attendService.attendArrive(attend);
			} catch (Exception e) {
				log.error("출근 등록 오류!", e);
				redirectAttr.addFlashAttribute("msg", "출근 등록 오류!");
			}
			return "redirect:/attend/attend.do";
		}
		
		//퇴근
		@RequestMapping("/attendLeave.do")
		public String attendLeabe( RedirectAttributes redirectAttr,
									HttpServletRequest request,  Attend attend) {
			
			HttpSession session = request.getSession();
			Member loginMember = (Member)session.getAttribute("loginMember");
			attend.setMemId(loginMember.getMemberId());

			try {
				attendService.attendLeabe(attend);
			} catch (Exception e) {
				log.error("출결 등록 오류!", e);
				redirectAttr.addFlashAttribute("msg", "퇴근 등록 오류!");
			}
			return "redirect:/attend/attend.do";
		}
		
		
		//Cal근태호출
		@RequestMapping("/attendCal.do")
		public String attendtest() {
			//코드
			
		return "attend/attendCal";
		}
		
		//캘린더 데이터값
		@ResponseBody
		@RequestMapping("/selectCalAttend.do") 
		public List<Attend> selectCalAttend(Attend attend ,
									@RequestParam("memId") String memId ,
									@RequestParam("yyyymm") String yyyymm ) {
		attend.setMemId(memId);
		attend.setYyyymm(yyyymm+'%');
		List<Attend> list = attendService.selectCalAttend(attend);
		
		return list;
		}
		
		//wj
		@RequestMapping("/getTodayAttend")
		public ResponseEntity<?> getTodayCount(@RequestParam HashMap<Object,Object> params) {
			
			String date = (String)params.get("date");

			int num = attendService.getTodayCount(date);

			return new ResponseEntity<>(num,HttpStatus.OK);		
		}
		
		
		@RequestMapping("/getAttendLeave")
		public ResponseEntity<?> getAttendLeave(@RequestParam HashMap<Object,Object> params) {
			
			String date = (String)params.get("date");

			Attend attend = attendService.getAttendLeave(params);
			
			return new ResponseEntity<>(attend,HttpStatus.OK);		
		}
		
		

}
