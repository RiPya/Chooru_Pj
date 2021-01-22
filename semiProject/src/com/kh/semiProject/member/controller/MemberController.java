package com.kh.semiProject.member.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.service.MemberService;
import com.kh.semiProject.member.model.vo.Member;
import com.kh.semiProject.reply.model.vo.Reply;
import com.sun.javafx.collections.MappingChange.Map;

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
			
			// 회원가입 Controller------------------------------
			else if(command.equals("/signUp.do")) {
				errorMsg = "회원가입 진행 중 오류 발생";
				
				// 전달받은 파라미터 변수에 저장
				String memId = request.getParameter("id");
				String memPw = request.getParameter("pwd1");
				String memNm = request.getParameter("name");
				String phone = request.getParameter("phone");
				String nickName = request.getParameter("nickName");
				String email = request.getParameter("email");
				
				char petYn = request.getParameter("petYn").charAt(0);
				
				
				// Member 객체에 파라미터 모두 저장
				Member member = new Member(memId, 
										   memPw, 
										   memNm, 
										   phone, 
										   nickName, 
										   email, 
										   petYn);
				
				
				// 비즈니스 로직
				int result = service.signUp(member);
				
				
				if(result > 0) {
					swalIcon = "success";
					swalTitle = "회원가입 되셨습니다.";
					swalText = nickName + "님의 회원가입을 환영합니다.";
				}else {
					swalIcon = "error";
					swalTitle = "회원가입에 실패하셨습니다.";
					swalText = "적절하지않은 정보가 포함되어 있습니다.";
				}
				
				HttpSession session = request.getSession();
				
				session.setAttribute("swalIcon", swalIcon);
				session.setAttribute("swalTitle", swalTitle);
				session.setAttribute("swalText", swalText);
				
				response.sendRedirect(request.getContextPath());
			}
			
			// 아이디 중복검사 Controller ------------------
			else if(command.equals("/idDupCheck.do")) {
				// 아이디 중복검사용 파라미터
				String id = request.getParameter("id");
				
				int result = service.idDupCheck(id);
				
				response.getWriter().print(result);
				// System.out.println(result);
			}
			
			// 닉네임 중복검사 Controller ---------------------
			else if(command.equals("/nickNameDupCheck.do")) {
				// 닉네임 중복검사용 파라미터
				String nickName = request.getParameter("nickName");
				
				int result = service.nickNameDupCheck(nickName);
				
				response.getWriter().print(result);
				// System.out.println("닉네임: " + result);
			}
			
			// 이메일 중복검사 Controller -----------------------
			else if(command.equals("/emailDupCheck.do")) {
				// 이메일 중복검사용 파라미터
				String email = request.getParameter("email");
				// System.out.println("입력: " + email);
				
				int result = service.emailDupCheck(email);
				
				response.getWriter().print(result);
				// System.out.println(result);
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
				response.sendRedirect(request.getContextPath()); // 메인으로
				// response.sendRedirect(request.getHeader("referer")); // 로그아웃 한 페이지
			}
			
			
			// 아이디찾기 페이지 1 : 찾기 폼으로 연결--------------
			else if(command.equals("/idFind1.do")) {
				errorMsg = "아이디 찾기 과정에서 오류 발생";
				
				// 파라미터 얻어오기
				String memberName = request.getParameter("memberName");
				String email = request.getParameter("email");
				
				// 회원 번호 얻어오기
				HttpSession session = request.getSession();
				Member loginMember = (Member)session.getAttribute("loginMember");
				
				loginMember.setEmail(email);
				
				Member member = new Member();
				member.setMemNm(memberName);
				member.setEmail(loginMember.getEmail());
				
				// 회원 확인
				int result = service.emailCheck(member);
				
				if(result > 0) { // 이메일이 받아져왔을때
					path = "/WEB-INF/views/member/idFind1.jsp";
				}else {
					swalIcon = "error";
					swalTitle = "이메일 입력을 다시 확인해주세요.";
				}
				
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
			else if(command.equals("/myActiveListForm.do")) {
				errorMsg = "비밀번호 확인 과정에서 오류 발생";
				
				String currentPwd = request.getParameter("currentPwd"); // 현재 비밀번호
				
				// 회원 정보
				HttpSession session = request.getSession();
				Member loginMember = (Member)session.getAttribute("loginMember");
				
				loginMember.setMemPw(currentPwd);
				
				int result = service.checkPwd(loginMember);
				// System.out.println(result);
				
				if(result > 0) {
					path = "myActiveList.do?tp=mypage";
				
				}else {
					swalIcon = "error";
					swalTitle = "비밀번호를 잘못 입력하셨습니다.";
					path = request.getHeader("referer");
				}
				
				session.setAttribute("swalIcon", swalIcon);
				session.setAttribute("swalTitle", swalTitle);
				response.sendRedirect(path);
			}
			
			
			// 마이페이지: 내 활동 정보 목록 :-----------------------------
			//mypageCode가 myActive이면 내 활동 정보
			else if(command.equals("/myActiveList.do")) {
				errorMsg = "내 활동 정보 페이지 연결 과정에서 오류 발생";
				//activeCode가 myActiveList면 내가 쓴 글, myActiveReply이면 내가 쓴 댓글
				
				
				// 세션에 로그인 되어있는 내 정보 가져오기
				HttpSession session = request.getSession();
				Member loginMember = (Member)session.getAttribute("loginMember");
				
				// 페이징 처리를 위한 Service 호출
				PageInfo pInfo = service.getPageInfo(cp, loginMember);
				
				// 게시글 목록조회 비즈니스 로직
				List<Board> bList = service.selectMyActiveList(pInfo, loginMember);
				// System.out.println("내 게시글 목록 " + bList);
				
				// 게시글 목록에 썸네일 목록 조회 비즈니스 로직
				if(bList != null) {
					List<Image> iList = service.myActiveImage(pInfo, loginMember);
					// System.out.println("내 게시글 이미지 " + iList);
					
					// 썸네일 목록이 비어있지 않을 때
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
				}
				
				request.setAttribute("bList", bList);
				request.setAttribute("pInfo", pInfo);
				
				// 활동 정보를 보여주는 서블릿
				path = "/WEB-INF/views/member/myPage.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			else if(command.equals("/myActiveReply.do")) {
				errorMsg = "내가 쓴 댓글 보기 페이지 연결 과정에서 오류 발생";
				
				// 세션에 로그인 되어있는 내 정보 가져오기
				HttpSession session = request.getSession();
				Member loginMember = (Member)session.getAttribute("loginMember");
				
				// 페이징 처리를 위한 Service 호출
				PageInfo pInfo = service.getReplyPageInfo(cp, loginMember);
				
				// 댓글 목록조회 비즈니스 로직
				List<Reply> pList = service.selectMyReplyList(pInfo, loginMember);
				// System.out.println(pList);
				
				request.setAttribute("pList", pList);
				request.setAttribute("pInfo", pInfo);
				
				// 활동 정보를 보여주는 서블릿
				path = "/WEB-INF/views/member/myPage2.jsp";
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
			
			// 내 정보 수정 Controller --------------------------
			else if(command.equals("/updateMyInfo.do")) {
				errorMsg = "내 정보 수정 과정에서 오류 발생";
				
				String name = request.getParameter("name");
				String phone = request.getParameter("phone");
				String email = request.getParameter("email");
				String nickName = request.getParameter("nickName");
				char petYn = request.getParameter("petYn").charAt(0);
				
				// Session에 있는 로그인 정보 얻어오기(회원정보)
				HttpSession session = request.getSession();
				Member loginMember = (Member)session.getAttribute("loginMember");
				
				// 수정정보와 회원정보를 하나의 객체에 담기
				Member member = new Member();
				member.setMemNo(loginMember.getMemNo());
				member.setMemNm(name);
				member.setPhone(phone);
				member.setEmail(email);
				member.setNickName(nickName);
				member.setPetYn(petYn);
				
				// 비즈니스 로직
				int result = service.updateMyInfo(member);
				
				if(result > 0) {
					swalIcon = "success";
					swalTitle = "회원 정보 수정 성공";
					swalText = "회원 정보가 수정되었습니다.";
					
					// DB 데이터가 갱신 된 경우, 세션에 담긴 회원 정보도 갱신
					// 기존 로그인 정보에서 id를 얻어와, 갱신에 사용된 member객체에 저장
					// → member 객체가 갱신된 회원 정보를 모두 갖게된다.
					member.setMemId(loginMember.getMemId());
					member.setMemNm(loginMember.getMemNm());
					member.setGrade(loginMember.getGrade());
					
					// Session에 있는 loginMember 정보를 member로 갱신
					session.setAttribute("loginMember", member);
				}else {
					swalIcon = "error";
					swalTitle = "회원 정보 수정 실패";
					swalText = "고객센터에 문의 바랍니다.";
				}
				
				session.setAttribute("swalIcon", swalIcon);
				session.setAttribute("swalTitle", swalTitle);
				session.setAttribute("swalText", swalText);
				
				response.sendRedirect(request.getHeader("referer"));
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
					swalIcon = "error";
					swalTitle = "비밀번호 변경에 실패하였습니다.";
				}else { // 비밀번호 불일치
					swalIcon = "warning";
					swalTitle = "현재 비밀번호가 일치하지 않습니다.";
				}
				session.setAttribute("swalIcon", swalIcon);
				session.setAttribute("swalTitle", swalTitle);
				
				response.sendRedirect(request.getHeader("referer"));
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
			
			//마이페이지: 탈퇴  Controller-------------
			else if(command.equals("/updateStatus.do")) {
				errorMsg = "탈퇴 과정 중 오류 발생";
				
				// 현재 비밀번호 얻어오기
				String currentPwd = request.getParameter("currentPwd");
				
				// 세션에 로그인 되어있는 내 정보 가져오기
				HttpSession session = request.getSession();
				Member loginMember = (Member)session.getAttribute("loginMember");
				
				// loginMember 객체에 현재 비밀번호 세팅
				loginMember.setMemPw(currentPwd);
				
				// 비즈니스 로직 수행
				int result = service.updateStatus(loginMember);
				
				if(result > 0) {
					swalIcon = "success";
					swalTitle = "탈퇴되었습니다.";
					
					// 탈퇴 성공 시 (로그아웃 후 메인페이지 이동)
					session.invalidate();
					
					// 세션 무효화 시 현재 얻어온 세션을 사용할 수 없으므로
					// 새로운 세션 얻어오기
					session = request.getSession();
					
					path = request.getContextPath();

				}else if(result == 0) {
					swalIcon = "error";
					swalTitle = "회원 탈퇴에 실패하셨습니다.";
					
					// 탈퇴 페이지 유지
					path = "updateStatusForm.do";
				
				}else {
					swalIcon = "warning";
					swalTitle = "현재 비밀번호가 일치하지 않습니다.";
					
					path = "updateStatusForm.do";
				}
				
				session.setAttribute("swalIcon", swalIcon);
				session.setAttribute("swalTitle", swalTitle);
				
				response.sendRedirect(path);
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
