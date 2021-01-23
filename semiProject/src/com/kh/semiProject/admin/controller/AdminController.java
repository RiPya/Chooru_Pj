package com.kh.semiProject.admin.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.semiProject.admin.model.service.AdminService;
import com.kh.semiProject.member.model.vo.Member;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Front Controller 패턴
		// -클라이언트의 요청을 한 곳으로 집중시켜 개발하는 패턴
		// -요청마다 Servlet을 생성하는 것이 아닌 하나의 Servlet에 작성하여 관리가 용이해짐
		
		//Controller 역할 : 요청에 맞는 Service 호출, 응답을 위한 View 선택
		
		String uri = request.getRequestURI();
		// ex) /semiProject/information/login.do
		
		String contextPath = request.getContextPath();
		// /semiProject

		String command = uri.substring((contextPath + "/admin").length());
		// uri에서 "/semiProject/member"(contextPath+"/information")를 제거하겠다
		// → login.do
	
//						System.out.println(uri);
//						System.out.println(contextPath);
//						System.out.println(command);
		
		//컨트롤러 내에서 공용으로 사용할 변수 미리 선언
		String path = null; //forward 또는 redirect 경로를 저장할 변수
		RequestDispatcher view = null; //요청 위임 객체
		
		//sweet alert로 메시지 전달하는 용도
		String swalIcon = null;
		String swalTitle = null;
		String swalText = null;
		
		//에러 메시지 전달용 변수
		String errorMsg = null;
	
		
		try {
			AdminService service = new AdminService();
			
			//현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			//파라미터 tp는 필수로 전달해야함!!!!!
			//입양/분양, 자유, 고객센터, 마이페이지는 cd도 꼭 전달해야 함!!
			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String brdType = request.getParameter("tp");
			
			
			//회원 관리 목록 ------------------------
			if(command.equals("/adminMem.do")) {
	            errorMsg = "회원 관리 페이지 연결 과정에서 오류 발생";
	            
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getPageInfo(cp);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Member> mList = service.selectMemberList(pInfo);
				//pInfo에 있는 currentPage, limit을 사용해야지만
				//현재 페이지에 맞는 게시글 목록만 조회할 수 있음
	            //System.out.println("m" + mList);
				request.setAttribute("mList", mList);
				request.setAttribute("pInfo", pInfo);
				
	            path = "/WEB-INF/views/admin/adminMem.jsp";
	            view = request.getRequestDispatcher(path);
	            view.forward(request, response);
	         }
			
			
			// 회원 등급 변경 Controller -------------------------------
			else if(command.equals("/updateAdminMem.do")){
	            errorMsg = "회원 등급 변경 과정에서 오류 발생";
	            
	            String numberList = request.getParameter("numberList"); // 회원 번호가 담겨있음
	            //System.out.println(numberList);
	            
	            String grade = request.getParameter("grade");
	            //System.out.println(grade);
	            
	            int result = service.updateMemGrade(numberList, grade);
	            System.out.println(result);
	            response.getWriter().print(result);
	         }
			
			
			//게시글 블라인드 controller ---------------------------------
			else if (command.equals("/blindBrd.do")) {
				int brdNo = Integer.parseInt(request.getParameter("no"));
				
				int result = service.blindBrdStatus(brdNo);
				
				if(result>0) {
					//블라인드 성공 시
					swalIcon = "success";
					swalTitle = "게시글 블라인드 성공";
					
					//공지사항, 고객센터는 블라인드가 필요 없을 듯?
					switch(brdType) {
					case "b2" : path = "/semiProject/adoption/"; break;//입양/분양
					case "b3" : path = "/semiProject/review/"; break;//입양 후기
					case "b4" : path = "/semiProject/free/"; break;//자유
					}
					
					path += "list.do";

				} else {
					//삭제 실패 시 : 삭제 시도한 게시글의 상세 조회 페이지로 redirect
					swalIcon = "error";
					swalTitle = "게시글 블라인드 실패";
					
					//이전 페이지의 상세 주소 
					path = request.getHeader("referer");
				}
				
				request.getSession().setAttribute("swalIcon", swalIcon);
	            request.getSession().setAttribute("swalTitle", swalTitle);

				response.sendRedirect(path);
			}
			
			
			//게시글 관리 페이지 연결
			else if(command.equals("/adminBrd.do")) {
				errorMsg = "게시글 관리 조회 과정에서 오류 발생";
		        
				//System.out.println("연결2");
				
				PageInfo pInfo = service.getBrdPInfo(cp);
				
				//System.out.println("pinfo: "+pInfo);
			
				
				List<Board> bList = service.selectBrdList(pInfo);
				
				//System.out.println("blist: "+bList);
				
				request.setAttribute("bList", bList);
				request.setAttribute("pInfo", pInfo);
				
				
	            path = "/WEB-INF/views/admin/adminBrdStatus.jsp";
	            view = request.getRequestDispatcher(path);
	            view.forward(request, response);
			}
			
			
			//게시글 상태 변경 controller
			else if(command.equals("/updateBrdSt.do")) {
				errorMsg = "게시글 상태 변경 과정에서 오류 발생";
				
				//System.out.println("넘어왔어");
				
				String brdNoList = request.getParameter("checkList");
				
				String status = request.getParameter("status");
				
				int result = service.updateBrdStatus(brdNoList, status);
				
				
	            System.out.println(result);
	            response.getWriter().print(result);
				
				//System.out.println(result);
//				
//				if(result > 0) {
//					swalIcon = "success";
//					swalTitle = "게시글 상태 변경 성공";
//					path = "adminBrd.do?tp=adminMem&cd=adBrd";
//				} else {
//					swalIcon = "error";
//					swalTitle = "게시글 상태 변경 실패";
//					
//					//이전 페이지의 상세 주소 
//					path = request.getHeader("referer");
//				}
//				request.getSession().setAttribute("swalIcon", swalIcon);
//	            request.getSession().setAttribute("swalTitle", swalTitle);
//
//				response.sendRedirect(path);
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
