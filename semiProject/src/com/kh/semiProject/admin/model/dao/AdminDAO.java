package com.kh.semiProject.admin.model.dao;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class AdminDAO {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;//xml 파일 읽어오기용
	
	public AdminDAO(){
		String fileName
		 = AdminDAO.class.getResource("/com/kh/semiProject/sql/admin/admin-query.xml").getPath();
		
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName)); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**게시글 블라인드(상태 변경) dao
	 * @param conn
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int blindBrdStatus(Connection conn, int brdNo) throws Exception{
		int result = 0;
		
		String query = prop.getProperty("blindBrdStatus");
		/*UPDATE BOARD SET BRD_STATUS = 'B' WHERE BRD_NO = ?*/

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, brdNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;		
	}
	
	
	
	
}
