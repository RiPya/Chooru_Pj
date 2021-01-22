package com.kh.semiProject.common;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator{
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication("Chooruproject@gmail.com", "^semi_porject_cR#1");
												// 메일을 발송할 관리자의 아이디, 비밀번호 
	}

}
