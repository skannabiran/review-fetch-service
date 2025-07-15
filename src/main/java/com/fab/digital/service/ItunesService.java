package com.fab.digital.service;

import static com.fab.digital.util.ApplicationUtil.formatErrorResponse;

import com.fab.digital.config.ApplicationProperties;
import com.fab.digital.entity.CustomerReview;
import com.fab.digital.exception.ReviewAnalysisException;
import com.fab.digital.model.itunes.ItunesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItunesService {

  private final RestTemplate restTemplate;
  private final ApplicationProperties applicationProperties;

  public ItunesResponse getReviews(String appId, Integer pageNum) {
    String errorCode = "ITUNES_API_SERVER_ERROR";
    String errorMessage = "ERROR_WHILE_INVOKING_ITUNES_GET_REVIEWS_API";
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<?> entity = new HttpEntity<>(headers);
    try {
      ResponseEntity<ItunesResponse> response =
          restTemplate.exchange(
              applicationProperties.itunesReviewApi(),
              HttpMethod.GET,
              entity,
                  ItunesResponse.class,
              pageNum, appId);
      if (response.getStatusCode().is2xxSuccessful()) {
//        log.info("itunes response::{}", response.getBody());
        return response.getBody();
      } else return null;
    } catch (Exception e) {
      String error = e.getMessage();
      if (e instanceof RestClientResponseException ex) {
        errorCode = errorCode.concat("_").concat(String.valueOf(ex.getStatusCode().value()));
        error = formatErrorResponse(ex);
      }
      log.error("Error occurred while calling Itune API::{}", error);
      throw new ReviewAnalysisException(errorCode, errorMessage);
    }
  }
}
