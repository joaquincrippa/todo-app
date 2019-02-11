package com.mavha.todo.web.rest.dto;

import java.io.Serializable;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import com.mavha.todo.domain.enumeration.TaskStatus;

/**
 * A DTO for the Task entity.
 */
@SuppressWarnings("serial")
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private TaskStatus status;

    @Lob
    private byte[] picture;
    private String pictureContentType;

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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}
