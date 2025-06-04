package com.fab.digital.model.android;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Comment {
  @JsonProperty("userComment")
  UserComment userComment;

  @JsonProperty("developerComment")
  DeveloperComment developerComment;
}
