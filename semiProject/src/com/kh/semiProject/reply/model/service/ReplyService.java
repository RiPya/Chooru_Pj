package com.kh.semiProject.reply.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;
import static com.kh.semiProject.common.JDBCTemplate.close;
import static com.kh.semiProject.common.JDBCTemplate.getConnection;
import static com.kh.semiProject.common.JDBCTemplate.commit;
import static com.kh.semiProject.common.JDBCTemplate.getConnection;
import static com.kh.semiProject.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.util.List;

import com.kh.semiProject.reply.model.dao.ReplyDAO;
import com.kh.semiProject.reply.model.vo.Reply;

public class ReplyService {
	private ReplyDAO dao = new ReplyDAO();

	/** 댓글 목록 조회 Service
	 * @param brdNo
	 * @return rpList
	 * @throws Exception
	 */
	public List<Reply> selectList(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		List<Reply> rpList = dao.selectList(conn, brdNo);
		
		close(conn);
		
		return rpList;
	}

	/** 댓글 삽입 Service
	 * @param reply
	 * @return result
	 * @throws Exception
	 */
	public int insertReply(Reply reply) throws Exception {
		Connection conn = getConnection();

		String replyContent = reply.getReplyContent();

		// 크로스 사이트 스크립팅 방지 처리
		replyContent = replaceParameter(replyContent);
		
		// 개행문자 변환 처리
		// ajax 통신 시 textarea의 개행 문자가 \n 하나만 넘어옴
		//  \n -> <br>
		replyContent = replyContent.replaceAll("\n", "<br>");
		
		// 변경된 replyContent를 다시 reply에 세팅
		reply.setReplyContent(replyContent);
		
		int result = dao.insertReply(conn, reply);

		// 트랜잭션 처리
		if(result > 0) commit(conn);
		else 		   rollback(conn);
		
		return result;
	}

	// 크로스 사이트 스크립트 방지 메소드
	private String replaceParameter(String param) {
		String result = param;
		if(param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}
		
		return result;
	}
	
	/** 댓글 삭제(상태변경) Service
	 * @param replyNo
	 * @return result
	 * @throws Exception
	 */
	public int updateReplyStatus(int replyNo) throws Exception {
		
		Connection conn = getConnection();
		
		int result = dao.updateReplyStatus(conn, replyNo);
		
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn);
		
		return result;
	}
}
