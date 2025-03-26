package com.project.tripmate.tourAPI.repository;

import com.project.tripmate.tourAPI.domain.CourseUser;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {
    @Query("SELECT cu.course.id FROM CourseUser cu WHERE cu.user.id = :userId")
    List<Long> findCourseIdsByUserId(@Param("userId") Long userId);
}

