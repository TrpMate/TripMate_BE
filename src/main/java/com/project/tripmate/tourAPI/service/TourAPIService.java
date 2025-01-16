package com.project.tripmate.tourAPI.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;

@Service
public class TourAPIService {

    @Value("${tourAPI.apiKey}")
    private String apiKey;

//    @Value("${tourAPI.baseUrl}")
//    private String baseUrl;

    // 1. 위치기반 관광 정보 조회 메서드
    public String getLocationBasedList(double mapX, double mapY, int radius, String arrange, String contentTypeId,
            int numOfRows, int pageNo) {
        RestTemplate restTemplate = new RestTemplate();

        // URL 구성을 위한 UriBuilderFactory
        final var builder = new DefaultUriBuilderFactory();
        builder.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); // 인코딩 비활성화

        // API 호출을 위한 URL 구성
        final String uriString = builder.builder()
                .scheme("http")
                .host("apis.data.go.kr")
                .path("/B551011/KorService1/locationBasedList1")
                .queryParam("serviceKey", apiKey)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .queryParam("_type", "json")
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", radius)
                .queryParam("arrange", arrange)  // 정렬 기준
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .build()
                .toString();

        // URI 객체 생성
        final URI uri = URI.create(uriString);

        System.out.println("Request URL: " + uri); // URL 로그 확인

        // API 호출 및 응답 데이터 받기
        return restTemplate.getForObject(uri, String.class); // JSON 응답 반환
    }

    // 2. 	키워드 검색 조회
    public String searchKeyword(int numOfRows, int pageNo, String keyword, String arrange, String contentTypeId) {
        RestTemplate restTemplate = new RestTemplate();

        // URL 구성을 위한 UriBuilderFactory
        final var builder = new DefaultUriBuilderFactory();
        builder.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); // 인코딩 비활성화

        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        // API 호출을 위한 URL 구성
        final String uriString = builder.builder()
                .scheme("http")
                .host("apis.data.go.kr")
                .path("/B551011/KorService1/searchKeyword1")
                .queryParam("serviceKey", apiKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "AppTest")
                .queryParam("_type", "json")
                .queryParam("listYN", "Y")
                .queryParam("arrange", arrange)
                .queryParam("keyword", encodedKeyword)
                .queryParam("contentTypeId", contentTypeId)
                .build()
                .toString();

        // URI 객체 생성
        final URI uri = URI.create(uriString);

        System.out.println("Request URL: " + uri); // URL 로그 확인

        // API 호출 및 응답 데이터 받기
        return restTemplate.getForObject(uri, String.class); // JSON 응답 반환
    }

    // 5. 관광 상세 정보 조회 메서드
    public String getDetailCommon(String contentId, String contentTypeId) {
        RestTemplate restTemplate = new RestTemplate();

        // URL 구성을 위한 UriBuilderFactory
        final var builder = new DefaultUriBuilderFactory();
        builder.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); // 인코딩 비활성화

        // API 호출을 위한 URL 구성
        final String uriString = builder.builder()
                .scheme("http")
                .host("apis.data.go.kr")
                .path("/B551011/KorService1/detailCommon1")
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
                .queryParam("numOfRows", "10")
                .queryParam("pageNo", "1")
                .build()
                .toString();

        // URI 객체 생성
        final URI uri = URI.create(uriString);

        System.out.println("Request URL: " + uri); // URL 로그 확인

        // API 호출 및 응답 데이터 받기
        return restTemplate.getForObject(uri, String.class); // JSON 응답 반환
    }


}
