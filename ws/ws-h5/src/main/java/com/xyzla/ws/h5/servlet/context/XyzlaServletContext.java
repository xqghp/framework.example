package com.xyzla.ws.h5.servlet.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 封装的请求实现
 */
public class XyzlaServletContext {

    /**
     * 请求缓存
     */
    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();

    /**
     * 响应缓存
     */
    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();

    /**
     * 设置值
     *
     * @param req
     * @param res
     */
    public static void setServletContext(ServletRequest req, ServletResponse res) {
        requestLocal.set((HttpServletRequest) req);
        responseLocal.set((HttpServletResponse) res);
    }

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return requestLocal.get();
    }

    /**
     * 获取response
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return responseLocal.get();
    }

    /**
     * 获取会话session
     *
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取application
     *
     * @return
     */
    public static ServletContext getApplication() {
        return getSession().getServletContext();
    }

    /**
     * 清除缓存
     */
    public static void clear() {
        requestLocal.remove();
        responseLocal.remove();
    }
}
