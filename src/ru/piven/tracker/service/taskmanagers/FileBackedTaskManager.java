package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.enums.Status;
import ru.piven.tracker.service.enums.TaskType;
import ru.piven.tracker.service.exceptions.ManagerSaveException;
import ru.piven.tracker.service.history.HistoryManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public static final String IF_TIME_NOT_SET = "";
    private final Path tasksFile;

    public FileBackedTaskManager(Path tasksFile) {
        super();
        this.tasksFile = tasksFile;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        //maxId необходим для того, чтобы понять с какого айдишника нужно будет продолжать добавлять следующие задачи
        int maxId = 0;
        FileBackedTaskManager fm = new FileBackedTaskManager(path);
        try (BufferedReader bufferedReader = Files.newBufferedReader((path), StandardCharsets.UTF_8)) {
            //пропускаем заголовок
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            //Читаем до пустой строки, следующая строка будет содержать историю
            while (!line.equals("")) {
                Task task = fromString(line);
                maxId = Integer.max(maxId, task.getId());
                switch (task.getType()) {
                    case TASK:
                        fm.addTask(task.getId(), task);
                        break;
                    case EPIC:
                        fm.addEpic(task.getId(), (Epic) task);
                        break;
                    case SUBTASK:
                        fm.addSubTask(task.getId(), (SubTask) task, ((SubTask) task).getEpicId());
                        break;
                    default:
                        throw new ManagerSaveException();
                }
                line = bufferedReader.readLine();
            }
            Optional<String> history = Optional.ofNullable(bufferedReader.readLine());
            if (history.isPresent()) {
                String[] historyElems = history.get().split(",");
                for (String historyElem : historyElems) {
                    int id = Integer.valueOf(historyElem);
                    //Дергаем get по айдишнику из массива. В какой-то из хэшмап он точно будет и тогда попадет в историю
                    fm.getTask(id);
                    fm.getEpic(id);
                    fm.getSubTask(id);
                }
                fm.setIdCounter(new AtomicInteger(maxId));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        return fm;
    }

    private static Task fromString(String value) {
        Task task = null;
        String[] values = value.split(";");
        Integer id = Integer.valueOf(values[1]);
        String name = values[2];
        Status status = Status.valueOf(values[3]);
        String description = values[4];
        String startTime;
        String duration;
        switch (TaskType.valueOf(values[0])) {
            case TASK:
                if (values[5].equals(IF_TIME_NOT_SET)) {
                    task = new Task(id, name, status, description);
                } else {
                    startTime = values[5];
                    duration = values[6];
                    task = new Task(id, name, status, description, startTime, duration);
                }
                break;
            case SUBTASK:
                int epicId = Integer.valueOf(values[7]);
                if (values[5].equals(IF_TIME_NOT_SET)) {
                    task = new SubTask(id, name, status, description, epicId);
                } else {
                    startTime = values[5];
                    duration = values[6];
                    task = new SubTask(id, name, status, description, startTime, duration, epicId);
                }
                break;
            case EPIC:
                task = new Epic(id, name, status, description);
                break;
        }
        return task;
    }

    private static String historyToString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public boolean addTask(Task task) {
        if (super.addTask(task)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTask(int taskId, Task task) {
        if (super.updateTask(taskId, task)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeTask(int taskId) {
        if (super.removeTask(taskId)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean addSubTask(SubTask subTask, Integer epicId) {
        if (super.addSubTask(subTask, epicId)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSubTask(int subTaskId, SubTask subTask) {
        if (super.updateSubTask(subTaskId, subTask)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeSubTask(int subTaskId) {
        if (super.removeSubTask(subTaskId)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public boolean updateEpic(int epicId, Epic epic) {
        if (super.updateEpic(epicId, epic)) {
            save();
            return true;
        }
        return false;
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = super.getSubTask(subTaskId);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public boolean removeEpic(int epicId) {
        if (super.removeEpic(epicId)) {
            save();
            return true;
        }
        return false;
    }

    protected void save() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tasksFile, StandardCharsets.UTF_8)) {
            bufferedWriter.write("type; id; name; status; description; startTime; duration; epicId(only For Subtasks)\n");
            for (Task task : getAllTasks()) {
                bufferedWriter.write(task.toString());
                bufferedWriter.write("\n");
            }
            for (Epic epic : getAllEpics()) {
                bufferedWriter.write(epic.toString());
                bufferedWriter.write("\n");
            }
            for (SubTask subTask : getAllSubTasks()) {
                bufferedWriter.write(subTask.toString());
                bufferedWriter.write("\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }
}


