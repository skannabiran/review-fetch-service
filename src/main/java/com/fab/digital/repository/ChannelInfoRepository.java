package com.fab.digital.repository;

import static com.fab.digital.mapper.ChannelInfoMapper.fromDynamoItem;

import com.fab.digital.entity.ChannelInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fab.digital.entity.CustomerReview;
import com.fab.digital.mapper.ChannelInfoMapper;
import com.fab.digital.mapper.CustomerReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

@Repository
@RequiredArgsConstructor
public class ChannelInfoRepository {

  private static final String TABLE_NAME = "channel_info";
  private final DynamoDbClient dynamoDbClient;

  public List<ChannelInfo> findAll() {
    ScanRequest scanRequest = ScanRequest.builder().tableName(TABLE_NAME).build();
    List<Map<String, AttributeValue>> items = dynamoDbClient.scan(scanRequest).items();
    if (items.isEmpty()) {
      return new ArrayList<>();
    }
    return items.stream().map(ChannelInfoMapper::fromDynamoItem).toList();
  }
}
