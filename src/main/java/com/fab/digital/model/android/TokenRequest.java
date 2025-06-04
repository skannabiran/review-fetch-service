package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
  @JsonProperty("code")
  String code;

  @JsonProperty("client_id")
  String clientId;

  @JsonProperty("client_secret")
  String clientSecret;

  @JsonProperty("redirect_uri")
  String redirectUri;

  @JsonProperty("grant_type")
  String grantType;
}
