package com.kh.semiProject.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.semiProject.member.model.service.MemberService;
import com.kh.semiProject.member.model.vo.Member;

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
		String swalIcon = null;
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
			
			
			// 로그인 작업을 위한 Controller ----------------------
			else if(command.equals("/login.do")) {
				errorMsg = "로그인 작업 중 오류 발생";
				
				// 파라미터 얻어오기
				String memberId = request.getParameter("memberId");
				String memberPwd = request.getParameter("memberPwd");
				String save = request.getParameter("save");
				
				// 로그인 모달창에 잘 적용 되는지 확인용 콘솔
				// System.out.println(memberId + "/" + memberPwd + "/" + save);
				
				// JDBC 수행 (아이디, 비번 Member객체에 담아주기)
				Member member = new Member();
				member.setMemId(memberId);
				member.setMemPw(memberPwd);
				
				Member loginMember = service.loginMember(member);
				
				// Service에서 값을 잘 받아왔는지 확인하기위한 콘솔
				// System.out.println(loginMember);
				
				// 응답화면 문서타입 지정
				response.setContentType("text/html; charset=UTF-8");
				
				// session 객체를 얻어와 로그인 정보를 추가
				HttpSession session = request.getSession();
				
				// 로그인이 성공했을 때만 Session에 로그인 정보 추가
				if(loginMember != null) {
					// 30분동안 동작이 없을 경우 Session 만료
					session.setMaxInactiveInterval(60*30);
					
					// Session에 로그인 정보 추가
					session.setAttribute("loginMember", loginMember);
					
					// 아이디를 쿠키에 저장
					// 쿠키 객체 생성
					Cookie cookie = new Cookie("saveId", memberId);
					
					// 아이디 저장 체크가 되었는지 확인
					// 일주일동안 쿠키 유효
					if(save != null) {
						cookie.setMaxAge(60*60*24*7); // 일주일
					}else {
						// 아이디 저장 체크가 안된경우
						cookie.setMaxAge(0);
					}
					
					// 쿠키 유효 디렉토리 지정 (semiProject)
					cookie.setPath(request.getContextPath());
					
					// 생성된 쿠키를 클라이언트로 전달
					// → 응답 성공 시 클라이언트 컴퓨터에 쿠키파일 자동저장
					response.addCookie(cookie);
				}else {
					session.setAttribute("swalIcon", "error");
					session.setAttribute("swalTitle", "로그인 실패");
					session.setAttribute("swalText", "아이디 또는 비밀번호를 확인해주세요.");
				}
				
				response.sendRedirect(request.getHeader("referer"));
			}
			
			
			// 로그아웃 기능 controller ---------------------------
			else if(command.equals("/logout.do")) {
				// 세션 만료(무효화)
				request.getSession().invalidate();
				
				// 로그아웃 후 메인 또는 로그아웃 을 수행한 페이지로
				// response.sendRedirect(request.getContentType()); // 메인으로
				response.sendRedirect(request.getHeader("referer")); // 로그아웃 한 페이지
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
			
			// 비밀번호 변경 Controller ---------------------
			else if(command.equals("/updatePwd.do")) {
				errorMsg = "비밀번호 변경 과정에서 오류 발생";
				
				// 현재 비밀번호, 새로운 비밀번호
				String currentPwd = request.getParameter("currentPwd"); // 현재 비밀번호
				String newPwd = request.getParameter("newPwd1"); // 새 비밀번호
				
				// 현재 로그인한 회원의 정보를 얻어와 확인
				HttpSession session = request.getSession();
				Member loginMember = (Member)session.getAttribute("loginMember");
				
				loginMember.setMemPw(currentPwd);
				
				// 비즈니스 로직 처리
				int result = service.updatePwd(loginMember, newPwd);
				
				if(result > 0) { // 비밀번호 변경 성공
					swalIcon = "success";
					swalTitle = "비밀번호가 변경되었습니다.";
				}else if(result == 0) { // 비밀번호 변경 실패
					
					
				}
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
