package com.fab.digital.service;

import com.fab.digital.entity.ChannelInfo;
import com.fab.digital.entity.CustomerReview;
import com.fab.digital.model.android.AndroidResponse;
import com.fab.digital.model.android.Comment;
import com.fab.digital.model.android.LastModified;
import com.fab.digital.model.android.Review;
import com.fab.digital.model.itunes.ItunesResponse;
import com.fab.digital.model.itunes.Link;
import com.fab.digital.repository.ChannelInfoRepository;
import com.fab.digital.repository.CustomerReviewRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewFetchService {

  private final ItunesService itunesService;
  private final AndroidService androidService;
  private final ChannelInfoRepository channelInfoRepository;
  private final CustomerReviewRepository customerReviewRepository;

  public String fetchReview() {
    log.info("Fetch Review started...");
    Iterable<ChannelInfo> channelInfoList = channelInfoRepository.findAll();
    channelInfoList.forEach(
        channelInfo -> {
          log.info("Fetch review started for channelInfo::{}", channelInfo);
          if ("Y".equalsIgnoreCase(channelInfo.getEnabled())) {
            if ("IOS".equalsIgnoreCase(channelInfo.getPlatform())) {
              int pageNum = 1, totalPageNum = 0;
              do {
                ItunesResponse itunesResponse =
                    itunesService.getReviews(channelInfo.getAppId(), pageNum++);
                totalPageNum = getPageNum(itunesResponse);
                log.info("itunes-totalPageNum::{}", totalPageNum);
                //              ItunesResponse itunesResponse = DataSupplier.getItunesResponse();
                List<CustomerReview> customerReviewList =
                    constructCustomerReview(itunesResponse, channelInfo);
                //                    log.info("customerReview::{}", customerReview);
                customerReviewList.forEach(this::saveCustomerReview);
              } while (pageNum <= totalPageNum);
            } else if ("ANDROID".equalsIgnoreCase(channelInfo.getPlatform())) {
              int nextPageToken = 0;
              do {
                AndroidResponse androidResponse =
                    androidService.getReviews(channelInfo.getPackageName(), nextPageToken);
                nextPageToken = getNextPageToken(androidResponse);
                //              AndroidResponse androidResponse = DataSupplier.getAndroidResponse();
                List<CustomerReview> customerReviewList =
                    constructCustomerReview(androidResponse, channelInfo);
                customerReviewList.forEach(this::saveCustomerReview);
              } while (nextPageToken > 0);
            }
            log.info("Fetch review ended for channelInfo::{}", channelInfo);
          }
        });
    return "Success";
  }

  private void saveCustomerReview(CustomerReview customerReview) {
    //                    log.info("customerReview::{}", customerReview);
    Optional<CustomerReview> customerReviewOpt =
        customerReviewRepository.findCustomerReview(customerReview);
    if (customerReviewOpt.isEmpty()) {
      customerReviewRepository.save(customerReview);
    }
  }

  private List<CustomerReview> constructCustomerReview(
      ItunesResponse itunesResponse, ChannelInfo channelInfo) {
    return itunesResponse.feed.getEntry().stream()
        .map(
            entry ->
                CustomerReview.builder()
                    .product(channelInfo.getPlatform() + "-" + channelInfo.getChannel())
                    .id(entry.getId().getLabel())
                    .title(entry.getTitle().getLabel())
                    .comment(entry.getContent().getLabel())
                    .rating(entry.getImRating().getLabel())
                    .version(entry.getImVersion().getLabel())
                    .author(entry.getAuthor().getName().getLabel())
                    .commentTS(entry.getUpdated().getLabel())
                    .insertTS(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).toString())
                    .build())
        .toList();
  }

  private List<CustomerReview> constructCustomerReview(
      AndroidResponse androidResponse, ChannelInfo channelInfo) {
    log.info("AndroidResponse::{}", androidResponse);
    List<CustomerReview> customerReviews = new ArrayList<>();
    for (Review review : androidResponse.getReviews()) {
      for (Comment comment : review.getComments()) {
        if (ObjectUtils.isNotEmpty(comment.getUserComment())) {
          customerReviews.add(
              CustomerReview.builder()
                  .product(channelInfo.getPlatform() + "-" + channelInfo.getChannel())
                  .id(review.getReviewId())
                  .author(review.getAuthorName())
                  .comment(comment.getUserComment().getText())
                  .rating(comment.getUserComment().getStarRating())
                  .version(
                      (StringUtils.isEmpty(comment.getUserComment().getAppVersionName())
                          ? ""
                          : comment.getUserComment().getAppVersionName()))
                  .commentTS(getTimeStamp(comment.getUserComment().getLastModified()))
                  .insertTS(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC).toString())
                  .build());
        }
      }
    }
    return customerReviews;
  }

  public Integer getPageNum(ItunesResponse itunesResponse) {
    int pageNum = 1;
    HashMap<String, String> urlMap = new HashMap<>();
    Optional<Link> linkOptional =
        itunesResponse.getFeed().getLink().stream()
            .filter(e -> e.getAttributes().getRel().equalsIgnoreCase("last"))
            .findFirst();
    if (linkOptional.isPresent()) {
      String lastPageUrl = linkOptional.get().getAttributes().getHref();
      for (String param : lastPageUrl.split("/")) {
        urlMap.put(StringUtils.substringBefore(param, "="), StringUtils.substringAfter(param, "="));
      }
      pageNum = Integer.parseInt(urlMap.get("page"));
    }
    return pageNum;
  }

  private Integer getNextPageToken(AndroidResponse androidResponse) {
    if (ObjectUtils.isNotEmpty(androidResponse.getTokenPagination())) {
      return androidResponse.getTokenPagination().getNextPageToken();
    }
    return 0;
  }

  public List<CustomerReview> getReview(String appDetail, String fromDate) {
    return customerReviewRepository.findBySearchCriteria(appDetail, fromDate);
  }

  public String deleteReview() {
    customerReviewRepository.deleteAll();
    return HttpStatus.OK.toString();
  }

  private String getTimeStamp(LastModified lastModified) {
    Instant instant = Instant.ofEpochSecond(lastModified.getSeconds(), lastModified.getNanos());
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toString();
  }
}
