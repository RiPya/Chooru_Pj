package com.kh.semiProject.admin.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kh.semiProject.adoption.model.vo.Adoption;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.member.model.vo.Member;


public class AdminDAO {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;//xml 파일 읽어오기용
	
	public AdminDAO(){
		String fileName
		 = AdminDAO.class.getResource("/com/kh/semiProject/sql/admin/admin-query.xml").getPath();
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName)); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/** pageInfo 객체를 통한 전체 게시글 수 반환 DAO
	 * @param conn
	 * @return listCount
	 * @throws Exception
	 */
	public int getListCount(Connection conn) throws Exception{
		int listCount = 0;
		
		String query = prop.getProperty("getListCount");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			if(rset.next()) {
				listCount = rset.getInt(1);
			}
		}finally {
			close(rset);
			close(stmt);
		}
		return listCount;
	}
	
	
	/** 회원 목록 조회 DAO
	 * @param conn
	 * @param pInfo
	 * @return mList
	 * @throws Exception
	 */
	public List<Member> selectMemberList(Connection conn, PageInfo pInfo) throws Exception{
		List<Member> mList = null;
		
		String query = prop.getProperty("selectMemberList");
		
		try {
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
	
	/** 회원 등급 변경 DAO
	    * @param conn
	    * @param numberList
	    * @return result
	    * @throws Exception
	    */
	   public int updateMemGrade(Connection conn, String numberList, String grade) throws Exception{
	      
	      int result = 0;
	      
	      String query = "UPDATE MEMBER SET "
	             + " GRADE = ? "
	             + " WHERE MEM_NO IN( " + numberList + ")";
	      
	      try {
	    	  pstmt = conn.prepareStatement(query);
	    	  pstmt.setString(1, grade);
	   
	    	  result = pstmt.executeUpdate();
	    	  //System.out.println("result" + result);
	      }catch (Exception e){
	    	  e.printStackTrace();
	         
	         close(pstmt);
	      }
	      
	      return result;
	   }
	   
	   
	/** 다음 회원 번호 조회 DAO
	 * @param conn
	 * @return memNo
	 * @throws Exception
	 */
	public int selectNextNo(Connection conn) throws Exception{
		int memNo = 0;
		
		String query = prop.getProperty("selectNextNo");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			if(rset.next()) {
				memNo = rset.getInt(1);
			}
		}finally {
			close(rset);
			close(stmt);
		}
		return memNo;
	}
	
	
	/**게시글 블라인드(상태 변경) dao
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int blindBrdStatus(Connection conn, int brdNo) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("blindBrdStatus");
		/*UPDATE BOARD SET BRD_STATUS = 'B' WHERE BRD_NO = ?*/

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;		
	}
	
	
	
	/** 회원 관리 게시글 pInfo
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public int getBrdCount(Connection conn) throws Exception {
		
		int listCount = 0;
		
		String query
		= "SELECT COUNT(*) FROM BOARD";
		
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

	
	
	/**회원 관리 게시글 목록 조회 dao
	 * @param conn
	 * @param pInfo
	 * @return
	 * @throws Exception
	 */
	/*V_STATUS		"SELECT BRD_NO, BRD_TYPE, TITLE, MEM_ID,"
					+ " N_NM, BRD_CRT_DT, BRD_STATUS"
					+ " FROM BOARD"
					+ " JOIN MEMBER USING(MEM_NO)"
					+ " WHERE BRD_STATUS = 'N' OR BRD_STATUS = 'B'"*/
	
	public List<Board> selectBrdList(Connection conn, PageInfo pInfo) throws Exception{
		
		List<Board> bList = null;
		
		String query = "SELECT * "
					+ "FROM (SELECT ROWNUM RNUM, V.* "
							+ "FROM(SELECT * FROM V_STATUS "
								+	"ORDER BY BRD_NO DESC) V) "
		 	       	+ "WHERE RNUM BETWEEN ? AND ? ";
		
		try {
			//sql 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
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

	/** 회원 관리 게시글 상태 변경 dao
	 * @param conn
	 * @param brdNoList
	 * @param status 
	 * @return
	 * @throws Exception
	 */
	public int updateBrdStatus(Connection conn, String brdNoList, String status) throws Exception {
		
		int result = 0;
		
		String query = "UPDATE BOARD SET BRD_STATUS = ? WHERE BRD_NO IN (" + brdNoList + ")";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, status);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	
	//게시글 조회-------------------------------------------------------------

	/** 공지사항 select
	 * @param conn
	 * @param board
	 * @return notice
	 * @throws Exception
	 */
	public Board selectNotice(Connection conn, Board board) throws Exception {
		Board notice = null;
		
		String query = prop.getProperty("selectNotice");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, board.getBrdNo());
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				notice = new Board();
				notice.setBrdNo(rset.getInt("BRD_NO"));
				notice.setBrdTitle(rset.getString("TITLE"));
				notice.setBrdContent(rset.getString("CONTENT"));
				notice.setNickName(rset.getString("N_NM"));
				notice.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				notice.setBrdModify(rset.getTimestamp("BRD_MODIFY"));
				notice.setReadCount(rset.getInt("READ_COUNT"));
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return notice;
	}


	/** 후기 select
	 * @param conn
	 * @param board
	 * @return review
	 * @throws Exception
	 */
	public Board selectReview(Connection conn, Board board) throws Exception{
		Board review = null;
		
		String query = prop.getProperty("selectReview");
		/*SELECT * FROM V_REVIEW WHERE BRD_NO = ? AND BRD_STATUS = 'Y'*/
		/* VIEW :
		  	SELECT BRD_NO, TITLE, CONTENT, ADT_DATE, ADT_LINK, N_NM, 
			BRD_CRT_DT, BRD_MODIFY, READ_COUNT, BRD_STATUS*/
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, board.getBrdNo());
			
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


	/**자유 select
	 * @param conn
	 * @param board
	 * @return free
	 * @throws Exception
	 */
	public Board selectFree(Connection conn, Board board) throws Exception{
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
			
			
			pstmt.setInt(1, board.getBrdNo());
			
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


	/** 고객센터 select
	 * @param conn
	 * @param board
	 * @return info
	 * @throws Exception
	 */
	public Board selectInfo(Connection conn, Board board) throws Exception {
		Board info = null;
		
		String query = prop.getProperty("selectInfo");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, board.getBrdNo());
			
			rset = pstmt.executeQuery();
						
			if(rset.next()) {
				info = new Board();
				info.setBrdNo(rset.getInt("BRD_NO"));
				info.setBrdTitle(rset.getString("TITLE"));
				info.setBrdContent(rset.getString("CONTENT"));
				info.setCode(rset.getString("INFO_TYPE"));//코드 자리에 고객센터 타입(한글이름)
				info.setNickName(rset.getString("N_NM"));
				info.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				info.setReadCount(rset.getInt("READ_COUNT"));
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return info;
	}


	/** 입양/분양 select
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
	
	
	
	
}
