package com.project.tripmate.tourAPI.repository;

import com.project.tripmate.tourAPI.domain.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {
}
