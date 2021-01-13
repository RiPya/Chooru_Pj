package com.kh.semiProject.search.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/search/*")
public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		// ex) /semiProject/member/login.do
		
		String contextPath = request.getContextPath();
		// /semiProject

		String command = uri.substring((contextPath + "/search").length());
		// uri에서 "/semiProject/member"(contextPath+"/member")를 제거하겠다
		// → login.do
	
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
			
//			SearchService service = new SearchService();
			
			String cp = request.getParameter("cp");
			
			String code = request.getParameter("cd");
			
			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String boardType = request.getParameter("tp");
			
			String searchKey = request.getParameter("sk");
			String searchValue = request.getParameter("sv");
			
			
			
			//전체 검색 Controller
			if(command.equals("/search.do")) {
				errorMsg = "전체 검색 과정에서 오류 발생";
				
				//1.페이징 처리를 위한 값 계산 service 호출
				//PageInfo pInfo = service.getPageInfo(cp);
				
				/* System.out.println(pInfo); */
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				//List<Free> bList = service.selectFreeList(pInfo);
				//pInfo에 있는 currentPage, limit을 사용해야지만
				//현재 페이지에 맞는 게시글 목록만 조회할 수 있음
				
				path = "/WEB-INF/views/search/searchList.jsp";
				
				//request.setAttribute("fList", fList);
				//request.setAttribute("pInfo", pInfo);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			//공지 게시판 search
			else if(command.equals("/noticeSearch.do")) {
				errorMsg = "공지사항 검색 과정에서 오류 발생";
				
				path = "/WEB-INF/views/notice/noticeList.jsp";
				//검색 과정은 모두 이 과정으로?
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
		} catch (Exception e) {
			
			
		}
	
	
	
	
	
	
	
	
	
	
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
