package ru.piven.tracker.service.taskmanagers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.server.KVTaskClient;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager {

    private KVTaskClient client;
    private static Gson gson = new Gson();

    public HttpTaskManager(String serverURL) throws InterruptedException, IOException {
        super(null);
        client = new KVTaskClient(serverURL);
    }

    @Override
    protected void save() {
        try {
            client.put("tasks", gson.toJson(this.getAllTasks()));
            client.put("epics", gson.toJson(this.getAllEpics()));
            client.put("subtasks", gson.toJson(this.getAllSubTasks()));
            client.put("history", gson.toJson(this.getHistory()));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Во время выполнения запроса произошла ошибка.", e);
        }
    }


    public HttpTaskManager loadFromServer(String serverUrl) throws IOException, InterruptedException {
        HttpTaskManager taskManager = new HttpTaskManager(serverUrl);
        int maxId = 0;
        Collection<Task> tasks = gson.fromJson(client.load("tasks"), new TypeToken<List<Task>>() {
        }.getType());
        Collection<Epic> epics = gson.fromJson(client.load("epics"), new TypeToken<List<Epic>>() {
        }.getType());
        Collection<SubTask> subTasks = gson.fromJson(client.load("subtasks"), new TypeToken<List<SubTask>>() {
        }.getType());
        Collection<Task> history = gson.fromJson(client.load("history"), new TypeToken<List<Task>>() {
        }.getType());
        for (Task task : tasks) {
            maxId = Integer.max(maxId, task.getId());
            taskManager.addTask(task.getId(), task);
        }
        for (Epic epic : epics) {
            maxId = Integer.max(maxId, epic.getId());
            taskManager.addEpic(epic.getId(), epic);
        }
        for (SubTask subTask : subTasks){
            maxId = Integer.max(maxId, subTask.getId());
            taskManager.addSubTask(subTask.getId(),subTask,subTask.getEpicId());
        }
        for(Task task: history){
            int id = task.getId();
            taskManager.getTask(id);
            taskManager.getEpic(id);
            taskManager.getSubTask(id);
        }
        return taskManager;
    }

}
