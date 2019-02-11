package com.mavha.todo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mavha.todo.TodoAppApplication;
import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;
import com.mavha.todo.repository.TaskRepository;
import com.mavha.todo.service.TaskService;
import com.mavha.todo.web.rest.dto.TaskDTO;
import com.mavha.todo.web.rest.mapper.TaskMapper;

/**
 * Integration Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoAppApplication.class)
public class TaskResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.PENDING;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.RESOLVED;

    private static final byte[] DEFAULT_PICTURE = (DEFAULT_DESCRIPTION).getBytes();

    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";

    
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private Validator validator;

    private MockMvc restTaskMockMvc;

    private Task task;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskResource taskResource = new TaskResource(taskService, taskMapper);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setValidator(validator).build();
    }

    public static Task createEntity() {
        Task task = new Task()
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
        return task;
    }

    @Before
    public void initTest() {
        task = createEntity();
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        TaskDTO taskDTO = taskMapper.toDto(task);
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(taskDTO)))
            .andExpect(status().isCreated());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTask.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testTask.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc.perform(post("/api/tasks")
        	.contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setDescription(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        taskRepository.saveAndFlush(task);

        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }
    
    @Test
    @Transactional
    public void getTask() throws Exception {
        task = taskRepository.saveAndFlush(task);
        
        //Not implemented
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void getAllTasksByDescriptionIsEqualToSomething() throws Exception {
        taskRepository.saveAndFlush(task);
        defaultTaskShouldBeFound("description=" + DEFAULT_DESCRIPTION);
        defaultTaskShouldNotBeFound("description=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTasksByDescriptionContainsShouldWork() throws Exception {
        taskRepository.saveAndFlush(task);
        defaultTaskShouldBeFound("description=" + DEFAULT_DESCRIPTION.substring(3));
        defaultTaskShouldBeFound("description=" + DEFAULT_DESCRIPTION.substring(2));
    }

    @Test
    @Transactional
    public void getAllTasksByStatusIsEqualToSomething() throws Exception {
        taskRepository.saveAndFlush(task);

        defaultTaskShouldBeFound("status=" + DEFAULT_STATUS);

        defaultTaskShouldNotBeFound("status=" + UPDATED_STATUS);
    }

    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }


    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
   }


    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // It is not implemented
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
    	restTaskMockMvc.perform(put("/api/tasks")
            .content(mapper.writeValueAsBytes(null)))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Transactional
    public void updateNonExistingTask() throws Exception {
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc.perform(put("/api/tasks")
            .content(mapper.writeValueAsBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId()))
            .andExpect(status().isMethodNotAllowed());
    }

}
