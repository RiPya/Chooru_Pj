package com.kh.semiProject.review.model.service;

import static com.kh.semiProject.common.JDBCTemplate.*;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kh.semiProject.common.model.exception.FileInsertFailedException;
import com.kh.semiProject.image.model.vo.Image;
import com.kh.semiProject.review.model.dao.ReviewDAO;

public class ReviewService {
	
	private ReviewDAO dao = new ReviewDAO();
	
	/** 썸머노트 사용 시 이미지 url 추출 service(html코드에서 img src 추출)
	 * @param content
	 * @return imageUrl
	 * @throws Exception
	 */
	public List<String> getImageList(String content) throws Exception {
		
		//자바 정규표현식을 사용하여 <img>태그의 src 가져오기
		Pattern nonValidPattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");

		List<String> imgUrl = new ArrayList<String>();
		Matcher matcher = nonValidPattern.matcher(content);

		while (matcher.find()) {
			imgUrl.add(matcher.group(1));
		}
		
		System.out.println(imgUrl);//src 추출 확인

		return imgUrl;
	}

	
	
	/** 입양 후기 데이터 DB 추가 service
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertReview(Map<String, Object> map) throws Exception{
		
		Connection conn = getConnection();
		
		//1. 게시글 번호 얻어오기
		int brdNo = dao.selectNextNo(conn);
		
		if(brdNo > 0) {
			map.put("brdNo", brdNo);
			//스크립팅 방지, 개행문자 처리 안함!!!!!!(content가 html코드이기 때문)
		}
		
		int result = 0;
		
		try {
			//2. 공통 게시글 부분(Board에 글번호, 제목, 내용, 작성자, 게시판타입) 삽입
			result = dao.insertBoard(conn, map);
			
			//게시판 타입도 추가를 해야되는 건지 물어보자
			
			//3. 입양 후기 부분(ADT_REVIEW에 입양날짜, 입양url) 삽입
			if(result > 0) {
				result = 0;//result 재활용
				
				result = dao.insertReview(conn, map);

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
							throw new FileInsertFailedException("파일 정보 삽입 실패");
							//밑에 있는 catch문에서 잡음
						}
					}//이미지 삽입 for문 끝
				}//이미지 삽입 if문 끝
			}//입양 후기 + 이미지 삽입 부분 끝
			
		} catch (Exception e) {
			//4 추가 : 게시글 또는 파일 정보 삽입 중 에러 발생 시
			//서버에 저장된 파일 삭제
			
			List<Image> iList = (List<Image>)map.get("iList");
			
			if(!iList.isEmpty()) {
				for(Image img : iList) {
					String filePath = img.getFilePath();
					String fileName = img.getFileName();
					
					File deleteFile = new File(filePath + fileName);
					//해당 파일의 전체 주소 : filePath + fileName
					//File 객체는 전체 주소에 있는 파일 객체를 선택할 때 사용함
					// → filePath+fileName인 파일이 없다면 deleteFile은 null 값.
					
					if(deleteFile.exists()) deleteFile.delete();
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
		
		return result;
	}
	
	

}






















