package com.kh.semiProject.member.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;

import com.kh.semiProject.member.model.dao.MemberDAO;
import com.kh.semiProject.member.model.vo.Member;

public class MemberService {
	
	private MemberDAO dao = new MemberDAO();

	
	/** 로그인 용 Service
	 * @param member
	 * @return loginMember
	 * @throws Exception
	 */
	public Member loginMember(Member member) throws Exception{
		Connection conn = getConnection();
		
		// 결과를 반환받아올 DAO 구문
		Member loginMember = dao.loginMember(conn, member);
		
		close(conn);
		return loginMember;
	}

}
