package com.kh.semiProject.information.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import static com.kh.semiProject.common.JDBCTemplate.close;
import static com.kh.semiProject.common.JDBCTemplate.commit;
import static com.kh.semiProject.common.JDBCTemplate.getConnection;
import static com.kh.semiProject.common.JDBCTemplate.rollback;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kh.semiProject.common.model.exception.FileInsertFailedException;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.information.model.dao.InformationDAO;
import com.kh.semiProject.image.model.vo.Image;

public class InformationDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;//xml 파일 읽어오기용
	
	public InformationDAO(){
		String fileName
		 = InformationDAO.class.getResource("/com/kh/semiProject/sql/information/information-query.xml").getPath();
		
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

	/** INFOMATION에 게시글(고객센터) 삽입 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertInfo(Connection conn, Map<String, Object> map) throws Exception{
		
		int result = 0;
		
		String query = prop.getProperty("insertInfo");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, (int)map.get("brdNo"));
			pstmt.setString(2, (String)map.get("category"));
			
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

	/** 고객센터 상세 조회 dao
	 * @param conn
	 * @param brdNo
	 * @return info
	 * @throws Exception
	 */
	public Board selectInfo(Connection conn, int brdNo) throws Exception{
		Board info = null;
		
		String query = prop.getProperty("selectInfo");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, brdNo);
			
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

	/**상세 조회 시 조회수 증가 dao
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

	/**게시글 목록 조회 dao
	 * @param conn
	 * @param pInfo
	 * @return ifList
	 * @throws Exception
	 */
	public List<Board> selectInfoList(Connection conn, PageInfo pInfo) throws Exception{
		List<Board> ifList = null;
		
		String query = prop.getProperty("selectInfoList");

		try {
			//sql 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			ifList = new ArrayList<Board>();
			
			while(rset.next()) {
				Board info = new Board();
				
				info.setBrdNo(rset.getInt("BRD_NO"));
				info.setCode(rset.getString("INFO_TYPE"));
				info.setBrdTitle(rset.getString("TITLE"));
				info.setNickName(rset.getString("N_NM"));
				info.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				info.setReadCount(rset.getInt("READ_COUNT"));
				
				ifList.add(info);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return ifList;
	}

	/**게시글 썸네일 목록 조회 dao
	 * @param conn
	 * @param pInfo
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectThumbnails(Connection conn, PageInfo pInfo) throws Exception {
		
		List<Image> iList = null;
		
		String query = prop.getProperty("selectThumbnails");
		
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
	 * @return replyMap
	 * @throws Exception
	 */
	public List<Map<String, String>> selectReplyCount(Connection conn, PageInfo pInfo) throws Exception{
		List<Map<String, String>> commCounts = null;
		
		String query = prop.getProperty("selectReplyCount");
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

	/** 고객센터 BOARD(공통 부분) 수정 dao
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

	/**고객센터 INFORAMTION 수정 dao
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateInfo(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("updateInfo");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, (String)map.get("InfoCode"));
			pstmt.setInt(2, (int)map.get("brdNo"));
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**고객센터 수정 : 특정 게시물의 기존 이미지 목록 가져오기
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

	/**고객센터 수정 : 수정 전 이미지 목록(oldImages) DB에서 삭제
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int deleteOldImages(Connection conn, int brdNo) throws Exception{
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

	/**고객센터 삭제(상태 변경) dao
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int updateBrdStatus(Connection conn, int brdNo) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("updateBrdStatus");
		
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

