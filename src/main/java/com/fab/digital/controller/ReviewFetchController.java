package com.fab.digital.controller;

import com.fab.digital.entity.CustomerReview;
import com.fab.digital.service.ReviewFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/review")
public class ReviewFetchController {

  private final ReviewFetchService reviewFetchService;

  @GetMapping(
      value = "/fetch",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<String> fetchReview() {
    String status = reviewFetchService.fetchReview();
    return new ResponseEntity<>(status, HttpStatus.OK);
  }

  @GetMapping(
      value = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<List<CustomerReview>> getReview(
      @RequestParam(name = "product") String product,
      @RequestParam(name = "fromDate", required = false) String fromDate) {
    List<CustomerReview> customerReviewList = reviewFetchService.getReview(product, fromDate);
    return new ResponseEntity<>(customerReviewList, HttpStatus.OK);
  }

  @DeleteMapping(
      value = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<String> deleteReview() {
    String status = reviewFetchService.deleteReview();
    return new ResponseEntity<>(status, HttpStatus.OK);
  }
}
