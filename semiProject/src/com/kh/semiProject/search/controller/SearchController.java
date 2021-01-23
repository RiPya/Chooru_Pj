package com.kh.semiProject.search.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.semiProject.adoption.model.vo.Adoption;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.vo.Member;
import com.kh.semiProject.search.model.service.SearchService;


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
		String swalIcon = null;
		String swalTitle = null;
		String swalText = null;
		
		//에러 메시지 전달용 변수
		String errorMsg = null;
		
		
		try {
			
			SearchService service = new SearchService();
			
			
			//condition을 만들기 위해 파라미터에서 값 얻어와 조율하기
			
			String currentPage = request.getParameter("cp");
			String searchValue = request.getParameter("sv");
			String searchKey = request.getParameter("sk");

			//게시판 타입 : b1(공지) b2(입양) b3(후기) b4(자유) b5(고객센터) 
			//mypage(마이페이지) adminMem(회원관리)
			String brdType = request.getParameter("tp");
			//System.out.println(brdType);

			//입양/분양, 자유, 고객센터에서 사용
			String code = request.getParameter("cd");
			
			//검색을 위한 condition 만들기-----------------------
			//각 조건문에서 매개변수가 null일 때 "all"로 바꿔주는 과정 진행
			
			//key, value에 해당하는 condition 가져오기
			String keyValue = service.getSkSvCondition(searchKey, searchValue);
			
			//brdType에 해당하는 condition
			String tpCondition = service.getTpCondition(brdType);
			
			//code에 해당하는 condition
			String cdCondition = service.getCdCondition(code);
			
			
			
			//전체 검색 Controller
			if(command.equals("/search.do")) {
				errorMsg = "전체 검색 과정에서 오류 발생";				
			
				//System.out.println(keyValue);
				//System.out.println(tpCondition);
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("tpCondition", tpCondition);
				map.put("cdCondition", cdCondition);
				
				//어떤 게시판의 dao로 보낼 지 확인하기 위해나 brdType
				map.put("brdType", brdType);
				
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getSearchPage(map);
				//System.out.println(pInfo);
				
				map.put("pInfo", pInfo);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Board> sList = service.searchAllList(map);
				
				if(sList != null) {
					List<Image> iList = service.selectSearchThumbs(map);
					
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
					
					//댓글 수 확인
					//comm이라는 map에 brdNo, count(댓글 수) 반환
					List<Map<String, String>> commCounts = service.selectReplyCount(map);
					if(!commCounts.isEmpty()) {
						request.setAttribute("commCounts", commCounts);
					}
					
				}
				
				//System.out.println(sList);
				
				path = "/WEB-INF/views/search/searchList.jsp";
				
				request.setAttribute("sList", sList);
				request.setAttribute("pInfo", pInfo);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
				
			}
			
			//공지 게시판 search-------------------------------------------
			else if(command.equals("/noticeSearch.do")) {
				errorMsg = "게시판 내 검색 과정에서 오류 발생";
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("tpCondition", tpCondition);
				map.put("cdCondition", cdCondition);
				map.put("brdType", brdType);
				
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getSearchPage(map);
				
				map.put("pInfo", pInfo);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Board> nList = service.searchInsideList(map);
				
				if(nList != null) {
					List<Image> iList = service.selectSearchThumbs(map);
					
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
				}

				request.setAttribute("nList", nList);
				request.setAttribute("pInfo", pInfo);
				
				
				path = "/WEB-INF/views/notice/noticeList.jsp";
				//검색 과정은 모두 이 과정으로?
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			//입양/분양 게시판 search----------------------------------------
			else if(command.equals("/adoptionSearch.do")) {
				errorMsg = "게시판 내 검색 과정에서 오류 발생";
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("tpCondition", tpCondition);
				map.put("cdCondition", cdCondition);
				map.put("brdType", brdType);
				
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getSearchPage(map);
				
				map.put("pInfo", pInfo);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Adoption> aList = service.searchAdoptList(map);
				
				if(aList != null) {
					List<Image> iList = service.selectSearchThumbs(map);
					
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
					
					//댓글 수 확인
					//comm이라는 map에 brdNo, count(댓글 수) 반환
					List<Map<String, String>> commCounts = service.selectReplyCount(map);
					if(!commCounts.isEmpty()) {
						request.setAttribute("commCounts", commCounts);
					}
				}

				request.setAttribute("aList", aList);
				request.setAttribute("pInfo", pInfo);
				
				path = "/WEB-INF/views/adoption/adoptionList.jsp";
				//검색 과정은 모두 이 과정으로?
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			
			//입양 후기 게시판 search ------------=---------------------------
			else if(command.equals("/reviewSearch.do")) {

				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("tpCondition", tpCondition);
				map.put("cdCondition", cdCondition);
				map.put("brdType", brdType);
				
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getSearchPage(map);
				
				map.put("pInfo", pInfo);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Board> rList = service.searchInsideList(map);
				
				if(rList != null) {
					List<Image> iList = service.selectSearchThumbs(map);
					
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
					
					//댓글 수 확인
					//comm이라는 map에 brdNo, count(댓글 수) 반환
					List<Map<String, String>> commCounts = service.selectReplyCount(map);
					if(!commCounts.isEmpty()) {
						request.setAttribute("commCounts", commCounts);
					}
				}

				request.setAttribute("rList", rList);
				request.setAttribute("pInfo", pInfo);
				
				path = "/WEB-INF/views/review/reviewList.jsp";
				//검색 과정은 모두 이 과정으로?
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			
			//자유 게시판 search-------------------------------------------
			else if(command.equals("/freeSearch.do")) {
				errorMsg = "게시판 내 검색 과정에서 오류 발생";
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("tpCondition", tpCondition);
				map.put("cdCondition", cdCondition);
				map.put("brdType", brdType);
				
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getSearchPage(map);
				
				map.put("pInfo", pInfo);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Board> fList = service.searchInsideList(map);
				
				if(fList != null) {
					List<Image> iList = service.selectSearchThumbs(map);
					
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
					
					//댓글 수 확인
					//comm이라는 map에 brdNo, count(댓글 수) 반환
					List<Map<String, String>> commCounts = service.selectReplyCount(map);
					if(!commCounts.isEmpty()) {
						request.setAttribute("commCounts", commCounts);
					}
				}

				request.setAttribute("fList", fList);
				request.setAttribute("pInfo", pInfo);
				
				path = "/WEB-INF/views/free/freeList.jsp";
				//검색 과정은 모두 이 과정으로?
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			//고객센터 검색----------------------------------
			else if(command.equals("/infoSearch.do")) {
				errorMsg = "게시판 내 검색 과정에서 오류 발생";
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("tpCondition", tpCondition);
				map.put("cdCondition", cdCondition);
				map.put("brdType", brdType);
				
				//1.페이징 처리를 위한 값 계산 service 호출
				PageInfo pInfo = service.getSearchPage(map);
				
				map.put("pInfo", pInfo);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Board> ifList = service.searchInsideList(map);
				
				if(ifList != null) {
					List<Image> iList = service.selectSearchThumbs(map);
					
					if(!iList.isEmpty()) {
						request.setAttribute("iList", iList);
					}
					
					//댓글 수 확인
					//comm이라는 map에 brdNo, count(댓글 수) 반환
					List<Map<String, String>> commCounts = service.selectReplyCount(map);
					if(!commCounts.isEmpty()) {
						request.setAttribute("commCounts", commCounts);
					}
				}

				request.setAttribute("ifList", ifList);
				request.setAttribute("pInfo", pInfo);
				
				path = "/WEB-INF/views/information/informationList.jsp";
				//검색 과정은 모두 이 과정으로?
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			//관리자 페이지 : 회원 검색
			else if(command.equals("/memStatus.do")) {
				errorMsg = "회원 검색 과정에서 오류 발생";
				
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("code", code);
				
				PageInfo pInfo = service.getadminPage(map);
				
				map.put("pInfo", pInfo);
				
				//2.게시글 목록 조회 비즈니스 로직 수행
				List<Member> mList = service.selectMemberList(map);
				
				request.setAttribute("mList", mList);
				request.setAttribute("pInfo", pInfo);
				
	            path = "/WEB-INF/views/admin/adminMem.jsp";
	            view = request.getRequestDispatcher(path);
	            view.forward(request, response);
			}
			
			//관리자 페이지 : 블라인드/삭제 게시글 검색
			else if(command.equals("/brdStatus.do")) {
				errorMsg = "블라인드/삭제 게시글 검색 과정에서 오류 발생";
				Map<String, Object> map = new HashMap<String, Object>();
				
				System.out.println("왔니?");
				System.out.println(code);
				
				map.put("currentPage", currentPage);
				map.put("keyValue", keyValue);
				map.put("code", code);
				
				PageInfo pInfo = service.getadminPage(map);
				
				map.put("pInfo", pInfo);
				

				List<Board> bList = service.selectBrdList(map);
				
				System.out.println("blist: "+bList);
				
				request.setAttribute("bList", bList);
				request.setAttribute("pInfo", pInfo);
				
				
	            path = "/WEB-INF/views/admin/adminBrdStatus.jsp";
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
