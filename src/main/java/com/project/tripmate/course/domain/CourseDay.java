package com.project.tripmate.course.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CourseDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int dayNum;

    private LocalDate dayDate;

    public void setDayNumAndDate(int dayNum, LocalDate dayDate) {
      this.dayNum = dayNum;
      this.dayDate = dayDate;
    };

}
