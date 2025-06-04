/*
package com.fab.digital.repository;

import com.fab.digital.dto.CustomerReview;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Hooks;

@SpringBootTest
public class CustomerReviewRepositoryTest {

  @Autowired CustomerReviewRepository customerReviewRepository;

  @BeforeEach
  public void setup() {
    Hooks.onOperatorDebug();
    database
        .sql("DELETE FROM CustomerReview;")
        .fetch()
        .rowsUpdated()
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  public void tests_persisEmployee() {
    CustomerReview customerReview1 =
        CustomerReview.builder()
            .id(UUID.randomUUID().toString())
            .appId("123")
            .platform("IOS")
            .reviewId("12533682956")
            .title("Mobile number update")
            .content("Thank you finally my issue was resolved")
            .rating("1")
            .version("2.2.22")
            .author("Kellen@12")
            .build();
    CustomerReview customerReview2 =
        CustomerReview.builder()
            .id(UUID.randomUUID().toString())
            .appId("456")
            .platform("IOS")
            .reviewId("12533682956")
            .title("Mobile number update")
            .content("Thank you finally my issue was resolved")
            .rating("1")
            .version("2.2.22")
            .author("Kellen@12")
            .build();
    this.customerReviewRepository
        .saveAll(Arrays.asList(customerReview1, customerReview2))
        .as(StepVerifier::create)
        .expectNextCount(2)
        .verifyComplete();
    this.customerReviewRepository
        .findAll()
        .as(StepVerifier::create)
        .expectNext(customerReview1)
        .expectNext(customerReview2)
        .verifyComplete();
  }
}
*/
