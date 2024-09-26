package com.project.tripmate.tourAPI.repository;

import com.project.tripmate.tourAPI.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

