package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public SubTask(String name, String description,Status status) {
        super(name, description,status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}

