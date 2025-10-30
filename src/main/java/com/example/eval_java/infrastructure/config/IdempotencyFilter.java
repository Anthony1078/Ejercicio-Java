package com.example.eval_java.infrastructure.config;

import com.example.eval_java.infrastructure.persistence.IdempotencyService;
import com.example.eval_java.shared.util.DigestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyService service;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("POST".equalsIgnoreCase(request.getMethod())
                && request.getRequestURI().startsWith("/api/v1/users"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        final String idKey = req.getHeader("Idempotency-Key");
        if (idKey == null || idKey.isBlank()) {
            chain.doFilter(req, res);
            return;
        }

        ContentCachingRequestWrapper reqWrap = new ContentCachingRequestWrapper(req);
        ContentCachingResponseWrapper resWrap = new ContentCachingResponseWrapper(res);

        try {
            processOrReplay(reqWrap, resWrap, chain, idKey);
        } finally {
            resWrap.copyBodyToResponse();
        }
    }

    private void processOrReplay(ContentCachingRequestWrapper req,
                                 ContentCachingResponseWrapper res,
                                 FilterChain chain,
                                 String idKey) throws IOException, ServletException {

        byte[] bodyBytes = req.getContentAsByteArray();
        String reqHash = DigestUtil.sha256Hex(bodyBytes);

        var existingOpt = service.findByKey(idKey);
        if (existingOpt.isPresent()) {
            var rec = existingOpt.get();

            if (!rec.getRequestHash().equals(reqHash)) {
                res.setStatus(HttpStatus.CONFLICT.value());
                res.setContentType("application/json");
                res.getWriter().write("""
                  {"codigo":"IDEMPOTENCY_CONFLICT","mensaje":"Idempotency-Key ya usada con otro contenido","timestamp":"%s"}
                  """.formatted(Instant.now()));
                return;
            }

            if (rec.getHttpStatus() != null && rec.getResponseBody() != null) {
                res.setStatus(rec.getHttpStatus());
                if (rec.getContentType() != null) res.setContentType(rec.getContentType());
                res.getWriter().write(rec.getResponseBody());
                return;
            }
        } else {
            try {
                service.createPending(idKey, reqHash);
            } catch (DataIntegrityViolationException e) {
                var rec = service.findByKey(idKey).orElseThrow();
                if (!rec.getRequestHash().equals(reqHash)) {
                    res.setStatus(HttpStatus.CONFLICT.value());
                    res.setContentType("application/json");
                    res.getWriter().write("""
                      {"codigo":"IDEMPOTENCY_CONFLICT","mensaje":"Idempotency-Key ya usada con otro contenido","timestamp":"%s"}
                      """.formatted(Instant.now()));
                    return;
                }
                if (rec.getHttpStatus() != null && rec.getResponseBody() != null) {
                    res.setStatus(rec.getHttpStatus());
                    if (rec.getContentType() != null) res.setContentType(rec.getContentType());
                    res.getWriter().write(rec.getResponseBody());
                    return;
                }
            }
        }

        chain.doFilter(req, res);

        String contentType = res.getContentType();
        int status = res.getStatus();
        String body = new String(res.getContentAsByteArray(), res.getCharacterEncoding());
        service.storeResponse(idKey, reqHash, status, contentType, body);
    }
}