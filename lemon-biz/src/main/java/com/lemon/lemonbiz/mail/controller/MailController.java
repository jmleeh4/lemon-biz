package com.lemon.lemonbiz.mail.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lemon.lemonbiz.mail.model.service.MailService;
import com.lemon.lemonbiz.mail.model.vo.Mail;
import com.lemon.lemonbiz.member.model.service.MemberService;
import com.lemon.lemonbiz.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/mail")
@SessionAttributes({ "loginMember" })
@Slf4j
public class MailController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailService mailService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ResourceLoader resourceLoader;


// 여기부터 DQL
	// 각 카테고리당 페이지를 분리하지 않고 한 페이지에서 navbar를 통해 분기처리 하므로 /mailList 하나의 페이지에 모든 리스트 조회 결과를 보냄.
	@RequestMapping(value = "/mailList", method = RequestMethod.GET, 
					produces = "application/json; charset=utf8")
	@ResponseBody
	public ModelAndView mailList(ModelAndView mav, Member member, HttpServletRequest request) {
		
		// 전체 메일 조회
		List<Mail> list = mailService.selectMailList();
		
		// 현재 로그인 중인 사용자와 같은 부서 소속인이 보낸 메일만 조회
		HttpSession session = request.getSession();
		Member loginMember = (Member) session.getAttribute("loginMember");
		List<Mail> listDept = mailService.selectMailDept(loginMember);
		
		// 사용자가 발송한 메일 조회
		List<Mail> listMy = mailService.selectMyMail(loginMember);
				
		// 사용자가 수신한 메일 중 중요 메일로 체크한 매일 조회 
		List<Mail> listStarred = mailService.selectStarredMail(loginMember);
		
		
		log.debug("list = {}", list); 
		log.debug("listDept = {}", listDept);
		log.debug("listMy = {}", listMy);
		log.debug("listStarred = {}", listStarred);

		mav.addObject("list", list);
		mav.addObject("listDept", listDept);
		mav.addObject("listMy", listMy);
		mav.addObject("listStarred", listStarred);
		mav.setViewName("mail/mailList");

		return mav;
	}

	/*
	 * @RequestMapping("/selectMail.do") public ResponseEntity<?> selectMailList() {
	 * List<Mail> list = mailService.selectMailList(); return new
	 * ResponseEntity<>(list, HttpStatus.OK); }
	 */
// 여기까지 DQL
	
// 여기부터 DML
	
	@RequestMapping(value = "/sendMail")
	public String sendMail() {
	    return "/mail/sendMail";
	  }  
	
	@RequestMapping(value = "/mailSend")
	public String mailSend(Member member, 
			/* @RequestParam(value = "upFile", required = false) MultipartFile[] upFiles, */
			/* RedirectAttributes redirectAttr, */
			HttpServletRequest request, Model model) 
	/* throws IllegalStateException, IOException */
	{
		     									
		// 메일 작성 시 자동으로 기입할 작성자 정보를 먼저 호출한다.
		/*
		 * HttpSession session = request.getSession(); Member loginMember = (Member)
		 * session.getAttribute("loginMember"); Member myInfo =
		 * mailService.selectMyInfo(loginMember);
		 * 
		 * log.debug("myInfo = {}", myInfo); model.addAttribute("myInfo", myInfo);
		 */
		/* mav.setViewName("mail/sendMail"); */
		
		String mFrom = "lemonbiz.manager@gmail.com";
		String mTo = request.getParameter("mTo"); 
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String filename = request.getServletContext().getRealPath("resources/upload/mail/test.jpg");
		
		 try {
		      MimeMessage message = mailSender.createMimeMessage();
		      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		 
		      messageHelper.setFrom(mFrom);  
		      messageHelper.setTo(mTo);    
		      messageHelper.setSubject(title); 
		      messageHelper.setText(content); 
		      
		      FileSystemResource fsr = new FileSystemResource(filename);
		      messageHelper.addAttachment("test.jpg", fsr);
		     
		      mailSender.send(message);
		    } catch(Exception e){
		      System.out.println(e);
		    }
		 return "redirect:/mail/sendMail";
		} 
	}
		/*
		 * 
		 * 
		 * @RequestMapping("/mailForm.do") public void MailForm() { }
		 * 
		 * @RequestMapping(value = "/mailForm.do", method = RequestMethod.POST) public
		 * String mailEnroll(Mail mail, @RequestParam(value = "upFile", required =
		 * false) MultipartFile[] upFiles, RedirectAttributes redirectAttr,
		 * HttpServletRequest request) throws IllegalStateException, IOException {
		 * 
		 * List<Attachment> attachList = new ArrayList<>(); Attachment attach = new
		 * Attachment();
		 * 
		 * // 보안 정책에 의해서 사용자가 업로드 한 파일을 바로 수신자에게 전달할 수 없으므로 반드시 서버 컴퓨터에 1차 저장 후 이를전송하여야
		 * 함.* String saveDirectory =
		 * request.getServletContext().getRealPath("/resources/upload/mail");
		 * 
		 * String mFrom = request.getParameter("mFrom"); String mTo =
		 * request.getParameter("mTo"); String title = request.getParameter("title");
		 * String content = request.getParameter("content"); String filename =
		 * request.getParameter("fileName");
		 * 
		 * for (MultipartFile upFile : upFiles) { if (upFile.isEmpty()) continue; String
		 * renamedFilename = Utils.getRenamedFileName(upFile.getOriginalFilename());
		 * 
		 * File dest = new File(saveDirectory, renamedFilename);
		 * upFile.transferTo(dest);
		 * 
		 * attach.setOriginName(upFile.getOriginalFilename());
		 * attach.setReName(renamedFilename); attachList.add(attach);
		 * log.debug("attachList = {}", attachList); mail.setAttachList(attachList); }
		 * 
		 * try { MimeMessage message = mailSender.createMimeMessage(); MimeMessageHelper
		 * helper = new MimeMessageHelper(message, true, "UTF-8");
		 * 
		 * helper.setFrom(mFrom); helper.setTo(mTo); helper.setSubject(title);
		 * helper.setText(content);
		 * 
		 * // 파일첨부 FileSystemResource file = new FileSystemResource(new
		 * File(saveDirectory)); helper.addAttachment("test.hwp", file);
		 * 
		 * mailSender.send(message); int result = mailService.insertMail(mail);
		 * redirectAttr.addFlashAttribute("msg", "메일 전송 성공");
		 * 
		 * } catch (Exception e) { } return "redirect:/mail/mailList.do"; }
		 */
