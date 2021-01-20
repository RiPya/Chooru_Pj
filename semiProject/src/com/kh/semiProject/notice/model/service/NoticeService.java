package com.kh.semiProject.notice.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kh.semiProject.common.model.exception.FileInsertFailedException;
import com.kh.semiProject.common.model.vo.Board;
import com.kh.semiProject.common.model.vo.PageInfo;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.notice.model.dao.NoticeDAO;

public class NoticeService {
	
	private NoticeDAO dao = new NoticeDAO();

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

	
	/** 게시글 목록 조회 Service
	 * @param pInfo
	 * @return nList
	 * @throws Exception
	 */
	public List<Board> selectNoticeList(PageInfo pInfo) throws Exception{
		Connection conn = getConnection();
		
		List<Board> nList = dao.selectNoticeList(conn, pInfo);
		
		close(conn);
		
		return nList;
	}


	/** 썸네일 목록조회 Service
	 * @param pInfo
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectThumbnails(PageInfo pInfo) throws Exception{
		Connection conn = getConnection();
		
		List<Image> iList = dao.selectThumbnails(conn, pInfo);
		
		close(conn);
		
		return iList;
	}


	/** 썸머노트 사용시 이미지 url 추출용 Service(html코드에서 img src추출)
	 * @param content
	 * @return imageUl
	 * @throws Exception
	 */
	public List<String> getImageList(String content) throws Exception{
		// 자바 정규 표현식을 사용해 <img>태그의 src 가져오기
		Pattern nonValidPattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
		
		List<String> imgUl = new ArrayList<String>();
		Matcher matcher = nonValidPattern.matcher(content);
		
		while(matcher.find()) {
			imgUl.add(matcher.group(1));
		}
		return imgUl;
	}


	/** 공지사항 등록 DAO
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertNotice(Map<String, Object> map) throws Exception{
		Connection conn = getConnection();
		
		// 1. 게시글 번호 얻어오기
		int brdNo = dao.selectNextNo(conn);
		
		if(brdNo > 0) {
			// 스크립팅 방지, 개행문자 처리 안함(content가 html코드라서)
			map.put("brdNo", brdNo);
		}
		
		int result = 0;
		
		try {
			// 2. 게시글 부분(글번호, 제목, 내용, 작성자, 게시판 타입)
			result = dao.insertBoard(conn, map);
			// System.out.println("서비스 result"+result);
			
			if(result > 0) {
				// 3. 이미지 파일 목록 iList를 꺼내어 향상 for문으로 하나씩 DAO 호출
				List<Image> iList = (List<Image>)map.get("iList");
				
				if(result > 0 && !iList.isEmpty()) {
					result = 0; // result 재활용2
					
					for(Image img : iList) {
						// Image 객체에 글 번호 추가
						img.setBrdNo(brdNo);
						
						result = dao.insertImage(conn, img);
						// System.out.println("이미지service "+result);
						
						if(result == 0) {
							// 삽입 실패시, 강제로 예외를 발생시켜 catch문에서 파일 삭제를 진행
							throw new FileInsertFailedException("이미지 정보 삽입 실패");
						}
					}// 이미지 삽입 for문 종료
				}// 이미지 삽입 if문 종료
			}// 게시판 + 이미지 삽입 종료
			
		} catch (Exception e) {
			// 4 추가: 게시글 또는 파일정보 삽입 중 에러 발생 시 서버에 저장된 파일 삭제
			List<Image> iList = (List<Image>)map.get("iList");
			
			if(!iList.isEmpty()) {
				for(Image img : iList) {
					if(!img.getFilePath().contains("http")) {//인터넷 주소로 연결한 img의 경우 삭제 진행X
						//현재 썸머 노트를 통해 저장된 이미지의 주소는 ../resource/uploadImages 
						//(썸머노트에서 src에 하려면 이 주소만 되어서 상대주소로 작성..)
						//→ 이미지 삭제를 위해 필요한 주소는 C:/workspace/semi/Chooru_Pj\semiProject\WebContent\resources/uploadImages
						//→ 바꿔줘야 함
						String filePath = (String)map.get("root");
						filePath += img.getFilePath().substring(3);
						
						String fileName = img.getFileName();
						
						File deleteFile = new File(filePath + fileName);
						//해당 파일의 전체 주소 : filePath + fileName
						//File 객체는 전체 주소에 있는 파일 객체를 선택할 때 사용함
						// → filePath+fileName인 파일이 없다면 deleteFile은 null 값.
						
						if(deleteFile.exists()) deleteFile.delete();
					}
				}
			}
			throw e; //현재 발생해서 처리한 에러도 다시 controller로 전달
		}
		if(result > 0) {
			commit(conn);
			result = brdNo; //result로 글번호 넘기기
		}else {
			rollback(conn);
		}
		close(conn);
		return result;
	}


	/** 공지글 상세조회 Service
	 * @param brdNo
	 * @return notice
	 * @throws Exception
	 */
	public Board selectNotice(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		Board notice = dao.selectNotice(conn, brdNo);
		
		if(notice != null) {
			int result = dao.increaseReadCount(conn, brdNo);
			
			if(result > 0) {
				commit(conn);
				notice.setReadCount(notice.getReadCount() + 1);
			}else {
				rollback(conn);
			}
		}
		close(conn);
		return notice;
	}


	/** 공지글 수정 폼 정보 출력용 Service
	 * @param brdNo
	 * @return notice
	 * @throws Exception
	 */
	public Board updateView(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		Board notice = dao.selectNotice(conn, brdNo);
		
		close(conn);
		return notice;
	}


