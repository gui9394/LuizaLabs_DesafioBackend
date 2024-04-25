package com.gui9394.order.common.http;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RequestLogger extends HttpFilter {

    private static final String MESSAGE = "host={} method={} path={} mediaType={} statusCode={} duration={}";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        var start = System.currentTimeMillis();

        super.doFilter(request, response, chain);

        var status = response.getStatus();
        var params = new Object[] {
                request.getRemoteHost(),
                request.getMethod(),
                request.getRequestURI(),
                request.getHeader(HttpHeaders.ACCEPT),
                status,
                System.currentTimeMillis() - start,
        };

        if (status < 400) {
            log.info(MESSAGE, params);
        }
        else if (status < 500) {
            log.warn(MESSAGE, params);
        }
        else {
            log.error(MESSAGE, params);
        }
    }

}
