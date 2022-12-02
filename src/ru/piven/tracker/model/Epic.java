package ru.piven.tracker.model;

import ru.piven.tracker.service.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description,Status status) {
        super(name, description,status);
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTask(Integer subTaskId) {
        subTasksIds.add(subTaskId);
    }

    public void removeSubTask(Integer subTaskId){
        subTasksIds.remove(subTaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