	/** 공지사항 수정 Service
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateNotice(Map<String, Object> map) throws Exception{
		Connection conn = getConnection();
		
		int result = 0;
		
		// 서버에서 삭제될 파일 저장 List
		List<Image> deleteImages = new ArrayList<Image>();
		
		try {
			// 1. 공통 게시글 부분 수정(BOARD 제목, 내용)
			result = dao.updateBoard(conn, map);
			
			// 공통 게시글 부분 수정 성공 시
			if(result > 0) {
				// map에서 iList를 가져와 newImages에 저장
				List<Image> newImages = (List<Image>)map.get("iList");
				
				// 기존의 IMAGE 테이블에서 해당 bordNo의 이미지 목록(oldImages) 가져오기
				int brdNo = (int)map.get("brdNo");
				List<Image> oldImages = dao.selectOldImages(conn, brdNo);
				
				boolean newFlag = false; //newImages가 있을 때 newImages를 삽입하기 위한 flag
				
				// 2. oldImages(수정전 이미지 목록)이 있다면
				if(!oldImages.isEmpty()) {
					result = 0; // 초기화 재활용
					
					// 2-1. oldImages 목록 DB에서 지우기
					// → newImages가 있으나 없으나 지운다.
					result = dao.deleteOldImages(conn, brdNo);
					
					// 2-2. oldImages가 삭제 된 후, 수정 후 이미지(newImages)가 있다면
					if(result > 0 && !newImages.isEmpty()) {
						newFlag = true; // newImages가 있다는 flag를 true로
						
						// 2-3. newImages의 파일명과 oldImages의 파일명 비교
						// 같은 이름이 없다면 deleteImages에 oldImg를 추가한다(삭제파일)
						for(Image oldImg : oldImages) {
							boolean flag = true;
							//같은 이름이 있을 때 false → deleteImages에 추가X									
							
							for(Image newImg : newImages) {
								if(oldImg.getFileName().equals(newImg.getFileName())) {
									flag = false;
									break; //같은 이름 있으면 for문 중단
								}
							}
							System.out.println(flag); // 확인용
							
							// 같은 이름이 없을 때 oldImg 추가
							if(flag)	deleteImages.add(oldImg);
							
							System.out.println(deleteImages); // 확인용
						}
					}//newImages, oldImages 둘 다 있는 경우 if문 끝
					
					// 2-4. oldImages가 삭제되었고, newImages는 없을 때
					//		→ deleteImages에 oldImages 넣기
					else if(result > 0 && newImages.isEmpty()) {
						deleteImages = oldImages;
					}
					//----oldImages가 있을 때 코드 종료	
				}else {
					// 3. 수정전 이미지(oldImages)가 없고 수정 후 이미지(newImages)가 있으면
					if(!newImages.isEmpty()) {
						newFlag = true; //newImages가 있다는 flag를 true로 (후에 진행)
					}
					// newImages가 없을 때는 따로 수행할 코드 없음
				}// oldImages가 있을때, 없을때의 코드 종료
				
				// 4. newImages가 있고 oldImages를 DB에서 삭제 성공했을 때
				// IMAGE 테이블에 삽입(등록 시 작성한 insertImage() 재 사용)
				if(newFlag && result > 0) {
					result = 0; // 초기화 재활용2
					
					for(Image img : newImages) {
						// Image 객체에 글 번호 추가
						img.setBrdNo(brdNo);
						
						result = dao.insertImage(conn, img);
						
						if(result == 0) {
							// 삽입 실패 시 강제로 예외를 발생 시켜
							// catch문에서 파일 삭제를 진행시킴
							throw new FileInsertFailedException("이미지 정보 삽입 실패");
						}
					}
				} // newImages IMAGE에 삽입 종료
			} // 입양 후기 게시글 수정 성공했을 시의 코드 종료
		} catch (Exception e) {
			//글 정보(BOARD/REVIEW) 오류 발생 시 or 파일 정보(IMAGE) 업로드 실패 시
			//→ 서버에 저장된 파일을 삭제
			List<Image> iList = (List<Image>)map.get("iList");
			
			if(!iList.isEmpty()) {
				for(Image img : iList) {
					if(!img.getFilePath().contains("http")) {
						String filePath = (String)map.get("root");
						filePath += img.getFilePath().substring(3);
						
						String fileName = img.getFileName();
						
						File deleteFile = new File(filePath + fileName);
						
						if(deleteFile.exists()) {
							deleteFile.delete();
						}
					}
				}
			}
			throw e;
		}
		//-------try-catch문 끝---------------------------------
		
		//모든 과정이 성공했을 때 commit + deleteImages 삭제
		if(result > 0) {
			commit(conn);
			
			// deleteImages가 있을 때 삭제 진행
			if(!deleteImages.isEmpty()) {
				for(Image img : deleteImages) {
					if(!img.getFilePath().contains("http")) {//인터넷 주소로 연결한 img의 경우 삭제 진행X
						//현재 썸머 노트를 통해 저장된 이미지의 주소는 ../resource/uploadImages 
						//(썸머노트에서 src에 하려면 이 주소만 되어서 상대주소로 작성..)
						//→ 이미지 삭제를 위해 필요한 주소는 C:\workspace\semi\Chooru_Pj\semiProject\WebContent\resources/uploadImages
						//→ 바꿔줘야 함
						String filePath = (String)map.get("root");
						filePath += img.getFilePath().substring(3);
						
						String fileName = img.getFileName();
						
						File deleteFile = new File(filePath + fileName);
						
						// System.out.println(deleteFile);
						
						if(deleteFile.exists()) {
							deleteFile.delete();
						}
					}
				}
			}
		}else {
			rollback(conn);
		}
		close(conn);
		return result;
	}


	/** 공지글 삭제 Service
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int updateBrdStatus(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.updateBrdStatus(conn, brdNo);
		
		if(result > 0)	commit(conn);
		else			rollback(conn);
		
		close(conn);
		return result;
	}

}
