package com.fab.digital.model.itunes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Entry {

    @JsonProperty("author")
    Author author;

    @JsonProperty("updated")
    Updated updated;

    @JsonProperty("im:rating")
    ImRating imRating;

    @JsonProperty("im:version")
    ImVersion imVersion;

    @JsonProperty("id")
    Id id;

    @JsonProperty("title")
    Title title;

    @JsonProperty("content")
    Content content;

    @JsonProperty("link")
    Link link;

    @JsonProperty("im:voteSum")
    ImVoteSum imVoteSum;

    @JsonProperty("im:contentType")
    ImContentType imContentType;

    @JsonProperty("im:voteCount")
    ImVoteCount imVoteCount;

}