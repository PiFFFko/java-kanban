package ru.piven.tracker.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.server.enums.HttpCode;
import ru.piven.tracker.server.enums.HttpMethod;
import ru.piven.tracker.server.response.ErrorResponse;
import ru.piven.tracker.server.response.SuccessResponse;
import ru.piven.tracker.service.taskmanagers.TaskManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class HttpEpicHandler extends HttpRequestHandler {

    public HttpEpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleHttpExchange(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if (HttpMethod.GET.name().equals(requestMethod)) {
            handleGetHttpExchange(httpExchange);
            return;
        }
        if (HttpMethod.POST.name().equals(requestMethod)) {
            handlePostHttpExchange(httpExchange);
            return;
        }
        if (HttpMethod.DELETE.name().equals(requestMethod)) {
            handleDeleteHttpExchange(httpExchange);
            return;
        }
        writeResponse(httpExchange, HttpCode.METHOD_NOT_ALLOWED.getCode(), new ErrorResponse("Доступны только методы GET, POST, DELETE"));
    }

    @Override
    protected void handleGetHttpExchange(HttpExchange httpExchange) throws IOException {
        Optional<String> query = Optional.ofNullable(httpExchange.getRequestURI().getQuery());
        //Если query пустой, значит строка запроса вида tasks/task, соответственно возвращаем все таски
        if (query.isEmpty()) {
            writeResponse(httpExchange, HttpCode.SUCCESS.getCode(), taskManager.getAllEpics());
            return;
        }
        Optional<Integer> epicId = Optional.ofNullable(Integer.parseInt(getParamFromQuery(query.get()).get("id")));
        if (epicId.isEmpty()) {
            writeResponse(httpExchange, HttpCode.BAD_REQUEST.getCode(), new ErrorResponse("Неправильно задан запрос."));
            return;
        }
        Optional<Task> task = Optional.ofNullable(taskManager.getEpic(epicId.get()));
        if (task.isPresent()) {
            writeResponse(httpExchange, HttpCode.SUCCESS.getCode(), task);
        } else {
            writeResponse(httpExchange, HttpCode.BAD_REQUEST.getCode(), new ErrorResponse("Эпика с таким идентификатором не сущесвует"));
        }
    }

    @Override
    protected void handlePostHttpExchange(HttpExchange httpExchange) throws IOException {
        try (InputStream inputStream = httpExchange.getRequestBody();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            Epic epic = gson.fromJson(bufferedReader, Epic.class);
            taskManager.addEpic(epic);
            writeResponse(httpExchange, HttpCode.CREATED.getCode(), new SuccessResponse("Эпик успешно добавлен!"));
        }
    }

    @Override
    protected void handleDeleteHttpExchange(HttpExchange httpExchange) throws IOException {
        Optional<String> query = Optional.ofNullable(httpExchange.getRequestURI().getQuery());
        if (query.isEmpty()) {
            taskManager.removeAllEpics();
            writeResponse(httpExchange, HttpCode.SUCCESS.getCode(), new SuccessResponse("Эпики успешно удалены!"));
            return;
        }
        Optional<Integer> epicId = Optional.ofNullable(Integer.parseInt(getParamFromQuery(query.get()).get("id")));
        if (epicId.isEmpty()) {
            writeResponse(httpExchange, HttpCode.BAD_REQUEST.getCode(), new ErrorResponse("Неправильно задан запрос."));
            return;
        }
        if (taskManager.removeEpic(epicId.get())) {
            writeResponse(httpExchange, HttpCode.SUCCESS.getCode(), new SuccessResponse(String.format("Эпик с id %s успешно удалена", epicId.get())));
        } else {
            writeResponse(httpExchange, HttpCode.BAD_REQUEST.getCode(), new ErrorResponse("Эпика с таким идентификатором не сущесвует"));
        }
    }
}
