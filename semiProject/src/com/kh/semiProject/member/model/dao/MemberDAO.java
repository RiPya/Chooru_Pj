package com.kh.semiProject.member.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.kh.semiProject.member.model.vo.Member;

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



}
