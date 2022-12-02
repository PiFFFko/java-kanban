package ru.piven.tracker.service;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    public Collection<Task> getAllTasks();

    public void removeAllTasks();

    public Task getTask(int taskId);

    public void addTask(Task task);

    public void updateTask(int taskId, Task task);

    public void removeTask(int taskId);

    public Collection<SubTask> getAllSubTask();

    public void removeAllSubTasks();

    public SubTask getSubTask(int subTaskId);

    public void addSubTask(SubTask subTask, Integer epicId);

    public void updateSubTask(int subTaskId, SubTask subTask);

    public void removeSubTask(int subTaskId);

    public Collection<Epic> getAllEpics();

    public void removeAllEpics();

    public Epic getEpic(int epicId);

    public void addEpic(Epic epic);

    public void updateEpic(int epicId, Epic epic);

    public void removeEpic(int epicId);

    public ArrayList<SubTask> getEpicSubTasks(int epicId);

    public List<Task> getHistory();

}

