package com.kh.semiProject.admin.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;

import com.kh.semiProject.admin.model.dao.AdminDAO;

public class AdminService {
	
	private AdminDAO dao = new AdminDAO();

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
