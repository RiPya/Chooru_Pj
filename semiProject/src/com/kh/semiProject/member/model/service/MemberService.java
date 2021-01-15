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


	/** 비밀번호 변경용 Service
	 * @param loginMember
	 * @param newPwd
	 * @return result
	 * @throws Exception
	 */
	public int updatePwd(Member loginMember, String newPwd) throws Exception{
		Connection conn = getConnection();
		
		// 1. 현재 비밀번호 일치여부 검사
		int result = dao.checkCurrentPwd(conn, loginMember);
		
		// 2. 현재 비밀번호가 일치했을 때, 새 비밀번호로 수정
		if(result > 0) {
			
			// loginMember 비밀번호 필드에 newPwd를 세팅하여 재활용
			loginMember.setMemPw(newPwd);
			
			result = dao.updatePwd(conn, loginMember);
			
			// 2번의 트랜잭션 처리
			if(result > 0)	commit(conn);
			else			rollback(conn);
		}else {
			result = -1;
		}
		return result;
	}

}
