package com.fab.digital.service;

import static com.fab.digital.util.ApplicationUtil.formatErrorResponse;

import com.fab.digital.config.ApplicationProperties;
import com.fab.digital.exception.ReviewAnalysisException;
import com.fab.digital.model.android.AndroidResponse;
import com.fab.digital.model.android.ServiceAccount;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {
    private final RestTemplate restTemplate;
    private final PrivateKey privateKey;
    private final ApplicationProperties applicationProperties;

    public String getAccessToken() {
        String jwt = createJwtAssertion();
        log.info("Jwt::{}", jwt);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
        formData.add("assertion", jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(
                        applicationProperties.androidTokenApi(), HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getString("access_token");
        } else {
            throw new RuntimeException("Failed to retrieve access token: " + response.getStatusCode());
        }
    }

    public String createJwtAssertion() {
//        PrivateKey privateKey;
        String serviceAccountFilePath = "dubai-first-poc-service-account.json";
        ServiceAccount serviceAccount = getServiceAccountDetails(serviceAccountFilePath);
      /*  try {
      String key = getSecret();
         *//*         String key =    Files.readString(
              new ClassPathResource("private-key.pem").getFile().toPath(), Charset.defaultCharset());*//*
            log.info("private key::{}", key);
            String privateKeyPEM =
                    key.replace("-----BEGIN PRIVATE KEY-----", "")
                            .replaceAll(System.lineSeparator(), "")
                            .replace("-----END PRIVATE KEY-----", "");
            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyPEM));

            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(keySpec);
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Error occurred while parsing key::{}", e.getMessage());
            throw new ReviewAnalysisException("ERROR_WHILE_PARSING_KEY", e.getMessage());
        }*/
        return Jwts.builder()
                .header()
                .keyId(serviceAccount.getPrivateKeyId())
                .add("alg", "RS256")
                .add("typ", "JWT")
                .and()
                .issuer(serviceAccount.getClientEmail())
                .claim("scope", "https://www.googleapis.com/auth/androidpublisher")
                .claim("aud", serviceAccount.getTokenUri())
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(privateKey)
                .compact();
    }

    public ServiceAccount getServiceAccountDetails(String filePath) {
        try {
            ObjectMapper mapper =
                    new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(new ClassPathResource(filePath).getFile(), ServiceAccount.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
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



}
