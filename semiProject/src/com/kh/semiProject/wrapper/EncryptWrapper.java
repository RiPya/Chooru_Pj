package com.kh.semiProject.wrapper;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncryptWrapper extends HttpServletRequestWrapper {
	/* HttpServletReqeustWrapper
	 * - HttpServletRequest 객체를 가공할 수 있는 객체
	 * - 기본 생성자가 존재하지 않아서 상속된 매개변수 1개짜리 생성자를 오버라이딩 해야한다.
	 * */

	public EncryptWrapper(HttpServletRequest request) {
		super(request);
	}

	
	@Override
	public String getParameter(String name) {
		String encPwd = null; //암호화가 적용된 비밀번호를 저장할 변수 선언
		
		switch(name) {
		case "memberPwd": //로그인 시 비밀번호 name 속성 값
		case "pwd1": // 회원가입 시 비밀번호 name 속성 값
		case "currentPwd": // 비밀번호 변경 시 현재 비밀번호의 name 속성 값
		case "newPwd1": // 비밀번호 변경 시 새 비밀번호 name 속성 값

			
		
		encPwd = getSha512(super.getParameter(name)); // 암호화
		break;
		
		default: encPwd = super.getParameter(name); 
		// 이전 getParameter 유지
		}
		
		return encPwd;
	}
	
	
	/** SHA512 해시함수를 사용한 암호화 메소드
	 * @param pwd
	 * @return encPwd
	 */
	public static String getSha512(String pwd) {
		String encPwd = null;
		
		// SHA-512 방식의 암호화 객체 저장 변수 선언
		MessageDigest md = null;
		// MessageDigest: 지정된 알고리즘에 따라 해시함수를 진행하는 객체
		// 해시함수: 입력 값을 여러 단계의 연산을 거쳐 일정한 길이의 무작위 값을 만들어내는 함수(단방향)
		
		try {
			md = MessageDigest.getInstance("SHA-512");
			// 지정된 알고리즘(SHA-512)를 이용할 수 있는 MessageDigest 객체를 생성 후 얻어옴.
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		// 암호화 전 전달받은 문자열(입력받은 비밀번호)를 
		// 바이트 배열 형식으로 변환
		byte[] bytes = pwd.getBytes(Charset.forName("UTF-8"));
		
		// md 객체에 비밀번호 바이트 배열을 전달하여 갱신
		md.update(bytes); // 실제 암호화 진행
		
		// java.util.Base64 인코더를 이용해서 
		// 암호화된 바이트 배열을 인코딩 후, 문자열로 반환
		encPwd = Base64.getEncoder().encodeToString(md.digest());
		// md.digest(): md.update()에서 암호화된 것을 꺼내옴
		
//		System.out.println("암호화 전: " + pwd);
//		System.out.println("암호화 후: " + encPwd);
		
		
		return encPwd;
	}

}
