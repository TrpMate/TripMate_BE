package com.project.tripmate.course.repository;

import com.project.tripmate.course.domain.CourseDay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDayRepository extends JpaRepository<CourseDay, Long> {
    List<CourseDay> findByCourseId(Long courseId);
}
