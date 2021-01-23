package com.kh.semiProject.admin.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import com.kh.semiProject.admin.model.dao.AdminDAO;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.member.model.vo.Member;

public class AdminService {
	
	private AdminDAO dao = new AdminDAO();

	/** 페이징 처리를 위한  Service
	 * @param cp
	 * @return pInfo
	 * @throws Exception
	 */
	public PageInfo getPageInfo(String cp) throws Exception{
		Connection conn = getConnection();
		
		int currentPage = 0;
		
		if(cp == null)	currentPage = 1;
		else			currentPage = Integer.parseInt(cp);
		
		// db에서 게시글 수 조회하여 반환받기
		int listCount = dao.getListCount(conn);
		
		close(conn);
		
		//얻어온 현재 페이지와 DB에서 조회한 전체 게시글 수를 이용해 PageInfo 객체 생성해 반환
		return new PageInfo(currentPage, listCount);
	}
	
	
	/** 회원 목록 조회 Service
	 * @param pInfo
	 * @return mList
	 * @throws Exception
	 */
	public List<Member> selectMemberList(PageInfo pInfo) throws Exception{
		Connection conn = getConnection();
		
		List<Member> mList = dao.selectMemberList(conn, pInfo);
		
		close(conn);
		
		return mList;
	}
	
	
	/** 회원 등급 변경 Service
	 * @param numberList
	 * @return result
	 * @throws Exception
	 */
	public int updateMemGrade(String numberList, String grade) throws Exception {
	      
	      Connection conn = getConnection();
	      
	      int result = dao.updateMemGrade(conn, numberList, grade);
	      
	      if(result > 0) {
	         commit(conn);
	      }else {
	         rollback(conn);
	      }
	      
	      close(conn);
	      
	      
	      return result;
	   }
	
	/** 게시글 블라인드(상태 변경) service
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int blindBrdStatus(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.blindBrdStatus(conn, brdNo);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

}
