package com.project.tripmate.tourAPI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TourDetailService {

    @Value("${tourAPI.apiKey}")
    private String apiKey;

    private final String baseUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1";

    public String getTourDetail(String contentId, String contentTypeId) {
        RestTemplate restTemplate = new RestTemplate();

        // API 호출을 위한 URL 구성
        String uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .queryParam("_type", "json")
                .queryParam("contentId", contentId)
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("defaultYN", "Y")
                .queryParam("firstImageYN", "Y")
                .queryParam("areacodeYN", "Y")
                .queryParam("catcodeYN", "Y")
                .queryParam("addrinfoYN", "Y")
                .queryParam("mapinfoYN", "Y")
                .queryParam("overviewYN", "Y")
                .toUriString();

        // API 호출 및 응답 데이터 받기
        String response = restTemplate.getForObject(uri, String.class);

        return response;  // JSON 응답 반환
    }
}
