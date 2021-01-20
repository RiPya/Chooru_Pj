package com.kh.semiProject.notice.model.dao;

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
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;

public class NoticeDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	Properties prop = null;
	
	public NoticeDAO() {
		String fileName
			= NoticeDAO.class.getResource("/com/kh/semiProject/sql/notice/notice-query.xml").getPath();
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName));
		}catch (Exception e){
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


	/** 게시글 목록조회 DAO
	 * @param conn
	 * @param pInfo
	 * @return nList
	 * @throws Exception
	 */
	public List<Board> selectNoticeList(Connection conn, PageInfo pInfo) throws Exception{
		List<Board> nList = null;
		
		String query = prop.getProperty("selectNoticeList");
		
		try {
			// sql 구문 조건절에 대입할 변수
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
				// System.out.println("nList");
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return nList;
	}


	/** 썸네일 목록조회 DAO
	 * @param conn
	 * @param pInfo
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectThumbnails(Connection conn, PageInfo pInfo) throws Exception{
		List<Image> iList = null;
		
		String query = prop.getProperty("selectThumbnails");
		
		try {
			// sql 구문 조건절에 대입할 변수
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


	/** 다음 게시글번호 조회 DAO
	 * @param conn
	 * @return brdNo
	 * @throws Exception
	 */
	public int selectNextNo(Connection conn) throws Exception{
		int brdNo = 0;
		
		String query = prop.getProperty("selectNextNo");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			if(rset.next()) {
				brdNo = rset.getInt(1);
			}
		}finally {
			close(rset);
			close(stmt);
		}
		return brdNo;
	}


	/** 공지글 상세조회 DAO
	 * @param conn
	 * @param brdNo
	 * @return notice
	 * @throws Exception
	 */
	public Board selectNotice(Connection conn, int brdNo) throws Exception{
		Board notice = null;
		
		String query = prop.getProperty("selectNotice");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, brdNo);
			
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


	/** 상세 조회 시 조회수 증가 DAO
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


	/** 게시글(글번호, 제목, 내용, 작성자, 게시판타입) 삽입 DAO
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, Map<String, Object> map) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, (int)map.get("brdNo"));
			pstmt.setString(2, (String)map.get("title"));
			pstmt.setString(3, (String)map.get("content"));
			pstmt.setInt(4, (int)map.get("memNo")); //회원번호
			pstmt.setString(5, (String)map.get("brdType"));//게시판 타입(b1)
			
			result = pstmt.executeUpdate();
			// System.out.println("게시글등록 DAO" + result);
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
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, img.getFileName());
			pstmt.setString(2, img.getFilePath());
			pstmt.setInt(3, img.getFileLevel());
			pstmt.setInt(4, img.getBrdNo());
			
			result = pstmt.executeUpdate();	
			// System.out.println("이미지 DAO " +result);
		} finally {
			close(pstmt);
		}
		return result;
	}


	/** 공지글 BOARD 부분 수정 DAO
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


	/** 공지글 수정_ 게시물의 기존 이미지 목록 가져오기
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
		}
		return oldImages;
	}


	/**공지글 수정_수정 전 이미지목록을 DB에서 삭제하기
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


	/** 공지글 삭제 DAO
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int updateBrdStatus(Connection conn, int brdNo) throws Exception{
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
