package com.fab.digital.service;

import com.fab.digital.config.ApplicationProperties;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
        return Jwts.builder()
                .header()
                .keyId("2376a8bb205a6cd43624d70e682ee0615b7b3251") //private_key_id
                .add("alg", "RS256")
                .add("typ", "JWT")
                .and()
                .issuer("dubaifirstpoc@payit-site.iam.gserviceaccount.com") //client_email
                .claim("scope", "https://www.googleapis.com/auth/androidpublisher")
                .claim("aud", "https://oauth2.googleapis.com/token") //token_uri
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(privateKey)
                .compact();
    }
}
