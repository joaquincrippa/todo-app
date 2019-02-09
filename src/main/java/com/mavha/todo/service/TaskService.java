package com.mavha.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mavha.todo.domain.Task;

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
     * Get all the tasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Task> findAll(Pageable pageable);

    
}
