package com.school.school_management_system.controller;

import com.school.school_management_system.dto.CourseDto;
import com.school.school_management_system.models.Course;
import com.school.school_management_system.models.Teacher;
import com.school.school_management_system.repository.CourseRepository;
import com.school.school_management_system.repository.TeacherCourseRepository;
import com.school.school_management_system.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teachers/courses")
public class TeacherCourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherCourseRepository teacherCourseRepository;

    // Get all courses assigned to the teacher
    @GetMapping("/{teacherId}")
    public ResponseEntity<List<CourseDto>> getTeacherCourses(@PathVariable Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        List<CourseDto> courses = teacher.getCourses().stream()
                .map(course -> new CourseDto(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getTeacher() != null ? course.getTeacher().getId() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(courses);
    }

    // Assign a course to a teacher
    @PostMapping("/assign")
    public ResponseEntity<String> assignCourseToTeacher(@RequestParam Long teacherId, @RequestParam Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + teacherId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        // Check if the course is already assigned to the teacher
        if (teacher.getCourses().contains(course)) {
            return ResponseEntity.badRequest().body("Course is already assigned to this teacher.");
        }

        teacher.getCourses().add(course);
        teacherRepository.save(teacher);
        course.setTeacher(teacher);
        courseRepository.save(course);

        return ResponseEntity.ok("Course successfully assigned to the teacher.");
    }
}

