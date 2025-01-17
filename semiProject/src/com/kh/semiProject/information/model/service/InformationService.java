package com.kh.semiProject.information.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import static com.kh.semiProject.common.JDBCTemplate.close;
import static com.kh.semiProject.common.JDBCTemplate.commit;
import static com.kh.semiProject.common.JDBCTemplate.getConnection;
import static com.kh.semiProject.common.JDBCTemplate.rollback;

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
import com.kh.semiProject.information.model.dao.InformationDAO;

public class InformationService {
	
	private InformationDAO dao = new InformationDAO();
	
	/** 썸머노트 사용 시 이미지 url 추출 service(html코드에서 img src 추출)
	 * @param content
	 * @return imageUrl
	 * @throws Exception
	 */
	public List<String> getImageList(String content) throws Exception{
		//자바 정규표현식을 사용하여 <img>태그의 src 가져오기
		Pattern nonValidPattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
	
		List<String> imgUrl = new ArrayList<String>();
		Matcher matcher = nonValidPattern.matcher(content);
	
		while (matcher.find()) {
			imgUrl.add(matcher.group(1));
		}
		
		//System.out.println(imgUrl);//src 추출 확인
	
		return imgUrl;
	}

	/** 고객센터 데이터 DB 추가 service
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertInfo(Map<String, Object> map) throws Exception {
		
		Connection conn = getConnection();
		
		//1. 게시글 번호 얻어오기
		int brdNo = dao.selectNextNo(conn);
		
		if(brdNo > 0) {
			map.put("brdNo", brdNo);
			//스크립팅 방지, 개행문자 처리 안함!!!!!!(content가 html코드이기 때문)
		}
		
		int result = 0;
		
		try {
			//2. 공통 게시글 부분 삽입
			result = dao.insertBoard(conn, map);
			
			//공통 게시글 부분 삽입 실패 시 사진 삭제를 위해 throw?
			//if(result == 0) throw new FileInsertFailedException("게시글 삽입 실패");
			
			//3. 고객센터 부분(Info에 고객센터 코드 )삽입
			if(result > 0) {
				result = 0;//result 재활용
				
				result = dao.insertInfo(conn, map);
				
				//고객센터 부분 삽입 실패 시 사진 삭제를 위해 throw?
				//if(result == 0) throw new FileInsertFailedException("게시글 삽입 실패");

				//4. 이미지 파일 목록 iList 꺼내서 향상된 for문으로 하나씩 dao 호출
				List<Image> iList = (List<Image>)map.get("iList");
				
				if(result > 0 && !iList.isEmpty()) {
					result = 0;//result 재활용2
					
					for(Image img : iList) {
						//Image 객체에 글번호 추가
						img.setBrdNo(brdNo);
						
						result = dao.insertImage(conn, img);
						
						if(result == 0) {
							//삽입 실패 시 강제로 예외를 발생 시켜 
							//catch문에서 파일 삭제를 진행해야함
							throw new FileInsertFailedException("이미지 정보 삽입 실패");
							//밑에 있는 catch문에서 잡음
						}
					}//이미지 삽입 for문 끝
				}//이미지 삽입 if문 끝
			}//자유게시판 + 이미지 삽입 부분 끝
			
		} catch (Exception e) {
			//4 추가 : 게시글 또는 파일 정보 삽입 중 에러 발생 시
			//서버에 저장된 파일 삭제
			
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
		//5. 트랜잭션 처리
		if(result > 0) {
			commit(conn);
			result = brdNo; //result로 글번호 넘기기
		} 
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

	/**고객센터 상세 조회 service
	 * @param brdNo
	 * @return info
	 * @throws Exception
	 */
	public Board selectInfo(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		Board info = dao.selectInfo(conn, brdNo);
		
		if(info != null) {
			int result = dao.increaseReadCount(conn, brdNo);
			
			if(result > 0) {
				commit(conn);
				info.setReadCount(info.getReadCount() + 1);
			} else { rollback(conn); }
		} 
		
		close(conn);
		
		return info;
	}

