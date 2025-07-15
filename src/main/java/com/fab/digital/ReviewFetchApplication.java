package com.fab.digital;

import com.fab.digital.model.EventRequest;
import com.fab.digital.model.EventResponse;
import com.fab.digital.service.ReviewFetchService;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class ReviewFetchApplication {

  private final ReviewFetchService reviewFetchService;

    @Bean
  public Function<String, String> uppercase() {
    return event -> reviewFetchService.fetchReview();
  }
  
  public static void main(String[] args) {
    SpringApplication.run(ReviewFetchApplication.class, args);
  }
}
