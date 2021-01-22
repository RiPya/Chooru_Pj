package com.kh.semiProject.reply.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.semiProject.reply.model.service.ReplyService;
import com.kh.semiProject.reply.model.vo.Reply;

@WebServlet("/reply/*")
public class ReplyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = uri.substring((contextPath+"/reply").length());
		
		try {
			ReplyService service = new ReplyService();

			// 현재 페이지를 얻어옴
			String cp = request.getParameter("cp");
			
			// 게시판 타입 : b1(공지) b2(입양/분양) b3(후기) b4(자유) b5(고객센터) 
			// 			 mypage(마이페이지) adminMem(회원관리)
			String brdType = request.getParameter("tp");
			
			// 게시판의 메뉴 카테고리를 얻어옴
			String code = request.getParameter("cd");
			//if(code  == null) code  = "adtAll";
			
			
			// 댓글 목록 조회 Controller ---------------
			if(command.equals("/selectList.do")) {
				
				int brdNo = Integer.parseInt(request.getParameter("brdNo"));
				System.out.println(brdNo);
				
				List<Reply> rpList = service.selectList(brdNo);
					
				Gson gson = new GsonBuilder().setDateFormat("yyyy년 MM월 dd일 HH:mm").create();
				gson.toJson(rpList, response.getWriter());
				// 성공 시, reply.jsp -> ajax success에 담감
			}
			
			// 댓글 등록 Controller ---------------
			else if(command.equals("/insertReply.do")) {
				
				// 오라클에서 숫자로 이루어진 문자열은 자동으로 숫자로 변환되는 특징을 사용할 예쩡
				int brdNo = Integer.parseInt(request.getParameter("brdNo"));
				String replyWriter = request.getParameter("replyWriter");
				String replyContent = request.getParameter("replyContent");
				
				Reply reply = new Reply();
				reply.setNickName(replyWriter); // 회원 번호 담겨 있음
				reply.setReplyContent(replyContent);
				reply.setBrdNo(brdNo);
				
				int result = service.insertReply(reply);
				
				// ajax에서 결과 처리 해줘서 결과를 바로 응답해줌
				response.getWriter().print(result);
			}
			
			// 댓글 삭제 Controller ---------------
			else if(command.equals("/deleteReply.do")) {
				
				int replyNo = Integer.parseInt(request.getParameter("replyNo"));
				
				int result = service.updateReplyStatus(replyNo);
				
				response.getWriter().print(result);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// 에러 페이지는 ajax로 처리
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
