package ru.piven.tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.taskmanagers.FileBackedTaskManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class InMemoryTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{

    @BeforeEach
    void setUp() {
        Path file = Paths.get(System.getProperty("user.dir"), "data", "data.csv");
        taskManager = new FileBackedTaskManager(file);
        task1 = new Task("Task1", "Task1Description");
        task2 = new Task("Task2", "Task2Description");
        subTask1 = new SubTask("SubTask1", "SubTask1Description");
        subTask2 = new SubTask("SubTask2", "SubTask2Description");
        subTask3 = new SubTask("SubTask3", "SubTask3Description");
        epic1 = new Epic("Epic1", "Epic1Description");
        epic2 = new Epic("Epic2", "Epic2Description");
    }




}
