package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeveloperComment {
  @JsonProperty("text")
  String text;

  @JsonProperty("lastModified")
  LastModified lastModified;
}
