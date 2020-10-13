package com.lemon.lemonbiz.board.controller;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lemon.lemonbiz.board.model.service.BoardService;
import com.lemon.lemonbiz.board.model.vo.Board;
import com.lemon.lemonbiz.board.model.vo.BoardComment;
import com.lemon.lemonbiz.common.Utils;
import com.lemon.lemonbiz.common.vo.Attachment;
import com.lemon.lemonbiz.common.vo.Paging;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private ResourceLoader resourceLoader;
	
	@RequestMapping("/boardList.do")
	public ModelAndView boardList(ModelAndView mav,HttpServletRequest request) {

		int numPerPage = 10;
		int cPage = 1;
		int startRnum = (cPage-1) * numPerPage + 1;
		int endRnum = cPage * numPerPage;
		try {
			cPage = Integer.parseInt(request.getParameter("cPage"));
		} catch (NumberFormatException e) {
			
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("startRnum", startRnum);
		map.put("endRnum", endRnum);
		List<Board> list = boardService.selectBoardList(map);
		int totalContents = boardService.countBoard();
		
		String url = request.getRequestURI();
		String pageBar = Paging.getPageBarHtml(cPage, numPerPage, totalContents, url);
		
		log.debug("list = {}", list);
		mav.addObject("list", list);
		mav.addObject("pagebar",pageBar);
		
		mav.setViewName("board/boardList");
		return mav;
	}
	
	@RequestMapping("/selectList.do")
	public ResponseEntity<?> selectList(){
		List<Map<String, Object>> list = boardService.selectBoardMapList();
		return new ResponseEntity<>(list, HttpStatus.OK);  
	}
	
	
	@RequestMapping("/boardForm.do")
	public void boardForm() {

	}
	
	@RequestMapping(value = "/boardEnroll.do",
			method = RequestMethod.POST)
	public String boardEnroll(Board board, 
						  @RequestParam(value = "upFile",
								  	    required = false) MultipartFile[] upFiles,
						  RedirectAttributes redirectAttr,
						  HttpServletRequest request) 
								  throws IllegalStateException, IOException {
	log.debug("board = {}", board);
	
	
	//1. 서버컴퓨터에 업로드한 파일 저장하기
	List<Attachment> attachList = new ArrayList<>();
	
	//저장경로
	String saveDirectory = request.getServletContext()
								  .getRealPath("/resources/upload/board");
	
	for(MultipartFile upFile : upFiles) {
		//파일을 선택하지 않고 전송한 경우
		if(upFile.isEmpty())
			continue;
		
		//1.파일명(renameFilename) 생성
		String renamedFilename = Utils.getRenamedFileName(upFile.getOriginalFilename());
		
		//2.메모리의 파일 -> 서버컴퓨터 디렉토리 저장  transferTo
		File dest = new File(saveDirectory, renamedFilename);
		upFile.transferTo(dest);
		
		//3.attachment객체 생성
		Attachment attach = new Attachment();
		attach.setOriginName(upFile.getOriginalFilename());
		attach.setReName(renamedFilename);
		attachList.add(attach);
		
	//		log.debug("upFile.name = {}", upFile.getOriginalFilename());
	//		log.debug("upFile.size = {}", upFile.getSize());
		
	}
	
	log.debug("attachList = {}", attachList);
	board.setAttachList(attachList);
	
	
	//2. Board, Attachment객체 DB에 저장하기
	int result = boardService.insertBoard(board);
	
	//3. 처리결과 msg 전달
	redirectAttr.addFlashAttribute("msg", "게시글 등록 성공");
	
	
	return "redirect:/board/boardList.do";
	}

	
	@RequestMapping("boardDetail.do")
	public ModelAndView boardDeatil(@RequestParam("key") int key, ModelAndView mav,
						  			HttpServletRequest request, HttpServletResponse response) {
		
		Cookie[] cookies = request.getCookies();
		String boardCookieVal = "";
		boolean hasRead = false;//현재 요청(브라우져)에서 이 게시글을 이미 읽었는가 여부
		
		if(cookies != null) {
			for(Cookie c : cookies) {
				String name = c.getName();
				String value = c.getValue();
				
				if("boardCookie".equals(name)) {
					boardCookieVal = value;
//					System.out.println(name + " = " + value);
					
					//이번 게시글 읽음 여부
					if(value.contains("[" + key + "]")) {
						hasRead = true;
						break;
					}
					
				}
			}
		}
		
		//게시글을 읽지 않은 경우
		if(hasRead == false) {
			Cookie boardCookie = new Cookie("boardCookie",
											boardCookieVal + "[" + key + "]");
			boardCookie.setMaxAge(365*24*60*60);//영속쿠키
			// /mvc/board/view
			boardCookie.setPath(request.getContextPath() + "/board/");
			response.addCookie(boardCookie);
		}
		
		//collection 이용 join
		Board board = boardService.selectOneBoardCollection(key,hasRead);
		List<BoardComment> commentList = boardService.selectCommentList(key);
		
		log.debug("commentList = {}", commentList);
		mav.addObject("board", board);
		mav.addObject("commentList", commentList);
		
		mav.setViewName("board/boardDetail");
		
		return mav;
	}
	
	@RequestMapping(value = "/fileDownload.do")
	@ResponseBody
	public Resource fileDownload(@RequestParam("key") int key,
								 @RequestHeader("user-agent") String userAgent,
								 HttpServletRequest request,
								 HttpServletResponse response) throws UnsupportedEncodingException {
		//1. Attachment 객체 가져오기
		Attachment attach = boardService.selectOneAttachment(key);
		
		//2. Resource 객체 생성
		String saveDirectory = request.getServletContext()	
									  .getRealPath("/resources/upload/board");
		File downFile = new File(saveDirectory, attach.getReName());
		Resource resource = resourceLoader.getResource("file:" + downFile);
		log.debug("resource = {}", resource);
		
		//3. 응답헤더
		//IE대비 파일명 분기처리
		boolean isMSIE = userAgent.indexOf("MSIE") != -1 
                	  || userAgent.indexOf("Trident") != -1;
		String originalFilename = attach.getOriginName();
	    if(isMSIE){
	        //ie 구버젼을 위해 퍼센트인코딩을 명시적으로 처리. 
	    	//퍼센트인코딩(URLEncoder.encode)이후 공백을 의미하는 +를 %20로 다시 치환.
	        originalFilename = URLEncoder.encode(originalFilename, "UTF-8")//%EC%B7%A8+%EC%97%85+%ED%8A%B9+%EA%B0%95.txt
	        							 .replaceAll("\\+", "%20");
	    } 
	    else {
	        originalFilename = new String(originalFilename.getBytes("UTF-8"),"ISO-8859-1");
	    }

	    response.setContentType("application/octet-stream; charset=utf-8");
		response.addHeader("Content-Disposition", "attachment; filename=\"" + originalFilename + "\"");//쌍따옴표 사용하지 말것.
		
		return resource;
	}
	
	@RequestMapping(value ="/boardUpdate.do")
	
	public ModelAndView boardUpdate(@RequestParam("key") int key ,ModelAndView mav) {

		Board board = boardService.selectOneBoardCollection(key);
		mav.addObject("board", board);
	    boardService.updateBoard(board,key);
	    mav.setViewName("board/boardForm2");
 
	    return mav; 
	}
	
	@RequestMapping(value="/boardInsert.do", method = RequestMethod.POST)
	
	public void boardInsert(@ModelAttribute BoardComment boardComment) {		
		
		boardService.boardInsert(boardComment);
		 
 
	}
}
