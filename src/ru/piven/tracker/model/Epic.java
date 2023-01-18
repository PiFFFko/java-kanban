package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.taskmanagers.TaskType;

import java.util.ArrayList;

public class Epic extends Task {
    private TaskType type = TaskType.EPIC;
    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTasksIds = new ArrayList<>();
    }

    public Epic(Integer id, String name, Status status, String description) {
        super(id, name, status, description);
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTask(Integer subTaskId) {
        subTasksIds.add(subTaskId);
    }

    public void removeSubTask(Integer subTaskId) {
        subTasksIds.remove(subTaskId);
    }

    @Override
    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s", type, id, name, status, description);
    }

}
