package com.kh.semiProject.free.model.vo;

public class PageInfo {
	//공지사항, 자유게시판, 고객센터, 내글조회 등 페이징 처리를 위한 값을 저장할 객체
	
	//얻어올 값
	private int currentPage; // 현재 페이지 번호를 저장할 변수
	private int listCount; //전체 게시글 수를 저장할 변수
	
	//설정할 값
	private int limit = 10; //한 페이지에 보여질 게시글 목록 수
	private int pageSize = 10; //페이징바에 표시될 페이지 수
	
	//계산할 값
	private int maxPage; //전체 목록 페이지의 수
	private int startPage; //페이징바 시작 페이지 번호
	private int endPage; //페이징바 끝 페이지 번호
	
	//기본생성자 사용X
	
	//
	public PageInfo(int currentPage, int listCount) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		
		//전달받은 값과 명시적으로 선언된 값을 이용해 makePageInfo 수행()
		//값을 계산하는 메소드
		makePageInfo();
	}

	public PageInfo(int currentPage, int listCount, int limit, int pageSize) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		this.limit = limit;
		this.pageSize = pageSize;
		
		//전달받은 값과 명시적으로 선언된 값을 이용해 makePageInfo 수행()
		//값을 계산하는 메소드
		makePageInfo();
	}

	//getter setter
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
		//listCount가 변하면 모든 값이 바껴야 함
		//makePageInfo() 값을 계산하는 메소드 필요
		makePageInfo();
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
		//setLimit가 변하면 모든 값이 바껴야 함
		//makePageInfo() 값을 계산하는 메소드 필요
		makePageInfo();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		//pageSize가 변하면 모든 값이 바껴야 함
		//makePageInfo() 값을 계산하는 메소드 필요
		makePageInfo();
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	@Override
	public String toString() {
		return "PageInfo [currentPage=" + currentPage + ", listCount=" + listCount + ", limit=" + limit + ", pageSize="
				+ pageSize + ", maxPage=" + maxPage + ", startPage=" + startPage + ", endPage=" + endPage + "]";
	}
	
	//페이징 처리에 필요한 값을 계산하는 메소드
	private void makePageInfo() {

		//maxPage : 총 페이지 수 == 마지막 페이지
		//총 게시글 수 101개, 한 페이지에 보여지는 게시글 수 10개
		// → 총 페이지 수는? 101 / 10 = 10.1(올림 처리) → 11page
		 
		//listCount(게시글 수)를 double형으로 형변환하고 
		//limit(한페이지 글 수)로 나눈 후 Math.ceil으로 올림처리
		//maxPage는 int형 → int형으로 형변환 다시 필요
		// 총 페이지 수 계산 완료
		maxPage = (int)Math.ceil((double)listCount / limit);
		
		
		//페이징바에 페이지를 10개씩 보여줄 경우
		//startPage : 페이징바 시작 번호
		//현재 페이지 1~10 → 시작 페이지 1
		//현재 페이지 11~20 → 시작 페이지 11
		//현재 페이지 21~30 → 시작 페이지 21
		
		//왜 currentPage-1? 현재 페이지가 10, 20...인 경우 때문에 -1 필요?
		startPage = ((currentPage - 1)/pageSize) * pageSize + 1;
		
		//endPage : 페이징바 끝 번호
		//현재 페이지 5 / 시작 페이지 1 → 10
		//현재 페이지 15 / 시작 페이지 11 → 20
		endPage = startPage + pageSize - 1;
		
		//총 페이지 수가 end페이지 보다 작을 경우
		if(maxPage <= endPage) {
			endPage = maxPage;
		}
	}
	
}

























