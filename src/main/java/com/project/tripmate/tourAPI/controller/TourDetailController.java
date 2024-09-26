package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.tourAPI.service.TourDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TourDetailController {

    private final TourDetailService tourDetailService;

    @GetMapping("/tour-detail")
    public String getTourDetail(@RequestParam String contentId, @RequestParam String contentTypeId) {
        return tourDetailService.getTourDetail(contentId, contentTypeId);
    }
}