// 여기까지 DML
	
	
	
		/*
		 * @RequestMapping("/mailDetail.do") public ModelAndView
		 * mailDetail(@RequestParam("key") int key, ModelAndView mav) { // 1. 각 테이블별 쿼리로
		 * 처리 // Mail mail = mailService.selectOnemail(key); // 2. mybatis collection을
		 * 이용해서 join된 쿼리 전송 Mail mail = mailService.selectOneMailCollection(key);
		 * log.debug("mail = {}", mail);
		 * 
		 * mav.addObject("mail", mail);
		 * 
		 * mav.setViewName("/mail/mailDetail"); return mav; }
		 * 
		 * @RequestMapping(value = "/fileDownload.do")
		 * 
		 * @ResponseBody public Resource fileDownload(@RequestParam("key") int
		 * key, @RequestHeader("user-agent") String userAgent, HttpServletRequest
		 * request, HttpServletResponse response) throws UnsupportedEncodingException {
		 * // 1. Attachment 객체 가져오기 Attachment attach =
		 * mailService.selectOneAttachment(key);
		 * 
		 * // 2. Resource 객체 생성 String saveDirectory =
		 * request.getServletContext().getRealPath("/resources/upload/mail"); File
		 * downFile = new File(saveDirectory, attach.getReName()); Resource resource =
		 * resourceLoader.getResource("file:" + downFile); log.debug("resource = {}",
		 * resource);
		 * 
		 * // 3. 응답헤더 // IE대비 파일명 분기처리 boolean isMSIE = userAgent.indexOf("MSIE") != -1
		 * || userAgent.indexOf("Trident") != -1; String originalFilename =
		 * attach.getOriginName(); if (isMSIE) { // ie 구버젼을 위해 퍼센트인코딩을 명시적으로 처리. //
		 * 퍼센트인코딩(URLEncoder.encode)이후 공백을 의미하는 +를 %20로 다시 치환. originalFilename =
		 * URLEncoder.encode(originalFilename, "UTF-8")//
		 * %EC%B7%A8+%EC%97%85+%ED%8A%B9+%EA%B0%95.txt .replaceAll("\\+", "%20"); } else
		 * { originalFilename = new String(originalFilename.getBytes("UTF-8"),
		 * "ISO-8859-1"); }
		 * 
		 * response.setContentType("application/octet-stream; charset=utf-8");
		 * response.addHeader("Content-Disposition", "attachment; filename=\"" +
		 * originalFilename + "\"");
		 * 
		 * return resource; } }
		 */

	/*
	 * @RequestMapping(value = "/mail/mailSend") public String
	 * mailSend(HttpServletRequest request) { String mFrom =
	 * request.getParameter("mFrom"); String mTo = request.getParameter("mTo");
	 * String title = request.getParameter("title"); String content =
	 * request.getParameter("content"); String filename =
	 * request.getParameter("fileName");
	 * 
	 * try { MimeMessage message = mailSender.createMimeMessage(); MimeMessageHelper
	 * helper = new MimeMessageHelper(message, true, "UTF-8");
	 * 
	 * helper.setFrom(mFrom); helper.setTo(mTo); helper.setSubject(title);
	 * helper.setText(content);
	 * 
	 * 
	 * // 파일첨부 FileSystemResource file = new FileSystemResource(new
	 * File("E:/test.hwp"); helper.addAttachment("test.hwp", file);
	 * 
	 * 
	 * mailSender.send(message);
	 * 
	 * } catch(Exception e){ }
	 * 
	 * return "redirect:/mail/sendMail"; } }
	 */