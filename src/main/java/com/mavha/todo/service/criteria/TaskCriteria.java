package com.mavha.todo.service.criteria;

import org.springframework.data.jpa.domain.Specification;

import com.mavha.todo.domain.Task;
import com.mavha.todo.domain.enumeration.TaskStatus;

/**
 * Criteria class for the Task entity. This class is used in TaskResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * This class is also used in TaskService to get the specification for tasks filtering
 * in TaskRepository.
 * 
 * Filter criteria:
 * ID exact value
 * description like value
 * status exact value
*/
public class TaskCriteria {

	private Long id;
	
	private String description;
	
	private TaskStatus status;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
    public String toString() {
        return "TaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
            "}";
    }
    
    /**
     * Get the criteria specification.
     * If criteria is not defined, it returns null.
     * 
     * @return the specification
     */
    public Specification<Task> getSpecification() {
    	if (id == null && description == null && status == null) {
    		return null;
    	}
		Specification<Task> spec = Specification.where(alwaysTrue());
		if(id != null) spec = spec.and(is(id));
		if(description != null) spec = spec.and(descriptionContains(description));
		if(status != null) spec = spec.and(hasStatus(status));
		return spec;
    }
    
	private static Specification<Task> is(Long id) {
	    return (task, cq, cb) -> cb.equal(task.get("id"), id);
	}
	 
	private static Specification<Task> descriptionContains(String description) {
	    return (task, cq, cb) -> cb.like(cb.lower((task.get("description"))), "%" + description.toLowerCase() + "%");
	}
	
	private static Specification<Task> hasStatus(TaskStatus status) {
		return (task, cq, cb) -> cb.equal(task.get("status"), status);
	}
	
	private static Specification<Task> alwaysTrue() {
		return (task, cq, cb) -> cb.and();
	}
}
