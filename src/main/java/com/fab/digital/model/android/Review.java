package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Review {
  @JsonProperty("reviewId")
  String reviewId;

  @JsonProperty("authorName")
  String authorName;

  @JsonProperty("comments")
  List<Comment> comments;
}
