package com.kh.semiProject.admin.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import com.kh.semiProject.admin.model.dao.AdminDAO;
import com.kh.semiProject.adoption.model.vo.Adoption;
import com.kh.semiProject.common.model.vo.Board;
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

	/** 관리 블라인드/삭제 게시글 pInfo
	 * @param cp
	 * @return
	 * @throws Exception
	 */
	public PageInfo getBrdPInfo(String cp) throws Exception{
		
		Connection conn = getConnection();
		
		int currentPage = 0;
		if(cp == null) currentPage = 1;
		else currentPage = Integer.parseInt(cp);
		
		int listCount = dao.getBrdCount(conn);
		
		close(conn);
		
		return new PageInfo(currentPage, listCount);
	}

	
	/** 회원 게시글 관리 목록 service
	 * @param pInfo
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> selectBrdList(PageInfo pInfo) throws Exception {
		Connection conn = getConnection();
		
		List<Board> bList = dao.selectBrdList(conn, pInfo);
		
		close(conn);
		
		return bList;
	}

	
	/** 회원 관리 게시글 상태 변경
	 * @param brdNoList
	 * @param status 
	 * @return result
	 * @throws Exception
	 */
	public int updateBrdStatus(String brdNoList, String status) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.updateBrdStatus(conn, brdNoList, status);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		return result;
	}


	/** 관리페이지에서 게시글 조회
	 * @param board
	 * @return board
	 * @throws Exception
	 */
	public Board selectBrd(Board board) throws Exception {
		Connection conn = getConnection();
		
		
		String brdType = board.getBrdType();
		if(brdType == null) brdType = "all";//전체 검색(헤더 검색창)
		
		switch(brdType) {
		case "b1" : board = dao.selectNotice(conn, board); break; //공지사항
		case "b3" : board = dao.selectReview(conn, board); break; //입양 후기
		case "b4" : board = dao.selectFree(conn, board); break;//자유
		case "b5" : board = dao.selectInfo(conn, board); break; //고객센터
		}
		
		return board;
	}


	/** 입양/분양 select
	 * @param brdNo
	 * @return adoption
	 * @throws Exception
	 */
	public Adoption selectAdoption(int brdNo) throws Exception{
		
		Connection conn = getConnection();
		
		Adoption adoption = dao.selectAdoption(conn, brdNo);
		
		close(conn);
		
		return adoption;
	}
}

	
	

