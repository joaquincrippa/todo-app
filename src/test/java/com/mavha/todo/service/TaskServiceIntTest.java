package com.mavha.todo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mavha.todo.TodoAppApplication;
import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;
import com.mavha.todo.repository.TaskRepository;

import javassist.NotFoundException;

/**
 * Integration Test class for TaskService.
 *
 * @see TaskService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoAppApplication.class)
public class TaskServiceIntTest {
	
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";

    private static final byte[] DEFAULT_PICTURE = (DEFAULT_DESCRIPTION).getBytes();

    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;
    
    private Task task;
	
    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        taskService.save(DEFAULT_DESCRIPTION, DEFAULT_PICTURE, DEFAULT_PICTURE_CONTENT_TYPE);

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(testTask.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testTask.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();

        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Description must not be null");
        
        taskService.save(null, DEFAULT_PICTURE, DEFAULT_PICTURE_CONTENT_TYPE);
        
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }
    
    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        
    	taskService.save(DEFAULT_DESCRIPTION, DEFAULT_PICTURE, DEFAULT_PICTURE_CONTENT_TYPE);
    	
    	assertThat(taskService.findAll(null, PageRequest.of(0, 20)).getTotalElements()).isEqualTo(databaseSizeBeforeTest + 1);
    }
    
    @Test
    @Transactional
    public void checkStatusIsUpdated() throws Exception {
    	task = taskService.save(DEFAULT_DESCRIPTION, DEFAULT_PICTURE, DEFAULT_PICTURE_CONTENT_TYPE);
        taskService.updateStatus(task.getId(), TaskStatus.RESOLVED);
        assertThat(taskRepository.getOne(task.getId()).getStatus()).isEqualTo(TaskStatus.RESOLVED);
    }
    
    @Test
    @Transactional
    public void checkStatusIsRequiredWhenChangeStatus() throws Exception {
    	task = taskService.save(DEFAULT_DESCRIPTION, DEFAULT_PICTURE, DEFAULT_PICTURE_CONTENT_TYPE);
    	
    	exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Status must not be null");
        
        taskService.updateStatus(task.getId(), null);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
    }
    
    @Test
    @Transactional
    public void checkTaskExistsWhenChangeStatus() throws Exception {
    	task = taskService.save(DEFAULT_DESCRIPTION, DEFAULT_PICTURE, DEFAULT_PICTURE_CONTENT_TYPE);
    	
    	exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage("The resource does not exist");
        
        taskService.updateStatus(-1L, TaskStatus.PENDING);
    }
    
    @Test
    @Transactional
    public void checkInitialStatusIsPending() throws Exception {
    	task = taskService.save(DEFAULT_DESCRIPTION, DEFAULT_PICTURE, DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(taskRepository.getOne(task.getId()).getStatus()).isEqualTo(TaskStatus.PENDING);
    }
    
}
