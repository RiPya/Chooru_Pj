package com.kh.semiProject.mainpage.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.mainpage.model.dao.MainDAO;

public class MainService {
	
	private MainDAO dao = new MainDAO();

	/** 메인 입양 후기 목록 조회service
	 * @return rList
	 * @throws Exception
	 */
	public List<Board> selectReviewMain() throws Exception{
		
		Connection conn = getConnection();
		
		List<Board> rList = dao.selectReviewMain(conn);
		
		close(conn);
		
		return rList;
	}

	/**메인 입양 후기 썸네일 목록 service
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectReviewThumb() throws Exception{
		
		Connection conn = getConnection();
		
		List<Image> iList = dao.selectReviewThumb(conn);
		
		close(conn);
		
		return iList;
	}

	/**메인 공지사항 목록 조회 service
	 * @return nList
	 * @throws Exception
	 */
	public List<Board> selectNoticeMain() throws Exception{
		Connection conn = getConnection();
		
		List<Board> nList = dao.selectNoticeMain(conn);
		
		close(conn);
		
		return nList;			
	}

	/**메인 입양/분양 목록 조회 service
	 * @return aList
	 * @throws Exception
	 */
	public List<Board> selectAdoptMain() throws Exception {
		Connection conn = getConnection();
		
		List<Board> aList = dao.selectAdoptMain(conn);
		
		close(conn);
		
		return aList;
	}

	/**메인 입양/분양 목록 썸네일 목록 service
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectAdoptThumb() throws Exception{
		Connection conn = getConnection();
		
		List<Image> iList = dao.selectAdoptThumb(conn);
		
		close(conn);
		
		return iList;
	}

}
