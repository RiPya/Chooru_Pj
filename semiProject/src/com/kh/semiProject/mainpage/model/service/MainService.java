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
	public List<Image> selectThumbMain() throws Exception{
		
		Connection conn = getConnection();
		
		List<Image> iList = dao.selectThumbMain(conn);
		
		close(conn);
		
		return iList;
	}

}
