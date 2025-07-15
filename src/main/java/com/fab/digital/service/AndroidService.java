package com.fab.digital.service;

import com.fab.digital.config.ApplicationProperties;
import com.fab.digital.exception.ReviewAnalysisException;
import com.fab.digital.model.android.AndroidResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.fab.digital.util.ApplicationUtil.formatErrorResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AndroidService {

  private final RestTemplate restTemplate;
  private final GoogleAuthService googleAuthService;
  private final ApplicationProperties applicationProperties;

  public AndroidResponse getReviews(String packageName, Integer nextPageToken) {
    Map<String, String> uriVariables = new HashMap<>();
    String url = applicationProperties.androidReviewApi();
    if (nextPageToken > 0) {
      url = url + "&token={next_page_token}";
      uriVariables.put("next_page_token", nextPageToken.toString());
    }
    uriVariables.put("package_name", packageName);
    uriVariables.put("access_token", googleAuthService.getAccessToken());
    String errorCode = "ANDROID_API_SERVER_ERROR";
    String errorMessage = "ERROR_WHILE_INVOKING_ANDROID_GET_REVIEWS_API";
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<?> entity = new HttpEntity<>(headers);
    try {
      ResponseEntity<AndroidResponse> response =
          restTemplate.exchange(url, HttpMethod.GET, entity, AndroidResponse.class, uriVariables);
      if (response.getStatusCode().is2xxSuccessful()) {
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
