package com.kh.semiProject.image;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;


public class MyFileRenamePolicy implements FileRenamePolicy {
	

	@Override
	public File rename(File originalFile) {
		//업로드된 시간을 파일명에 작성 + 5자리 랜덤 숫자 추가
		// ex) 20210107101311_12345.jpg
		
		long currentTime = System.currentTimeMillis();
		
		//날짜 출력 포맷 객체 : 년월일시분초로 출력
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmss");
		
		int random = (int)(Math.random() * 100000);//5자리 난수
					// 0 <= Math.random() < 1
		String str = "_" + String.format("%05d", random);
			// %5d 다섯칸 오른쪽 정렬된 정수 / %05d 앞 빈칸에 0 추가
			// %0d5 예시 : 00012
		
		//파일명을 변경해도 확장자는 유지 되어야 하므로
		//파일 업로드된 원본 파일의 확장자 부분만 얻어오기
		int dot = originalFile.getName().lastIndexOf(".");
			//원본 파일의 원래 이름에서 "."이 있는 제일 마지막 인덱스 가져오기
			//"."이 없으면 -1이 저장됨
		
		String ext =  ""; //확장자를 저장할 변수
		
		if(dot != -1) { //확장자가 있다면
			ext = originalFile.getName().substring(dot);
			//원본 파일의 원래 이름에서 dot(.의 마지막 인덱스)까지 자르기
			//→ 확장자만 남고 앞의 원래 이름은 제거됨
		}
		
		String fileName = ft.format(new Date(currentTime)) + str + ext;
		
		return new File(originalFile.getParent(), fileName);
	}

}
