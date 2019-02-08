package com.mavha.todo.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;
import com.mavha.todo.repository.TaskRepository;
import com.mavha.todo.service.TaskService;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Create a pending task.
     *
     * @param description the description of the task.
     * @param picture the picture content.
     * @param pictureContentType the picture content type.
     * @return the persisted task.
     */
	public Task save(String description, byte[] picture, String pictureContentType) {
        log.debug("Request to save Task. Description: %s", description);
		Task task = new Task();
		task.description(description).picture(picture).pictureContentType(pictureContentType);
		task.status(TaskStatus.PENDING);
		return taskRepository.save(task);
	}

}
