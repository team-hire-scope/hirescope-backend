package com.project.hirescopebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // BaseTimeEntity의 @CreatedDate, @LastModifiedDate 동작에 필요
public class HirescopeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HirescopeBackendApplication.class, args);
    }
}
