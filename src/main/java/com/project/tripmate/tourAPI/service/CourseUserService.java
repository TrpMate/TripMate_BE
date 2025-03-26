package com.project.tripmate.tourAPI.service;

import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.domain.CourseUser;
import com.project.tripmate.tourAPI.repository.CourseRepository;
import com.project.tripmate.tourAPI.repository.CourseUserRepository;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseUserService(CourseUserRepository courseUserRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.courseUserRepository = courseUserRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public CourseUser createCourseUser(Long courseId, Long userId) {
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<User> user = userRepository.findById(userId);
        if (course.isPresent() && user.isPresent()) {
            CourseUser courseUser = CourseUser.builder()
                    .course(course.get())
                    .user(user.get())
                    .joinedDate(LocalDateTime.now())
                    .build();
            return courseUserRepository.save(courseUser);
        }
        return null;
    }

    public List<Long> getCourseIdListByUserId(Long userId) {
        return courseUserRepository.findCourseIdsByUserId(userId);
    }

    public Optional<CourseUser> getCourseUserById(Long id) {
        return courseUserRepository.findById(id);
    }

    public List<CourseUser> getAllCourseUsers() {
        return courseUserRepository.findAll();
    }

    public CourseUser updateCourseUser(Long id, Long courseId, Long userId) {
        Optional<CourseUser> existingCourseUser = courseUserRepository.findById(id);
        if (existingCourseUser.isPresent()) {
            CourseUser courseUser = existingCourseUser.get();
            Optional<Course> course = courseRepository.findById(courseId);
            Optional<User> user = userRepository.findById(userId);
            if (course.isPresent() && user.isPresent()) {
                courseUser.setCourseAndUser(course.get(), user.get());
                return courseUserRepository.save(courseUser);
            }
        }
        return null;
    }

    public void deleteCourseUser(Long id) {
        courseUserRepository.deleteById(id);
    }
}
