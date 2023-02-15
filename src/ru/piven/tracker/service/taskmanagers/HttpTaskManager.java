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
    private static final String TASK_KEY = "tasks";
    private static final String SUBTASK_KEY = "subtasks";
    private static final String EPIC_KEY = "epics";
    private static final String HISTORY_KEY = "history";


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


    public static HttpTaskManager loadFromServer(String serverURL) throws IOException, InterruptedException {
        KVTaskClient client = new KVTaskClient(serverURL);
        HttpTaskManager taskManager = new HttpTaskManager(serverURL);
        Collection<Task> tasks = gson.fromJson(client.load(TASK_KEY), new TypeToken<List<Task>>() {
        }.getType());
        Collection<Epic> epics = gson.fromJson(client.load(EPIC_KEY), new TypeToken<List<Epic>>() {
        }.getType());
        Collection<SubTask> subTasks = gson.fromJson(client.load(SUBTASK_KEY), new TypeToken<List<SubTask>>() {
        }.getType());
        Collection<Task> history = gson.fromJson(client.load(HISTORY_KEY), new TypeToken<List<Task>>() {
        }.getType());
        for (Task task : tasks) {
            taskManager.addTask(task.getId(), task);
        }
        for (Epic epic : epics) {
            taskManager.addEpic(epic.getId(), epic);
        }
        for (SubTask subTask : subTasks){
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
