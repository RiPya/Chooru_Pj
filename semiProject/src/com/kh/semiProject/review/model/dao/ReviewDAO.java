package com.kh.semiProject.review.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
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
	
	
	/** pageInfo 객체를 위한 전체 게시글 수 반환 DAO
	 * @param conn
	 * @return listCount
	 * @throws Exception
	 */
	public int getListCount(Connection conn) throws Exception{
		
		int listCount = 0;
		
		String query = prop.getProperty("getListCount");
		/*SELECT COUNT(*) FROM V_REVIEW AND BRD_STATUS = 'Y'*/
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			if(rset.next()) {
				listCount = rset.getInt(1);
			}
			
		} finally {
			close(rset);
			close(stmt);
		}
		return listCount;
	}
	
	

	/**게시글 목록 조회 dao
	 * @param conn
	 * @param map
	 * @return rList
	 * @throws Exception
	 */
	public List<Board> selectReviewList(Connection conn, PageInfo pInfo) throws Exception{
		
		List<Board> rList = null;
		
		String query = prop.getProperty("selectReviewList");
		/* 입양 후기 조회를 위한 SQL
		  	SELECT RNUM, TITLE, N_NM, BRD_NO
			FROM (SELECT ROWNUM RNUM, V.*
			 	       FROM(SELECT * FROM V_REVIEW 
			 	       			WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V)
			WHERE RNUM BETWEEN ? AND ?
		 * */		
		
		try {
			//sql 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			rList = new ArrayList<Board>();
			
			while(rset.next()) {
				Board review = new Board();
				
				review.setBrdTitle(rset.getString("TITLE"));
				review.setNickName(rset.getString("N_NM"));
				review.setBrdNo(rset.getInt("BRD_NO"));
				
				rList.add(review);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return rList;
	}
	
	

	/**입양 후기 썸네일 목록 조회 dao
	 * @param conn
	 * @param pInfo
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectThumbnails(Connection conn, PageInfo pInfo) throws Exception{
		
		List<Image> iList = null;
		
		String query = prop.getProperty("selectThumbnails");
		/*
		 SELECT * FROM IMAGE
  		 WHERE BRD_NO IN
			(SELECT BRD_NO
				FROM (SELECT ROWNUM RNUM, V.*
				 	       FROM(SELECT * FROM V_REVIEW WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V)
				WHERE RNUM BETWEEN ? AND ?)
		 AND FILE_LEVEL = 0 
		 */
		
		try {
			//sql 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
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
			close(pstmt);
		}
		return iList;
	}
	
	
	/**입양 후기 상세 dao
	 * @param conn
	 * @param brdNo
	 * @return review
	 * @throws Exception
	 */
	public Board selectReview(Connection conn, int brdNo) throws Exception{
		
		Board review = null;
		
		String query = prop.getProperty("selectReview");
		/*SELECT * FROM V_REVIEW WHERE BRD_NO = ? AND BRD_STATUS = 'Y'*/
		/* VIEW :
		  	SELECT BRD_NO, TITLE, CONTENT, ADT_DATE, ADT_LINK, N_NM, 
			BRD_CRT_DT, BRD_MODIFY, READ_COUNT, BRD_STATUS*/
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, brdNo);
			
			rset = pstmt.executeQuery();
						
			if(rset.next()) {
				review = new Board();
				review.setBrdNo(rset.getInt("BRD_NO"));
				review.setBrdTitle(rset.getString("TITLE"));
				review.setBrdContent(rset.getString("CONTENT"));
				review.setAdtDate(rset.getDate("ADT_DATE"));
				review.setAdtLink(rset.getString("ADT_LINK"));
				review.setNickName(rset.getString("N_NM"));
				review.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				review.setBrdModify(rset.getTimestamp("BRD_MODIFY"));
				review.setReadCount(rset.getInt("READ_COUNT"));
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return review;
	}

	/**입양 후기 상세 조회 시 조회수 증가 dao
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(Connection conn, int brdNo) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("increaseReadCount");
		/*UPDATE BOARD SET READ_COUNT = READ_COUNT + 1 WHERE BRD_NO = ?*/
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);

			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
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



	/**입양 후기 BOARD(공통 부분) 수정 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Connection conn, Map<String, Object> map) throws Exception {
		
		int result = 0;
		
		String query = prop.getProperty("updateBoard");
		/*UPDATE BOARD SET TITLE = ?, CONTENT = ?, BRD_MODIFY = SYSDATE WHERE BRD_NO = ? */
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, (String)map.get("title"));
			pstmt.setString(2, (String)map.get("content"));
			pstmt.setInt(3, (int)map.get("brdNo"));
			
			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	/**입양 후기 REVIEW(입양 후기 부분) 수정 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateReview(Connection conn, Map<String, Object> map) throws Exception{
		
		int result = 0;
		
		String query = prop.getProperty("updateReview");
		/*UPDATE ADT_REVIEW SET ADT_DATE = ?, ADT_LINK = ? WHERE BRD_NO = ? */
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setDate(1, (Date)map.get("adtDate"));
			pstmt.setString(2, (String)map.get("adtLink"));
			pstmt.setInt(3, (int)map.get("brdNo"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}


	/**입양 후기 수정: 특정 게시물의 기존 이미지 목록 가져오기
	 * @param conn
	 * @param i
	 * @return oldImages
	 * @throws Exception
	 */
	public List<Image> selectOldImages(Connection conn, int brdNo) throws Exception{
		
		List<Image> oldImages = null;
		
		String query = prop.getProperty("selectOldImages");
		/*	SELECT FILE_NUMBER, FILE_NAME, FILE_PATH, FILE_LEVEL, BRD_NO 
			FROM IMAGE WHERE BRD_NO = ?
		 */
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);
			
			rset = pstmt.executeQuery();
			
			oldImages = new ArrayList<Image>();
			
			while(rset.next()) {
				Image img = new Image();
				img.setFileNo(rset.getInt("FILE_NUMBER"));
				img.setFileName(rset.getString("FILE_NAME"));
				img.setFilePath(rset.getString("FILE_PATH"));
				img.setFileLevel(rset.getInt("FILE_LEVEL"));
				img.setBrdNo(rset.getInt("BRD_NO"));
				
				oldImages.add(img);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return oldImages;
	}


	/**입양 후기 수정: 수정 전 이미지 목록(oldImages) DB에서 삭제
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int deleteOldImages(Connection conn, int brdNo) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("deleteOldImages");
		/*DELETE FROM IMAGE WHERE BRD_NO = ?*/
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}


	/**입양 후기 삭제(상태 변경) dao
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int updateBrdStatus(Connection conn, int brdNo) throws Exception{
		
		int result = 0;
		
		String query = prop.getProperty("updateBrdStatus");
		/*UPDATE BOARD SET BRD_STATUS = 'N' WHERE BRD_NO = ?*/

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}








	
	
	
	
}






















