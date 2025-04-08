package com.project.tripmate.course.domain;

import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CoursePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 다:1 관계 - 한 courseDay에 여러 장소가 연결됨
    @Setter
    @ManyToOne
    @JoinColumn(name = "course_day_id")
    private CourseDay courseDay;

    // 장소 이름
    private String placeName;

    // 장소 타입 (식당, 관광지 등)
    private String contentTypeId;

    // 방문 시간
    private LocalTime visitStartTime;
    private LocalTime visitEndTime;

    // 장소 좌표
    private double mapX; // longitude
    private double mapY; // latitude

    // 전화번호
    private String phoneNumber;

    // 메모
    @Column(length = 1000)
    private String memo;

    public void updateCoursePlace(
            String placeName,
            String contentTypeId,
            LocalTime visitStartTime,
            LocalTime visitEndTime,
            double mapX,
            double mapY,
            String phoneNumber,
            String memo
    ) {
        this.placeName = placeName;
        this.contentTypeId = contentTypeId;
        this.visitStartTime = visitStartTime;
        this.visitEndTime = visitEndTime;
        this.mapX = mapX;
        this.mapY = mapY;
        this.phoneNumber = phoneNumber;
        this.memo = memo;
    }


}
