package com.school.school_management_system.repository;

import com.school.school_management_system.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherCourseRepository extends JpaRepository<Teacher, Long> {
    // Custom query methods can be added here if needed
}
