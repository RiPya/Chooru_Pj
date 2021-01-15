package com.kh.semiProject.common.model.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class Board {
	
	//기본 필드
	private int brdNo;
	private String brdTitle;
	private String brdContent;
	private String nickName; //작성자(닉네임)
	private Timestamp brdCrtDt; //작성일
	private Timestamp brdModify; //수정일
	private char brdStatus; //게시글 상태 (y/n/b)
	private int readCount; //조회수
	private String brdType; //게시판 타입(b1, b2, b3, b4, b5)
	
	//자유, 고객센터용 카테고리(code, cd) 필드
	private String code; //자유(FR_DAY/FR_REVIEW/FR_INFO) 고객센터코드(QUEST/REPORT)
	
	//입양 후기 관련 필드
	private Date adtDate;//입양 날짜
	private String adtLink;//입양글 링크
	
	
	
	//기본 생성자
	public Board(){}
	
	//매개변수 생성자
	public Board(int brdNo, String brdTitle, String brdContent, String nickName, Timestamp brdCrtDt,
			Timestamp brdModify, char brdStatus, int readCount, String brdType, String code, Date adtDate,
			String adtLink) {
		super();
		this.brdNo = brdNo;
		this.brdTitle = brdTitle;
		this.brdContent = brdContent;
		this.nickName = nickName;
		this.brdCrtDt = brdCrtDt;
		this.brdModify = brdModify;
		this.brdStatus = brdStatus;
		this.readCount = readCount;
		this.brdType = brdType;
		this.code = code;
		this.adtDate = adtDate;
		this.adtLink = adtLink;
	}


	//게터/세터
	public int getBrdNo() {
		return brdNo;
	}

	public void setBrdNo(int brdNo) {
		this.brdNo = brdNo;
	}

	public String getBrdTitle() {
		return brdTitle;
	}

	public void setBrdTitle(String brdTitle) {
		this.brdTitle = brdTitle;
	}

	public String getBrdContent() {
		return brdContent;
	}

	public void setBrdContent(String brdContent) {
		this.brdContent = brdContent;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Timestamp getBrdCrtDt() {
		return brdCrtDt;
	}

	public void setBrdCrtDt(Timestamp brdCrtDt) {
		this.brdCrtDt = brdCrtDt;
	}

	public Timestamp getBrdModify() {
		return brdModify;
	}

	public void setBrdModify(Timestamp brdModify) {
		this.brdModify = brdModify;
	}

	public char getBrdStatus() {
		return brdStatus;
	}

	public void setBrdStatus(char brdStatus) {
		this.brdStatus = brdStatus;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getAdtDate() {
		return adtDate;
	}

	public void setAdtDate(Date adtDate) {
		this.adtDate = adtDate;
	}

	public String getAdtLink() {
		return adtLink;
	}

	public void setAdtLink(String adtLink) {
		this.adtLink = adtLink;
	}

	@Override
	public String toString() {
		return "Board [brdNo=" + brdNo + ", brdTitle=" + brdTitle + ", brdContent=" + brdContent + ", nickName="
				+ nickName + ", brdCrtDt=" + brdCrtDt + ", brdModify=" + brdModify + ", brdStatus=" + brdStatus
				+ ", readCount=" + readCount + ", brdType=" + brdType + ", code=" + code + ", adtDate=" + adtDate
				+ ", adtLink=" + adtLink + "]";
	};
	
	
}
