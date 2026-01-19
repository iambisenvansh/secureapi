package com.vansh.secure_ai_gateway_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 50;

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts =
            new ConcurrentHashMap<>();

    private final Set<String> blockedIps =
            ConcurrentHashMap.newKeySet();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ DO NOT rate-limit auth or admin APIs
        if (path.startsWith("/auth") || path.startsWith("/admin")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();

        if (blockedIps.contains(ip)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        requestCounts.putIfAbsent(ip, new AtomicInteger(0));
        int count = requestCounts.get(ip).incrementAndGet();

        if (count > MAX_REQUESTS) {
            blockedIps.add(ip);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    public int getBlockedCount() {
        return blockedIps.size();
    }
}
