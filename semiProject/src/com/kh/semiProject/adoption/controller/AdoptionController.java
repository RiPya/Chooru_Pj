package com.kh.semiProject.adoption.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/adoption/*")
public class AdoptionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdoptionController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Front Controller 패턴
		// -클라이언트의 요청을 한 곳으로 집중시켜 개발하는 패턴
		// -요청마다 Servlet을 생성하는 것이 아닌 하나의 Servlet에 작성하여 관리가 용이해짐
		
		//Controller 역할 : 요청에 맞는 Service 호출, 응답을 위한 View 선택
		
		String uri = request.getRequestURI();
		// ex) /semiProject/member/login.do
		
		String contextPath = request.getContextPath();
		// /semiProject

		String command = uri.substring((contextPath + "/adoption").length());
		// uri에서 "/semiProject/adoption"(contextPath+"/adoption")를 제거하겠다
		// → login.do
	
//						System.out.println(uri);
//						System.out.println(contextPath);
//						System.out.println(command);
		
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
			
			//AdoptionService service = new AdoptionService();
			
			//현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			//입양/분양의 메뉴 카테고리를 얻어옴
			//adAll adDog adCat adEtc adTemp
			String adoptCode = request.getParameter("cd");
			
			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String boardType = request.getParameter("tp");
			
			
			
			//입양/분양 목록 연결 Controller
			if(command.equals("/list.do")) {
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";
				
				path = "/WEB-INF/views/adoption/adoptionList.jsp";
				
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			//입양/분양  상세 조회 연결 controller
			else if(command.equals("/view.do")) {
				errorMsg = "게시판 상세 조회 과정에서 오류 발생";

				path = "/WEB-INF/views/adoption/adoptionView.jsp";
				
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			// 입양/분양 글 작성 화면 연결 Controller
			else if(command.equals("/insertForm.do")) {
				errorMsg = "게시판 상세 조회 과정에서 오류 발생";

				path = "/WEB-INF/views/adoption/adoptionInsert.jsp";
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}

			// 입양/분양 글 수정 화면 연결 Controller
			else if(command.equals("/updateForm.do")) {
				errorMsg = "게시판 상세 조회 과정에서 오류 발생";
				
				path = "/WEB-INF/views/adoption/adoptionUpdate.jsp";
				
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
