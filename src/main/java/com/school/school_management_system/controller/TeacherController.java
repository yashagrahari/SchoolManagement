package com.school.school_management_system.controller;

import com.school.school_management_system.models.Teacher;
import com.school.school_management_system.repository.TeacherRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    // Get all teachers
    @GetMapping("/")
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // Get a teacher by ID
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            return ResponseEntity.ok(teacher.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    // Create a new teacher
//    @PostMapping
//    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
//        Teacher savedTeacher = teacherRepository.save(teacher);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
//    }

    // Update teacher details
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @Valid @RequestBody Teacher teacherDetails) {
        Optional<Teacher> existingTeacher = teacherRepository.findById(id);
        if (existingTeacher.isPresent()) {
            Teacher teacher = existingTeacher.get();
            teacher.setName(teacherDetails.getName());
            teacher.setEmail(teacherDetails.getEmail());
            // Update other fields as necessary
            teacherRepository.save(teacher);
            return ResponseEntity.ok(teacher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a teacher
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        Optional<Teacher> existingTeacher = teacherRepository.findById(id);
        if (existingTeacher.isPresent()) {
            teacherRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
