package com.school.school_management_system.controller;

import com.school.school_management_system.dto.CourseDto;
import com.school.school_management_system.dto.MessageResponse;
import com.school.school_management_system.models.Course;
import com.school.school_management_system.models.Student;
import com.school.school_management_system.repository.CourseRepository;
import com.school.school_management_system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students/courses")
public class StudentCourseController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentCourseController(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // Enroll a student in a course
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollStudentInCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));

        course.getStudents().add(student);

        courseRepository.save(course);

        return ResponseEntity.ok(new MessageResponse("Student enrolled successfully in the course!"));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentCourses(@PathVariable Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

//        Set<Course> courses = student.getCourses();
        Set<CourseDto> courses = student.getCourses().stream()
                .map(course -> new CourseDto(course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getTeacher() != null ? course.getTeacher().getId() : null))
                .collect(Collectors.toSet());

        return ResponseEntity.ok(courses);
    }
}
