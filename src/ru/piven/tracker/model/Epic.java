package ru.piven.tracker.model;

import ru.piven.tracker.service.enums.Status;
import ru.piven.tracker.service.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static ru.piven.tracker.service.taskmanagers.FileBackedTaskManager.IF_TIME_NOT_SET;

public class Epic extends Task {
    private transient final TaskType type = TaskType.EPIC;
    private final ArrayList<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public Epic(Integer id, String name, Status status, String description) {
        super(id, name, status, description);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        recalculateStartTimeAndEndTime();
    }

    public void removeSubTask(Integer subTaskId) {
        subTasks.remove(subTaskId);
    }

    @Override
    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        String startTimeStr = startTime.isEmpty() ? IF_TIME_NOT_SET : startTime.get().format(DATE_TIME_FORMATTER);
        return String.format("%s;%s;%s;%s;%s;%s;%s;noEpicId", type, id, name, status, description, startTimeStr, getDuration().toString());
    }

    private void recalculateStartTimeAndEndTime() {
        LocalDateTime startTime = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(0);
        for (SubTask subTask : subTasks) {
            if(subTask.getStartTime().isPresent()){
                if (startTime.isBefore(subTask.getStartTime().get())) {
                    startTime = subTask.getStartTime().get();
                }
                duration = duration.plus(subTask.getDuration());
            }
        }
        if(!startTime.isEqual(LocalDateTime.MIN)){
            setStartTime(startTime);
            setDuration(duration);
            setEndTime(startTime.plus(duration));
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, subTasks);
    }
}
