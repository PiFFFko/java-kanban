package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.service.history.HistoryManager;
import ru.piven.tracker.service.history.InMemoryHistoryManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    private static final String defaultURL = "http://localhost";
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(defaultURL);
    }


}
