package com.fab.digital.util;

import com.fab.digital.config.ApplicationProperties;
import com.fab.digital.model.itunes.ItunesResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

public class TestDataSupplier {

  public static ApplicationProperties getApplicationProperties() {
    return new ApplicationProperties(
            "xyz",
            "abc123",
            "us-east",
            "http://localhost:9090/ae/rss/customerreviews/page={page_num}/id={app_id}/sortby=mostrecent/json",
            "http://localhost:9090/o/oauth2/token",
            "http://localhost:9091/androidpublisher/v3/applications/{package_name}/reviews?access_token={access_token}&maxResults=100&translationLanguage=en");
  }

  public static ItunesResponse getItunesResponse() {
    return loadTestData("/itunesresponse.json", ItunesResponse.class);
  }

  public static String getItunesResponseAsString() {
    return loadTestDataAsString("/itunesresponse.json");
  }

  public static String loadTestDataAsString(String fileName) {
    try {
      return new String(Files.readAllBytes(new ClassPathResource(fileName).getFile().toPath()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T loadTestData(String fileName, Class<T> clazz ){
    try{
      ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return mapper.readValue(new ClassPathResource(fileName).getFile(), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
