package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.history.HistoryManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    protected HistoryManager historyManager;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private AtomicInteger idCounter = new AtomicInteger(1);

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    public Task getTask(int taskId) {
        Task task = tasks.getOrDefault(taskId, null);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    public void addTask(Task task) {
        Integer id = idCounter.getAndIncrement();
        task.setId(id);
        tasks.put(id, task);
    }

    public void addTask(Integer id, Task task) {
        tasks.put(id, task);
    }

    public void updateTask(int taskId, Task task) {
        if (tasks.containsKey(taskId))
            tasks.put(taskId, task);
    }

    public void removeTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            historyManager.remove(taskId);
        }
    }

    public Collection<SubTask> getAllSubTasks() {
        return subTasks.values();
    }

    public void removeAllSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        subTasks.clear();
    }

    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = subTasks.getOrDefault(subTaskId, null);
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;
    }

    //Подразумеваем, что при создании подзадачи укажут к какому эпику она принадлежит
    //Сообщаем эпику, что у него есть новая подзадача занося id этой подзадачи
    //в список подзадач эпика.
    public void addSubTask(SubTask subTask, Integer epicId) {
        if (epics.containsKey(epicId)) {
            int id = idCounter.getAndIncrement();
            subTask.setEpicId(epicId);
            subTask.setId(id);
            epics.get(epicId).addSubTask(id);
            subTasks.put(id, subTask);
            handleEpicStatus(epicId);
        }
    }

    public void addSubTask(Integer id, SubTask subTask, Integer epicId) {
        if (epics.containsKey(epicId)) {
            subTask.setEpicId(epicId);
            subTask.setId(id);
            epics.get(epicId).addSubTask(id);
            subTasks.put(id, subTask);
            handleEpicStatus(epicId);
        }
    }

    public void updateSubTask(int subTaskId, SubTask subTask) {
        if (subTasks.containsKey(subTaskId)) {
            Integer epicId = subTasks.get(subTaskId).getEpicId();
            subTask.setEpicId(epicId);
            subTasks.put(subTaskId, subTask);
            handleEpicStatus(epicId);
        }
    }

    public void removeSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            int epicId = subTasks.get(subTaskId).getEpicId();
            epics.get(epicId).removeSubTask(subTaskId);
            subTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
    }

    public Collection<Epic> getAllEpics() {
        return epics.values();
    }

    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            ArrayList<Integer> subTaskIds = epic.getSubTasksIds();
            historyManager.remove(epic.getId());
            for (Integer subTaskId : subTaskIds) {
                removeSubTask(subTaskId);
            }
        }
        epics.clear();
    }

    public Epic getEpic(int epicId) {
        Epic epic = epics.getOrDefault(epicId, null);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    public void addEpic(Epic epic) {
        int id = idCounter.getAndIncrement();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void addEpic(Integer id, Epic epic) {
        epics.put(id, epic);
    }

    public void updateEpic(int epicId, Epic epic) {
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
        }
    }

    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            List<Integer> subTaskIds = (List<Integer>) epics.get(epicId).getSubTasksIds().clone();
            for (Integer subTaskId : subTaskIds) {
                removeSubTask(subTaskId);
            }
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        ArrayList<Integer> subTasksIds = epics.get(epicId).getSubTasksIds();
        ArrayList<SubTask> tempSubTasks = new ArrayList<>();
        for (Integer subTaskId : subTasksIds) {
            tempSubTasks.add(subTasks.get(subTaskId));
        }
        return tempSubTasks;
    }

    private void handleEpicStatus(int epicId) {
        Status status = Status.IN_PROGRESS;
        boolean doneFlag = true;
        boolean newFlag = true;
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> tempSubTasks = getEpicSubTasks(epicId);
        for (SubTask subTask : tempSubTasks) {
            if (subTask.getStatus() != Status.DONE) {
                doneFlag = false;
            }
            if (subTask.getStatus() != Status.NEW) {
                newFlag = false;
            }
        }
        if (newFlag) {
            status = Status.NEW;
        } else if (doneFlag) {
            status = Status.DONE;
        }
        epic.setStatus(status);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void setIdCounter(AtomicInteger idCounter) {
        this.idCounter = idCounter;
    }


}
