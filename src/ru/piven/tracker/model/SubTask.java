package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.taskmanagers.TaskType;

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
        return String.format("%s;%s;%s;%s;%s;%s", type, id, name, status, description, epicId);
    }
}

