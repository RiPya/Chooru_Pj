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

import com.kh.semiProject.adoption.model.vo.Adoption;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.vo.Member;


public class SearchDAO {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	
	
	/**검색 조건에 맞는 게시글 수를 가져와 pInfo를 생성하는 dao
	 * @param conn
	 * @param keyValue
	 * @return listCount
	 * @throws Exception
	 */
	public int getSearchCount(Connection conn, Map<String, Object> map) throws Exception {
		int listCount = 0;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String tpCondition = (String)map.get("tpCondition");//brdType 조건문(뷰 이름)
		String cdCondition = (String)map.get("cdCondition");//code 조건문
		
		String query = "SELECT COUNT(*) FROM " + tpCondition
				+ "WHERE " + keyValue + cdCondition + " AND BRD_STATUS = 'Y'";	

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
		String cdCondition = (String)map.get("cdCondition");//code 조건문
		if(cdCondition.equals("none")) cdCondition = " ";
		
		String query =
					"SELECT FILE_NAME, FILE_PATH, BRD_NO FROM IMAGE "
				   + "WHERE BRD_NO IN "
					  + "(SELECT BRD_NO "
						+ "FROM (SELECT ROWNUM RNUM, V.* "
						 	     + "FROM(SELECT * FROM " + tpCondition 
						 	       		+ "WHERE " + keyValue + cdCondition
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
	        	 img.setFilePath(rset.getString("FILE_PATH"));
	        	 
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
		String cdCondition = (String)map.get("cdCondition");//code 조건문
		if(cdCondition.equals("none")) cdCondition = " ";
		
		String query =
				"SELECT * FROM V_COMMCOUNT "
			   + "WHERE BRD_NO IN "
				  + "(SELECT BRD_NO "
					+ "FROM (SELECT ROWNUM RNUM, V.* "
					 	     + "FROM(SELECT * FROM " + tpCondition 
					 	       		+ "WHERE " + keyValue + cdCondition
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


	
	//--------------------------------------전체 검색--------------------------------------
		//condition을 사용하는 경우가 많기 때문에 xml을 사용X
		/*전체 검색 뷰
		 * CREATE OR REPLACE VIEW V_SEARCH
			AS
			SELECT BRD_NO, TITLE, N_NM, CONTENT, READ_COUNT, BRD_CRT_DT, BRD_STATUS, BRD_TYPE
			FROM BOARD
			JOIN MEMBER USING (MEM_NO);
		 */
		
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
		
		String query = "SELECT BRD_NO, TITLE, N_NM, BRD_CRT_DT, READ_COUNT, BRD_TYPE "
						+ "FROM (SELECT ROWNUM RNUM, V.* "
						 	     + "FROM(SELECT * FROM " + tpCondition 
						 	       		+ "WHERE " + keyValue
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
	
	

//----------------------------------공지사항----------------------------------------------	
	
	/**공지사항 내부 검색 board 리스트 dao
	 * @param conn
	 * @param map
	 * @return nList
	 * @throws Exception
	 */
	public List<Board> searchNoticeList(Connection conn, Map<String, Object> map) throws Exception{
		List<Board> nList = null;;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		
		String query = "SELECT TITLE, N_NM, BRD_NO, READ_COUNT, BRD_CRT_DT "
						+ "FROM (SELECT ROWNUM RNUM, V.* "
									+ "FROM(SELECT * FROM V_NOTICE "
											+ "WHERE" + keyValue
											+ " AND BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
					    +  "WHERE RNUM BETWEEN ? AND ?";
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
			
			nList = new ArrayList<Board>();
			
			while(rset.next()) {
				Board notice = new Board();
				
				notice.setBrdTitle(rset.getString("TITLE"));
				notice.setNickName(rset.getString("N_NM"));
				notice.setBrdNo(rset.getInt("BRD_NO"));
				notice.setReadCount(rset.getInt("READ_COUNT"));
				notice.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				
				nList.add(notice);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return nList;
	}
	

//------------------------------입양/분양--------------------------------------------------------

	/**입양/분양 내부 검색 board 리스트 dao
	 * @param conn
	 * @param map
	 * @return aList
	 * @throws Exception
	 */
	public List<Adoption> searchAdoptList(Connection conn, Map<String, Object> map) throws Exception{
		List<Adoption> aList = null;;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String cdCondition = (String)map.get("cdCondition");//code 조건문
		if(cdCondition.equals("none")) cdCondition = " ";
		
		String query =
			"SELECT TITLE, ADT_CODE, ADT_BREED, ADT_GENDER, ADT_AGE, ADT_YN, N_NM, BRD_NO "
			+ "FROM (SELECT ROWNUM RNUM, V.* "
		       		+ "FROM(SELECT * FROM V_ADOPTION "
		       			+ "WHERE " + keyValue + cdCondition
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
			
	
	
//---------------------------------입양 후기------------------------------------------------------
	/**입양 후기 내부 검색 board 리스트 dao
	 * @param conn
	 * @param map
	 * @return rList
	 * @throws Exception
	 */
	public List<Board> searchReviewList(Connection conn, Map<String, Object> map) throws Exception{
		List<Board> rList = null;;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		
		String query = "SELECT TITLE, N_NM, BRD_NO "
						+ "FROM (SELECT ROWNUM RNUM, V.* "
									+ "FROM(SELECT * FROM V_REVIEW "
											+ "WHERE" + keyValue
											+ " AND BRD_STATUS = 'Y' ORDER BY BRD_NO DESC) V) "
					    +  "WHERE RNUM BETWEEN ? AND ?";
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
	

	
	//----------------------------자유 게시판--------------------------------------
	/* 자유 게시판 뷰
	  	CREATE OR REPLACE VIEW V_FREE
		AS
		SELECT BRD_NO, TITLE, CONTENT, FREE_CODE, FREE_TYPE, N_NM, 
		BRD_CRT_DT, BRD_MODIFY, READ_COUNT, BRD_STATUS
		FROM BOARD
		JOIN BOARD_TYPE USING (BRD_TYPE)
		JOIN MEMBER USING (MEM_NO)
		JOIN FREE USING (BRD_NO)
		JOIN FREE_CATEGORY USING(FREE_CODE)
		WHERE BRD_TYPE = 'b4'
	 * */
	

	/**자유 게시판 내부 검색 board 리스트 dao
	 * @param conn
	 * @param map
	 * @return fList
	 * @throws Exception
	 */
	public List<Board> searchFreeList(Connection conn, Map<String, Object> map) throws Exception {
		List<Board> fList = null;;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String cdCondition = (String)map.get("cdCondition");//code 조건문
		if(cdCondition.equals("none")) cdCondition = " ";
		
		String query =
			"SELECT BRD_NO, TITLE, FREE_TYPE, N_NM, BRD_CRT_DT, READ_COUNT "
			+ "FROM (SELECT ROWNUM RNUM, V.* "
			 	     + "FROM(SELECT * FROM V_FREE "
			 	       		+ "WHERE " + keyValue + cdCondition
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
	         
	         fList = new ArrayList<Board>();
	         
	         while(rset.next()) {
	        	 Board board = new Board();
	        	 board.setBrdNo(rset.getInt("BRD_NO"));
	        	 board.setCode(rset.getString("FREE_TYPE"));
	        	 board.setBrdTitle(rset.getString("TITLE"));
	        	 board.setNickName(rset.getString("N_NM"));
	        	 board.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
	        	 board.setReadCount(rset.getInt("READ_COUNT"));
	        	 
	        	 fList.add(board);
	         }
			
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return fList;
	}

	/**고객센터 내부 검색 board 리스트 dao
	 * @param conn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Board> searchInfoList(Connection conn, Map<String, Object> map) throws Exception {
		List<Board> ifList = null;;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		String cdCondition = (String)map.get("cdCondition");//code 조건문
		if(cdCondition.equals("none")) cdCondition = " ";
		
		String query =
			"SELECT BRD_NO, INFO_TYPE, TITLE, N_NM, BRD_CRT_DT, READ_COUNT "
			+ "FROM (SELECT ROWNUM RNUM, V.* "
			 	     + "FROM(SELECT * FROM V_INFORMATION  "
			 	       		+ "WHERE " + keyValue + cdCondition
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
	         
	         ifList = new ArrayList<Board>();
	         
	         while(rset.next()) {
	        	 Board board = new Board();
	        	 board.setBrdNo(rset.getInt("BRD_NO"));
	        	 board.setCode(rset.getString("INFO_TYPE"));
	        	 board.setBrdTitle(rset.getString("TITLE"));
	        	 board.setNickName(rset.getString("N_NM"));
	        	 board.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
	        	 board.setReadCount(rset.getInt("READ_COUNT"));
	        	 
	        	 ifList.add(board);
	         }
			
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return ifList;
	}
	
	
	//-----------------------------관리자 페이지---------------------------------
	/** 관리자 페이지 회원 관리 페이지 뷰 dao 
	 * @param conn
	 * @param map
	 * @return listCount
	 * @throws Exception
	 */
	public int getMemCount(Connection conn, Map<String, Object> map) throws Exception {
		int listCount = 0;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		
		String query = "SELECT COUNT(*) FROM MEMBER "
				+ "WHERE " + keyValue;	

		System.out.println(query);
		
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

	/** 회원 검색 목록 조회 dao
	 * @param conn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Member> selectMemberList(Connection conn, Map<String, Object> map) throws Exception{
		List<Member> mList = null;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		
		String query =
				"SELECT RNUM, MEM_NO, MEM_ID, N_NM, MEM_NM, GRADE, ENROLL_DATE, PHONE, EMAIL "
				+ "FROM (SELECT ROWNUM RNUM, V.* "
				 	     +  "FROM(SELECT * " 
				 	       	    + "FROM V_MEMBER "
				 	       	    + "WHERE " + keyValue
				 	       		+ " ORDER BY MEM_NO DESC) V) "
				+ "WHERE RNUM BETWEEN ? AND ? ";

		try {
			PageInfo pInfo = (PageInfo)map.get("pInfo");
			
			// sql 구문 조건절에 대입할 변수
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			mList = new ArrayList<Member>();
			
			while(rset.next()) {
				Member member = new Member();
				
				member.setMemNo(rset.getInt("MEM_NO"));
				member.setMemId(rset.getString("MEM_ID"));
				member.setNickName(rset.getString("N_NM"));
				member.setMemNm(rset.getString("MEM_NM"));
				member.setGrade(rset.getString("GRADE").charAt(0));
				member.setEnrollDate(rset.getDate("ENROLL_DATE"));
				member.setPhone(rset.getString("PHONE"));
				member.setEmail(rset.getString("EMAIL"));
				
				mList.add(member);
				// System.out.println("mList");
			}
			
		} finally {
			close(rset);
			close(pstmt);
		}
		return mList;
	}
	
	
	
	/**회원 관리 게시글 목록 검색 조회 dao
	 * @param conn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Board> selectBrdList(Connection conn, Map<String, Object> map) throws Exception {
		
		List<Board> bList = null;
		
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		if(keyValue.equals("none")) {
			keyValue = " ";
		} else {
			keyValue = keyValue + " AND";
		}
		
		String status = (String)map.get("status");
		
		String query = "SELECT * "
					+ "FROM (SELECT ROWNUM RNUM, V.* "
							+ "FROM(SELECT * FROM V_STATUS "
								+	"WHERE " + keyValue + " BRD_STATUS IN (" + status + ")"
								+	" ORDER BY BRD_NO DESC) V) "
		 	       	+ "WHERE RNUM BETWEEN ? AND ? ";
		try {
			PageInfo pInfo = (PageInfo)map.get("pInfo");
			//sql 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			System.out.println(query);
			rset = pstmt.executeQuery();
			
			bList = new ArrayList<Board>();
			
			while(rset.next()) {
				Board board = new Board();
				
				board.setBrdNo(rset.getInt("BRD_NO"));
				board.setBrdType(rset.getString("BRD_TYPE"));
				board.setBrdTitle(rset.getString("TITLE"));
				board.setNickName(rset.getString("MEM_ID") + "/" + rset.getString("N_NM"));
				board.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				board.setBrdStatus(rset.getString("BRD_STATUS").charAt(0));
				
				bList.add(board);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return bList;
	}

	public int getBrdCount(Connection conn, Map<String, Object> map) throws Exception{
		
		int listCount = 0;
		String keyValue = (String)map.get("keyValue");//key-value 조건문
		if(keyValue.equals("none")) {
			keyValue = " ";
		} else {
			keyValue = keyValue + " AND";
		}
		
		String status = (String)map.get("status");
		
		String query
			= "SELECT COUNT(*) FROM V_STATUS WHERE " + keyValue + " BRD_STATUS IN (" + status + ")" ;
		
		
		
		System.out.println(query);
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

	

}











