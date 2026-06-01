package com.vansh.secure_ai_gateway_backend.security;

import com.vansh.secure_ai_gateway_backend.model.ThreatEvent;
import com.vansh.secure_ai_gateway_backend.service.ThreatStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 50;
    private static final int SUSPICIOUS_THRESHOLD = 20;

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts =
            new ConcurrentHashMap<>();

    private final Set<String> blockedIps =
            ConcurrentHashMap.newKeySet();

    private final ThreatStore threatStore;

    // ✅ Constructor injection
    public RateLimitFilter(ThreatStore threatStore) {
        this.threatStore = threatStore;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ Do not rate-limit auth & admin endpoints
        if (path.startsWith("/auth") || path.startsWith("/admin")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();
        String method = request.getMethod();

        if (blockedIps.contains(ip)) {
            logThreat(ip, path, method, "MALICIOUS");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        requestCounts.putIfAbsent(ip, new AtomicInteger(0));
        int count = requestCounts.get(ip).incrementAndGet();

        if (count > MAX_REQUESTS) {
            blockedIps.add(ip);
            logThreat(ip, path, method, "MALICIOUS");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (count > SUSPICIOUS_THRESHOLD) {
            logThreat(ip, path, method, "SUSPICIOUS");
        } else {
            logThreat(ip, path, method, "NORMAL");
        }

        filterChain.doFilter(request, response);
    }

    private void logThreat(String ip, String endpoint, String method, String label) {
        ThreatEvent event = new ThreatEvent(
                ip,
                endpoint,
                method,
                label,
                LocalDateTime.now()
        );
        threatStore.add(event);
    }

    public int getBlockedCount() {
        return blockedIps.size();
    }
}
