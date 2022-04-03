package com.zaomengjia.auth.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 21:06
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authentication");

        if(jwtToken != null && !"".equals(jwtToken.trim())) {
            //开始验证jwtToken
            

        }

        filterChain.doFilter(request, response);
    }
}
