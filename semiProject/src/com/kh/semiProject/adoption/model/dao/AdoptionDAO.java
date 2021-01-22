package com.kh.semiProject.adoption.model.dao;

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

import com.kh.semiProject.adoption.model.vo.Adoption;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
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
	
	/** pageInfo 객체를 위한 전체 게시글 수 반환 DAO
	 * @param conn
	 * @param condition 
	 * @return listCount
	 * @throws Exception
	 */
	public int getListCount(Connection conn, String condition) throws Exception {
		
		int listCount = 0;
		
		String query = "SELECT COUNT(*) FROM V_ADOPTION WHERE BRD_STATUS = 'Y'" + condition;
		
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

	/** 게시글 목록 조회 DAO
	 * @param conn
	 * @param map
	 * @return rList
	 * @throws Exception
	 */
	public List<Adoption> selectAdoptionList(Connection conn, PageInfo pInfo, String condition) throws Exception{
		
		List<Adoption> aList = null;
		
		String query = 		
				"SELECT RNUM, TITLE, ADT_CODE, ADT_BREED, ADT_GENDER, ADT_AGE, ADT_YN, N_NM, BRD_NO "
			    		+ "FROM (SELECT ROWNUM RNUM, V.* "
			 	       		+ "FROM(SELECT * FROM V_ADOPTION "
			 	       			+ "WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
	 	       			+ "WHERE RNUM BETWEEN ? AND ? " + condition;
	
		
		try {
			// sql 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			aList = new ArrayList<Adoption>();
			
			while(rset.next()) {
				Adoption adoption = new Adoption();
				
				adoption.setAdtBrdTitle(rset.getString("TITLE"));
				adoption.setAdtCode(rset.getString("ADT_CODE"));
				adoption.setAdtBreed(rset.getString("ADT_BREED"));
				adoption.setAdtGender(rset.getString("ADT_GENDER"));
				adoption.setAdtAge(rset.getString("ADT_AGE"));
				adoption.setAdtYn(rset.getString("ADT_YN").charAt(0));
				adoption.setNickName(rset.getString("N_NM"));
				adoption.setAdtBrdNo(rset.getInt("BRD_NO"));
				
				
				aList.add(adoption);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return aList;
	}
	
	/**입양 후기 썸네일 목록 조회 DAO
	 * @param conn
	 * @param pInfo
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectThumbnails(Connection conn, PageInfo pInfo, String condition) throws Exception{
		
		List<Image> iList = null;
		
		String query = "";
		query = "SELECT * FROM IMAGE "
		 		 + "WHERE BRD_NO IN "
			 		+ "(SELECT BRD_NO "
			 			+ "FROM (SELECT ROWNUM RNUM, V.* "
			 				 	    + "FROM(SELECT * FROM V_ADOPTION WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
			 			+ "WHERE RNUM BETWEEN ? AND ? " + condition + ") "
		 		+ "AND FILE_LEVEL = 0 ";
		
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
	
	/** 입양/분양 상세 조회 DAO
	 * @param conn
	 * @param brdNo
	 * @return adoption
	 * @throws Exception
	 */
	public Adoption selectAdoption(Connection conn, int brdNo) throws Exception {
		
		Adoption adoption = null;
		
		String query = prop.getProperty("selectAdoption");

		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, brdNo);
			
			rset = pstmt.executeQuery();
						
			if(rset.next()) {
				adoption = new Adoption();
				
				adoption.setAdtBrdNo(rset.getInt("BRD_NO"));
				adoption.setAdtCode(rset.getString("ADT_CODE"));
				adoption.setAdtBrdTitle(rset.getString("TITLE"));
				adoption.setAddress(rset.getString("ADDRESS"));
				adoption.setAdtNote(rset.getString("ADT_NOTE"));
				adoption.setAdtBreed(rset.getString("ADT_BREED"));
				adoption.setAdtAge(rset.getString("ADT_AGE"));
				adoption.setAdtGender(rset.getString("ADT_GENDER"));
				adoption.setAdtVaccination(rset.getString("ADT_VACCINATION").charAt(0));
				adoption.setAdtTime(rset.getDate("ADT_TIME"));
				adoption.setAdtYn(rset.getString("ADT_YN").charAt(0));
				adoption.setAdtBrdContent(rset.getString("CONTENT"));
				adoption.setNickName(rset.getString("N_NM"));
				adoption.setAdtBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				adoption.setAdtBrdModify(rset.getTimestamp("BRD_MODIFY"));
				adoption.setReadCount(rset.getInt("READ_COUNT"));

			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return adoption;
	}
	
	/** 입양/분양 게시글 상세 조회 시 조회수 증가
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(Connection conn, int brdNo) throws Exception{
		
		int result = 0;
		
		String query = prop.getProperty("increaseReadCount");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);

			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
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
			pstmt.setString(5, (String)map.get("brdType")); // 게시판 타입(B2)
			
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
			pstmt.setString(2, (String)map.get("adtCode"));
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

	/** 입양/분양  BOARD(공통 부분) 수정 DAO
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Connection conn, Map<String, Object> map) throws Exception{
		
		int result = 0;
		
		String query = prop.getProperty("updateBoard");
		
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

	/**입양/분양 ADOPTION(입양/분양 부분) 수정 DAO
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateAdoption(Connection conn, Map<String, Object> map) throws Exception{
		
		int result = 0;
		
		String query = prop.getProperty("updateAdoption");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, (String)map.get("category"));
			pstmt.setString(2, (String)map.get("adtBreed"));
			pstmt.setString(3, (String)map.get("adtGender"));
			pstmt.setString(4, (String)map.get("adtAge"));
			pstmt.setString(5, (String)map.get("address"));
			pstmt.setString(6, (String)map.get("adtVaccination"));
			pstmt.setString(7, (String)map.get("adtNote"));
			pstmt.setDate(8, (Date)map.get("adtDate"));
			pstmt.setString(9, (String)map.get("adtYn"));
			pstmt.setInt(10, (int)map.get("brdNo"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** 입양/분양 수정: 특정 게시물의 기존 이미지 목록 가져오기
	 * @param conn
	 * @param brdNo
	 * @return oldImages
	 * @throws Exception
	 */
	public List<Image> selectOldImages(Connection conn, int brdNo) throws Exception{
		
		List<Image> oldImages = null;
		
		String query = prop.getProperty("selectOldImages");
		
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
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** Adoption 글 삭제 DAO
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int updateBoardStatus(Connection conn, int brdNo) throws Exception {
		
		int result = 0;
		
		String query = prop.getProperty("updateBoardStatus");
		
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
