package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserComment {
  @JsonProperty("text")
  String text;

  @JsonProperty("lastModified")
  LastModified lastModified;

  @JsonProperty("starRating")
  String starRating;

  @JsonProperty("reviewerLanguage")
  String reviewerLanguage;

  @JsonProperty("device")
  String device;

  @JsonProperty("androidOsVersion")
  String androidOsVersion;

  @JsonProperty("appVersionCode")
  String appVersionCode;

  @JsonProperty("appVersionName")
  String appVersionName;

  @JsonProperty("thumbsUpCount")
  int thumbsUpCount;

  @JsonProperty("thumbsDownCount")
  int thumbsDownCount;

  @JsonProperty("deviceMetadata")
  DeviceMetadata deviceMetadata;
}
