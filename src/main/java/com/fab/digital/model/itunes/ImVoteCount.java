package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImVoteCount {

    @JsonProperty("label")
    String label;

}
