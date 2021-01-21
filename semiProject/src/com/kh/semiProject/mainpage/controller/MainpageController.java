package com.kh.semiProject.mainpage.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.mainpage.model.service.MainService;


@WebServlet("/main/*")
public class MainpageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MainpageController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI(); // ex) wsp/board/list.do
		String contextPath = request.getContextPath(); // ex) wsp
		String command = uri.substring((contextPath + "/main").length());
		// ex) wsp/board/list.do-(wsp/board/) = /list.do만 남음
		
		String path = null;
		RequestDispatcher view = null;
		
		String swalIcon = null;
		String swalTitle = null;
		String swalText = null;
		
		String errorMsg = null;
		
		try {
			//service 얻어오기
			MainService service = new MainService();

			//입양/분양 최신 게시글 9개 얻어오기 
			//같은 9개 == 기존의 /adoption/list.do에서 사용되는 service, dao재활용
			//섬네일 사진, 글번호(안보임) 얻어오기. 게시판타입('B2' 입양/분양)
			
			
			
			//입양 후기 최신 게시글 3개 얻어오기-----------------
			//제목, 입양날짜, 글번호(안보임) 얻어오기.
			if(command.equals("/selectReview.do")) {

				//String brdType = request.getParameter("brdType");
				
				Map<String, Object> map = null;
				
				List<Board> rList = service.selectReviewMain();
				
				//3. 게시글 목록이 조회 되었을 때 썸네일 목록 조회 비즈니스 로직
				if(rList != null) {
					map = new HashMap<String, Object>();
					map.put("rList", rList);
					
					List<Image> iList = service.selectReviewThumb();
					
					//썸네일 목록이 비어있지 않은 경우
					if(!iList.isEmpty()) {
						map.put("iList", iList);
					}
				}
				
				Gson gson = new GsonBuilder().setDateFormat("yyyy년 MM월 dd일").create();
				gson.toJson(map, response.getWriter());
			}
			
			//공지사항 최신글 5개 얻어오기------------------
			//제목, 작성일, 글번호(안보임) 얻어오기
			else if(command.equals("/selectNotice.do")) {
				List<Board> nList = service.selectNoticeMain();
				
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				gson.toJson(nList, response.getWriter());
			}
			
			
			//입양/분양 최신글 9개 얻어오기------------------------
			else if(command.equals("/selectAdoption.do")) {
				Map<String, Object> map = null;
				
				List<Board> aList = service.selectAdoptMain();
				if(aList != null) {
					map = new HashMap<String, Object>();
					map.put("aList", aList);
					
					List<Image> iList = service.selectAdoptThumb();
					
					//썸네일 목록이 비어있지 않은 경우
					if(!iList.isEmpty()) {
						map.put("iList", iList);
					}
					//System.out.println(iList);
				}
				
//				for(Board brd : aList) {
//					System.out.println(brd.getBrdTitle());
//				}
				
				
				Gson gson = new Gson();
				gson.toJson(map, response.getWriter());
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			
			//예외 발생 시 errorPage.jsp로 요청 위임
			path = "/WEB-INF/views/common/errorPage.jsp";
			request.setAttribute("errorMsg", errorMsg);
			view = request.getRequestDispatcher(path);
			view.forward(request, response);
		}
		
	
	
	
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
