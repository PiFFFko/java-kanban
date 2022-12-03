package ru.piven.tracker.service;

import ru.piven.tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 10;

    public List<Task> getHistory() {
        return history;
    }

    public void add(Task task) {
        if (history.size() >= MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(task);
    }
}
