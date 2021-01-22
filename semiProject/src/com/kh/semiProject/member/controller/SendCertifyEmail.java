package com.kh.semiProject.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sendEmail.do")//이메일 인증을 위한 이메일 보내기 서블릿
public class SendCertifyEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SendCertifyEmail() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String toAddr = request.getParameter("이메일주소");//사용자 이메일
		String from = "관리자이메일아이디";//관리자 이메일 아이디
		String subject = "야멍아 옹멍해 회원가입 인증번호입니다."; //메일 제목
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
