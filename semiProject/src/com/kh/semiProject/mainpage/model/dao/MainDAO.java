package com.kh.semiProject.mainpage.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.review.model.dao.ReviewDAO;

public class MainDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;//xml 파일 읽어오기용
	
	public MainDAO(){
		String fileName
		 = ReviewDAO.class.getResource("/com/kh/semiProject/sql/main/main-query.xml").getPath();
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName)); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Board> selectReviewMain(Connection conn) throws Exception{
		
		List<Board> rList = null;
		
		String query = prop.getProperty("selectReviewMain");
		/*SELECT TITLE, ADT_DATE, BRD_NO
			FROM (SELECT ROWNUM RNUM, V.*
			 	       FROM(SELECT * FROM V_REVIEW WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V)
			WHERE RNUM BETWEEN 1 AND 3*/
		
		try {
			stmt = conn.createStatement();
			
			rset = stmt.executeQuery(query);
			
			rList = new ArrayList<Board>();
			while(rset.next()) {
				Board temp = new Board();
				
				temp.setBrdTitle(rset.getString("TITLE"));
				temp.setAdtDate(rset.getDate("ADT_DATE"));
				temp.setBrdNo(rset.getInt("BRD_NO"));
				
				rList.add(temp);
			}
			
		} finally {
			close(rset);
			close(stmt);
		}
		
		return rList;
	}

	/** 메인 입양 후기 썸네일 목록 dao
	 * @param conn
	 * @return iList
	 */
	public List<Image> selectThumbMain(Connection conn) throws Exception {
		
		List<Image> iList = null;
		
		String query = prop.getProperty("selectThumbMain");
		/*SELECT * FROM IMAGE
			WHERE BRD_NO IN
				(SELECT BRD_NO
					FROM (SELECT ROWNUM RNUM, V.*
					 	       FROM(SELECT * FROM V_REVIEW WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V)
					WHERE RNUM BETWEEN 1 AND 3)
			AND FILE_LEVEL = 0*/
		try {
			stmt = conn.createStatement();
			
			rset = stmt.executeQuery(query);
			
			iList = new ArrayList<Image>();
			
			while(rset.next()) {
				Image img = new Image();
				img.setFileName(rset.getString("FILE_NAME"));
				img.setFilePath(rset.getString("FILE_PATH"));
				img.setBrdNo(rset.getInt("BRD_NO"));
				
				iList.add(img);
			}
			
		} finally {
			close(rset);
			close(stmt);
		}
		
		return iList;
	}
	
}















