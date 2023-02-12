package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.enums.Status;
import ru.piven.tracker.service.history.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    protected HistoryManager historyManager;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private AtomicInteger idCounter = new AtomicInteger(1);
    private Set<Task> taskByTime = new TreeSet<>();

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            taskByTime.remove(task);
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

    public boolean addTask(Task task) {
        if (!isIntersectionWithTasks(task)) {
            Integer id = idCounter.getAndIncrement();
            task.setId(id);
            tasks.put(id, task);
            taskByTime.add(task);
            return true;
        }
        return false;
    }

    public void addTask(Integer id, Task task) {
        if (!isIntersectionWithTasks(task)) {
            tasks.put(id, task);
            taskByTime.add(task);
        }

    }

    public boolean updateTask(int taskId, Task task) {
        if (tasks.containsKey(taskId)) {
            if (!isIntersectionWithTasks(task)) {
                tasks.put(taskId, task);
                return true;
            }
        }
        return false;
    }

    public boolean removeTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            taskByTime.remove(tasks.get(taskId));
            tasks.remove(taskId);
            historyManager.remove(taskId);
            return true;
        }
        return false;
    }

    public Collection<SubTask> getAllSubTasks() {
        return subTasks.values();
    }

    public void removeAllSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
            taskByTime.contains(subTask);
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
    public boolean addSubTask(SubTask subTask, Integer epicId) {
        if (!isIntersectionWithTasks(subTask)) {
            if (epics.containsKey(epicId)) {
                int id = idCounter.getAndIncrement();
                subTask.setEpicId(epicId);
                subTask.setId(id);
                epics.get(epicId).addSubTask(subTask);
                subTasks.put(id, subTask);
                taskByTime.add(subTask);
                handleEpicStatus(epicId);
                return true;
            }
        }
        return false;
    }

    public boolean addSubTask(Integer id, SubTask subTask, Integer epicId) {
        if (!isIntersectionWithTasks(subTask)) {
            if (epics.containsKey(epicId)) {
                subTask.setEpicId(epicId);
                subTask.setId(id);
                epics.get(epicId).addSubTask(subTask);
                subTasks.put(id, subTask);
                taskByTime.add(subTask);
                handleEpicStatus(epicId);
                return true;
            }
        }
        return false;
    }

    public boolean updateSubTask(int subTaskId, SubTask subTask) {
        if (subTasks.containsKey(subTaskId)) {
            if (!isIntersectionWithTasks(subTask)) {
                Integer epicId = subTasks.get(subTaskId).getEpicId();
                subTask.setEpicId(epicId);
                subTasks.put(subTaskId, subTask);
                handleEpicStatus(epicId);
                return true;
            }
        }
        return false;
    }

    public boolean removeSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            taskByTime.remove(subTasks.get(subTaskId));
            int epicId = subTasks.get(subTaskId).getEpicId();
            epics.get(epicId).removeSubTask(subTaskId);
            subTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
            return true;
        }
        return false;
    }

    public Collection<Epic> getAllEpics() {
        return epics.values();
    }

    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            ArrayList<SubTask> subTasks = epic.getSubTasks();
            historyManager.remove(epic.getId());
            for (SubTask subTask : subTasks) {
                removeSubTask(subTask.getId());
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

    public boolean updateEpic(int epicId, Epic epic) {
        if (epics.containsKey(epicId)) {
            epics.put(epicId, epic);
            return true;
        }
        return false;
    }

    public boolean removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            for (SubTask subTask : epics.get(epicId).getSubTasks()) {
                removeSubTask(subTask.getId());
            }
            epics.remove(epicId);
            historyManager.remove(epicId);
            return true;
        }
        return false;
    }

    public ArrayList<SubTask> getEpicSubTasks(int epicId) {
        return epics.get(epicId).getSubTasks();
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

    protected void setIdCounter(AtomicInteger idCounter) {
        this.idCounter = idCounter;
    }

    private boolean isIntersectionWithTasks(Task task) {
        for (Task task2 : taskByTime) {
            //для случая если мы хотим заапдейтить таску, но с тем же временем
            if (task.getId() != task2.getId()) {
                if (isTimeIntersection(
                        task.getStartTime().orElse(LocalDateTime.MIN),
                        task.getDuration(),
                        task2.getStartTime().orElse(LocalDateTime.MIN.plusNanos(1)),
                        task2.getDuration())) {
                    return true;
                }
            }
        }
        return false;
    }

    //Конец первой задачи на временном промежутке всегда будет правее начала первой задачи, т.е. позже.
    //Поэтому нужно проверить, что конец раньше начала второй задачи, тогда и начало первой будет раньше начала второй
    //Так и с началом. Начало на временном промежутке всегда будет раньше конца, значит достаточно проверить, что начало
    //первой будет позже конца второй. Эти два условия обуславливают непересечение двух задач.
    private boolean isTimeIntersection(LocalDateTime startTime1, Duration duration1, LocalDateTime startTime2, Duration duration2) {
        LocalDateTime endTime1 = startTime1.plus(duration1);
        LocalDateTime endTime2 = startTime2.plus(duration2);
        return !(endTime1.isBefore(startTime2) || startTime1.isAfter(endTime2));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(taskByTime);
    }

}
