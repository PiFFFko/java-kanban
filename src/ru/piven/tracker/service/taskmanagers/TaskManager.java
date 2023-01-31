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

    boolean addTask(Task task);

    boolean updateTask(int taskId, Task task);

    boolean removeTask(int taskId);

    Collection<SubTask> getAllSubTasks();

    void removeAllSubTasks();

    SubTask getSubTask(int subTaskId);

    boolean addSubTask(SubTask subTask, Integer epicId);

    boolean updateSubTask(int subTaskId, SubTask subTask);

    boolean removeSubTask(int subTaskId);

    Collection<Epic> getAllEpics();

    void removeAllEpics();

    Epic getEpic(int epicId);

    void addEpic(Epic epic);

    boolean updateEpic(int epicId, Epic epic);

    boolean removeEpic(int epicId);

    ArrayList<SubTask> getEpicSubTasks(int epicId);

    List<Task> getHistory();

}