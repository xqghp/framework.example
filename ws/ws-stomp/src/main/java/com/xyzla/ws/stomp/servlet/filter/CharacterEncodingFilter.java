package com.xyzla.ws.stomp.servlet.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 编码格式过滤
 */
public class CharacterEncodingFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contentType = request.getContentType();
        System.out.println("请求URL" + request.getRequestURL() + " 请求头ContentType:" + contentType);
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
//        response.setContentType(contentType);
        response.setContentType("text/html;charset=UTF-8");
        //		String name=request.getParameter("name");
        //		System.out.println(name+"======"+new String(name.getBytes("ISO8859-1"),"UTF-8"));
        chain.doFilter(req, res);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
