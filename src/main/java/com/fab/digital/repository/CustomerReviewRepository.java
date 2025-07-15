package com.fab.digital.repository;

import static com.fab.digital.mapper.CustomerReviewMapper.fromDynamoItem;

import com.fab.digital.entity.CustomerReview;
import com.fab.digital.mapper.CustomerReviewMapper;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomerReviewRepository {

  private static final String TABLE_NAME = "customer_review";
  private final DynamoDbClient dynamoDbClient;

  public Optional<CustomerReview> findCustomerReview(CustomerReview customerReview) {
    Map<String, AttributeValue> key =
        Map.of(
            "product",
            AttributeValue.builder().s(customerReview.getProduct()).build(),
            "id",
            AttributeValue.builder().s(customerReview.getId()).build());
    GetItemRequest request = GetItemRequest.builder().tableName(TABLE_NAME).key(key).build();
    Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
    if (item == null || item.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(fromDynamoItem(item));
  }

  public List<CustomerReview> findBySearchCriteria(String product, String updatedTime) {
    Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
    expressionAttributeValues.put(":product", AttributeValue.builder().s(product).build());
    expressionAttributeValues.put(":comment_ts", AttributeValue.builder().s(updatedTime).build());
    QueryRequest queryRequest =
        QueryRequest.builder()
            .tableName(TABLE_NAME)
            .keyConditionExpression("product = :product")
                .filterExpression("comment_ts > :comment_ts")
            .expressionAttributeValues(expressionAttributeValues)
            .build();
    List<Map<String, AttributeValue>> items = dynamoDbClient.query(queryRequest).items();
    return items.stream().map(CustomerReviewMapper::fromDynamoItem).toList();
  }

  public List<CustomerReview> findAll() {
    ScanRequest scanRequest = ScanRequest.builder().tableName(TABLE_NAME).build();
    List<Map<String, AttributeValue>> items = dynamoDbClient.scan(scanRequest).items();
    if (items.isEmpty()) {
      return new ArrayList<>();
    }
    return items.stream().map(CustomerReviewMapper::fromDynamoItem).toList();
  }

  public void deleteAll() {
    List<CustomerReview> customerReviews = findAll();
    customerReviews.forEach(
        customerReview -> {
          Map<String, AttributeValue> key =
              Map.of(
                  "product",
                  AttributeValue.builder().s(customerReview.getProduct()).build(),
                  "id",
                  AttributeValue.builder().s(customerReview.getId()).build());
          DeleteItemRequest request =
              DeleteItemRequest.builder().tableName(TABLE_NAME).key(key).build();
          try {
            dynamoDbClient.deleteItem(request);
          } catch (DynamoDbException e) {
            log.error("Error saving data to DynamoDB:{}", e.getMessage(), e);
          }
        });
  }

  public void save(CustomerReview customerReview) {
    PutItemRequest request =
        PutItemRequest.builder()
            .tableName(TABLE_NAME)
            .item(CustomerReviewMapper.toDynamoItem(customerReview))
            .build();
    try {
      log.info("CustomerReview-save::{}", request);
      dynamoDbClient.putItem(request);
    } catch (DynamoDbException e) {
      log.error("Error saving data to DynamoDB:{}", e.getMessage(), e);
    }
  }
}
