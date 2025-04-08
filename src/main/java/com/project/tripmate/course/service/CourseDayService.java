package com.project.tripmate.course.service;

import com.project.tripmate.course.domain.Course;
import com.project.tripmate.course.domain.CourseDay;
import com.project.tripmate.course.domain.CoursePlace;
import com.project.tripmate.course.dto.CourseDayAndPlaceDTO;
import com.project.tripmate.course.dto.CourseDayDTO;
import com.project.tripmate.course.dto.CoursePlaceDTO;
import com.project.tripmate.course.mapper.CourseDayAndPlaceMapper;
import com.project.tripmate.course.repository.CourseDayRepository;
import com.project.tripmate.course.repository.CoursePlaceRepository;
import com.project.tripmate.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseDayService {

    private final CourseDayRepository courseDayRepository;
    private final CourseRepository courseRepository;
    private final CoursePlaceRepository coursePlaceRepository;
    private final CourseDayAndPlaceMapper courseDayAndPlaceMapper;

    public CourseDay createCourseDay(Long courseId, int dayNum, LocalDate dayDate) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            CourseDay courseDay = CourseDay.builder()
                    .course(course.get())
                    .dayNum(dayNum)
                    .dayDate(dayDate)
                    .build();
            return courseDayRepository.save(courseDay);
        }
        return null;
    }

    public Optional<CourseDay> getCourseDayById(Long id) {
        return courseDayRepository.findById(id);
    }

    public List<CourseDay> getAllCourseDays() {
        return courseDayRepository.findAll();
    }

    public List<CourseDay> getCourseDaysByCourseId(Long courseId) {
        return courseDayRepository.findByCourseId(courseId);
    }

    public CourseDay updateCourseDay(Long id, int dayNum, LocalDate dayDate) {
        Optional<CourseDay> existingCourseDay = courseDayRepository.findById(id);
        if (existingCourseDay.isPresent()) {
            CourseDay courseDay = existingCourseDay.get();
            courseDay.setDayNumAndDate(dayNum, dayDate);
            return courseDayRepository.save(courseDay);
        }
        return null;
    }

    public void deleteCourseDay(Long id) {
        courseDayRepository.deleteById(id);
    }

    public Optional<CourseDayAndPlaceDTO> getCourseDayDTOById(Long id) {
        Optional<CourseDay> optionalCourseDay = courseDayRepository.findById(id);

        if (optionalCourseDay.isEmpty()) return Optional.empty();

        CourseDay courseDay = optionalCourseDay.get();
        List<CoursePlace> coursePlaces = coursePlaceRepository.findByCourseDayId(id); // 따로 조회

        return Optional.of(courseDayAndPlaceMapper.toDTO(courseDay, coursePlaces));
    }

}
