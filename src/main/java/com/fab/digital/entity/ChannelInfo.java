package com.fab.digital.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelInfo {

  private String appId;
  private String channel;
  private String platform;
  private String packageName;
  private String enabled;
}