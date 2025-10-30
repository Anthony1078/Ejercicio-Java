package com.example.eval_java.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final byte[] secret;
    private final String issuer;
    private final long accessMillis;

    public JwtService(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.issuer:eval-java}") String issuer,
            @Value("${app.security.jwt.access-minutes:30}") long accessMinutes
    ) {
        this.secret = secret.getBytes();
        this.issuer = issuer;
        this.accessMillis = accessMinutes * 60_000;
    }

    public String issueFor(String subjectEmail) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subjectEmail)
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessMillis))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }
}