	/**페이징 처리를 위한 계산 service
	 * @param cp
	 * @param infoCode 
	 * @return pInfo
	 * @throws Exception
	 */
	public PageInfo getPageInfo(String cp) throws Exception{
		Connection conn = getConnection();
		
		//cp가 null일 경우 참(1), 거짓(parseInt()를 사용해 cp 대입)
		int currentPage = 0;
		
		if(cp == null) currentPage = 1;
		else currentPage = Integer.parseInt(cp);
	
		
		//db에서 전체 게시글 수 조회하여 반환 받기(select count 사용)
		int listCount = dao.getListCount(conn);
		
		close(conn);
		
		//얻어온 현재 페이지와 DB에서 조회한 전체 게시글 수를 이용해 PageInfo 객체 생성해 반환
		return new PageInfo(currentPage, listCount);
	}
	
	
	/**게시글 목록 조회 service
	 * @param pInfo
	 * @param InfoCode 
	 * @return ifList
	 * @throws Exception
	 */
	public List<Board> selectInfoList(PageInfo pInfo) throws Exception{
		Connection conn = getConnection();
		
		List<Board> ifList = dao.selectInfoList(conn, pInfo);
		
		close(conn);
		
		return ifList;	
	}
	

	/** 고객센터 썸네일 목록 조회 service 
	 * @param pInfo
	 * @param freeCode 
	 * @return iList
	 * @throws Exception
	 */
	public List<Image> selectThumbnails(PageInfo pInfo) throws Exception{
		
		Connection conn = getConnection();
		
		List<Image> iList = dao.selectThumbnails(conn, pInfo);
		
		close(conn);
		
		return iList;
	}
	
	/**게시글 목록 조회 댓글 수 조회 service
	 * @param pInfo
	 * @param InfoCode
	 * @return replyMap
	 * @throws Exception
	 */
	public List<Map<String, String>> selectReplyCount(PageInfo pInfo) throws Exception{
		
		Connection conn = getConnection();
		
		List<Map<String, String>> commCounts = dao.selectReplyCount(conn, pInfo);
		
		close(conn);
		
		return commCounts;
	}

	/**고객센터 수정 폼 정보 출력용
	 * @param brdNo
	 * @return info
	 * @throws Exception
	 */
	public Board updateView(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		Board info = dao.selectInfo(conn, brdNo);
		
		close(conn);
		
		return info;
	}

