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

}
