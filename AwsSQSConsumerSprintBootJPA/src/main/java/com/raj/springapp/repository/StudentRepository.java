package com.raj.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raj.springapp.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
