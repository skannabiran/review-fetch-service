package com.fab.digital.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
public class ApplicationUtil {

  public static String formatErrorResponse(RestClientResponseException exception) {
    return String.format(
        "ErrorMessage:%s|Status:%s|StatusCode:%s|Body:%s",
        exception.getMessage(),
        exception.getStatusText(),
        exception.getStatusCode().value(),
        exception.getResponseBodyAsString());
  }
}
