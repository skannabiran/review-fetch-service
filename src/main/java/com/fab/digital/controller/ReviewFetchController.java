package com.fab.digital.controller;

import com.fab.digital.model.EventRequest;
import com.fab.digital.model.EventResponse;
import com.fab.digital.service.ReviewFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewFetchController {

  private final ReviewFetchService reviewFetchService;

  @PostMapping(
      value = "/customer-review-fetch",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<EventResponse> fetchReview() {
//    EventResponse response = EventResponse.builder().status("hello").build();
    String response = reviewFetchService.fetchReview();
    return new ResponseEntity<>(EventResponse.builder().status(response).build(), HttpStatus.OK);
  }

}
