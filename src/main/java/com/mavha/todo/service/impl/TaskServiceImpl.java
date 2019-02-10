package com.mavha.todo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;
import com.mavha.todo.repository.TaskRepository;
import com.mavha.todo.service.TaskService;

import javassist.NotFoundException;

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

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
	@Transactional(readOnly = true)
	public Page<Task> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable);
	}

    /**
     * Update the status of a task.
     *
     * @param id the id of the task
     * @param newStatus the new status of the task 
     * @return the updated task
     * @throws NotFoundException, IllegalArgumentException 
     */
	public Task updateStatus(Long id, TaskStatus newStatus) throws NotFoundException, IllegalArgumentException {
		log.debug("Request to update the status of a task | id: %s, status: %s", id, newStatus);
		if(newStatus == null) {
			throw new IllegalArgumentException("Status must not be null");
		}
		Integer updatedCount = taskRepository.updateStatus(newStatus, id);
		if(updatedCount == 0) {
			throw new NotFoundException("The resource does not exist");
		}
		return taskRepository.getOne(id);
	}
}
