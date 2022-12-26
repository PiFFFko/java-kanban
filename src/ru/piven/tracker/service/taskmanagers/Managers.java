package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.service.history.HistoryManager;
import ru.piven.tracker.service.history.InMemoryHistoryManager;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

}
