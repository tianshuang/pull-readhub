package me.tianshuang;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PullReadhubApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PullReadhubApplication.class)
                .properties("spring.config.name:pull-readhub")
                .build()
                .run(args);
    }
}
