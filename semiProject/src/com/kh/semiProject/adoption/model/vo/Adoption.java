package com.kh.semiProject.adoption.model.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class Adoption { 

	//기본 필드
	private int adtBrdNo;
	private String adtBrdTitle;
	private String adtBrdContent;
	private String nickName; //작성자(닉네임)
	private Timestamp adtBrdCrtDt; //작성일
	private Timestamp adtBrdModify; //수정일
	private char adtBrdStatus; //게시글 상태 (y/n/b)
	private int readCount; //조회수
	private String brdType; //게시판 타입(b1, b2, b3, b4, b5)
	
	//입양/분양 전용 필드
	private String adtCode; // 입양카테고리(AD_D / AD_C / AD_E / AD_T)
	private String adtBreed;// 품종
	private String adtGender;
	private String adtAge;
	private String address; //주소 ex) 서울특별시 종로구
	private char adtVaccination;//예방접종 여부 y/n
	private String adtNote; //특이사항
	private Date adtTime;//공고기간
	private char adtYn;//입양 여부 (y 입양완료, n 진행중)
	
	//기본 생성자
	public Adoption() {
	}

	//매개변수 생성자
	public Adoption(int adtBrdNo, String adtBrdTitle, String adtBrdContent, String nickName, Timestamp adtBrdCrtDt,
			Timestamp adtBrdModify, char adtBrdStatus, int readCount, String brdType, String adtCode, String adtBreed,
			String adtGender, String adtAge, String address, char adtVaccination, String adtNote, Date adtTime,
			char adtYn) {
		super();
		this.adtBrdNo = adtBrdNo;
		this.adtBrdTitle = adtBrdTitle;
		this.adtBrdContent = adtBrdContent;
		this.nickName = nickName;
		this.adtBrdCrtDt = adtBrdCrtDt;
		this.adtBrdModify = adtBrdModify;
		this.adtBrdStatus = adtBrdStatus;
		this.readCount = readCount;
		this.brdType = brdType;
		this.adtCode = adtCode;
		this.adtBreed = adtBreed;
		this.adtGender = adtGender;
		this.adtAge = adtAge;
		this.address = address;
		this.adtVaccination = adtVaccination;
		this.adtNote = adtNote;
		this.adtTime = adtTime;
		this.adtYn = adtYn;
	}

	
	//게터/세터
	public int getAdtBrdNo() {
		return adtBrdNo;
	}

	public void setAdtBrdNo(int adtBrdNo) {
		this.adtBrdNo = adtBrdNo;
	}

	public String getAdtBrdTitle() {
		return adtBrdTitle;
	}

	public void setAdtBrdTitle(String adtBrdTitle) {
		this.adtBrdTitle = adtBrdTitle;
	}

	public String getAdtBrdContent() {
		return adtBrdContent;
	}

	public void setAdtBrdContent(String adtBrdContent) {
		this.adtBrdContent = adtBrdContent;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Timestamp getAdtBrdCrtDt() {
		return adtBrdCrtDt;
	}

	public void setAdtBrdCrtDt(Timestamp adtBrdCrtDt) {
		this.adtBrdCrtDt = adtBrdCrtDt;
	}

	public Timestamp getAdtBrdModify() {
		return adtBrdModify;
	}

	public void setAdtBrdModify(Timestamp adtBrdModify) {
		this.adtBrdModify = adtBrdModify;
	}

	public char getAdtBrdStatus() {
		return adtBrdStatus;
	}

	public void setAdtBrdStatus(char adtBrdStatus) {
		this.adtBrdStatus = adtBrdStatus;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public String getBrdType() {
		return brdType;
	}

	public void setBrdType(String brdType) {
		this.brdType = brdType;
	}

	public String getAdtCode() {
		return adtCode;
	}

	public void setAdtCode(String adtCode) {
		this.adtCode = adtCode;
	}

	public String getAdtBreed() {
		return adtBreed;
	}

	public void setAdtBreed(String adtBreed) {
		this.adtBreed = adtBreed;
	}

	public String getAdtGender() {
		return adtGender;
	}

	public void setAdtGender(String adtGender) {
		this.adtGender = adtGender;
	}

	public String getAdtAge() {
		return adtAge;
	}

	public void setAdtAge(String adtAge) {
		this.adtAge = adtAge;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public char getAdtVaccination() {
		return adtVaccination;
	}

	public void setAdtVaccination(char adtVaccination) {
		this.adtVaccination = adtVaccination;
	}

	public String getAdtNote() {
		return adtNote;
	}

	public void setAdtNote(String adtNote) {
		this.adtNote = adtNote;
	}

	public Date getAdtTime() {
		return adtTime;
	}

	public void setAdtTime(Date adtTime) {
		this.adtTime = adtTime;
	}

	public char getAdtYn() {
		return adtYn;
	}

	public void setAdtYn(char adtYn) {
		this.adtYn = adtYn;
	}

	@Override
	public String toString() {
		return "Adoption [adtBrdNo=" + adtBrdNo + ", adtBrdTitle=" + adtBrdTitle + ", adtBrdContent=" + adtBrdContent
				+ ", nickName=" + nickName + ", adtBrdCrtDt=" + adtBrdCrtDt + ", adtBrdModify=" + adtBrdModify
				+ ", adtBrdStatus=" + adtBrdStatus + ", readCount=" + readCount + ", brdType=" + brdType + ", adtCode="
				+ adtCode + ", adtBreed=" + adtBreed + ", adtGender=" + adtGender + ", adtAge=" + adtAge + ", address="
				+ address + ", adtVaccination=" + adtVaccination + ", adtNote=" + adtNote + ", adtTime=" + adtTime
				+ ", adtYn=" + adtYn + "]";
	}
	
	
	
	
}
