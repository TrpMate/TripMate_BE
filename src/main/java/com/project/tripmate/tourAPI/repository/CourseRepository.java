package com.project.tripmate.tourAPI.repository;

import com.project.tripmate.tourAPI.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // 공개 코스를 이름으로 검색하는 메서드
    List<Course> findByCourseNameContainingAndIsPublicTrue(String courseName);
}
