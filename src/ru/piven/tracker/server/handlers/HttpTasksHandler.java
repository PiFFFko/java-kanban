package ru.piven.tracker.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.piven.tracker.server.enums.HttpCode;
import ru.piven.tracker.server.enums.HttpMethod;
import ru.piven.tracker.server.response.ErrorResponse;
import ru.piven.tracker.service.taskmanagers.TaskManager;

import java.io.IOException;

public class HttpTasksHandler extends HttpRequestHandler{

    public HttpTasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleHttpExchange(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if(!HttpMethod.GET.name().equals(requestMethod)){
            writeResponse(httpExchange, HttpCode.METHOD_NOT_ALLOWED.getCode(), new ErrorResponse("Доступен только метод GET."));
            return;
        }
        handleGetHttpExchange(httpExchange);
    }

    @Override
    protected void handleGetHttpExchange(HttpExchange httpExchange) throws IOException{
        writeResponse(httpExchange,HttpCode.SUCCESS.getCode(), taskManager.getPrioritizedTasks());
    }

    @Override
    protected void handlePostHttpExchange(HttpExchange httpExchange) {
    }

    @Override
    protected void handleDeleteHttpExchange(HttpExchange httpExchange) {
    }

}
