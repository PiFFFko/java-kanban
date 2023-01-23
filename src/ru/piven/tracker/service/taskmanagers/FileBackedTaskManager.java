package ru.piven.tracker.service.taskmanagers;

import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.exceptions.ManagerSaveException;
import ru.piven.tracker.service.history.HistoryManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private Path tasksFile;

    public FileBackedTaskManager(Path tasksFile) {
        super();
        this.tasksFile = tasksFile;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        //maxId необходим для того, чтобы понять с какого айдишника нужно будет продолжать добавлять следующие задачи
        Integer maxId = 0;
        FileBackedTaskManager fm = new FileBackedTaskManager(path);
        try (BufferedReader bufferedReader = Files.newBufferedReader((path), StandardCharsets.UTF_8)) {
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
            String history = bufferedReader.readLine();
            String[] historyElems = history.split(",");
            for (String historyElem : historyElems) {
                Integer id = Integer.valueOf(historyElem);
                //Дергаем get по айдишнику из массива. В какой-то из хэшмап он точно будет и тогда попадет в историю
                fm.getTask(id);
                fm.getEpic(id);
                fm.getSubTask(id);
            }
            fm.setIdCounter(new AtomicInteger(maxId));
        } catch (IOException e) {
            throw new ManagerSaveException();
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
        switch (values[0]) {
            case ("TASK"):
                task = new Task(id, name, status, description);
                break;
            case ("SUBTASK"):
                int epicId = Integer.valueOf(values[5]);
                task = new SubTask(id, name, status, description, epicId);
                break;
            case ("EPIC"):
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

    private static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();
        String[] values = value.split(",");
        for (String id : values) {
            ids.add(Integer.valueOf(id));
        }
        return ids;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(int taskId, Task task) {
        super.updateTask(taskId, task);
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask, Integer epicId) {
        super.addSubTask(subTask, epicId);
        save();
    }

    @Override
    public void updateSubTask(int subTaskId, SubTask subTask) {
        super.updateSubTask(subTaskId, subTask);
        save();
    }

    @Override
    public void removeSubTask(int subTaskId) {
        super.removeSubTask(subTaskId);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(int epicId, Epic epic) {
        super.updateEpic(epicId, epic);
        save();
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
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    private void save() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tasksFile, StandardCharsets.UTF_8)) {
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
            throw new ManagerSaveException();
        }
    }
}


