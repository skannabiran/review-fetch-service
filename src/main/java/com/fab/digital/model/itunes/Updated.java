package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Updated {

  @JsonProperty("label")
  String label;
    }