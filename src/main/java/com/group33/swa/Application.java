package com.group33.swa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
/** Application class starts spring boot package */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Application {
  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }
}
