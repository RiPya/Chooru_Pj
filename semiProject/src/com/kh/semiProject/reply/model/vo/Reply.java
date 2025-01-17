package com.kh.semiProject.reply.model.vo;

import java.sql.Timestamp;

public class Reply {
	
	private int replyNo; // 댓글번호
	private String replyContent; // 댓글 내용
	private Timestamp replyDate; // 댓글 작성일
	private char replyStatus; // 댓글 상태
	private String nickName; // 작성자(닉네임)
	private int brdNo; // 게시글 번호
	private String brdTitle; // 게시글 제목
	private String brdType; // 게시글 타입
	
	// 기본 생성자
	public Reply() {}

	// 매개변수 있는 생성자
	public Reply(int replyNo, String replyContent, Timestamp replyDate, char replyStatus, String nickName, int brdNo,
			String brdTitle, String brdType) {
		super();
		this.replyNo = replyNo;
		this.replyContent = replyContent;
		this.replyDate = replyDate;
		this.replyStatus = replyStatus;
		this.nickName = nickName;
		this.brdNo = brdNo;
		this.brdTitle = brdTitle;
		this.brdType = brdType;
	}

	public int getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(int replyNo) {
		this.replyNo = replyNo;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Timestamp getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Timestamp replyDate) {
		this.replyDate = replyDate;
	}

	public char getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(char replyStatus) {
		this.replyStatus = replyStatus;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

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

	public String getBrdType() {
		return brdType;
	}

	public void setBrdType(String brdType) {
		this.brdType = brdType;
	}

	@Override
	public String toString() {
		return "Reply [replyNo=" + replyNo + ", replyContent=" + replyContent + ", replyDate=" + replyDate
				+ ", replyStatus=" + replyStatus + ", nickName=" + nickName + ", brdNo=" + brdNo + ", brdTitle="
				+ brdTitle + ", brdType=" + brdType + "]";
	}
}
