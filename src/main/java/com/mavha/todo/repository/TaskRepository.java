package com.mavha.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	@Modifying(clearAutomatically = true)
	@Query("Update Task task set task.status = ?1 where task.id = ?2")
	Integer updateStatus(TaskStatus status, long id);
}