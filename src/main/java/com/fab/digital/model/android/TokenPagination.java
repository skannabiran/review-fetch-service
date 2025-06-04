package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenPagination {
  @JsonProperty("nextPageToken")
  int nextPageToken;
}
