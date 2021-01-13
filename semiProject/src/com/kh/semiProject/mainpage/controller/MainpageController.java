package com.kh.semiProject.mainpage.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//@WebServlet("${contextPath}")
public class MainpageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MainpageController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI(); // ex) wsp/board/list.do
		String contextPath = request.getContextPath(); // ex) wsp
		String command = uri.substring((contextPath + "/board").length());
		// ex) wsp/board/list.do-(wsp/board/) = /list.do만 남음
		
		String path = null;
		RequestDispatcher view = null;
		
		String swalIcon = null;
		String swalTitle = null;
		String swalText = null;
		
		String errorMsg = null;
		
		try {
			//service 얻어오기
			
			//입양/분양 최신 게시글 9개 얻어오기 
			//같은 9개 == 기존의 /adoption/list.do에서 사용되는 service, dao재활용
			//섬네일 사진, 글번호 얻어오기. 게시판타입('B2' 입양/분양)
			
			
			//공지사항 최신 게시글 3개 얻어오기
			//제목, 작성일, 글번호(안보임) 얻어오기. 게시판 타입('B1' 공지사항) 
			
			//입양 후기 최신 게시글 3개 얻어오기
			//제목, 카테고리, 날짜, 글번호(안보임) 얻어오기.
			

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
