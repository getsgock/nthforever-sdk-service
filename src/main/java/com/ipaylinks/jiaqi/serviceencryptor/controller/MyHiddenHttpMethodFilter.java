package com.ipaylinks.jiaqi.serviceencryptor.controller;

import org.springframework.http.MediaType;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyHiddenHttpMethodFilter extends HiddenHttpMethodFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("POST".equals(request.getMethod())
                && MediaType.APPLICATION_FORM_URLENCODED.toString().equals(request.getContentType())){
            //Skip this filter and call the next filter in the chain.
            filterChain.doFilter(request, response);
            System.out.println("doFilterInternal post");
        } else {
            //Continue with processing this filter.
            System.out.println("doFilterInternal not");
            super.doFilterInternal(request, response, filterChain);
        }
//        super.doFilterInternal(request, response, filterChain);
    }
}
