package com.kh.semiProject.search.model.dao;
import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;


public class SearchDAO {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	
	//condition을 사용하는 경우가 많기 때문에 xml을 사용X
	/*전체 검색 뷰
	 * CREATE OR REPLACE VIEW V_SEARCH
		AS
		SELECT BRD_NO, TITLE, N_NM, CONTENT, READ_COUNT, BRD_CRT_DT, BRD_STATUS, BRD_TYPE
		FROM BOARD
		JOIN MEMBER USING (MEM_NO);
	 */
	
	
	/**검색 조건에 맞는 게시글 수를 가져와 pInfo를 생성하는 dao
	 * @param conn
	 * @param keyValue
	 * @return listCount
	 * @throws Exception
	 */
	public int getSearchCount(Connection conn, Map<String, Object> map) throws Exception {
		int listCount = 0;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String tpCondition = (String)map.get("tpCondition");//brdType 조건문
		
		String query = "SELECT COUNT(*) FROM V_SEARCH "
						+ "WHERE BRD_STATUS = 'Y' AND" + keyValue + tpCondition;		
		//System.out.println(query);
		
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


	/**전체 검색 게시글 목록 리스트 조회 dao
	 * @param conn
	 * @param map
	 * @return sList
	 * @throws Exception
	 */
	public List<Board> searchBrdList(Connection conn, Map<String, Object> map) throws Exception{
		List<Board> sList = null;;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String tpCondition = (String)map.get("tpCondition");//brdType 조건문
		
		String query =
			"SELECT BRD_NO, TITLE, N_NM, BRD_CRT_DT, READ_COUNT, BRD_TYPE "
			+ "FROM (SELECT ROWNUM RNUM, V.* "
			 	     + "FROM(SELECT * FROM V_SEARCH "
			 	       		+ "WHERE " + keyValue + tpCondition
			 	       		+ " AND BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
			+ "WHERE RNUM BETWEEN ? AND ?";
		
		//System.out.println(query);
		
		try {
			//SQL 구문 조건절에 대입할 변수 생성
			 PageInfo pInfo = (PageInfo)map.get("pInfo");
			
		 	 int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
		 	 int endRow = startRow + pInfo.getLimit() - 1;
			
		 	 pstmt = conn.prepareStatement(query);
			
	         pstmt.setInt(1, startRow);
	         pstmt.setInt(2, endRow);
	         
	         rset = pstmt.executeQuery();
	         
	         sList = new ArrayList<Board>();
	         
	         while(rset.next()) {
	        	 Board board = new Board();
	        	 board.setBrdNo(rset.getInt("BRD_NO"));
	        	 board.setBrdTitle(rset.getString("TITLE"));
	        	 board.setNickName(rset.getString("N_NM"));
	        	 board.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
	        	 board.setReadCount(rset.getInt("READ_COUNT"));
	        	 board.setBrdType(rset.getString("BRD_TYPE"));
	        	 
	        	 sList.add(board);
	         }
			
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return sList;
	}


	/**검색이 적용된 썸네일 목록 조회 dao
	 * @param conn
	 * @param map
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectSearchThumbs(Connection conn, Map<String, Object> map) throws Exception{
		List<Image> iList = null;;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String tpCondition = (String)map.get("tpCondition");//brdType 조건문
		
		String query =
					"SELECT FILE_NAME, BRD_NO FROM IMAGE "
				   + "WHERE BRD_NO IN "
					  + "(SELECT BRD_NO "
						+ "FROM (SELECT ROWNUM RNUM, V.* "
						 	     + "FROM(SELECT * FROM V_SEARCH "
						 	       		+ "WHERE " + keyValue + tpCondition
						 	       		+ " AND BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
						+ "WHERE RNUM BETWEEN ? AND ?) "
				   + "AND FILE_LEVEL = 0";
		
		try {
			//SQL 구문 조건절에 대입할 변수 생성
			 PageInfo pInfo = (PageInfo)map.get("pInfo");
			
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


	/**검색 게시글 목록 댓글 수 조회 dao
	 * @param conn
	 * @param map
	 * @return replyMap
	 * @throws Exception
	 */
	public List<Map<String, String>> selectReplyCount(Connection conn, Map<String, Object> map) throws Exception {

		List<Map<String, String>> commCounts = null;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String tpCondition = (String)map.get("tpCondition");//brdType 조건문
		
		String query =
				"SELECT * FROM V_COMMCOUNT "
						   + "WHERE BRD_NO IN "
							  + "(SELECT BRD_NO "
								+ "FROM (SELECT ROWNUM RNUM, V.* "
								 	     + "FROM(SELECT * FROM V_SEARCH "
								 	       		+ "WHERE " + keyValue + tpCondition
								 	       		+ " AND BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
								+ "WHERE RNUM BETWEEN ? AND ?) ";
		
		try {
			//SQL 구문 조건절에 대입할 변수 생성
			 PageInfo pInfo = (PageInfo)map.get("pInfo");
			
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
	
	
	
}
















