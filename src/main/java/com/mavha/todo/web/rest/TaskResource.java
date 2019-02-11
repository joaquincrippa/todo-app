package com.mavha.todo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mavha.todo.domain.Task;
import com.mavha.todo.service.TaskService;
import com.mavha.todo.service.criteria.TaskCriteria;
import com.mavha.todo.web.rest.dto.TaskDTO;
import com.mavha.todo.web.rest.dto.TaskDTOStatusOnly;
import com.mavha.todo.web.rest.mapper.TaskMapper;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api")
@CrossOrigin(
		origins = "*",
		maxAge = 3600,
		exposedHeaders = {"current-page", "total-items", "total-items"})
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
    
    /**
     * GET  /tasks : get all the tasks.
     *
     * @param criteria the filter criteria information
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks(TaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tasks: {}", pageable);
        Page<TaskDTO> page = taskService.findAll(criteria, pageable).map(taskMapper::toDto);
        HttpHeaders headers = generatePaginationHttpHeaders(page, "/api/tasks");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * PATCH  /tasks : only update the status of a task.
     * the ResponseEntity with status 201 (Created) and with body the updated taskDTO,
     * with status 404 (Not Found) if the task does not exist,
     * with status 400 (Bad Request) if the status is not either PENDING or REALIZED
     *
     * @param entityWithNewStatus an object with the new status of the task
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @PatchMapping("/tasks")
    public ResponseEntity<TaskDTO> updateTaskStatus(@Valid @RequestBody TaskDTOStatusOnly entityWithNewStatus){
    	try {
			Task task = taskService.updateStatus(entityWithNewStatus.getId(), entityWithNewStatus.getStatus());
			return ResponseEntity.ok().body(taskMapper.toDto(task));
		} catch (NotFoundException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
    
    private <T> HttpHeaders generatePaginationHttpHeaders(Page<T> page, String baseUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("total-count", Long.toString(page.getTotalElements()));
        headers.add("current-page", Integer.toString(page.getNumber()));
        headers.add("total-pages", Integer.toString(page.getTotalPages()));
        return headers;
    }
}
