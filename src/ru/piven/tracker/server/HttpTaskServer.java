package ru.piven.tracker.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.piven.tracker.server.handlers.*;
import ru.piven.tracker.service.taskmanagers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final Integer PORT = 8080;

    public static void startTaskServer(TaskManager taskManager, Gson gson) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTasksHandler(taskManager, gson));
        httpServer.createContext("/tasks/history", new HttpHistoryHandler(taskManager, gson));
        httpServer.createContext("/tasks/task", new HttpTaskHandler(taskManager, gson));
        httpServer.createContext("/tasks/subtask", new HttpSubTaskHandler(taskManager, gson));
        httpServer.createContext("/tasks/subtask/epic", new HttpEpicSubtasksHandler(taskManager,gson));
        httpServer.createContext("/tasks/epic", new HttpEpicHandler(taskManager,gson));
 //       httpServer.start();
        System.out.printf("Сервер запущен на %s порту", PORT);
    }

}
