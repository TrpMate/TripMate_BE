package com.project.tripmate.course.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoursePlaceRequestDTO {
    private Long courseDayId;
    private String placeName;
    private String contentTypeId;
    private LocalTime visitStartTime;
    private LocalTime visitEndTime;
    private double mapX;
    private double mapY;
    private String phoneNumber;
    private String memo;
}
