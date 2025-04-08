package com.project.tripmate.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CoursePlaceDTO {
    private final Long id;
    private final Long courseDayId;
    private final String placeName;
    private final String contentTypeId;
    private final LocalTime visitStartTime;
    private final LocalTime visitEndTime;
    private final double mapX;
    private final double mapY;
    private final String phoneNumber;
    private final String memo;
}
