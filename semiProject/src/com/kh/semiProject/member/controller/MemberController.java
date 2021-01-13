package com.kh.semiProject.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.semiProject.member.model.service.MemberService;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public MemberController() {
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

		String command = uri.substring((contextPath + "/member").length());
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
			
			MemberService service = new MemberService();
			
			
			//현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			//파라미터 tp는 필수로 전달해야함!!!!!
			//입양/분양, 자유, 고객센터, 마이페이지는 cd도 꼭 전달해야 함!!
			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String boardType = request.getParameter("tp");
			
			//마이페이지 코드(내 활동 조회, 내 정보 수정, 비밀번호 변경, 회원 탈퇴)
			String mypageCode = request.getParameter("cd");
			
			//마이페이지-내활동 조회 코드(내가 쓴 글 / 내가 쓴 댓글)
			String activeCode = request.getParameter("my");
			
			
			
			//회원가입 화면으로 연결해주는 Controller
			if(command.equals("/signUpForm.do")) {
				path = "/WEB-INF/views/member/signUpForm.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			} 
			
			// 아이디찾기 페이지 1 : 찾기 폼으로 연결--------------
			else if(command.equals("/idFind1.do")) {
				errorMsg = "아이디 찾기 과정에서 오류 발생";
				
				path = "/WEB-INF/views/member/idFind1.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			// 아이디 찾기 페이지 2 : 찾기 진행 및 결과로 요청 위임
			else if(command.equals("/myIdFind.do")) {
				errorMsg = "아이디 찾기 과정에서 오류 발생";
				
				path = "/WEB-INF/views/member/idFind2.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			// 비밀번호찾기 페이지1 : 찾기 (인증) 폼으로 연결 --------------
			else if(command.equals("/pwdFind1.do")) {
				errorMsg = "비밀번호 찾기 과정에서 오류 발생";
				
				path = "/WEB-INF/views/member/pwdFind1.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			// 비밀번호 찾기 페이지2 : 인증 완료 후 비밀번호 변경 폼으로 연결
			else if(command.equals("/myPwdFind.do")) {
				errorMsg = "비밀번호 찾기 과정에서 오류 발생";
				
				path = "/WEB-INF/views/member/pwdFind2.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);		
			}
			// 비밀번호 찾기 페이지 3 : 비번 변경 및 결과로 요청 위임
			else if(command.equals("/pwdFindResult.do")) {
				errorMsg = "비밀번호 변경 과정에서 오류 발생";
				
				path = "/WEB-INF/views/member/pwdFind3.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			// 마이페이지 연결 전 비밀번호 확인---------
			
			
			
			// 마이페이지: 내 활동 정보 목록 :-----------------------------
			//mypageCode가 myActive이면 내 활동 정보
			else if(command.equals("/myActiveList.do")) {
				errorMsg = "내 활동 정보 페이지 연결 과정에서 오류 발생";
				
				//activeCode가 myActiveList면 내가 쓴 글, myActiveReply이면 내가 쓴 댓글
				
				// 활동 정보를 보여주는 서블릿
				path = "/WEB-INF/views/member/myPage.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			
			
			// 마이페이지: 내 정보 수정으로 연결 :
			//mypageCode가 updateInfo이면 내 정보 수정
			else if(command.equals("/updateMyInfoForm.do")) {
				errorMsg = "내 정보 수정 페이지 연결 과정에서 오류 발생";
				
				// 내 정보 수정을 보여주는 서블릿
				path = "/WEB-INF/views/member/updateMyInfo.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}

			
			// 마이페이지: 비밀번호 변경으로 연결 :
			//mypageCode가 updatePwd이면 내 정보 수정
			else if(command.equals("/changePwdForm.do")) {
				errorMsg = "비밀번호 변경 페이지 연결 과정에서 오류 발생";
				
				// 내 정보 수정을 보여주는 서블릿
				path = "/WEB-INF/views/member/changePwd.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}

			
			
			
			//마이페이지: 탈퇴 설명 폼으로 연결-------------------
			//mypageCode가 updateStatus이면 탈퇴
			else if(command.equals("/updateStatusForm.do")) {
				errorMsg = "탈퇴 페이지 연결 과정에서 오류 발생";
				
				// 내 정보 수정을 보여주는 서블릿
				path = "/WEB-INF/views/member/secession.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			//마이페이지: 탈퇴 비번 확인 후 탈퇴으로 연결 -------------
			
		
			
			
			
			

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
