package com.jongkeun.security_filter_chain.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FilterOrderLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(FilterOrderLoggingFilter.class);
    private final String filterName;
    private final int order;

    public FilterOrderLoggingFilter(String filterName, int order){
        this.filterName = filterName;
        this.order = order;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        logger.info("🔍 [FILTER-{}] {} BEFORE - URI: {}", order, filterName, httpRequest.getRequestURI());

        try {
            filterChain.doFilter(servletRequest, servletResponse);

            logger.info("✅ [FILTER-{}] {} AFTER", order, filterName);
        } catch (Exception e) {
            logger.error("❌ [FILTER-{}] {} ERROR: {}", order, filterName, e.getMessage());
        }
    }
}
