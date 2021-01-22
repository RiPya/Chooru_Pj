package com.kh.semiProject.free.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.review.model.dao.ReviewDAO;

	public class FreeDAO {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;//xml 파일 읽어오기용
	
	public FreeDAO(){
		String fileName
		 = FreeDAO.class.getResource("/com/kh/semiProject/sql/free/free-query.xml").getPath();
		
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
	public int getListCount(Connection conn, String condition) throws Exception{
		
		int listCount = 0;
		
		
		String query = "SELECT COUNT(*) FROM V_FREE WHERE BRD_STATUS = 'Y'" + condition;
		
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

	/**BOARD에 게시글(글번호, 제목, 내용, 작성자, 게시판타입) 삽입 dao
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

	/** FREE에 게시글(자유 카테고리) 삽입 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertFree(Connection conn, Map<String, Object> map) throws Exception{
		
		int result = 0;
		
		String query = prop.getProperty("insertFree");
		/*INSERT INTO FREE VALUES(?, ?) */
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, (int)map.get("brdNo"));
			pstmt.setString(2, (String)map.get("freeCode"));
			
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

	/**자유게시판 상세 dao
	 * @param conn
	 * @param brdNo
	 * @return free
	 * @throws Exception
	 */
	public Board selectFree(Connection conn, int brdNo) throws Exception{
		Board free = null;
		
		String query = prop.getProperty("selectFree");
		/*SELECT * FROM V_FREE WHERE BRD_NO = ? AND BRD_STATUS = 'Y'*/
		/* VIEW :
		  	SELECT BRD_NO, TITLE, CONTENT, FREE_CODE, FREE_TYPE, N_NM, 
			BRD_CRT_DT, BRD_MODIFY, READ_COUNT, BRD_STATUS*/
		
		try {
			pstmt = conn.prepareStatement(query);
			
			//System.out.println(query);
			//System.out.println(brdNo);
			
			
			pstmt.setInt(1, brdNo);
			
			rset = pstmt.executeQuery();
						
			if(rset.next()) {
				free = new Board();
				//System.out.println("1 : " + free);
				
				
				free.setBrdNo(rset.getInt("BRD_NO"));
				free.setBrdTitle(rset.getString("TITLE"));
				free.setBrdContent(rset.getString("CONTENT"));
				free.setCode(rset.getString("FREE_TYPE"));//코드 자리에 자유 타입(한글이름)
				free.setNickName(rset.getString("N_NM"));
				free.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				free.setBrdModify(rset.getTimestamp("BRD_MODIFY"));
				free.setReadCount(rset.getInt("READ_COUNT"));
				
				//System.out.println("2 : " + free);
			}
			
		} finally {
			close(rset);
			close(pstmt);
		}
		return free;
	}

	/**상세 조회 시 조회수 증가 dao
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

	/**게시글 목록 조회 dao
	 * @param conn
	 * @param pInfo
	 * @param condition 
	 * @return fList
	 * @throws Exception
	 */
	public List<Board> selectFreeList(Connection conn, PageInfo pInfo, String condition) throws Exception{
		List<Board> fList = null;
		
		String query =
		  	"SELECT BRD_NO, FREE_TYPE, TITLE, N_NM, BRD_CRT_DT, READ_COUNT "
			+ "FROM (SELECT ROWNUM RNUM, V.* "
			 	      + "FROM(SELECT * FROM V_FREE "
			 	       		  +	"WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
			+ "WHERE RNUM BETWEEN ? AND ? " + condition;

		try {
			//sql 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			fList = new ArrayList<Board>();
			
			while(rset.next()) {
				Board free = new Board();
				
				free.setBrdNo(rset.getInt("BRD_NO"));
				free.setCode(rset.getString("FREE_TYPE"));
				free.setBrdTitle(rset.getString("TITLE"));
				free.setNickName(rset.getString("N_NM"));
				free.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				free.setReadCount(rset.getInt("READ_COUNT"));
				
				fList.add(free);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return fList;
	}

	/**게시글 썸네일 목록 조회 dao
	 * @param conn
	 * @param pInfo
	 * @param condition
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectThumbnails(Connection conn, PageInfo pInfo, String condition) throws Exception {
		
		List<Image> iList = null;
		
		String query = "";
		query = "SELECT FILE_NAME, BRD_NO FROM IMAGE "
		 		 + "WHERE BRD_NO IN "
			 		+ "(SELECT BRD_NO "
			 			+ "FROM (SELECT ROWNUM RNUM, V.* "
			 				 	    + "FROM(SELECT * FROM V_FREE WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
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
				img.setBrdNo(rset.getInt("BRD_NO"));
				
				iList.add(img);
			}
			
		} finally {
			close(rset);
			close(pstmt);
		}
		return iList;
	}

	/**게시글 목록 조회 댓글 수 조회 dao
	 * @param conn
	 * @param pInfo
	 * @param condition
	 * @return replyMap
	 * @throws Exception
	 */
	public List<Map<String, String>> selectReplyCount(Connection conn, PageInfo pInfo, String condition) throws Exception{
		List<Map<String, String>> commCounts = null;
		
		String query = "";
		
		/*BRD_NO, COUNT(*)"COMM_COUNT"*/
		query = "SELECT * FROM V_COMMCOUNT "
		 		 + "WHERE BRD_NO IN "
			 		+ "(SELECT BRD_NO "
			 			+ "FROM (SELECT ROWNUM RNUM, V.* "
			 				 	    + "FROM(SELECT * FROM V_FREE WHERE BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
			 			+ "WHERE RNUM BETWEEN ? AND ? " + condition + ") ";
		
		try {
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			commCounts = new ArrayList<Map<String,String>>();

			while(rset.next()) {
				Map<String, String> comm = new HashMap<String, String>();
				comm.put("brdNo", rset.getInt("BRD_NO")+"");
				comm.put("count", rset.getInt("COMM_COUNT")+"");
				
				commCounts.add(comm);
			}
			
			
		} finally {
			close(rset);
			close(pstmt);
		}
		return commCounts;
	}

	/** 자유 게시판 BOARD(공통 부분) 수정 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Connection conn, Map<String, Object> map) throws Exception{
		
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

	/**자유 게시판 FREE(자유게시판 부분) 수정 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateFree(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("updateFree");
		/*UPDATE FREE SET FREE_CODE = ? WHERE BRD_NO = ? */
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, (String)map.get("freeCode"));
			pstmt.setInt(2, (int)map.get("brdNo"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**자유게시판 수정 : 특정 게시물의 기존 이미지 목록 가져오기
	 * @param conn
	 * @param brdNo
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

	/**자유게시판 수정 : 수정 전 이미지 목록(oldImages) DB에서 삭제
	 * @param conn
	 * @param brdNo
	 * @return
	 * @throws Exception
	 */
	public int deleteOldImages(Connection conn, int brdNo) throws Exception{
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

	/**자유게시판 삭제(상태 변경) dao
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int updateBrdStatus(Connection conn, int brdNo) throws Exception {
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	