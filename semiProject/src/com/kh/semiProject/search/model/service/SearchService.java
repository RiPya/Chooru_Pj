package com.kh.semiProject.search.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kh.semiProject.adoption.model.vo.Adoption;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.search.model.dao.SearchDAO;

public class SearchService {
	private SearchDAO dao = new SearchDAO();

//------------------------------pInfo-----------------------------------------------
	/** 검색 조건에 맞는 게시글 수를 가져와 pInfo를 생성하는 service
	 * @param map
	 * @return pInfo
	 * @throws Exception
	 */
	public PageInfo getSearchPage(Map<String, Object> map) throws Exception{
		
		Connection conn = getConnection();
		
		int currentPage = 0;
		if(map.get("currentPage") == null) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt((String)map.get("currentPage"));
		}
		//현재 페이지 전달
		map.put("currentPage", currentPage);
		
		//DB에서 조건을 만족하는 게시글 수를 조회하기
		int listCount = dao.getSearchCount(conn, map);
		close(conn);
		
		return new PageInfo((int)map.get("currentPage"), listCount);
	}
	
	
	//------------------조건문 service : keyValue, tpCondition, cdCondition-----------------------------------
	

	/** key, value에 해당하는 조건문 service
	 * @param map
	 * @return keyValue
	 * @throws Exception
	 */
	public String getSkSvCondition(String searchKey, String searchValue) throws Exception {
		
		String keyValue = null;
		
		if(searchKey == null) searchKey = "all"; //고객센터, 전체 검색
		//System.out.println(searchKey);
		
		//검색 조건(searchKey)에 따라 SQL 조합
		switch(searchKey) {
			/* 주의) '%searchValue%' 부분에 searchValue를 변수를 사용해 문자열로 만들려면
			 * 아래와 같이 "'%' || '" + 변수 + "' || '%'" 형태로 해야 함
			 * 그래야 DB에서 정상적으로 sql 구문이 수행함*/
		case "title" :	 //제목
			keyValue = " TITLE LIKE '%' || '" + searchValue + "' || '%' ";
			break;
		case "titcont" : //제목+내용
			keyValue = " (TITLE LIKE '%' || '" + searchValue + "' || '%' "
						+ "OR CONTENT LIKE '%' || '" + searchValue + "' || '%') ";		
			break;
		case "writer" : //내용
			keyValue = " N_NM LIKE '%' || '" + searchValue + "' || '%' ";
			break;
		case "allKey" : //'전체' 검색
		case "all" : //전체 (전체 검색 + 고객센터는 무조건 이쪽으로)
		default : 
			keyValue = " (TITLE LIKE '%' || '" + searchValue + "' || '%' "
					+ "OR CONTENT LIKE '%' || '" + searchValue + "' || '%' "
					+ "OR N_NM LIKE '%' || '" + searchValue + "' || '%') ";
			break;
		}
		
		return keyValue;
	}
	
	/** brdType에 해당하는 조건문 service
	 * @param brdType
	 * @return tpCondition
	 * @throws Exception
	 */
	public String getTpCondition(String brdType) throws Exception {
		String tpCondition = null;

		if(brdType == null) brdType = "all";//전체 검색(헤더 검색창)
		
		switch(brdType) {
		case "b1" : //공지사항
			tpCondition = " V_NOTICE "; break;
		case "b2" : //입양/분양
			tpCondition = " V_ADOPTION "; break;
		case "b3" : //입양 후기
			tpCondition = " V_REVIEW "; break;
		case "b4" : //자유 게시판
			tpCondition = " V_FREE "; break;
		case "all" :
		default : //전체
			tpCondition = " V_SEARCH "; break;
		}

		return tpCondition;
	}
	
 
	/** code에 해당하는 조건문 service
	 * @param code
	 * @return cdCondition
	 * @throws Exception
	 */
	public String getCdCondition(String code) throws Exception {
		
		String cdCondition = null;
		
		if(code == null) code = "all";//코드 없는 경우
		//System.out.println(code);
		
		switch(code) {
		//입양/분양 카테고리
		case "adtDog" : //입양 개
			cdCondition = " AND ADT_CODE = 'adtDog' "; break;
		case "adtCat" : //입양 고양이
			cdCondition = " AND ADT_CODE = 'adtCat' "; break;
		case "adtEtc" : //입양 기타
			cdCondition = " AND ADT_CODE = 'adtEtc' "; break;
		case "temp" : //임시 보호
			cdCondition = " AND ADT_CODE = 'temp' "; break;
		//자유 카테고리
		case "frDay" : //일상
			cdCondition = " AND FREE_CODE = 'frDay' "; break;
		case "frReview" : //제품 후기
			cdCondition = " AND FREE_CODE = 'frReview' "; break;
		case "frInfo" : //일상
			cdCondition = " AND FREE_CODE = 'frInfo' "; break;
			
		//추가
		case "all" :
		default : //adtAll, frAll, null값
			cdCondition = " ";
		}
		
		//System.out.println(cdCondition);
		return cdCondition;
	}

	
//--------------------------검색 조회 service ----------------------------
	
	
	/**검색 게시글 목록 리스트 조회 service
	 * @param map
	 * @return bList → sList, nList, aList, rList, fList
	 * @throws Exception
	 */
	public List<Board> searchAllList(Map<String, Object> map) throws Exception{
		Connection conn = getConnection();
		
		List<Board> bList = dao.searchBrdList(conn, map); //전체 검색, 

		close(conn);
		
		return bList;
	}
	
	public List<Board> searchInsideList(Map<String, Object> map) throws Exception{
		Connection conn = getConnection();
		
		List<Board> bList = null;
		
		String brdType = (String)map.get("brdType");
		if(brdType == null) brdType = "all";//전체 검색(헤더 검색창)
		
		switch(brdType) {
		case "b1" : bList = dao.searchNoticeList(conn, map); break; //공지사항
		case "b3" : bList = dao.searchReviewList(conn, map); break; //입양 후기
		case "b4" : bList = dao.searchFreeList(conn, map); break;//자유
		case "b5" : bList = dao.searchInfoList(conn, map); break; //고객센터
		}

		close(conn);
		
		return bList;
	}
	
	/**입양/분양 게시판 검색 목록 리스트 조회 service
	 * 입양/분양은 Adoption을 사용하기 때문에 반환 값이 달리 service를 다르게 해야함
	 * @param map
	 * @return aList
	 * @throws Exception
	 */
	public List<Adoption> searchAdoptList(Map<String, Object> map) throws Exception{
		Connection conn = getConnection();
		
		List<Adoption> aList = dao.searchAdoptList(conn, map);

		close(conn);
		
		return aList;
	}
	

	
	//--------------------썸네일, 목록 댓글 수 구하기----------------------------
	
	
	/** 검색이 적용된 썸네일 목록 조회 service
	 * @param map
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectSearchThumbs(Map<String, Object> map) throws Exception {
		Connection conn = getConnection();
		
		List<Image> iList = dao.selectSearchThumbs(conn, map);
		
		close(conn);
		
		return iList;
	}

	/**검색 게시글 목록 댓글 수 조회 service
	 * @param map
	 * @return replyMap
	 * @throws Exception
	 */
	public List<Map<String, String>> selectReplyCount(Map<String, Object> map) throws Exception{
		Connection conn = getConnection();
		
		List<Map<String, String>> commCounts = dao.selectReplyCount(conn, map);
		
		close(conn);
		
		return commCounts;
	}






}





















