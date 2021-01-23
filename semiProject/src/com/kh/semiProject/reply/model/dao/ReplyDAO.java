package com.kh.semiProject.reply.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;
import static com.kh.semiProject.common.JDBCTemplate.close;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kh.semiProject.reply.model.vo.Reply;


public class ReplyDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;

	private Properties prop;
		
	public ReplyDAO(){
		String fileName = ReplyDAO.class.getResource("/com/kh/semiProject/sql/reply/reply-query.xml").getPath();
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName)); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 댓글 목록 조회 DAO
	 * @param conn
	 * @param brdNo
	 * @return rpList
	 * @throws Exception
	 */
	public List<Reply> selectList(Connection conn, int brdNo) throws Exception{
		
		List<Reply> rpList = null;
		
		String query = prop.getProperty("selectList");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);
			
			rset = pstmt.executeQuery();
			
			rpList = new ArrayList<Reply>();
			
			while(rset.next()){
				Reply reply = new Reply();

				reply.setReplyNo(rset.getInt("COMM_NO"));
				reply.setReplyContent(rset.getString("COMM_CONTENT"));
				reply.setReplyDate(rset.getTimestamp("COMM_DATE"));
				reply.setNickName(rset.getString("N_NM"));
			
				rpList.add(reply);

			} 
				
			
			} finally {
				close(rset);
				close(pstmt);
			}
		return rpList;
	}

	/** 댓글 삽입 DAO
	 * @param conn
	 * @param reply
	 * @return result
	 * @throws Exception
	 */
	public int insertReply(Connection conn, Reply reply) throws Exception {
		
		int result = 0;
		
		String query = prop.getProperty("insertReply");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, reply.getReplyContent());
			pstmt.setInt(2, reply.getBrdNo());
			pstmt.setString(3, reply.getNickName()); // 회원 번호가 담겨 있음
			
			result = pstmt.executeUpdate();
			System.out.println("result : " + result);
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** 댓글 삭제(상태 변경) DAO
	 * @param conn
	 * @param replyNo
	 * @return result
	 * @throws Exception
	 */
	public int updateReplyStatus(Connection conn, int replyNo) throws Exception {
		
		int result = 0;
		
		String query = prop.getProperty("updateReplyStatus");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, replyNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}	
	
}
