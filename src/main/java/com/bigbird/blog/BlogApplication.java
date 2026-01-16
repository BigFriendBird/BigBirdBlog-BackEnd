package com.bigbird.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("com.bigbird.blog.mapper")
public class BlogApplication {

    private static final Logger log = LoggerFactory.getLogger(BlogApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BlogApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");

        log.info("\n" +
                "╔═══════════════════════════════════════════════════════════════════╗\n" +
                "║                                                                   ║\n" +
                "║     ██████╗ ██╗ ██████╗ ██████╗ ██╗██████╗ ██████╗                ║\n" +
                "║     ██╔══██╗██║██╔════╝ ██╔══██╗██║██╔══██╗██╔══██╗               ║\n" +
                "║     ██████╔╝██║██║  ███╗██████╔╝██║██████╔╝██║  ██║               ║\n" +
                "║     ██╔══██╗██║██║   ██║██╔══██╗██║██╔══██╗██║  ██║               ║\n" +
                "║     ██████╔╝██║╚██████╔╝██████╔╝██║██║  ██║██████╔╝               ║\n" +
                "║     ╚═════╝ ╚═╝ ╚═════╝ ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝                ║\n" +
                "║                                                                   ║\n" +
                "║           BigBird Blog Backend 启动成功!                          ║\n" +
                "║                                                                   ║\n" +
                "║   Local:      http://localhost:" + port + contextPath + "                         ║\n" +
                "║                                                                   ║\n" +
                "╚═══════════════════════════════════════════════════════════════════╝"
        );
    }

}
