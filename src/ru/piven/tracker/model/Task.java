package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;

public class Task {

    String name;
    String description;
    Integer id;
    Status status;

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
