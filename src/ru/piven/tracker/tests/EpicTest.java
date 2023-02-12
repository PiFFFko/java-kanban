package ru.piven.tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.service.enums.Status;
import ru.piven.tracker.service.taskmanagers.InMemoryTaskManager;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private InMemoryTaskManager taskManager;
    private Epic epic;

    @BeforeEach
    public void setUp(){
        taskManager = new InMemoryTaskManager();
        epic = new Epic("EpicName", "EpicDescription");
        taskManager.addEpic(epic);
    }

    @Test
    public void epicStatusNewWhenNoSubTasks(){
        assertEquals(Status.NEW,epic.getStatus());
    }

    @Test
    public void epicStatusNewWhenAllSubTasksNew(){
        SubTask subTask1 = new SubTask("SubTask1", "Subtask1Description", Status.NEW);
        SubTask subTask2 = new SubTask("SubTask2", "Subtask2Description", Status.NEW);
        taskManager.addSubTask(subTask1, 1);
        taskManager.addSubTask(subTask2, 1);
        assertEquals(Status.NEW,epic.getStatus());
    }

    @Test
    public void epicStatusDoneWhenAllSubTasksDone(){
        SubTask subTask1 = new SubTask("SubTask1", "Subtask1Description", Status.DONE,1);
        SubTask subTask2 = new SubTask("SubTask2", "Subtask2Description", Status.DONE,1);
        taskManager.addSubTask(subTask1, 1);
        taskManager.addSubTask(subTask2, 1);
        assertEquals(Status.DONE,epic.getStatus());
    }
    @Test
    public void epicStatusInProgressWhenSomeSubTasksDoneOrNew(){
        SubTask subTask1 = new SubTask("SubTask1", "Subtask1Description", Status.DONE,1);
        SubTask subTask2 = new SubTask("SubTask2", "Subtask2Description", Status.NEW,1);
        taskManager.addSubTask(subTask1, 1);
        taskManager.addSubTask(subTask2, 1);
        assertEquals(Status.IN_PROGRESS,epic.getStatus());
    }
    @Test
    public void epicStatusInProgressWhenAllSubTasksInProgress(){
        SubTask subTask1 = new SubTask("SubTask1", "Subtask1Description", Status.IN_PROGRESS,1);
        SubTask subTask2 = new SubTask("SubTask2", "Subtask2Description", Status.IN_PROGRESS,1);
        taskManager.addSubTask(subTask1, 1);
        taskManager.addSubTask(subTask2, 1);
        assertEquals(Status.IN_PROGRESS,epic.getStatus());
    }
}