package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.service.history.HistoryManager;
import ru.piven.tracker.service.history.InMemoryHistoryManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    private static final Path defaultFile = Paths.get(System.getProperty("user.dir"), "data", "data.csv");
    private static final String defaultURL = "http://localhost";
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTaskManager(){
        return new FileBackedTaskManager(defaultFile);
    }

    public static TaskManager getHttpTaskManager() throws IOException, InterruptedException {
        return new HttpTaskManager(defaultURL);
    }

}
