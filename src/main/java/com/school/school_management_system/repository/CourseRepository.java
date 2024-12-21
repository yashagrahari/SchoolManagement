package com.school.school_management_system.repository;

import com.school.school_management_system.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findById(int id);
}
