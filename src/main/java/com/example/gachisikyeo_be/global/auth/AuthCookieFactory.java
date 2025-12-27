package com.example.gachisikyeo_be.global.auth;

import org.springframework.http.ResponseCookie;

public class AuthCookieFactory {

    public static ResponseCookie httpOnlyCookie(
            String name,
            String value,
            boolean secure,
            String sameSite,
            long maxAgeSeconds
    ) {
        return ResponseCookie.from(name, value == null ? "" : value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(maxAgeSeconds)
                .build();
    }

    public static ResponseCookie deleteCookie(String name, boolean secure, String sameSite) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(0)
                .build();
    }
}
