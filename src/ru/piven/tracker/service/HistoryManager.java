package ru.piven.tracker.service;

import ru.piven.tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    public void add(Task task);

    public List<Task> getHistory();

}
