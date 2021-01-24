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
	
	
	
	
}
