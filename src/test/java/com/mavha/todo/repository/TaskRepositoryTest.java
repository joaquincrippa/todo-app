package com.mavha.todo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mavha.todo.TodoAppApplication;
import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;
import com.mavha.todo.repository.TaskRepository;

/**
 * Test class for the TaskRepository.
 *
 * @see TaskRepository
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoAppApplication.class)
public class TaskRepositoryTest {

	private static final byte[] DEFAULT_CONTENT_PICTURE = ("iVBORw0KGgoAAAANSUhEUgAAAOEAAADh"
			+ "CAMAAAAJbSJIAAAAA1BMVEUAAACnej3aAAAASElEQVR4nO3BgQAAAADDoPlTX+A"
			+ "IVQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
			+ "AAADwDcaiAAFXD1ujAAAAAElFTkSuQmCC").getBytes();

	private Task task;
	
	@Autowired
	private TaskRepository taskRepository;

	@Before
	public void init() {
		task = new Task();
		task.setDescription("another task");
		task.setPictureContentType("image/png");
		task.setPicture(DEFAULT_CONTENT_PICTURE);
		task.setStatus(TaskStatus.PENDING);
		
		task = taskRepository.save(task);
	}
	
	@Test
	@Transactional
	public void checkUpdatedStatus() {
		int count = taskRepository.updateStatus(TaskStatus.RESOLVED, task.getId());
		assertThat(count).isEqualTo(1);
		task = taskRepository.getOne(task.getId());
		assertThat(task.getStatus()).isEqualTo(TaskStatus.RESOLVED);
	}
	
	@Test
	@Transactional
	public void updateNonExistingTask() {
		int count = taskRepository.updateStatus(TaskStatus.RESOLVED, -1);
		assertThat(count).isEqualTo(0);
		assertThat(taskRepository.findById(-1L).isPresent()).isFalse();
	}

}
