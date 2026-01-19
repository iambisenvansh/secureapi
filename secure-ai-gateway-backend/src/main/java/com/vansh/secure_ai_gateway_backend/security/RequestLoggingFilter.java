package com.vansh.secure_ai_gateway_backend.security;

import com.vansh.secure_ai_gateway_backend.model.RequestLog;
import com.vansh.secure_ai_gateway_backend.model.ThreatAnalysisResult;
import com.vansh.secure_ai_gateway_backend.repository.RequestLogRepository;
import com.vansh.secure_ai_gateway_backend.service.ThreatDetectionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
//
//@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final RequestLogRepository logRepository;
    private final ThreatDetectionService threatDetectionService;

    public RequestLoggingFilter(RequestLogRepository logRepository,
                                ThreatDetectionService threatDetectionService) {
        this.logRepository = logRepository;
        this.threatDetectionService = threatDetectionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String endpoint = request.getRequestURI();

        // We don't log public endpoints like register/login/health
        boolean isPublicEndpoint =
                endpoint.startsWith("/auth/register") ||
                        endpoint.startsWith("/auth/login") ||
                        endpoint.startsWith("/health");

        if (auth != null && auth.isAuthenticated() && !isPublicEndpoint) {

            String username = auth.getName();
            String ip = request.getRemoteAddr();

            // Debug log so you can see in console that filter runs
            System.out.println("🔥 Logging request for user: " + username +
                    " | endpoint: " + endpoint + " | ip: " + ip);

            // 🔥 Analyze threat
            ThreatAnalysisResult analysis =
                    threatDetectionService.analyze(username, endpoint, ip);

            RequestLog log = new RequestLog(
                    username,
                    endpoint,
                    ip,
                    LocalDateTime.now(),
                    analysis.getScore(),
                    analysis.getLabel(),
                    analysis.getReason()
            );

            logRepository.save(log);
        }

        filterChain.doFilter(request, response);
    }
}
