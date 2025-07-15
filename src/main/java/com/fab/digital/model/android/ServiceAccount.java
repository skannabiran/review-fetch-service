package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ServiceAccount {

    @JsonProperty("type")
    String type;

    @JsonProperty("project_id")
    String projectId;

    @JsonProperty("private_key_id")
    String privateKeyId;

    @JsonProperty("private_key")
    String privateKey;

    @JsonProperty("client_email")
    String clientEmail;

    @JsonProperty("client_id")
    String clientId;

    @JsonProperty("auth_uri")
    String authUri;

    @JsonProperty("token_uri")
    String tokenUri;

    @JsonProperty("auth_provider_x509_cert_url")
    String authProviderX509CertUrl;

    @JsonProperty("client_x509_cert_url")
    String clientX509CertUrl;

    @JsonProperty("universe_domain")
    String universeDomain;
}
