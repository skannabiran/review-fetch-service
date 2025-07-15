package com.fab.digital.mapper;

import com.fab.digital.entity.ChannelInfo;
import java.util.Map;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Component
public class ChannelInfoMapper {
  public static Map<String, AttributeValue> toDynamoItem(ChannelInfo channelInfo) {
    return Map.of(
        "app_id",
        AttributeValue.builder().s(channelInfo.getAppId()).build(),
        "channel",
        AttributeValue.builder().s(channelInfo.getChannel()).build(),
        "platform",
        AttributeValue.builder().s(channelInfo.getPlatform()).build(),
        "package_name",
        AttributeValue.builder().s(channelInfo.getPackageName()).build(),
        "enabled",
        AttributeValue.builder().s(channelInfo.getPackageName()).build());
  }

  public static ChannelInfo fromDynamoItem(Map<String, AttributeValue> item) {
    return ChannelInfo.builder()
        .appId(item.get("app_id").s())
        .channel(item.get("channel").s())
        .platform(item.get("platform").s())
        .packageName(item.get("package_name").s())
        .enabled(item.get("enabled").s())
        .build();
  }
}
