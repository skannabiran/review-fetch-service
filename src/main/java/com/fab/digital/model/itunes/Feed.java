package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Feed{
    @JsonProperty("author")
    Author author;

    @JsonProperty("entry")
    List<Entry> entry;

    @JsonProperty("updated")
    Updated updated;

    @JsonProperty("rights")
    Rights rights;

    @JsonProperty("title")
    Title title;

    @JsonProperty("icon")
    Icon icon;

    @JsonProperty("link")
    List<Link> link;

    @JsonProperty("id")
    Id id;

}
