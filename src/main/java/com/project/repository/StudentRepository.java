package com.project.repository;

import com.project.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Student entities.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}

