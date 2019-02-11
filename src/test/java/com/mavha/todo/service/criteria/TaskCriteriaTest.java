package com.mavha.todo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mavha.todo.TodoAppApplication;

/**
 * Test class for the TaskCriteria.
 *
 * @see TaskCriteria
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoAppApplication.class)
public class TaskCriteriaTest {

	@Test
	public void getSpecificationWithAllFieldsNull() {
		TaskCriteria criteria = new TaskCriteria();
		assertThat(criteria.getSpecification()).isNull();
	}
	
}
