package me.tianshuang;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PullReadhubmeApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PullReadhubmeApplication.class)
                .properties("spring.config.name:pull-readhubme")
                .build()
                .run(args);
    }
}
