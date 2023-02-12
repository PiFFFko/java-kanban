package ru.piven.tracker.server;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.taskmanagers.HttpTaskManager;

import java.io.IOException;

public class kvServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost");
        Task task1 = new Task("Task1", "Task1Description", "23.01.2023 15:45", "PT20M");
        Task task2 = new Task("Task2", "Task2Description");
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description");
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2Description");
        SubTask subTask3 = new SubTask("SubTask3", "SubTask3Description");
        Epic epic1 = new Epic("Epic1", "Epic1Description");
        Epic epic2 = new Epic("Epic2", "Epic2Description");
        httpTaskManager.addTask(task1);
        httpTaskManager.addTask(task2);
        httpTaskManager.addEpic(epic1);
        httpTaskManager.addEpic(epic2);
        httpTaskManager.addSubTask(subTask1, 3);
        httpTaskManager.addSubTask(subTask2, 3);
        httpTaskManager.addSubTask(subTask3, 3);
        httpTaskManager.getTask(1);
        httpTaskManager.getTask(2);
        httpTaskManager.getSubTask(5);
        httpTaskManager.getSubTask(6);
        HttpTaskManager httpTaskManager1 = new HttpTaskManager("http://localhost");
        httpTaskManager1.getAllTasks();
    }
}
