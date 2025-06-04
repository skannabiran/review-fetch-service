package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Content{
    @JsonProperty("label")
    public String label;
    @JsonProperty("attributes")
    public Attributes attributes;
}
