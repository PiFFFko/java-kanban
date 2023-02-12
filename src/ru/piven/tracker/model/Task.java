package ru.piven.tracker.model;

import ru.piven.tracker.service.enums.Status;
import ru.piven.tracker.service.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import static ru.piven.tracker.service.taskmanagers.FileBackedTaskManager.IF_TIME_NOT_SET;

public class Task implements Comparable<Task> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    protected String name;
    protected String description;
    protected Integer id;
    protected Status status;
    protected TaskType type = TaskType.TASK;
    protected Optional<Duration> duration = Optional.empty();
    protected Optional<LocalDateTime> startTime = Optional.empty();

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, Status status, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, String startTime, String duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = Optional.of(LocalDateTime.parse(startTime, DATE_TIME_FORMATTER));
        this.duration = Optional.of(Duration.parse(duration));
    }

    public Task(Integer id, String name, Status status, String description, String startTime, String duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = Optional.of(LocalDateTime.parse(startTime, DATE_TIME_FORMATTER));
        this.duration = Optional.of(Duration.parse(duration));
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration.orElse(Duration.ofMinutes(0));
    }

    public void setDuration(Duration duration) {
        this.duration = Optional.of(duration);
    }

    public Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = Optional.of(startTime);
    }

    @Override
    public String toString() {
        String startTimeStr = startTime.isEmpty() ? IF_TIME_NOT_SET : startTime.get().format(DATE_TIME_FORMATTER);
        return String.format("%s;%s;%s;%s;%s;%s;%s;noEpicId", type, id, name, status, description, startTimeStr, getDuration().toString());
    }

    public LocalDateTime getEndTime() {
        return startTime.get().plus(duration.get());
    }

    @Override
    public int compareTo(Task o) {
        if (this.hashCode() == o.hashCode()){
            return 0;
        }
        return this.getStartTime().orElse(LocalDateTime.MIN).compareTo(o.getStartTime().orElse(LocalDateTime.MIN.plus(Duration.ofMinutes(1))));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name)
                && Objects.equals(description, task.description)
                && id.equals(task.id)
                && status == task.status
                && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, type);
    }
}
