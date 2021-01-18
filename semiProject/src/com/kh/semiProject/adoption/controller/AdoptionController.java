package com.kh.semiProject.adoption.controller;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.kh.semiProject.adoption.model.service.AdoptionService;
import com.kh.semiProject.adoption.model.vo.Adoption;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.vo.Member;


@WebServlet("/adoption/*")
public class AdoptionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdoptionController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Front Controller 패턴
		// -클라이언트의 요청을 한 곳으로 집중시켜 개발하는 패턴
		// -요청마다 Servlet을 생성하는 것이 아닌 하나의 Servlet에 작성하여 관리가 용이해짐
		
		//Controller 역할 : 요청에 맞는 Service 호출, 응답을 위한 View 선택
		
		String uri = request.getRequestURI();
		// ex) /semiProject/adoption/list.do
		
		String contextPath = request.getContextPath();
		// /semiProject

		String command = uri.substring((contextPath + "/adoption").length());
		// uri에서 "/semiProject/adoption"(contextPath+"/adoption")를 제거하겠다
		// → list.do
	
		// System.out.println(uri);
		// System.out.println(contextPath);
		// System.out.println(command);
		
		// 컨트롤러 내에서 공용으로 사용할 변수 미리 선언
		String path = null; //forward 또는 redirect 경로를 저장할 변수
		RequestDispatcher view = null; //요청 위임 객체
		
		// sweet alert로 메시지 전달하는 용도
		String swalIcon = null;
		String swalTitle = null;
		String swalText = null;
		
		//에러 메시지 전달용 변수
		String errorMsg = null;
		
		try {
			
			AdoptionService service = new AdoptionService();
			
			// 현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			// 게시판 타입 : b1(공지) b2(입양/분양) b3(후기) b4(자유) b5(고객센터) 
			// 			 mypage(마이페이지) adminMem(회원관리)
			String boardType = request.getParameter("tp");
			
			// 입양/분양 게시판의 메뉴 카테고리를 얻어옴
			// adtAll, adtDog, adtCat, adtEtc, temp
			String adoptionCode = request.getParameter("cd");
			if(adoptionCode == null) adoptionCode = "adtAll";
			
			// 입양/분양 목록 조회 Controller ---------------
			if(command.equals("/list.do")) {
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";
				
				// 1. 페이징 처리를 위한 값 계산 Service 호출
				PageInfo pInfo = service.getPageInfo(cp);
				
				// pInfo의 limit을 9로 바꿔주기 → 입양/분양은 한 페이지 조회 게시글 수 9
				pInfo.setLimit(9);
				
				// 2. 게시글 목록 조회 비즈니스 로직 수행
				List<Adoption> aList = service.selectAdoptionList(pInfo);
				
				// 3. 게시글 목록이 조회 되었을 때 썸네일 목록 조회 비즈니스 로직
				if(aList != null) {
					List<Image> iList = service.selectThumbnails(pInfo);

					// 썸네일 목록이 비어있지 않은 경우
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
				}
				
				request.setAttribute("aList", aList);
				request.setAttribute("pInfo", pInfo);
				
				path = "/WEB-INF/views/adoption/adoptionList.jsp";
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			//입양/분양  상세 조회 연결 Controller ---------------
			else if(command.equals("/view.do")) {
				errorMsg = "게시글 조회 과정에서 오류 발생";
				
				int brdNo = Integer.parseInt(request.getParameter("no"));
				
				Adoption adoption = service.selectAdoption(brdNo);
					
				if(adoption != null) {
					//이미지는 따로 가져올 필요 없음. content에 이미 이미지 url이 있음.
					
					path = "/WEB-INF/views/adoption/adoptionView.jsp";
					request.setAttribute("adoption", adoption);
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
					
				} 
				else {
					request.getSession().setAttribute("swalIcon", "error");
					request.getSession().setAttribute("swalTitle", "게시판 상세 조회 실패");
				}
			}
			
			// 입양/분양 글 작성 폼 연결 Controller
			else if(command.equals("/insertForm.do")) {
				errorMsg = "게시판 상세 조회 과정에서 오류 발생";

				path = "/WEB-INF/views/adoption/adoptionInsert.jsp";
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			// 입양/분양 게시글 등록 Controller ---------------
			else if(command.equals("/insert.do")) {
				errorMsg = "게시글 등록 과정에서 오류 발생";
			
				// 1. 게시글 정보 얻어와서 저장 (파라미터)
				String category = request.getParameter("category");
				String title = request.getParameter("title");
				String address = request.getParameter("address");
				String adtNote = request.getParameter("adtNote");
				String adtBreed = request.getParameter("adtBreed");
				String adtAge = request.getParameter("adtAge");
				String adtGender = request.getParameter("adtGender");
				String adtYn = request.getParameter("adtYn");
				String adtVaccination = request.getParameter("adtVaccination");
				
				// String 타입의 날짜를 Date형으로 변환
				String adtStr = request.getParameter("adtDate");
				
				/* System.out.println(adtStr); *///2021-01-20 형식
				//→ java.sql.Date의 valueOf(date) 사용하면 String → Date 변환 가능
				Date adtDate = Date.valueOf(adtStr);
				
				// 써머노트 오픈 소스의 content는 html 코드로 되어 있음
				String content = request.getParameter("content"); 
				
				// 게시판 타입 및 메뉴 카테고리는 위에서 가져옴
				
				// 2. 세션에서 로그인 회원의 회원번호 가져오기
				Member loginMember = (Member)request.getSession().getAttribute("loginMember");
				int memNo = loginMember.getMemNo();
					
				// 3. content에 있는 img 태그 내 src를 턱샌해 image url 목록 반환 받기
				List<String> imgUrl = service.getImageList(content);
				
				// 4. imgUrl을 사용하여 DB에 저장할 이미지 데이터 목록 만들기
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
				
				    // 4-1. 사진 삽입 실패 시 사진 삭제할 기본 주소 추가
					String root = request.getSession().getServletContext().getRealPath("/");
				
					// 5. VO 생성 대신 Map 객체를 이용하여
					//    얻어온 정보 + 이미지를 List Map에 담기
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("category", category);
					map.put("title", title);
					map.put("address", address);
					map.put("adtNote", adtNote);
					map.put("adtBreed", adtBreed);
					map.put("adtAge", adtAge);
					map.put("adtGender", adtGender);
					map.put("adtDate", adtDate);
					map.put("adtYn", adtYn);
					map.put("adtVaccination", adtVaccination);					
					map.put("content", content);
					map.put("memNo", memNo); // 회원번호
					map.put("boardType", boardType); // 게시판타입(B#)
					map.put("iList", iList);
					map.put("root", root);
					
					
					// 6. 게시글 등록 비즈니스 로직 수행 후 결과 반환 받기
					int result = service.insertAdoption(map);
					
					// 삽입 성공 시 result에 삽입 게시글 번호가 저장되어 있음!
					if(result > 0) { 
						swalIcon = "success";
						swalTitle = "입양 글 등록 성공";
						path = "view.do?tp=b2&cp=1&no=" + result;
							// 작성된 글번호와 게시판 타입 + 목록페이지를 파라미터로 전달
					} else {
						swalIcon = "error";
						swalTitle = "입양 글 등록 실패";
						path = "list.do?tp=b2";
							// 게시글 목록으로 돌아가기 + 게시판 타입 목록페이지로 전달
					}
					
					request.getSession().setAttribute("swalIcon", swalIcon);
					request.getSession().setAttribute("swalTitle", swalTitle);
					
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
				}
			
				// 입양/분양 수정 폼 연결 Controller ---------------
				else if(command.equals("/updateForm.do")) {
					
					errorMsg = "게시판 상세 조회 과정에서 오류 발생";
					
					// 기존 내용 작성
					int brdNo = Integer.parseInt(request.getParameter("no"));
					
					Adoption adoption = service.selectAdoption(brdNo);
					//개행 문자 처리가 필요 없기 때문에 그냥 기존의 상세 조회 시 사용한 service 재활용
					//이미지는 따로 가져올 필요 없음 이미 content에 있음
					System.out.println(adoption);
					
					if(adoption != null) {
						request.setAttribute("adoption", adoption);
						path = "/WEB-INF/views/adoption/adoptionUpdate.jsp";
						
						view = request.getRequestDispatcher(path);
						view.forward(request, response);
				} else {
					request.getSession().setAttribute("swalIcon", "error");
					request.getSession().setAttribute("swalTitle", "게시글 수정 화면 전환 실패");
					response.sendRedirect(request.getHeader("referer"));
				}
			}
			
			// 입양/분양 후기 수정 Controller ---------------
			else if(command.equals("/update.do")) {
				errorMsg = "게시글 수정 과정에서 오류 발생";
				
				// service로 보낼 데이터를 변수에 저장하는건 글 등록과 거의 유사 
				
				// 1. 게시글 정보 얻어와서 저장 (파라미터)
				int brdNo = Integer.parseInt(request.getParameter("no"));
				
				String category = request.getParameter("category");
				String title = request.getParameter("title");
				String address = request.getParameter("address");
				String adtNote = request.getParameter("adtNote");
				String adtBreed = request.getParameter("adtBreed");
				String adtAge = request.getParameter("adtAge");
				String adtGender = request.getParameter("adtGender");
				String adtYn = request.getParameter("adtYn");
				String adtVaccination = request.getParameter("adtVaccination");
				
				// String 타입의 날짜를 Date형으로 변환
				String adtStr = request.getParameter("adtDate");
				
				/* System.out.println(adtStr); *///2021-01-20 형식
				//→ java.sql.Date의 valueOf(date) 사용하면 String → Date 변환 가능
				Date adtDate = Date.valueOf(adtStr);
				
				// 써머노트 오픈 소스의 content는 html 코드로 되어 있음
				String content = request.getParameter("content");
				
				// 2.로그인 정보에서 회원번호 가져오기
				Member loginMember = (Member)request.getSession().getAttribute("loginMember");
				int memNo = loginMember.getMemNo();
				
				
				// 3. content에 있는 img 태그 내 src를 선택해 image url 목록 반환 받기
				List<String> imgUrl = service.getImageList(content);
				
				
				// 4. imgUrl을 사용하여 DB에 저장할 이미지 데이터 목록 만들기
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
				
				// 4-1. 사진 삽입 실패 시 사진 삭제할 기본 주소 추가
				String root = request.getSession().getServletContext().getRealPath("/");

				// 5. VO 생성 대신 Map 객체를 이용하여
				//    얻어온 정보 + 이미지를 List Map에 담기
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("brdNo", brdNo); // 게시글 확인하고 update해야 하니까 필요함
				map.put("category", category);
				map.put("title", title);
				map.put("address", address);
				map.put("adtNote", adtNote);
				map.put("adtBreed", adtBreed);
				map.put("adtAge", adtAge);
				map.put("adtGender", adtGender);
				map.put("adtDate", adtDate);
				map.put("adtYn", adtYn);
				map.put("adtVaccination", adtVaccination);					
				map.put("content", content);
				map.put("memNo", memNo); // 회원번호
				map.put("iList", iList); // 이미지 첨부
				map.put("root", root); // 이미지 첨부
				
				// 게시판 타입을 바뀔 필요 없음 → 보내지 않음...
				
				int result = service.updateAdoption(map);
				
				path = "view.do?tp=b2&cp=" + cp + "&no=" + brdNo;
				
				// 검색어, 검색 조건이 있는 경우 sk, sv도 path에 추가해야하므로
				// if문 조건을 만들기 위해 sk, sv를 파라미터에서 받아와 저장
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
			
			// 입양/분양 글 삭제 Controller ---------------
			else if(command.equals("/delete.do")) {
				
				int brdNo = Integer.parseInt(request.getParameter("no"));
				
				int result = service.updateBoardStatus(brdNo);
				
				if(result > 0) {
					swalIcon = "success";
					swalTitle = "게시글 삭제 성공";
					path = "list.do";
					
				} else {
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
			
			// 예외 발생 시 errorPage.jsp로 요청 위임
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
