package ru.piven.tracker.service.taskmanagers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.server.KVTaskClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {

    private KVTaskClient client;
    private String serverURL;
    private static Gson gson = new Gson();
    private static final String TASK_KEY = "tasks";
    private static final String SUBTASK_KEY = "subtasks";
    private static final String EPIC_KEY = "epics";
    private static final String HISTORY_KEY = "history";
    private static final Type taskType = new TypeToken<List<Task>>() {}.getType();
    private static final Type subtaskType = new TypeToken<List<SubTask>>() {}.getType();
    private static final Type epicType = new TypeToken<List<Epic>>() {}.getType();
    private static final Type idType = new TypeToken<List<Integer>>() {}.getType();

    public HttpTaskManager(String serverURL) throws InterruptedException, IOException {
        super(null);
        this.serverURL = serverURL;
        client = new KVTaskClient(serverURL);
        loadFromServer();
    }

    @Override
    protected void save() {
        try {
            client.put(TASK_KEY, gson.toJson(this.getAllTasks()));
            client.put(EPIC_KEY, gson.toJson(this.getAllEpics()));
            client.put(SUBTASK_KEY, gson.toJson(this.getAllSubTasks()));
            client.put(HISTORY_KEY, gson.toJson(this.getHistory().stream().map(Task::getId).collect(Collectors.toList())));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Во время выполнения запроса произошла ошибка.", e);
        }
    }


    private void loadFromServer() throws IOException, InterruptedException {
        Optional<Collection<Task>> tasks = Optional.ofNullable(gson.fromJson(client.load(TASK_KEY), taskType));
        Optional<Collection<Epic>> epics = Optional.ofNullable(gson.fromJson(client.load(EPIC_KEY), epicType));
        Optional<Collection<SubTask>> subTasks = Optional.ofNullable(gson.fromJson(client.load(SUBTASK_KEY), subtaskType));
        Optional<Collection<Integer>> history = Optional.ofNullable(gson.fromJson(client.load(HISTORY_KEY), idType));
        if (tasks.isPresent()){
            for (Task task : tasks.get()) {
                this.addTask(task.getId(), task);
            }
        }
        if (epics.isPresent()){
            for (Epic epic : epics.get()) {
                this.addEpic(epic.getId(), epic);
            }
        }
        if (subTasks.isPresent()){
            for (SubTask subTask : subTasks.get()){
                this.addSubTask(subTask.getId(),subTask,subTask.getEpicId());
            }
        }
        if(history.isPresent()){
            for(Integer id: history.get()){
                this.getTask(id);
                this.getEpic(id);
                this.getSubTask(id);
            }
        }
    }
}
