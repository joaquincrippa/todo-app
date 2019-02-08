package com.mavha.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mavha.todo.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
}