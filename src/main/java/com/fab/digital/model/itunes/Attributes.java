package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Attributes{
    @JsonProperty("type")
    public String type;
    @JsonProperty("rel")
    public String rel;
    @JsonProperty("href")
    public String href;
    @JsonProperty("term")
    public String term;
    @JsonProperty("label")
    public String label;
}
