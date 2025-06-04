package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Uri {
    @JsonProperty("label")
    String label;
}
