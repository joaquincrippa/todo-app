package com.mavha.todo.service;

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
    
}
