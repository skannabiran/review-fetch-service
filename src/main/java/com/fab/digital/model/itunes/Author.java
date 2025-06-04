package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Author{
    @JsonProperty("name")
    public Name name;
    @JsonProperty("uri")
    public Uri uri;
    @JsonProperty("label")
    public String label;

}
