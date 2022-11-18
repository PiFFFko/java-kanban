package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subTasksIds;

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public void addSubTask(Integer id) {
        subTasksIds.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksIds=" + subTasksIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
