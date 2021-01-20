package com.kh.semiProject.notice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.vo.Member;
import com.kh.semiProject.notice.model.service.NoticeService;

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
		String swalIcon = null;
		String swalTitle = null;
		String swalText = null;
		
		//에러 메시지 전달용 변수
		String errorMsg = null;
		
		
		try {
			NoticeService service = new NoticeService();
			
			//현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			
			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String brdType = request.getParameter("tp");
			
			
			//공지 목록 조회 Controller
			if(command.equals("/list.do")) {
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";
				
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getPageInfo(cp);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Board> nList = service.selectNoticeList(pInfo);
				//pInfo에 있는 currentPage, limit을 사용해야지만
				//현재 페이지에 맞는 게시글 목록만 조회할 수 있음
				
				// 게시글 목록이 조회 되었을 때 썸네일 목록 조회 비즈니스
				if(nList != null) {
					List<Image> iList = service.selectThumbnails(pInfo);

					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
				}
				
				request.setAttribute("nList", nList);
				request.setAttribute("pInfo", pInfo);
				
				path = "/WEB-INF/views/notice/noticeList.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			
			
			//-공지글 상세 조회 Controller--------------------------------------
			else if(command.equals("/view.do")) {
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";
				
				int brdNo = Integer.parseInt(request.getParameter("no"));
				
				Board notice = service.selectNotice(brdNo);
				
				if(notice != null) {
					request.setAttribute("notice", notice);

					path = "/WEB-INF/views/notice/noticeView.jsp";
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
				}else {
					request.getSession().setAttribute("swalIcon", "error");
					request.getSession().setAttribute("swalTitle", "게시판 상세 조회");
				}

				
			}
			
			// 공지글 작성 폼 연결 Controller ---------------------------------
			else if(command.equals("/noticeInsertForm.do")) {
				errorMsg = "공지사항 작성 페이지 연결 과정에서 오류 발생";
				
				path = "/WEB-INF/views/notice/noticeInsertForm.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			// 공지글 작성 Controller ---------------
			else if(command.equals("/noticeInsert.do")) {
				errorMsg = "게시글 등록 과정에서 오류 발생";
				
				// 1. 게시글 작성 파라미터 얻어오기
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				
				// 2. 로그인 정보
				Member loginMember = (Member)request.getSession().getAttribute("loginMember");
				int memNo = loginMember.getMemNo();
				
				// 3. content에 있는 img 태그 내 src 선택 후 image url 목록 반환받기
				List<String> imgUl = service.getImageList(content);
				
				// 4. imgUl을 이용하여 DB에 저장할 이미지데이터 목록 만들기
				List<Image> iList = new ArrayList<Image>();
				
				if(!imgUl.isEmpty()) { // imgUl이 있을 때 == 이미지 첨부 O
					int level = 0; // 사진 순서(0은 썸네일)
					
					for(String url : imgUl) {
						// src 뒤에서부터 첫번째 / index 뽑기
						int slash = url.lastIndexOf('/');
						
						Image temp = new Image();
						
						//문자열.substring() 사용해서 url에서 파일명, 파일경로 분리하기
						//substring(start) ~ start인덱스부터 마지막 인덱스까지 잘라내서 저장
						//substring(start, end) ~ start인덱스부터 end인덱스 전까지 잘라내서 저장
						
						temp.setFileName(url.substring(slash)+1);
						//ex) uploadImages/image1.jpg → image1.jpg
						temp.setFilePath(url.substring(0, slash+1)); 
						//ex) uploadImages/image1.jpg → uploadImages/
						temp.setFileLevel(level++); 
						
						iList.add(temp);
					}
				}
				
				// 4-1. 사진 삽입 실패 시 사진을 삭제할 기본 주소 추가
				String root = request.getSession().getServletContext().getRealPath("/");
				
				// 5. 입력 내용 + 이미지 List를 Map 에 담아서 service로 호출, 삽입 결과 반환받기
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", title);
				map.put("content", content);
				map.put("memNo", memNo); // 작성자(회원번호)
				map.put("brdType", brdType);//게시판타입(B#)				
				map.put("iList", iList);
				map.put("root", root);
				// System.out.println(iList + "ilst");
				
				int result = service.insertNotice(map);
				// System.out.println(result);
				// 삽입 성공시 result에 삽입 게시글 번호가 저장되어있다.
				// 실패시 0
				if(result > 0) {
					swalIcon = "success";
					swalTitle = "공지사항 등록 성공";
					path = "view.do?tp=b1&cp=1&no=" + result;
					// 작성된 글 번호와 게시판 타입 + 목록페이지를 파라미터로 전달
				}else {
					swalIcon = "error";
					swalTitle = "공지사항 등록 실패";
					path = "list.do?tp=b1";
					// 게시글 목록으로 돌아가기 + 게시판 타입 목록페이지로 전달
				}
				
				request.getSession().setAttribute("swalIcon", swalIcon);
				request.getSession().setAttribute("swalTitle", swalTitle);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			// 공지글 수정 폼 연결 ------------------------------------
			else if(command.equals("/updateForm.do")) {
				errorMsg = "게시글 수정 페이지 연결 과정에서 오류 발생";
				
				// 수정 화면에 기존 내용 작성
				int brdNo = Integer.parseInt(request.getParameter("no"));
				
				Board notice = service.updateView(brdNo);
				//개행 문자 처리가 필요 없기 때문에 그냥 기존의 상세 조회 시 사용한 service 재활용
				//이미지는 따로 가져올 필요 없음 이미 content에 있음	
				
				if(notice != null) {
					request.setAttribute("notice", notice);
					path = "/WEB-INF/views/notice/noticeUpdate.jsp";
					
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
				}else {
					request.getSession().setAttribute("swalIcon", "error");
					request.getSession().setAttribute("swalTitle", "게시글 수정 화면 전환 실패");
					response.sendRedirect(request.getHeader("referer"));
				}
			}
			
			// 공지글 수정 Controller ------------------------------
			else if(command.equals("/noticeUpdate.do")) {
				errorMsg = "게시글 수정 과정에서 오류 발생";
				
				// 글 등록과 비슷한 패턴
				
				// 1. 게시글 작성 파라미터 얻어오기
				int brdNo = Integer.parseInt(request.getParameter("no"));
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				
				// 2. content에 있는 img 태그 내 src를 선택하여 image url 목록 반환받기
				List<String> imgUl = service.getImageList(content);
				
				// 3. imgUl을 이용하여 DB에 저장할 이미지 데이터 목록 만들기
				List<Image> iList = new ArrayList<Image>();
				
				if(!imgUl.isEmpty()) {
					int level = 0;
					
					for(String url : imgUl) {
						int slash = url.lastIndexOf('/');
						
						Image temp = new Image(); 
						
						//문자열.substring() 사용해서 url에서 파일명, 파일경로 분리하기
						//substring(start) ~ start인덱스부터 마지막 인덱스까지 잘라내서 저장
						//substring(start, end) ~ start인덱스부터 end인덱스 전까지 잘라내서 저장
		
						temp.setFileName(url.substring(slash+1)); 
								//ex) uploadImages/image1.jpg → image1.jpg
						temp.setFilePath(url.substring(0, slash+1)); 
								//ex) uploadImages/image1.jpg → uploadImages/
						temp.setFileLevel(level++); 

						iList.add(temp); 						
					}
				}
				
				// 3-1. 사진 삽입 실패 시 사진을 삭제할 기본 주소 추가
				String root = request.getSession().getServletContext().getRealPath("/");
				
				//4. 입력 내용 + 이미지 List Map에 담아서 service로 호출 수정 결과 반환
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("brdNo", brdNo);//게시글 확인하고 update해야 하니까 필요함
				map.put("title", title);
				map.put("content", content);
				map.put("iList", iList);
				map.put("root", root);
				//게시판 타입을 바뀔 필요 없음 → 보내지 않음...
				
				int result = service.updateNotice(map);
				
				path = "view.do?tp=b1&cp=" + cp +"&no=" + brdNo;
				
				//검색어, 검색 조건이 있는 경우 sk, sv도 path에 추가해야하므로
				//if문 조건을 만들기 위해 sk, sv를 파라미터에서 받아와 저장
				String sk = request.getParameter("sk");
				String sv = request.getParameter("sv");
				
				//검색어, 검색 조건이 있는 경우 path에 sk, sv 추가
				if(sk != null && sv != null) {
					path += "&sk=" + sk + "&sv=" + sv;
				}
				
				//swalIcon, swalTitle 지정
				if(result > 0) {
					swalIcon = "success";
					swalTitle = "게시글 수정 성공";
					
				} else {
					swalIcon = "error";
					swalTitle = "게시글 수정 실패";
					
				}
				
				request.getSession().setAttribute("swalIcon", swalIcon);
	            request.getSession().setAttribute("swalTitle", swalTitle);

				response.sendRedirect(path);				
			}
			
			
			// 공지글 삭제 Controller -----------------------
			else if(command.equals("/noticeDelete.do")) {
				int brdNo = Integer.parseInt(request.getParameter("no"));
				
				int result = service.updateBrdStatus(brdNo);
				
				if(result > 0) {
					// 삭제 성공 시: 게시글 목록 조회
					swalIcon = "success";
					swalTitle = "게시글 삭제 성공";
					path = "list.do?tp=b1";
				}else {
					// 삭제 실패시: 삭제를 시도한 게시글의 상세 조회 페이지로 돌아가기
					swalIcon = "error";
					swalTitle = "게시글 삭제 실패";
					path = request.getHeader("referer");
				}
				request.getSession().setAttribute("swalIcon", swalIcon);
				request.getSession().setAttribute("swalTitle", swalTitle);
				
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
