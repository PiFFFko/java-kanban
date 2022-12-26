package ru.piven.tracker.service.history;

import ru.piven.tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

}
