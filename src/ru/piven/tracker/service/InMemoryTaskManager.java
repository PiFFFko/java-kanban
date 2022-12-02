package ru.piven.tracker.service;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager{
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private AtomicInteger idCounter = new AtomicInteger(1);

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }


    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTask(int taskId) {
        return tasks.getOrDefault(taskId, null);
    }

    public void addTask(Task task) {
        Integer id = idCounter.getAndIncrement();
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(int taskId, Task task) {
        if (tasks.containsKey(taskId))
            tasks.put(taskId, task);
    }

    public void removeTask(int taskId) {
        if (tasks.containsKey(taskId))
            tasks.remove(taskId);
    }


    public Collection<SubTask> getAllSubTask() {
        return subTasks.values();
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    public SubTask getSubTask(int subTaskId) {
        return subTasks.getOrDefault(subTaskId, null);
    }

    //Подразумеваем, что при создании подзадачи укажут к какому эпику она принадлежит
    //Сообщаем эпику, что у него есть новая подзадача занося id этой подзадачи
    //в список подзадач эпика.
    public void addSubTask(SubTask subTask, Integer epicId) {
        if (epics.containsKey(epicId)) {
            int id = idCounter.getAndIncrement();
            subTask.setEpicId(epicId);
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
            //удаление сабтаски из ее эпика
            epics.get(epicId).removeSubTask(subTaskId);
            subTasks.remove(subTaskId);
        }
    }

    public Collection<Epic> getAllEpics() {
        return epics.values();
    }

    public void removeAllEpics() {
        for (Epic epic: epics.values()){
            ArrayList<Integer> subTaskIds = epic.getSubTasksIds();
            for (Integer subTaskId :subTaskIds){
                removeSubTask(subTaskId);
            }
        }
        epics.clear();
    }

    public Epic getEpic(int epicId) {
        return epics.getOrDefault(epicId, null);
    }

    public void addEpic(Epic epic) {
        int id = idCounter.getAndIncrement();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void updateEpic(int epicId, Epic epic) {
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
        }
    }

    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subTaskIds = epics.get(epicId).getSubTasksIds();
            for (Integer subTaskId :subTaskIds){
                removeSubTask(subTaskId);
            }
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

}
