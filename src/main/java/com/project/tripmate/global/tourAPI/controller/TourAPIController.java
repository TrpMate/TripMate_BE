package com.project.tripmate.global.tourAPI.controller;

import com.project.tripmate.global.tourAPI.service.TourAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tourAPI")
public class TourAPIController {

    private final TourAPIService tourAPIService;

    // 1. 위치 기반 관광 정보 조회 엔드포인트+++++++
    @GetMapping("/tour-locationBasedList")
    public String getLocationBasedList(
            @RequestParam double mapX,
            @RequestParam double mapY,
            @RequestParam int radius,
            @RequestParam String arrange,
            @RequestParam(required = false) String contentTypeId,
            @RequestParam int numOfRows,
            @RequestParam int pageNo) {
        return tourAPIService.getLocationBasedList(mapX, mapY, radius, arrange, contentTypeId, numOfRows, pageNo);
    }

    // 2. 키워드 검색 조회
    @GetMapping("/tour-searchKeyword")
    public String getSearchKeyword(
            @RequestParam int numOfRows,
            @RequestParam int pageNo,
            @RequestParam String keyword,
            @RequestParam String arrange,
            @RequestParam(required = false) String contentTypeId) {
        return tourAPIService.searchKeyword(numOfRows, pageNo, keyword, arrange, contentTypeId);
    }

    // 5. 관광 상세 정보 조회 엔드포인트
    @GetMapping("/tour-detailCommon")
    public String getDetailCommon(
            @RequestParam String contentId,
            @RequestParam String contentTypeId) {
        return tourAPIService.getDetailCommon(contentId, contentTypeId);
    }

}
