package com.kh.semiProject.review.controller;

import java.io.IOException;
import java.sql.Date;
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
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.vo.Member;
import com.kh.semiProject.review.model.service.ReviewService;

@WebServlet("/review/*")
public class ReviewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReviewController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Front Controller 패턴
		// -클라이언트의 요청을 한 곳으로 집중시켜 개발하는 패턴
		// -요청마다 Servlet을 생성하는 것이 아닌 하나의 Servlet에 작성하여 관리가 용이해짐
		
		//Controller 역할 : 요청에 맞는 Service 호출, 응답을 위한 View 선택
		
		String uri = request.getRequestURI();
		// ex) /semiProject/review/list.do
		
		String contextPath = request.getContextPath();
		// /semiProject
	
		String command = uri.substring((contextPath + "/review").length());
		// uri에서 "/semiProject/review"(contextPath+"/review")를 제거하겠다
		// → list.do
	
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
			
			ReviewService service = new ReviewService();
			
			//현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String boardType = request.getParameter("tp");
			
			//파라미터 tp는 필수로 전달해야함!!!!!
			//입양/분양, 자유, 고객센터, 마이페이지는 cd도 꼭 전달해야 함!!
			/* String reviewCode = request.getParameter("cd"); */

			
			//입양 후기 목록 연결 Controller
			if(command.equals("/list.do")) {
				
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";
				
				path = "/WEB-INF/views/review/reviewList.jsp";
				
				//request.setAttribute("fList", fList);
				//request.setAttribute("pInfo", pInfo);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			

			//입양 후기 상세 조회 Controller----------------------------------------------
			else if(command.equals("/view.do")) {
				errorMsg = "게시글 조회 과정에서 오류 발생";
				
				path = "/WEB-INF/views/review/reviewView.jsp";
				
				//request.setAttribute("fList", fList);
				//request.setAttribute("pInfo", pInfo);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			//입양 후기 작성 폼 연결-------------------------------
			else if(command.equals("/insertForm.do")) {
				errorMsg = "게시글 작성 페이지 연결 과정에서 오류 발생";
				
				path = "/WEB-INF/views/review/reviewInsertForm.jsp";
				
				//request.setAttribute("fList", fList);
				//request.setAttribute("pInfo", pInfo);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			
			//입양 후기 글 등록 controller-----------------------------------------------------
			else if(command.equals("/reviewInsert.do")) {
				errorMsg = "게시글 등록 과정에서 오류 발생";
				
				//1. 게시글 작성 파라미터에서 얻어오기
				//1-1. 제목, 입양/분양 글 url
				String title = request.getParameter("title");
				String adtLink = request.getParameter("adtLink");
				
				//1-2. String 타입의 날짜를 Date형을 변환
				String adtStr = request.getParameter("adtDate");
				
				/* System.out.println(rAdtDate); *///2021-01-20 형식
				//→ java.sql.Date의 valueOf(date) 사용하면 String → Date 변환 가능
				Date adtDate = Date.valueOf(adtStr);
				
				//1-3. 내용(Content) 받기 : html 코드
				String content = request.getParameter("content");
				
				//1-4. 게시판 타입 위에서 가져옴
				
				//2.로그인 정보에서 회원번호 가져오기
				Member loginMember = (Member)request.getSession().getAttribute("loginMember");
				int memNo = loginMember.getMemNo();
				
				
				//3. content에 있는 img 태그 내 src를 선택해 image url 목록 반환 받기
				List<String> imgUrl = service.getImageList(content);
				
				
				//4. imgUrl을 사용하여 DB에 저장할 이미지 데이터 목록 만들기
				List<Image> iList = new ArrayList<Image>(); 

				if(!imgUrl.isEmpty()) {//imgUrl 있을 때 == 이미지가 첨부되었을 때

					int level = 0; //사진 레벨 -> 사진 순서(레벨 0은 썸네일)
	
					for(String url : imgUrl){
						int slash = url.lastIndexOf('/'); 
						//src에서 뒤에서부터 첫번째 '/'인 index 뽑기 
						// => '경로 / 파일명.확장자' 의 경계선인 '/'의 index 확인 가능
		
						Image temp = new Image(); 
		
						//문자열.substring() 사용해서 url에서 파일명, 파일경로 분리하기
						//substring(start) ~ start인덱스부터 마지막 인덱스까지 잘라내서 저장
						//substring(start, end) ~ start인덱스부터 end인덱스 전까지 잘라내서 저장
		
						temp.setFileName(url.substring(slash+1)); 
								//ex) uploadImages/image1.jpg → image1.jpg
						temp.setFilePath(url.substring(0, slash+1)); 
								//ex) uploadImages/image1.jpg → uploadImages/
						temp.setFileLevel(level++); 
//						System.out.println(temp.getFileName()); //확인용
//						System.out.println(temp.getFilePath()); //확인용
//						System.out.println(temp.getFileLevel()); //확인용

						iList.add(temp); 
					} 
				}
				

				//5. 입력 내용 + 이미지 List Map에 담아서 service로 호출 삽입 결과 반환
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", title);
				map.put("adtDate", adtDate);
				map.put("adtLink", adtLink);
				map.put("content", content);
				map.put("memNo", memNo);//회원번호 == 작성자
				map.put("brdType", boardType);//게시판타입(B#)
				map.put("iList", iList);
				
				int result = service.insertReview(map);
				
				
				//삽입 성공 시 result에 삽입 게시글번호 저장되어 있음
				//실패 시 0
				if(result > 0) { 
					swalIcon = "success";
					swalTitle = "입양 후기 등록 성공";
					path = "reviewView.do?tp=b3&cp=1&no=" + result;
						//작성된 글번호와 게시판 타입 + 목록페이지를 파라미터로 전달
				} else {
					swalIcon = "error";
					swalTitle = "입양 후기 등록 실패";
					path = "list.do?tp=b3";
						//게시글 목록으로 돌아가기 + 게시판 타입 목록페이지로 전달
				}
				
				request.getSession().setAttribute("swalIcon", swalIcon);
				request.getSession().setAttribute("swalTitle", swalTitle);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			
			//입양 후기 수정 폼 연결-------------------------------------------------
			else if(command.equals("/updateForm.do")) {
				errorMsg = "게시글 수정 페이지 연결 과정에서 오류 발생";
				
				path = "/WEB-INF/views/review/reviewUpdate.jsp";
				
				//request.setAttribute("fList", fList);
				//request.setAttribute("pInfo", pInfo);
				
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
