package com.kh.semiProject.notice.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/notice/*")
public class NoticeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public NoticeController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Front Controller 패턴
		// -클라이언트의 요청을 한 곳으로 집중시켜 개발하는 패턴
		// -요청마다 Servlet을 생성하는 것이 아닌 하나의 Servlet에 작성하여 관리가 용이해짐
		
		//Controller 역할 : 요청에 맞는 Service 호출, 응답을 위한 View 선택
		
		String uri = request.getRequestURI();
		// ex) /semiProject/notice/list.do
		
		String contextPath = request.getContextPath();
		// /semiProject

		String command = uri.substring((contextPath + "/notice").length());
		// uri에서 "/semiProject/notice"(contextPath+"/notice")를 제거하겠다
		// → list.do
	
//				System.out.println(uri);
//				System.out.println(contextPath);
//				System.out.println(command);
		
		//컨트롤러 내에서 공용으로 사용할 변수 미리 선언
		String path = null; //forward 또는 redirect 경로를 저장할 변수
		RequestDispatcher view = null; //요청 위임 객체
		
		//sweet alert로 메시지 전달하는 용도
		String swalICon = null;
		String swalTitle = null;
		String swalText = null;
		
		//에러 메시지 전달용 변수
		String errorMsg = null;
		
		
		try {
			
			//NoticeService service = new //NoticeService();
			
			
//AdoptionService service = new AdoptionService();
			
			//현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			
			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String boardType = request.getParameter("tp");
			
			
			//자유게시판 목록 연결 Controller
			if(command.equals("/list.do")) {
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";
				
				//1.페이징 처리를 위한 값 계산 service 호출
				//PageInfo pInfo = service.getPageInfo(cp);
				
				/* System.out.println(pInfo); */
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				//List<Free> bList = service.selectFreeList(pInfo);
				//pInfo에 있는 currentPage, limit을 사용해야지만
				//현재 페이지에 맞는 게시글 목록만 조회할 수 있음
				
				path = "/WEB-INF/views/notice/noticeList.jsp";
				
				//request.setAttribute("fList", fList);
				//request.setAttribute("pInfo", pInfo);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			
			
			//-자유게시판 상세 조회 Controller--------------------------------------
			else if(command.equals("/view.do")) {
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";


				path = "/WEB-INF/views/notice/noticeView.jsp";
				
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
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
