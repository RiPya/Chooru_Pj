package com.kh.semiProject.member.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Transport;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.semiProject.common.Gmail;

@WebServlet("/sendEmail.do")//이메일 인증을 위한 이메일 보내기 서블릿
public class SendCertifyEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SendCertifyEmail() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String toAddr = request.getParameter("email");//사용자 이메일
		String from = "Chooruproject";//관리자 이메일 아이디
		String subject = "야멍아 옹멍해 인증번호입니다."; //메일 제목
	
		//메일 내용 : 인증번호 (알파벳3글자+숫자 4자리)를 보냄
		////key의 형태 : AGB1235
		String content = "다음 번호를 인증 번호 입력창에 입력하세요.<br>";
		String key = "";//인증 번호용
		
		System.out.println("넘어왔따");
		for (int i = 0; i < 3; i++) {
			key += (char)(Math.random()*26+'A');//A~Z 중 하나 랜덤
		}

		key += (int)(Math.random()*10000);//4자리 난수
			content += "인증번호 : " + key;
			
			
			//SMTP(간이 우편 전송 프로토콜(Simple Mail Transfer Protocol)의 약자. 
			//이메일 전송에 사용되는 네트워크 프로토콜이다. from 나무위키)에 접속하기 위한 정보 기입
			//javax.mail.에 있는 것들 다 import 필요(gmail 라이브러리)
			//util.properties 필요
			Properties p = new Properties();
			p.put("mail.smtp.user", from);
			p.put("mail.smtp.host", "smtp.googlemail.com");
			p.put("mail.smtp.port", "465");//지메일에서 정해준 port
			p.put("mail.smtp.starttls.enable", "true");//시작 가능하게 하는 뭐..그런거
			p.put("mail.smtp.auth", "true");//진위검사..?
			p.put("mail.smtp.debug", "true");//debug 관련
			p.put("mail.smtp.socketFactory.port", "465");
			p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			p.put("mail.smtp.socketFactory.fallback", "false");
			
			
			//이메일 보내기 
			try{
			 Authenticator auth = new Gmail();
			 Session ses = Session.getInstance(p, auth);
			 ses.setDebug(true);

			 MimeMessage msg = new MimeMessage(ses);
			 msg.setSubject(subject);

			 Address fromAddr = new InternetAddress(from);
			 msg.setFrom(fromAddr);

			 msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
			 
			 msg.setContent(content, "text/html;charset=UTF8");
			 Transport.send(msg);
			 
			//인증 번호 key(회원가입창에서 비교 필요) 반환
			 PrintWriter out = response.getWriter();
			 out.append(key);
			 
			}catch (Exception e){
				e.printStackTrace();
				//예외 발생 시 errorPage.jsp로 요청 위임
				String errorMsg = null;
				String path = null;
				RequestDispatcher view = null; //요청 위임 객체
				
				errorMsg = "인증 번호 이메일 전송 과정에서 에러 발생";
				
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
