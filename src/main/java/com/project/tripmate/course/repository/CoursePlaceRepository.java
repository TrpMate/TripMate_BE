package com.project.tripmate.course.repository;

import com.project.tripmate.course.domain.CoursePlace;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePlaceRepository extends JpaRepository<CoursePlace, Long> {
    List<CoursePlace> findByCourseDayId(Long courseDayId);
}