	/**고객센터 수정(update) service
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateInfo(Map<String, Object> map) throws Exception{
		
		Connection conn = getConnection();
		
		int result = 0;
		
		List<Image> deleteImages = new ArrayList<Image>();
		//서버에서 삭제될 파일 저장 List
		
		try {
			//1. 공통 게시글 부분 수정(BOARD 제목, 내용)
			result = dao.updateBoard(conn, map);
			
			if(result > 0) {
				//공통 게시글 부분 수정 성공 시
				//2. 고객센터 부분(Information 고객센터)
				result = 0;//초기화
				result = dao.updateInfo(conn, map);
				
		//-----------------게시글 내용 수정 후 이미지 목록 수정 처리---------------------------------------------------------		
				
				if(result > 0) {
					//고객센터 부분 수정 성공 시
					
					//map에서 iList를 가져와 newImages에 저장
					List<Image> newImages = (List<Image>)map.get("iList");
					
					//기존의 IMAGE 테이블에서 해당 brdNo의 이미지 목록(oldImages) 가져오기
					int brdNo = (int)map.get("brdNo");
					List<Image> oldImages = dao.selectOldImages(conn, brdNo);
					
					boolean newFlag = false;//newImages가 있을 때 newImages를 삽입하기 위한 flag
					
					//3. oldImages(수정 전 이미지 목록)이 있으면
					if(!oldImages.isEmpty()) {
						
						result = 0;//초기화 재활용2
						
						//3-1. oldImages 목록 DB에서 지우기
						//→ newImages가 있으나 없으나 지움
						result = dao.deleteOldImages(conn, brdNo);
						
						//확인용
						//System.out.println("이전 이미지 수: " + oldImages.size());
						//System.out.println("삭제 데이터 개수 : " + result);//삭제 데이터 개수 확인
					
						//3-2. oldImages가 삭제되었을 때 수정 후 이미지(newImages)가 있으면 
						if(result > 0 && !newImages.isEmpty()) {
							
							newFlag = true;//newImages가 있다는 flag를 true로 (후에 진행)
							
							//3-3. newImages의 파일명 vs oldImages의 파일명 비교
							//같은 이름이 없으면 deleteImages에 oldImg 추가(삭제 파일)
							for(Image oldImg : oldImages) {
								boolean flag = true;
								//같은 이름이 있을 때 false → deleteImages에 추가X								
								
								for(Image newImg : newImages) {
									if(oldImg.getFileName().equals(newImg.getFileName())) {
										flag = false;
										break; //같은 이름 있으면 for문 중단
									}
								}
								System.out.println(flag);//확인용
								
								//같은 이름이 없을 때 oldImg 추가
								if(flag) deleteImages.add(oldImg);
								
								System.out.println(deleteImages);//확인용
							}
						}//newImages, oldImages 둘 다 있는 경우 if문 끝
						
						//3-4. oldImages가 삭제되었고, newImages는 없을 때
						// → deleteImages에 oldImages 넣기
						else if(result > 0 && newImages.isEmpty()) {
							deleteImages = oldImages;
						}
						//System.out.println(deleteImages);//확인용
						//oldImages 삭제에 실패했을 때 result == 0
						
						//----oldImages가 있을 때 코드 종료	
					} else { 
						
					//4. 수정 전 이미지(oldImages)가 없고 수정 후 이미지(newImages)가 있으면
						if(!newImages.isEmpty()) {
							newFlag = true;//newImages가 있다는 flag를 true로 (후에 진행)
						} 
						//newImages가 없을 때 따로 수행할 코드X
						
					}//oldImages가 있을 때, 없을 때 코드 끝
				
				
					//5. newImages가 있고 oldImages를 DB에서 삭제 성공했을 때
					//IMAGE 테이블에 삽입(등록 시 작성한 insertImage() 재사용)
					if(newFlag && result > 0) {
						result = 0;//초기화 재활용3
						
						for(Image img : newImages) {
							//Image 객체에 글번호 추가
							img.setBrdNo(brdNo);
							
							result = dao.insertImage(conn, img);
							
							if(result == 0) {
								//삽입 실패 시 강제로 예외를 발생 시켜 
								//catch문에서 파일 삭제를 진행해야함
								throw new FileInsertFailedException("이미지 정보 삽입 실패");
								//밑에 있는 catch문에서 잡음
							}
						}
					}//newImages IMAGE에 삽입 끝
					
				}//입양 후기 게시글 수정 성공했을 때 코드 끝
			}
			
		} catch (Exception e) {
			
			//글 정보(BOARD/REVIEW) 오류 발생 시 or 파일 정보(IMAGE) 업로드 실패 시
			//→ 서버에 저장된 파일을 삭제
			List<Image> iList = (List<Image>)map.get("iList");
			
			if(!iList.isEmpty()) {
				for(Image img : iList) {
					if(!img.getFilePath().contains("http")) {//인터넷 주소로 연결한 img의 경우 삭제 진행X
						//현재 썸머 노트를 통해 저장된 이미지의 주소는 ../resource/uploadImages 
						//(썸머노트에서 src에 하려면 이 주소만 되어서 상대주소로 작성..)
						//→ 이미지 삭제를 위해 필요한 주소는 C:\workspace\semi\Chooru_Pj\semiProject\WebContent\resources/uploadImages
						//→ 바꿔줘야 함
						String filePath = (String)map.get("root");
						filePath += img.getFilePath().substring(3);
						
						String fileName = img.getFileName();
						
						File deleteFile = new File(filePath + fileName);
						//해당 파일의 전체 주소 : filePath + fileName
						//File 객체는 전체 주소에 있는 파일 객체를 선택할 때 사용함
						// → filePath+fileName인 파일이 없다면 deleteFile은 null 값.
						
						if(deleteFile.exists()) {
							deleteFile.delete();
						}
					}
				}
			}
			
			throw e; //현재 발생해서 처리한 에러도 다시 controller로 전달
		}
		//-------try-catch문 끝---------------------------------
		
		//모든 과정이 성공했을 때 commit + deleteImages 삭제
		if(result > 0) {
			commit(conn);

			//deleteImages가 있을 때 삭제 진행
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
						
						System.out.println(deleteFile);
						
						if(deleteFile.exists()) {
							deleteFile.delete();
						}
					}
				}
			}
			
		} else { //과정 중 실패가 있었을 때 rollback
			rollback(conn);
		}
		
		close(conn);
		
		return result;
	}

	/** 고객센터 삭제(상태 변경) service
	 * @param brdNo
	 * @return result
	 * @throws Exception
	 */
	public int updateBrdStatus(int brdNo) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.updateBrdStatus(conn, brdNo);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}
}
