package com.kh.semiProject.image.model.vo;

import java.sql.Date;

public class Image {
	
	private int fileNo;//파일 넘버
	private String fileName; //파일명(시스템명)
	private String filePath; // 
	private int fileLevel;//파일 레벨
	private Date fileCrtDt;//등록 날짜
	private int brdNo;//게시글 번호
	
	//기본 생성자
	public Image() {
	}

	//매개변수 생성자
	public Image(int fileNo, String fileName, String filePath, int fileLevel, Date fileCrtDt, int brdNo) {
		super();
		this.fileNo = fileNo;
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileLevel = fileLevel;
		this.fileCrtDt = fileCrtDt;
		this.brdNo = brdNo;
	}

	public int getFileNo() {
		return fileNo;
	}

	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFileLevel() {
		return fileLevel;
	}

	public void setFileLevel(int fileLevel) {
		this.fileLevel = fileLevel;
	}

	public Date getFileCrtDt() {
		return fileCrtDt;
	}

	public void setFileCrtDt(Date fileCrtDt) {
		this.fileCrtDt = fileCrtDt;
	}

	public int getBrdNo() {
		return brdNo;
	}

	public void setBrdNo(int brdNo) {
		this.brdNo = brdNo;
	}

	@Override
	public String toString() {
		return "Image [fileNo=" + fileNo + ", fileName=" + fileName + ", filePath=" + filePath + ", fileLevel="
				+ fileLevel + ", fileCrtDt=" + fileCrtDt + ", brdNo=" + brdNo + "]";
	}
	
	

}
