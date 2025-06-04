package com.fab.digital;

import com.fab.digital.controller.ReviewFetchController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@Import(ReviewFetchController.class)
@SpringBootApplication
@ConfigurationPropertiesScan
public class ReviewFetchApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReviewFetchApplication.class, args);
  }
}
