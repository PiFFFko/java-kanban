package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.taskmanagers.TaskType;

public class Task {

    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;
    protected TaskType type = TaskType.TASK;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, Status status, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s", type, id, name, status, description);
    }
}
