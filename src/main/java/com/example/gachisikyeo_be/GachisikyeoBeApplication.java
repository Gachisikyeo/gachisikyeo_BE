package com.example.gachisikyeo_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 트랜잭션 커밋 시점에 플러시가 호출될 때, 하이버네이트가 자동으로 시간 값을 채워준다.
@SpringBootApplication
public class GachisikyeoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GachisikyeoBeApplication.class, args);
    }

}
