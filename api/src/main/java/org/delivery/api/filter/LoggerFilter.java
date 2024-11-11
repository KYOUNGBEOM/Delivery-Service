package org.delivery.api.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

@Slf4j
@Component
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // servletRequest, response를 읽어버리면 뒷 단에서 읽을 수 없도록 설정되어 있기때문에 ServletRequest와 ServletResponse 객체를 캐싱 가능한 래퍼 클래스로 감싸줌
        ContentCachingRequestWrapper request = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper response = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        filterChain.doFilter(request, response);

        // request 정보
        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        StringBuilder requestHeaderValues = new StringBuilder();

        requestHeaderNames.asIterator().forEachRemaining(headerKey -> {
            String requestHeaderValue = request.getHeader(headerKey);

            requestHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(requestHeaderValue)
                    .append("] ");
        });

        String requestBody = new String(request.getContentAsByteArray());
        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.info(">>>>>>>>>>>> uri : {}, method : {}, header : {}, body : {}", uri, method, requestHeaderValues, requestBody);

        // response 정보
        Collection<String> responseHeaderNames = response.getHeaderNames();
        StringBuilder responseHeaderValues = new StringBuilder();

        responseHeaderNames.forEach(headerKey -> {
            String responseHeaderValue = response.getHeader(headerKey);

            responseHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(responseHeaderValue)
                    .append("] ");
        });

        String responseBody = new String(response.getContentAsByteArray());

        log.info("<<<<<<<<<<<< uri : {}, method : {}, header : {}, body : {}", uri, method, responseHeaderValues, responseBody);

        response.copyBodyToResponse();
    }
}
