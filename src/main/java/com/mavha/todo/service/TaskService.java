package com.mavha.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;
import com.mavha.todo.service.criteria.TaskCriteria;

import javassist.NotFoundException;

public interface TaskService {

    /**
     * Create a pending task.
     *
     * @param description the description of the task.
     * @param picture the picture content.
     * @param pictureContentType the picture content type.
     * @return the persisted task.
     */
    Task save(String description, byte[] picture, String pictureContentType);
    
    /**
     * Get a page of tasks.
     * 
     * @param criteria the filter criteria information
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Task> findAll(TaskCriteria criteria, Pageable pageable);
    
    /**
     * Update the status of a task.
     *
     * @param id the id of the task
     * @param newStatus the new status of the task 
     * @return the updated task
     * @throws NotFoundException, IllegalArgumentException 
     */
    Task updateStatus(Long id, TaskStatus newStatus) throws NotFoundException, IllegalArgumentException;
    
}
