package com.mavha.todo.web.rest.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.mavha.todo.domain.enumeration.TaskStatus;

/**
 * A DTO for the Task entity to update only the status.
 */
@SuppressWarnings("serial")
public class TaskDTOStatusOnly implements Serializable {

    private Long id;

    @NotNull
    private TaskStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
