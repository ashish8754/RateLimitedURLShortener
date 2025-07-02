package com.example.RateLimitedURLShortener.Configuration;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitFilter  extends OncePerRequestFilter {

    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration config = BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofHours(1))))
            .build();

    public RateLimitFilter(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().equals("/api/shorten")) {
            String clientIp = request.getRemoteAddr();
            var bucket = proxyManager.builder().build(clientIp, config);
            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,"Too many requests");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
