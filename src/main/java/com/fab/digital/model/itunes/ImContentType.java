package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImContentType {
    @JsonProperty("attributes")
    public Attributes attributes;
}
