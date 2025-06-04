package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeviceMetadata {
  @JsonProperty("productName")
  String productName;

  @JsonProperty("manufacturer")
  String manufacturer;

  @JsonProperty("deviceClass")
  String deviceClass;

  @JsonProperty("screenWidthPx")
  int screenWidthPx;

  @JsonProperty("screenHeightPx")
  int screenHeightPx;

  @JsonProperty("nativePlatform")
  String nativePlatform;

  @JsonProperty("screenDensityDpi")
  int screenDensityDpi;

  @JsonProperty("glEsVersion")
  int glEsVersion;

  @JsonProperty("cpuModel")
  String cpuModel;

  @JsonProperty("cpuMake")
  String cpuMake;

  @JsonProperty("ramMb")
  int ramMb;
}
