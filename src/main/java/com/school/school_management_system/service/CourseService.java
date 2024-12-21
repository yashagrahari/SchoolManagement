package com.school.school_management_system.service;

import com.school.school_management_system.dto.CourseDto;
import com.school.school_management_system.models.Course;
import com.school.school_management_system.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Cacheable(value = "courses", key = "#id")
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));

        return new CourseDto(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getTeacher() != null ? course.getTeacher().getId() : null
        );
    }

    @CacheEvict(value = "courses", key = "#id")
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
        courseRepository.delete(course);
    }

    @CacheEvict(value = "courses", key = "#id")
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));

        course.setName(courseDetails.getName());
        course.setDescription(courseDetails.getDescription());
        return courseRepository.save(course);
    }
}
