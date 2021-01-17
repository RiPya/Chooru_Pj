package com.kh.semiProject.adoption.model.dao;

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
import com.kh.semiProject.review.model.dao.ReviewDAO;

public class AdoptionDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;

	private Properties prop = null;// xml 파일 읽어오기용

	public AdoptionDAO() {
		String fileName = ReviewDAO.class.getResource("/com/kh/semiProject/sql/adoption/adoption-query.xml").getPath();

		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 다음 게시글 번호 조회 DAO
	 * @param conn
	 * @return boardNo
	 * @throws Exception
	 */
	public int selectNextNo(Connection conn) throws Exception {
		
		int boardNo = 0;
		
		String query = prop.getProperty("selectNextNo");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			if(rset.next()) {
				boardNo = rset.getInt(1);
			}
		} finally {
			close(rset);
			close(stmt);
		}
		return boardNo;
	}

	/** BOARD에 게시글 삽입 DAO
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, Map<String, Object> map) throws Exception {
		
		int result = 0;
		
		String query = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			// map에서는 모든 value가 object 형태이므로 형변환 필요
			pstmt.setInt(1, (int)map.get("brdNo"));
			pstmt.setString(2, (String)map.get("title"));;
			pstmt.setString(3, (String)map.get("content"));
			pstmt.setInt(4, (int)map.get("memNo")); // 회원번호
			pstmt.setString(5, (String)map.get("boardType")); // 게시판 타입(B2)
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	/** Adoption에 게시글 삽입 DAO
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertAdoption(Connection conn, Map<String, Object> map) throws Exception {
		
		int result = 0;
		
		String query = prop.getProperty("insertAdoption");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, (int)map.get("brdNo"));
			pstmt.setString(2, (String)map.get("category"));
			pstmt.setString(3, (String)map.get("adtBreed"));
			pstmt.setString(4, (String)map.get("adtGender"));
			pstmt.setString(5, (String)map.get("adtAge"));
			pstmt.setString(6, (String)map.get("address"));
			pstmt.setString(7, (String)map.get("adtVaccination"));
			pstmt.setString(8, (String)map.get("adtNote"));
			pstmt.setDate(9, (Date)map.get("adtDate"));
			pstmt.setString(10, (String)map.get("adtYn"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	/** IMAGE에 이미지 삽입 DAO
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
