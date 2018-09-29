package com.ipaylinks.jiaqi.serviceencryptor.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class MyFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        ServletInputStream inputStream = request.getInputStream();
//        String s = IOUtils.toString(inputStream);
//        System.out.println("Request Body start = "+s);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/test/hello");
        requestDispatcher.forward(servletRequest,servletResponse);
//        String method = request.getParameter("method");
//        if (StringUtils.isEmpty(method)){
//            filterChain.doFilter(servletRequest,servletResponse);
//        }else {
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/"+method);
//            requestDispatcher.forward(servletRequest,servletResponse);
//        }
    }

    @Override
    public void destroy() {

    }
}
