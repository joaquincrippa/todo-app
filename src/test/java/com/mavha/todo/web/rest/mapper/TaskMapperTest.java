package com.mavha.todo.web.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mavha.todo.TodoAppApplication;
import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;
import com.mavha.todo.web.rest.dto.TaskDTO;
import com.mavha.todo.web.rest.mapper.TaskMapper;

/**
 * Test class for the TaskMapper.
 *
 * @see TaskMapper
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoAppApplication.class)
public class TaskMapperTest {

	private static final byte[] DEFAULT_CONTENT_PICTURE = ("AAAAAAAAAAAAAA").getBytes();
	
	
	@Autowired
	private TaskMapper taskMapper;
	
	private TaskDTO taskDto;
	private Task task;
	
	@Before
	public void init() {
		taskDto = new TaskDTO();
		taskDto.setDescription("a task");
		taskDto.setId(1L);
		taskDto.setPictureContentType("image/png");
		taskDto.setPicture(DEFAULT_CONTENT_PICTURE);
		taskDto.setStatus(TaskStatus.PENDING);
		
		task = new Task();
		task.setDescription("another task");
		task.setId(2L);
		task.setPictureContentType("image/png");
		task.setPicture(DEFAULT_CONTENT_PICTURE);
		task.setStatus(TaskStatus.RESOLVED);
	}
	
	@Test
	public void taskDTOToTask() {
		Task task = taskMapper.toEntity(taskDto);
		assertThat(task.getDescription()).isEqualTo(taskDto.getDescription());
		assertThat(task.getId()).isEqualTo(taskDto.getId());
		assertThat(task.getPicture()).isEqualTo(taskDto.getPicture());
		assertThat(task.getPictureContentType()).isEqualTo(taskDto.getPictureContentType());
		assertThat(task.getStatus()).isEqualTo(taskDto.getStatus());
	}
	
	@Test
	public void taskToTaskDTO() {
		TaskDTO taskDto = taskMapper.toDto(task);
		assertThat(task.getDescription()).isEqualTo(taskDto.getDescription());
		assertThat(task.getId()).isEqualTo(taskDto.getId());
		assertThat(task.getPicture()).isEqualTo(taskDto.getPicture());
		assertThat(task.getPictureContentType()).isEqualTo(taskDto.getPictureContentType());
		assertThat(task.getStatus()).isEqualTo(taskDto.getStatus());
	}
	
    @Test
    public void taskDTOToTaskMapWithNullTaskShouldReturnNull(){
        assertThat(taskMapper.toEntity((TaskDTO) null)).isNull();
        assertThat(taskMapper.toEntity((ArrayList<TaskDTO>) null)).isNull();
    }
    
    @Test
    public void taskToTaskDTOMapWithNullTaskShouldReturnNull(){
        assertThat(taskMapper.toDto((Task) null)).isNull();
        assertThat(taskMapper.toDto((ArrayList<Task>) null)).isNull();
    }
}
