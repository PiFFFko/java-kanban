package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.taskmanagers.TaskType;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static ru.piven.tracker.service.taskmanagers.FileBackedTaskManager.IF_TIME_NOT_SET;

public class SubTask extends Task {
    private TaskType type = TaskType.SUBTASK;
    private int epicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    public SubTask(Integer id, String name, Status status, String description, int epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
    }

    public SubTask(Integer id, String name, Status status, String description, String startTime, String duration, int epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, String startTime, String duration) {
        super(name, description, startTime, duration);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        String startTimeStr = startTime.isEmpty() ? IF_TIME_NOT_SET : startTime.get().format(DATE_TIME_FORMATTER);
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s", type, id, name, status, description, startTimeStr, getDuration().toString(),epicId);
    }
}

