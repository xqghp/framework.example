package com.xyzla.ws.stomp.servlet.filter;



import com.xyzla.ws.stomp.servlet.context.XyzlaServletContext;

import javax.servlet.*;
import java.io.IOException;


/**
 * 过滤用户请求
 */
public class ServletContextFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        XyzlaServletContext.setServletContext(req, res); //设置值

        chain.doFilter(req, res); //走下一个链条

        //该方法不做清空缓存处理
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
