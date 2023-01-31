package ru.piven.tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.taskmanagers.FileBackedTaskManager;
import ru.piven.tracker.service.taskmanagers.TaskManager;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private Path file = Paths.get(System.getProperty("user.dir"), "data", "data.csv");

    @BeforeEach
    void setUp() {
        taskManager = new FileBackedTaskManager(file);
        task1 = new Task("Task1", "Task1Description");
        task2 = new Task("Task2", "Task2Description");
        subTask1 = new SubTask("SubTask1", "SubTask1Description");
        subTask2 = new SubTask("SubTask2", "SubTask2Description");
        subTask3 = new SubTask("SubTask3", "SubTask3Description");
        epic1 = new Epic("Epic1", "Epic1Description");
        epic2 = new Epic("Epic2", "Epic2Description");
    }

    @Test
    void saveAndLoadWithEmptyTasksAndHistory(){
        taskManager.save();
        FileBackedTaskManager fileTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(fileTaskManager.getAllTasks().isEmpty());
        assertTrue(fileTaskManager.getHistory().isEmpty());
    }

    @Test
    void saveTaskToFileAndLoadTaskFromFile() {
        taskManager.addTask(task1);
        FileBackedTaskManager fileTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(task1,fileTaskManager.getTask(1));
    }

    @Test
    void saveBlankEpicToFileAndLoadEpicFromFile(){
        taskManager.addEpic(epic1);
        FileBackedTaskManager fileTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(epic1,fileTaskManager.getEpic(1));
    }


}
