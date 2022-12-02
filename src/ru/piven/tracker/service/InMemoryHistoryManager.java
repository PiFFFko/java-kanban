package ru.piven.tracker.service;

import ru.piven.tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();

    public List<Task> getHistory() {
        return history;
    }

    public void add(Task task) {
        if (history.size() >= 10){
            history.remove(0);
            history.add(task);
            return;
        } else history.add(task);
    }
}
