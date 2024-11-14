package com.example.prac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.prac.config.A;

@SpringBootApplication
public class PracApplication {

    public static void main(String[] args) {
        A a = new A();
        a.test();
        SpringApplication.run(PracApplication.class, args);
    }

}
