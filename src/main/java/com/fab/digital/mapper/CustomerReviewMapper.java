package com.fab.digital.mapper;

import com.fab.digital.entity.CustomerReview;
import java.util.Map;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Component
public class CustomerReviewMapper {
  public static Map<String, AttributeValue> toDynamoItem(CustomerReview customerReview) {
    return Map.ofEntries(
        Map.entry("product", AttributeValue.builder().s(customerReview.getProduct()).build()),
        Map.entry("id", AttributeValue.builder().s(customerReview.getId()).build()),
        Map.entry("rating", AttributeValue.builder().s(customerReview.getRating()).build()),
        Map.entry("comment", AttributeValue.builder().s(customerReview.getComment()).build()),
        Map.entry("version", AttributeValue.builder().s(customerReview.getVersion()).build()),
        Map.entry("author", AttributeValue.builder().s(customerReview.getAuthor()).build()),
        Map.entry("comment_time", AttributeValue.builder().s(customerReview.getCommentTime()).build()),
        Map.entry("insert_time", AttributeValue.builder().s(customerReview.getInsertTime()).build()));
  }

  public static CustomerReview fromDynamoItem(Map<String, AttributeValue> item) {
    return CustomerReview.builder()
        .product(item.get("product").s())
        .id(item.get("id").s())
        .rating(item.get("rating").s())
        .comment(item.get("comment").s())
        .version(item.get("version").s())
        .author(item.get("author").s())
        .commentTime(item.get("comment_time").s())
        .insertTime(item.get("insert_time").s())
        .build();
  }
}
