package com.project.hirescopebackend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800) // 30분
public class SessionConfig {
    // Spring Session + Redis 기반 세션 관리
    // maxInactiveIntervalInSeconds: 세션 만료 시간 (초 단위)
}
