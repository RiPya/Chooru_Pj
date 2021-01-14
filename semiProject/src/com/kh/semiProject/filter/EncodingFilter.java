package com.kh.semiProject.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

// @WebFilter 어노테이션
// - Servlet 3.0 이상부터 사용이 가능하다.
// - filterName: 필터 이름을 지정(필터 순서를 지정할 때 사용한다)
// - urlPatterns: 필터가 적용될 주소(url)을 지정(패턴 사용이 가능),
//				  (/*: 모든 url)
@WebFilter(filterName = "encoding", urlPatterns = "/*")
public class EncodingFilter implements Filter {

    public EncodingFilter() {  }

    // filter 생명 주기
    // init() → doFilter() → destroy()

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// url 패턴이 일치하는 요청/응답에 대해서 동작하는 메소드
		
		// 요청 / 응답 시 문자 인코딩을 모두 UTF-8로 바꾸는 필터 작성
		request.setCharacterEncoding("UTF-8"); // 요청 데이터 문자 인코딩 변경
		response.setCharacterEncoding("UTF-8"); // 응답 데이터 문자 인코딩 변경
		
		chain.doFilter(request, response);
	}


	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void destroy() {
	}
}
