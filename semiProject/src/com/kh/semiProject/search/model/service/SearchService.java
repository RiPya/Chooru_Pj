package com.kh.semiProject.search.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.search.model.dao.SearchDAO;

public class SearchService {
	private SearchDAO dao = new SearchDAO();

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

	/** key, value에 해당하는 조건문 service
	 * @param map
	 * @return keyValue
	 * @throws Exception
	 */
	public String getSkSvCondition(String searchKey, String searchValue) throws Exception {
		
		String keyValue = null;
		
		if(searchKey == null) {//전체검색 + 고객센터 검색
			searchKey = "all";
		}
		
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
		case "all" : //전체 (전체 검색 + 고객센터는 무조건 이쪽으로)
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
		if(brdType == null) brdType = "all";
		
		switch(brdType) {
		case "b1" : //공지사항
			tpCondition = " AND BRD_TYPE = 'b1' "; break;
		case "b2" : //입양/분양
			tpCondition = " AND BRD_TYPE = 'b2' "; break;
		case "b3" : //입양 후기
			tpCondition = " AND BRD_TYPE = 'b3' "; break;
		case "b4" : //자유 게시판
			tpCondition = " AND BRD_TYPE = 'b4' "; break;
		case "b5" : //고객센터
			tpCondition = " AND BRD_TYPE = 'b5' "; break;
		default : //전체
			tpCondition = " "; break;
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
		
		if(code == null) code = "all";
		
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
		//고객센터 카테고리
		//추가
		default : //adtAll, frAll, null값
			cdCondition = " ";
		}
		
		return cdCondition;
	}

	

	/**전체 검색 게시글 목록 리스트 조회 service
	 * @param map
	 * @return sList
	 * @throws Exception
	 */
	public List<Board> searchBrdList(Map<String, Object> map) throws Exception{
		Connection conn = getConnection();
		
		List<Board> sList = dao.searchBrdList(conn, map);
		
		close(conn);
		
		return sList;
	}

	/**검색이 적용된 썸네일 목록 조회 service
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


}





















