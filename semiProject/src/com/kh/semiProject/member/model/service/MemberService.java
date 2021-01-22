package com.kh.semiProject.member.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.member.model.dao.MemberDAO;
import com.kh.semiProject.member.model.vo.Member;
import com.kh.semiProject.reply.model.vo.Reply;
import com.sun.javafx.collections.MappingChange.Map;

public class MemberService {
	
	private MemberDAO dao = new MemberDAO();

	
	/** 회원가입용 Service
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int signUp(Member member) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.signUp(conn, member);
		
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn);
		return result;
	}
	
	
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


	/** 회원탈퇴용 Service
	 * @param loginMember
	 * @return result 
	 * @throws Exception
	 */
	public int updateStatus(Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		// 현재 비밀번호 일치 여부
		int result = dao.checkCurrentPwd(conn, loginMember);
		
		// 현재 비밀번호 일치 시 탈퇴 진행
		if(result > 0) {
			result = dao.updateStatus(conn, loginMember.getMemNo());
			
			if(result > 0)	commit(conn);
			else			rollback(conn);
		}else {
			result = -1;
		}
		
		close(conn);
		return result;
	}

	/** 아이디 중복 Service
	 * @param id
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(String id) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.idDupCheck(conn, id);
		
		close(conn);
		
		return result;
	}


	/** 닉네임 중복 Service
	 * @param nickName
	 * @return result
	 * @throws Exception
	 */
	public int nickNameDupCheck(String nickName) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.nickNameDupCheck(conn, nickName);
		// System.out.println("service1: " + result);
		
		close(conn);
		return result;
	}


	/** 이메일 중복 Service
	 * @param email
	 * @return result
	 * @throws Exception
	 */
	public int emailDupCheck(String email) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.emailDupCheck(conn, email);
		// System.out.println("service2: " + result);
		
		close(conn);
		return result;
	}

	
	/** 내 정보 수정 Service
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int updateMyInfo(Member member) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.updateMyInfo(conn, member);
		
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn);
		return result;
	}


	/** 페이징 처리 Service
	 * @param cp 
	 * @param loginMember 
	 * @return pInfo
	 * @throws Exception
	 */
	public PageInfo getPageInfo(String cp, Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		int currentPage = 0;
		
		if(cp == null)	currentPage = 1;
		else			currentPage = Integer.parseInt(cp);
		
		int listCount = dao.getListCount(conn, loginMember);
		
		close(conn);
		return new PageInfo(currentPage, listCount);
	}


	/** 내 게시글 목록 조회 Service
	 * @param pInfo
	 * @param loginMember
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> selectMyActiveList(PageInfo pInfo, Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		List<Board> bList = dao.selectMyActiveList(conn, pInfo, loginMember);
		
		close(conn);
		
		return bList;
	}


	/** 내 게시글 썸네일 조회 Service
	 * @param pInfo
	 * @param loginMember
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> myActiveImage(PageInfo pInfo, Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		List<Image> iList = dao.myActiveImage(conn, pInfo, loginMember);
		
		close(conn);
		return iList;
	}


	/** 비밀번호 확인 Servcie
	 * @param loginMember
	 * @return result
	 * @throws Exception
	 */
	public int checkPwd(Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		// 현재 비밀번호 확인 여부
		int result = dao.checkCurrentPwd(conn, loginMember);
		
		close(conn);
		return result;
	}

	/** 내 댓글 페이징처리 Service
	 * @param cp
	 * @param loginMember
	 * @return pInfo
	 * @throws Exception
	 */
	public PageInfo getReplyPageInfo(String cp, Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		int currentPage = 0;
		
		if(cp == null)	currentPage = 1;
		else			currentPage = Integer.parseInt(cp);
		
		int listCount = dao.getReplyPageInfo(conn, loginMember);
		
		close(conn);
		return new PageInfo(currentPage, listCount);
	}

	/** 내가 쓴 댓글 목록조회 Service
	 * @param pInfo
	 * @param loginMember
	 * @return pList
	 * @throws Exception
	 */
	public List<Reply> selectMyReplyList(PageInfo pInfo, Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		List<Reply> pList = dao.selectMyReplyList(conn, pInfo, loginMember);
		
		close(conn);
		return pList;
	}


	/** 아이디찾기 회원 일치여부 Service 
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int memberIdCheck(Member member) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.memberIdCheck(conn, member);
		
		close(conn);
		return result;
	}


	/** 비밀번호 찾기 회원 일치여부 Service
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int memberPwdCheck(Member member) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.memberPwdCheck(conn, member);
		
		close(conn);
		return result;
	}


	/** 회원 비밀번호 찾기 변경 Service
	 * @param loginMember
	 * @param loginMember
	 * @return result
	 * @throws Exception
	 */
	public int findPwd(Member loginMember) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.findPwd(conn, loginMember);
		
		close(conn);
		return result;
	}


	/** 아이디찾기 결과 Service
	 * @param loginMember
	 * @return result
	 * @throws Exception
	 */
	public String myId(Member member) throws Exception{
		Connection conn = getConnection();
		
		String memberId = dao.myId(conn, member);
		
		close(conn);
		return memberId;
	}
}
