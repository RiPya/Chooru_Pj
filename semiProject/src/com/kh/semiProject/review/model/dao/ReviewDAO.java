package com.kh.semiProject.review.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import com.kh.semiProject.image.model.vo.Image;

public class ReviewDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;//xml 파일 읽어오기용
	
	public ReviewDAO(){
		String fileName
		 = ReviewDAO.class.getResource("/com/kh/semiProject/sql/review/review-query.xml").getPath();
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName)); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 다음 brdNo(글번호) 조회하는 dao
	 * @param conn
	 * @return brdNo
	 * @throws Exception
	 */
	public int selectNextNo(Connection conn) throws Exception {
		int brdNo = 0;
		
		String query = prop.getProperty("selectNextNo");
		/*SELECT SEQ_BNO.NEXTVAL FROM DUAL*/
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			if(rset.next()) {
				brdNo = rset.getInt(1);
			}
		} finally {
			close(rset);
			close(stmt);
		}
		return brdNo;
	}

	/** BOARD에 게시글(글번호, 제목, 내용, 작성자, 게시판타입) 삽입 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("insertBoard");
		/*INSERT INTO BOARD(BRD_NO, TITLE, CONTENT, MEM_NO, BRD_TYPE)
			VALUES(?, ?, ?, ?, ?)*/
		
		try {
			pstmt = conn.prepareStatement(query);
			
			//map에서는 모든 value가 Object 형태 → 형변환 필요
			pstmt.setInt(1, (int)map.get("brdNo"));
			pstmt.setString(2, (String)map.get("title"));
			pstmt.setString(3, (String)map.get("content"));
			pstmt.setInt(4, (int)map.get("memNo")); //회원번호
			pstmt.setString(5, (String)map.get("brdType"));//게시판 타입(B3)
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	/** ADT_REVIEW에 게시글(입양날짜, 입양url) 삽입 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertReview(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("insertReview");
		/*INSERT INTO ADT_REVIEW VALUES(?, ?, ?) */
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, (int)map.get("brdNo"));
			pstmt.setDate(2, (Date)map.get("adtDate"));
			pstmt.setString(3, (String)map.get("adtLink"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	
	/**IMAGE에 이미지 삽입 dao
	 * @param conn
	 * @param img
	 * @return result
	 * @throws Exception
	 */
	public int insertImage(Connection conn, Image img) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("insertImage");
		/*INSERT INTO IMAGE VALUES(SEQ_INO.NEXTVAL, ?, ?, ?, SYSDATE, ?)*/
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, img.getFileName());
			pstmt.setString(2, img.getFilePath());
			pstmt.setInt(3, img.getFileLevel());
			pstmt.setInt(4, img.getBrdNo());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
	
	
}






















