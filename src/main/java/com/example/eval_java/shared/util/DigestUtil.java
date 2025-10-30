package com.example.eval_java.shared.util;

import java.security.MessageDigest;

public final class DigestUtil {
    private DigestUtil() {}
    public static String sha256Hex(byte[] bytes) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            var dig = md.digest(bytes);
            var sb = new StringBuilder(dig.length * 2);
            for (byte b : dig) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
