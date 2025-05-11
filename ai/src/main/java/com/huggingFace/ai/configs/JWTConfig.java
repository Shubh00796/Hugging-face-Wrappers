package com.huggingFace.ai.configs;

import org.springframework.stereotype.Component;

@Component
public class JWTConfig {
    public static final String SECRET = "ee43e6bc33b91d657f078e2a610680ba571ca4d788165a975f310002a0fead72";
    public static final long EXPIRATION_TIME = 864_000_000; // 1 day
}