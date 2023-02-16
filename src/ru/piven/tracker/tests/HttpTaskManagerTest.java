package ru.piven.tracker.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.server.KVServer;
import ru.piven.tracker.service.taskmanagers.HttpTaskManager;
import ru.piven.tracker.service.taskmanagers.TaskManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager("http://localhost");
        task1 = new Task("Task1", "Task1Description");
        task2 = new Task("Task2", "Task2Description");
        subTask1 = new SubTask("SubTask1", "SubTask1Description");
        subTask2 = new SubTask("SubTask2", "SubTask2Description");
        subTask3 = new SubTask("SubTask3", "SubTask3Description");
        epic1 = new Epic("Epic1", "Epic1Description");
        epic2 = new Epic("Epic2", "Epic2Description");
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    void saveTaskToServerAndLoadTaskFromServer() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        TaskManager httpTaskManager = new HttpTaskManager("http://localhost");
        assertEquals(task1, httpTaskManager.getTask(1));
    }

    @Test
    void saveBlankEpicToFileAndLoadEpicFromFile() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        TaskManager httpTaskManager = new HttpTaskManager("http://localhost");
        assertEquals(epic1, httpTaskManager.getEpic(1));
    }

}
