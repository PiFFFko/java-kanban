package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    Collection<Task> getAllTasks();

    void removeAllTasks();

    Task getTask(int taskId);

    void addTask(Task task);

    void updateTask(int taskId, Task task);

    void removeTask(int taskId);

    Collection<SubTask> getAllSubTask();

    void removeAllSubTasks();

    SubTask getSubTask(int subTaskId);

    void addSubTask(SubTask subTask, Integer epicId);

    void updateSubTask(int subTaskId, SubTask subTask);

    void removeSubTask(int subTaskId);

    Collection<Epic> getAllEpics();

    void removeAllEpics();

    Epic getEpic(int epicId);

    void addEpic(Epic epic);

    void updateEpic(int epicId, Epic epic);

    void removeEpic(int epicId);

    ArrayList<SubTask> getEpicSubTasks(int epicId);

    List<Task> getHistory();

}