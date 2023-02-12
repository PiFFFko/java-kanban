package ru.piven.tracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.taskmanagers.Managers;
import ru.piven.tracker.service.taskmanagers.TaskManager;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        Task task2 = new Task("Task2", "Task2Description");
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description");
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2Description");
        SubTask subTask3 = new SubTask("SubTask3", "SubTask3Description");
        Epic epic1 = new Epic("Epic1", "Epic1Description");
        Epic epic2 = new Epic("Epic2", "Epic2Description");
        TaskManager taskManager = Managers.getHttpTaskManager();
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubTask(subTask1,3);
        taskManager.addSubTask(subTask2, 3);
        taskManager.addSubTask(subTask3,3);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getSubTask(5);
        taskManager.getSubTask(6);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        HttpTaskServer.startTaskServer(taskManager, gson);
    }
}
