package com.fab.digital.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
@Profile("local")
public class DynamoDBConfig {

  @Bean
  public DynamoDbClient dynamoDbClient() {
    return DynamoDbClient.builder()
            .region(Region.US_EAST_1) // Use any AWS region (this is ignored by LocalStack)
            .endpointOverride(URI.create("http://localhost:8000")) // LocalStack URL
            .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create("dummyAccessKeyId", "dummySecretKey") // Fake credentials for LocalStack
            ))
            .build();
  }

}
