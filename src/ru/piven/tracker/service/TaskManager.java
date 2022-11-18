package ru.piven.tracker.service;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, SubTask> subTasks;
    public HashMap<Integer, Epic> epics;

    Integer idCounter = 1;

    public TaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }


    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tempTasks = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            tempTasks.add(tasks.get(key));
        }
        return tempTasks;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            return tasks.get(taskId);
        } else {
            return null;
        }
    }

    public void addTask(Task task) {
        tasks.put(idCounter, task);
        idCounter++;
    }

    public void updateTask(int taskId, Task task) {
        if (tasks.containsKey(taskId))
            tasks.put(taskId, task);
    }

    public void removeTask(int taskId) {
        if (tasks.containsKey(taskId))
            tasks.remove(taskId);
    }


    public ArrayList<SubTask> getAllSubTask() {
        ArrayList<SubTask> tempSubTasks = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            tempSubTasks.add(subTasks.get(key));
        }
        return tempSubTasks;
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    public SubTask getSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            return subTasks.get(subTaskId);
        } else {
            return null;
        }
    }

    //Подразумеваем, что при создании подзадачи укажут к какому эпику она принадлежит
    //Сообщаем эпику, что у него есть новая подзадача занося id этой подзадачи
    //в список подзадач эпика.
    public void addSubTask(SubTask subTask, Integer epicId) {
        if (epics.containsKey(epicId)) {
            subTask.setEpicId(epicId);
            epics.get(epicId).addSubTask(idCounter);
            subTasks.put(idCounter, subTask);
            handleEpicStatus(epicId);
            idCounter++;
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
            subTasks.remove(subTaskId);
        }
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> tempEpics = new ArrayList<>();
        for (Integer key : epics.keySet()) {
            tempEpics.add(epics.get(key));
        }
        return tempEpics;
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public Epic getEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId);
        } else {
            return null;
        }
    }

    //При добавлении нового эпика список подзадач будет пуст
    public void addEpic(Epic epic) {
        epics.put(idCounter, epic);
        idCounter++;
    }

    public void updateEpic(int epicId, Epic epic) {
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
        }
    }

    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            epics.remove(epicId);
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

    public void handleEpicStatus(int epicId) {
        Status status = Status.IN_PROGRESS;
        boolean doneFlag = true;
        boolean newFlag = true;
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> tempSubTasks = getEpicSubTasks(epicId);
        for (SubTask subTask : tempSubTasks) {
            if (subTask.getStatus() != Status.DONE) {
                doneFlag = false;
            }
        }
        for (SubTask subTask : tempSubTasks) {
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

}
