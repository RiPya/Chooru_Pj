package com.kh.semiProject.member.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.vo.Member;
import com.sun.javafx.collections.MappingChange.Map;

public class MemberDAO {
	
	// DAO에서 자주 사용되는 JDBC 참조변수
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;
	
	// 외부에 저장된 XML 파일로부터 SQL을 얻어옴 → 유지보수성 향상
	public MemberDAO() {
		try {
			String filePath
				= MemberDAO.class.getResource("/com/kh/semiProject/sql/member/member-query.xml").getPath();
			
			prop = new Properties();
			
			prop.loadFromXML(new FileInputStream(filePath));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/** 회원가입용 DAO
	 * @param conn
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int signUp(Connection conn, Member member) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("signUp");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, member.getMemId());
			pstmt.setString(2, member.getMemPw());
			pstmt.setString(3, member.getMemNm());
			pstmt.setString(4, member.getPhone());
			pstmt.setString(5, member.getNickName());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPetYn()+"");
			
			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	
	/** 로그인 용 DAO
	 * @param conn
	 * @param member
	 * @return loginMember
	 * @throws Exception
	 */
	public Member loginMember(Connection conn, Member member) throws Exception{
		Member loginMember = null;
		
		String query = prop.getProperty("loginMember");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, member.getMemId());
			pstmt.setString(2, member.getMemPw());
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				loginMember = new Member(rset.getInt("MEM_NO"), 
										 rset.getString("GRADE").charAt(0),
										 rset.getString("MEM_ID"), 
										 rset.getString("MEM_NM"), 
										 rset.getString("PHONE"), 
										 rset.getString("N_NM"), 
										 rset.getString("EMAIL"), 
										 rset.getString("PET_YN").charAt(0));
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return loginMember;
	}


	/** 현재 비밀번호 일치여부 DAO
	 * @param conn
	 * @param loginMember
	 * @return result
	 * @throws Exception
	 */
	public int checkCurrentPwd(Connection conn, Member loginMember) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("checkCurrentPwd");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, loginMember.getMemNo());
			pstmt.setString(2, loginMember.getMemPw());
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				result = rset.getInt(1);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return result;
	}


	/** 비밀번호 변경 DAO
	 * @param conn
	 * @param loginMember
	 * @return result
	 * @throws Exception
	 */
	public int updatePwd(Connection conn, Member loginMember) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("updatePwd");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, loginMember.getMemPw());
			pstmt.setInt(2, loginMember.getMemNo());
			
			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
	}


	/** 회원 탈퇴용 DAO
	 * @param conn
	 * @param memNo
	 * @return result
	 * @throws Exception
	 */
	public int updateStatus(Connection conn, int memNo) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("updateStatus");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, memNo);
			
			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
	}


	/** 아이디 중복 DAO
	 * @param conn
	 * @param id
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(Connection conn, String id) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("idDupCheck");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, id);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				result = rset.getInt(1);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return result;
	}


	/** 닉네임 중복검사 DAO
	 * @param conn
	 * @param nickName
	 * @return result
	 * @throws Exception
	 */
	public int nickNameDupCheck(Connection conn, String nickName) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("nickNameDupCheck");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, nickName);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				result = rset.getInt(1);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return result;
	}


	/** 내 정보 수정 DAO
	 * @param conn
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int updateMyInfo(Connection conn, Member member) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("updateMyInfo");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, member.getMemNm());
			pstmt.setString(2, member.getPhone());
			pstmt.setString(3, member.getNickName());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPetYn()+"");
			pstmt.setInt(6, member.getMemNo());
			
			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
	}


	/** pageInfo 객체를 위한 전체 게시글 수 DAO
	 * @param conn
	 * @param loginMember 
	 * @return listCount
	 * @throws Exception
	 */
	public int getPageInfo(Connection conn, Member loginMember) throws Exception{
		int listCount = 0;
		
		String query = "SELECT COUNT(*) FROM V_MYACTIVE_LIST WHERE BRD_STATUS = 'Y' AND MEM_NO = ?";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, loginMember.getMemNo());
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				listCount = rset.getInt(1);
			}
		}finally {
			close(rset);
			close(pstmt);
		}
		return listCount;
	}


	/** 내 게시글 목록 DAO
	 * @param conn
	 * @param pInfo 
	 * @param loginMember
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> myActiveList(Connection conn, PageInfo pInfo, Member loginMember) throws Exception{
		List<Board> bList = null;
		
		String query = prop.getProperty("myActiveList");
		
		try {
			int startRow = (pInfo.getCurrentPage() -1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() -1;
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, loginMember.getMemNo());
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			
			rset = pstmt.executeQuery();
			
			bList = new ArrayList<Board>();
			
			while(rset.next()) {
				Board active = new Board();
				
				active.setBrdNo(rset.getInt("BRD_NO"));
				active.setBrdType(rset.getString("BRD_TYPE"));
				active.setBrdTitle(rset.getString("TITLE"));
				active.setNickName(rset.getString("N_NM"));
				active.setBrdCrtDt(rset.getTimestamp("BRD_CRT_DT"));
				active.setReadCount(rset.getInt("READ_COUNT"));
				
				bList.add(active);
			}
		} finally {
			close(rset);
			close(pstmt);
		}
		return bList;
	}


	/** 내 게시글 썸네일 조회 DAO
	 * @param conn
	 * @param pInfo
	 * @param loginMember
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> myActiveImage(Connection conn, PageInfo pInfo, Member loginMember) throws Exception{
		List<Image> iList = null;
		
		String query = prop.getProperty("myActiveImage");
		
		try {
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() +1;
			int endRow = startRow + pInfo.getLimit() -1;
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, loginMember.getMemNo());
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			
			rset = pstmt.executeQuery();
			
			iList = new ArrayList<Image>();
			
			while(rset.next()) {
				Image img = new Image();
				img.setFileName(rset.getString("FILE_NAME"));
				img.setBrdNo(rset.getInt("BRD_NO"));
				
				iList.add(img);
			}
		}finally {
			close(rset);
			close(pstmt);
		}
		return iList;
	}



}
