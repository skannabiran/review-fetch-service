package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class AndroidResponse {

  @JsonProperty("reviews")
  List<Review> reviews;

  @JsonProperty("tokenPagination")
  TokenPagination tokenPagination;
}
