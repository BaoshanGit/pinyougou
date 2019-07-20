package cn.itcast.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 引导类的格式固定
 */
@SpringBootApplication//该注解表明该类为引导类（重要注解）
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
