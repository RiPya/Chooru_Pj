package com.kh.semiProject.member.model.vo;

import java.sql.Date;

public class Member {
	private int memNo;
	private char grade;
	private String memId;
	private String memPw;
	private String memNm;
	private String phone;
	private String nickName;
	private String email;
	private Date enrollDate;
	private char petYn;

	
	// 기본 생성자
	public Member() {}

	// 매개변수 생성자
	public Member(int memNo, char grade, String memId, String memPw, String memNm, String phone, String nickName,
			String email, Date enrollDate, char petYn) {
		super();
		this.memNo = memNo;
		this.grade = grade;
		this.memId = memId;
		this.memPw = memPw;
		this.memNm = memNm;
		this.phone = phone;
		this.nickName = nickName;
		this.email = email;
		this.enrollDate = enrollDate;
		this.petYn = petYn;
	}


	// 게터, 세터
	public int getMemNo() {
		return memNo;
	}


	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}


	public char getGrade() {
		return grade;
	}


	public void setGrade(char grade) {
		this.grade = grade;
	}


	public String getMemId() {
		return memId;
	}


	public void setMemId(String memId) {
		this.memId = memId;
	}


	public String getMemPw() {
		return memPw;
	}


	public void setMemPw(String memPw) {
		this.memPw = memPw;
	}


	public String getMemNm() {
		return memNm;
	}


	public void setMemNm(String memNm) {
		this.memNm = memNm;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Date getEnrollDate() {
		return enrollDate;
	}


	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}


	public char getPetYn() {
		return petYn;
	}


	public void setPetYn(char petYn) {
		this.petYn = petYn;
	}


	@Override
	public String toString() {
		return "Member [memNo=" + memNo + ", grade=" + grade + ", memId=" + memId + ", memPw=" + memPw + ", memNm="
				+ memNm + ", phone=" + phone + ", nickName=" + nickName + ", email=" + email + ", enrollDate="
				+ enrollDate + ", petYn=" + petYn + "]";
	}
}