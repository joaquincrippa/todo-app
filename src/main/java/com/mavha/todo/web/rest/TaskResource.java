package com.mavha.todo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mavha.todo.domain.Task;
import com.mavha.todo.service.TaskService;
import com.mavha.todo.web.rest.dto.TaskDTO;
import com.mavha.todo.web.rest.mapper.TaskMapper;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private final TaskService taskService;
    
    private final TaskMapper taskMapper;
    
    public TaskResource (TaskService taskService, TaskMapper taskMapper) {
    	this.taskService = taskService;
    	this.taskMapper = taskMapper;
    }
    
    /**
     * POST  /tasks : Create a new task.
     *
     * @param taskDTO the taskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskDTO, or with status 400 (Bad Request) if the task has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) throws URISyntaxException {
        log.debug("REST request to save Task : {}", taskDTO);
        if (taskDTO.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
            
        }
        Task task = taskService.save(taskDTO.getDescription(), taskDTO.getPicture(), taskDTO.getPictureContentType());
        TaskDTO result = taskMapper.toDto(task);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId())).body(result);
    }

}
