package com.project.tripmate.tourAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CoursePlaceDTO {
    private final Long id;
    private final Long courseDayId;
    private final String contentId;
    private final String contentTypeId;
    private final LocalDateTime placeTime;
    private final int courseOrder;
}
