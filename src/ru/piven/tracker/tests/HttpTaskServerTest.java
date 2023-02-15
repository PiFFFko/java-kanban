package ru.piven.tracker.tests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.server.HttpTaskServer;
import ru.piven.tracker.server.KVServer;
import ru.piven.tracker.server.enums.HttpCode;
import ru.piven.tracker.service.taskmanagers.Managers;
import ru.piven.tracker.service.taskmanagers.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerTest {

    static Gson gson;
    static HttpClient client;
    protected Task task1;
    protected Task task2;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected SubTask subTask3;
    protected Epic epic1;
    protected Epic epic2;
    TaskManager taskManager;
    KVServer kvServer;
    HttpTaskServer httpTaskServer;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        gson = new Gson();
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        taskManager = Managers.getDefault();
        httpTaskServer.startTaskServer(taskManager, gson);
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
        HttpTaskServer.stop();
        kvServer.stop();
    }


    @Test
    void GETHistory() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        //история пустая, должен вернуться пустой массив
        assertTrue(jsonElement.isJsonArray());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(0, jsonElement.getAsJsonArray().size());
        taskManager.addTask(task1);
        taskManager.getTask(task1.getId());
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(1, jsonElement.getAsJsonArray().size());
    }

    @Test
    void GETEpicSubtasksById() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, 1);
        taskManager.addSubTask(subTask2, 1);
        List<SubTask> testList = List.of(subTask1, subTask2);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        var subtasksFromJson = gson.fromJson(jsonElement, new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(subtasksFromJson, testList);
    }

    @Test
    void GETTasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(0, jsonElement.getAsJsonArray().size());
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        List<Task> testList = List.of(task1, task2);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response.body());
        var tasksFromJson = gson.fromJson(jsonElement, new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(tasksFromJson, testList);
    }

    @Test
    void GETTaskById() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        taskManager.addTask(task1);
        List<Task> testList = List.of(task1);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        var taskFromJson = gson.fromJson(jsonElement.getAsJsonObject().get("value"), Task.class);
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(task1, taskFromJson);
    }

    @Test
    void POSTTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/task/");
        String taskToJson = gson.toJson(task1);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(taskToJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.CREATED.getCode(), response.statusCode());
        uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> testList = List.of(task1);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        var taskFromJson = gson.fromJson(jsonElement.getAsJsonObject().get("value"), Task.class);
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(task1, taskFromJson);
    }

    @Test
    void DELETEAllTasks() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/task");
        //отправляем запрос на удаление
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //отправляем запрос на получение, чтобы убедиться, что вернется пустой список
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.getAsJsonArray().isEmpty());
    }

    @Test
    void DELETETaskById() throws IOException, InterruptedException {
        taskManager.addTask(task1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/task/?id=1");
        //отправляем запрос на удаление
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //отправляем запрос на получение, чтобы убедиться, что вернется пустой список
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
    }

    @Test
    void GETSubtasks() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(0, jsonElement.getAsJsonArray().size());
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, 1);
        taskManager.addSubTask(subTask2, 1);
        List<Task> testList = List.of(subTask1, subTask2);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response.body());
        var tasksFromJson = gson.fromJson(jsonElement, new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(tasksFromJson, testList);
    }

    @Test
    void GETSubtaskById() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/subtask/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, 1);
        List<SubTask> testList = List.of(subTask1);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        var subtaskFromJson = gson.fromJson(jsonElement.getAsJsonObject().get("value"), SubTask.class);
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(subTask1, subtaskFromJson);
    }

    @Test
    void POSTSubtask() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        subTask1.setEpicId(1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/subtask");
        String subtaskToJson = gson.toJson(subTask1);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subtaskToJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.CREATED.getCode(), response.statusCode());
        uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> testList = List.of(subTask1);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        var subtaskFromJson = gson.fromJson(jsonElement.getAsJsonObject().get("value"), SubTask.class);
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(subTask1, subtaskFromJson);
    }

    @Test
    void DELETEAllSubtasks() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, 1);
        taskManager.addSubTask(subTask2, 1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/subtask");
        //отправляем запрос на удаление
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //отправляем запрос на получение, чтобы убедиться, что вернется пустой список
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.getAsJsonArray().isEmpty());
    }

    @Test
    void DELETESubtaskById() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, 1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/subtask/?id=1");
        //отправляем запрос на удаление
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //отправляем запрос на получение, чтобы убедиться, что вернется пустой список
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
    }

    @Test
    void GETEpics() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(0, jsonElement.getAsJsonArray().size());
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        List<Epic> testList = List.of(epic1, epic2);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        jsonElement = JsonParser.parseString(response.body());
        var epicsFromJson = gson.fromJson(jsonElement, new TypeToken<List<Epic>>() {
        }.getType());
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(testList, epicsFromJson);
    }

    @Test
    void GETEpicByID() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
        taskManager.addEpic(epic1);
        List<Epic> testList = List.of(epic1);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        var epicFromJson = gson.fromJson(jsonElement.getAsJsonObject().get("value"), Epic.class);
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(epic1, epicFromJson);
    }

    @Test
    void POSTEpic() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/epic");
        String epicToJson = gson.toJson(epic1);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicToJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.CREATED.getCode(), response.statusCode());
        uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> testList = List.of(epic1);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        var epicFromJson = gson.fromJson(jsonElement.getAsJsonObject().get("value"), Epic.class);
        assertEquals(HttpCode.SUCCESS.getCode(), response.statusCode());
        assertEquals(epic1, epicFromJson);
    }

    @Test
    void DELETEEpics() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/epic");
        //отправляем запрос на удаление
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //отправляем запрос на получение, чтобы убедиться, что вернется пустой список
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.getAsJsonArray().isEmpty());
    }

    @Test
    void DELETEEpicById() throws IOException, InterruptedException {
        taskManager.addEpic(epic1);
        URI uri = URI.create("http://localhost:" + HttpTaskServer.getPort() + "/tasks/epic/?id=1");
        //отправляем запрос на удаление
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        //отправляем запрос на получение, чтобы убедиться, что вернется пустой список
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(HttpCode.BAD_REQUEST.getCode(), response.statusCode());
    }

}
