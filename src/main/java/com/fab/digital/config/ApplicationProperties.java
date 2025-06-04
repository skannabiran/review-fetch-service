package com.fab.digital.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
    String awsDynamoDbAccessKey,
    String awsDynamoDbSecretKey,
    String awsDynamoDbRegion,
    String itunesReviewApi,
    String androidTokenApi,
    String androidReviewApi) {}
