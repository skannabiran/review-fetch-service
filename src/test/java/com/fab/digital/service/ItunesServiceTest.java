package com.fab.digital.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fab.digital.config.ApplicationProperties;
import com.fab.digital.exception.ReviewAnalysisException;
import com.fab.digital.model.itunes.ItunesResponse;
import com.fab.digital.util.TestDataSupplier;
import java.io.IOException;
import java.net.URI;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class ItunesServiceTest {

  private ItunesService itunesService;

  private MockRestServiceServer mockServer;

  @BeforeEach
  void setup() {
      RestTemplate restTemplate = new RestTemplate();
    ApplicationProperties applicationProperties = TestDataSupplier.getApplicationProperties();
    mockServer = MockRestServiceServer.createServer(restTemplate);
    itunesService = new ItunesService(restTemplate, applicationProperties);
  }

  /*@Test
  @SneakyThrows
  public void invokeItunesAPIAndHandle200Response() {
    mockServer
        .expect(
            ExpectedCount.once(),
            requestTo(
                new URI(
                    "http://localhost:9090/ae/rss/customerreviews/page=1/id=123/sortby=mostrecent/json")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(TestDataSupplier.getItunesResponseAsString()));
    ItunesResponse itunesResponse = itunesService.getReviews("123", 1);
    mockServer.verify();
    assertNotNull(itunesResponse);
    assertEquals("12533682956", itunesResponse.getFeed().getEntry().get(0).getId().getLabel());
  }

  @Test
  @SneakyThrows
  public void invokeItunesAPIAndHandle4xxResponse() {
    mockServer
        .expect(
            ExpectedCount.once(),
            requestTo(
                new URI(
                    "http://localhost:9090/ae/rss/customerreviews/page=1/id=123/sortby=mostrecent/json")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withResourceNotFound());
    Then:
    {
      ReviewAnalysisException exception =
          assertThrows(ReviewAnalysisException.class, () -> itunesService.getReviews("123", 1));
      assertNotNull(exception);
      assertEquals("ITUNES_API_SERVER_ERROR_404", exception.getErrorCode());
    }
  }

  @Test
  @SneakyThrows
  public void invokeItunesAPIAndHandle5xxResponse() {
    mockServer
        .expect(
            ExpectedCount.once(),
            requestTo(
                new URI(
                    "http://localhost:9090/ae/rss/customerreviews/page=1/id=123/sortby=mostrecent/json")))
        .andExpect(method(HttpMethod.GET))
        .andRespond(
            withServiceUnavailable());
    Then:
    {
      ReviewAnalysisException exception =
          assertThrows(ReviewAnalysisException.class, () -> itunesService.getReviews("123", 1));
      assertNotNull(exception);
      assertEquals("ITUNES_API_SERVER_ERROR_503", exception.getErrorCode());
    }
  }

  @Test
  @SneakyThrows
  public void invokeItunesAPIAndHandleError() {
    mockServer
            .expect(
                    ExpectedCount.once(),
                    requestTo(
                            new URI(
                                    "http://localhost:9090/ae/rss/customerreviews/page=1/id=123/sortby=mostrecent/json")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                    withException(new IOException("SSL Handshake Error")));
    Then:
    {
      ReviewAnalysisException exception =
              assertThrows(ReviewAnalysisException.class, () -> itunesService.getReviews("123", 1));
      assertNotNull(exception);
      assertEquals("ITUNES_API_SERVER_ERROR", exception.getErrorCode());
    }
  }*/
}
