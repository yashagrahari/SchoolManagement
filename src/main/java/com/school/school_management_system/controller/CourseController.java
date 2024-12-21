package com.school.school_management_system.controller;

import com.school.school_management_system.dto.CourseDto;
import com.school.school_management_system.models.Course;
import com.school.school_management_system.models.Teacher;
import com.school.school_management_system.repository.CourseRepository;
import com.school.school_management_system.repository.TeacherRepository;
import com.school.school_management_system.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    private CourseService courseService;



    public CourseController(CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseRepository.findAll().stream()
                .map(course -> new CourseDto(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getTeacher() != null ? course.getTeacher().getId() : null
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
//
//        // Map the Course entity to CourseDto
//        CourseDto courseDto = new CourseDto(
//                course.getId(),
//                course.getName(),
//                course.getDescription(),
//                course.getTeacher() != null ? course.getTeacher().getId() : null // Handle null teacher
//        );
//
//        return ResponseEntity.ok(courseDto);
//    }

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody Course course, Authentication authentication) {
        String username = authentication.getName();

//        Teacher teacher = teacherRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Teacher not found for username: " + username));
//
//        course.setTeacher(teacher);

        Course savedCourse = courseRepository.save(course);

        return ResponseEntity.ok(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        Course updatedCourse = courseService.updateCourse(id, courseDetails);
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
//
//        course.setName(courseDetails.getName());
//        course.setDescription(courseDetails.getDescription());
//
//        Course updatedCourse = courseRepository.save(course);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
//        Course course = courseRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
//
//        courseRepository.delete(course);
        return ResponseEntity.ok("Course deleted successfully.");
    }
}

