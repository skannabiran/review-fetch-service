package com.fab.digital.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerReview {

  private String product;
  private String id;
  private String title;
  private String rating;
  private String comment;
  private String version;
  private String author;
  private String commentTS;
  private String insertTS;
}
