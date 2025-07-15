package com.fab.digital.config;


import com.fab.digital.exception.ReviewAnalysisException;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Configuration
@Profile("local")
@Data
@Slf4j
public class AppLocalConfig {
    @Bean
    public PrivateKey privateKey() {
        try{
            String pem = loadPrivateKey();
            log.info("private-key::{}",pem);
            String privateKeyPEM =
                    pem.replace("-----BEGIN PRIVATE KEY-----", "")
                            .replaceAll(System.lineSeparator(), "")
                            .replace("-----END PRIVATE KEY-----", "");
            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPEM));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Error occurred while parsing key::{}", e.getMessage());
            throw new ReviewAnalysisException("ERROR_WHILE_PARSING_KEY", e.getMessage());
        }
    }

    public String loadPrivateKey(){
        ClassPathResource resource = new ClassPathResource("private-key.pem");
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Error occurred while loading key::{}", e.getMessage());
            throw new ReviewAnalysisException("ERROR_WHILE_LOADING_KEY", e.getMessage());
        }
    }

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

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverter) {
                List<MediaType> supportedMediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
                supportedMediaTypes.add(MediaType.valueOf("text/javascript;charset=UTF-8")); // optional charset
                supportedMediaTypes.add(MediaType.valueOf("text/javascript"));
                supportedMediaTypes.add(MediaType.valueOf("application/x-www-form-urlencoded"));
                jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
            }
        }
        return restTemplate;
    }

}
