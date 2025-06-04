package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LastModified {
  @JsonProperty("seconds")
  long seconds;

  @JsonProperty("nanos")
  long nanos;
}
