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
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.IOException;
import java.io.InputStream;
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
@Profile("!local")
@Data
@Slf4j
public class AppConfig {

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
    } catch ( NoSuchAlgorithmException | InvalidKeySpecException e) {
        log.error("Error occurred while parsing key::{}", e.getMessage());
        throw new ReviewAnalysisException("ERROR_WHILE_PARSING_KEY", e.getMessage());
    }
    }

    public String loadPrivateKey() {
        String secretName = "fab-private-key";
        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.AP_SOUTHEAST_2)
                .build();
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse getSecretValueResponse;
        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            log.error("Error occurred while getting AWS secrets::{}", e.getMessage());
            throw new ReviewAnalysisException("ERROR_WHILE_GETTING_AWS_SECRETS", e.getMessage());
        }
        return getSecretValueResponse.secretString();

    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
//                .region(Region.ME_CENTRAL_1) // FAB
                .region(Region.AP_SOUTHEAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create()) // Auto-fetch credentials
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
