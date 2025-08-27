package com.jongkeun.security_filter_chain.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityLoggingFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)  servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        logger.info("🎬 [REQUEST START] {} {}", method, requestURI);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.isAuthenticated()) {
            logger.info("🔐 [AUTH STATUS] User: {}, Authorities: {}",
                    auth.getName(), auth.getAuthorities());
        } else {
            logger.info("👤 [AUTH STATUS] Not authenticated");
        }

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(servletRequest, servletResponse);

            long endTime = System.currentTimeMillis();

            logger.info("🏁 [REQUEST END] {} {} -> Status: {}, Time: {}ms", method, requestURI, httpResponse.getStatus(), (endTime - startTime));
        } catch (Exception e) {
            logger.error("❌ [REQUEST ERROR] {} {} -> Error: {}", method, requestURI, e.getMessage());
            throw e;
        }
    }
}