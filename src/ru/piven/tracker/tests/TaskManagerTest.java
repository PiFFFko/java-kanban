package ru.piven.tracker.tests;

import org.junit.jupiter.api.Test;
import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.taskmanagers.TaskManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected SubTask subTask3;
    protected Epic epic1;
    protected Epic epic2;

    @Test
    void getAllTasksShouldReturnCollectionWithTasksWhenSomeWereAdded(){
        taskManager.addTask(task1);
        Collection<Task> testList = Collections.singletonList(task1);
        assertArrayEquals(testList.toArray(),taskManager.getAllTasks().toArray());
    }

    @Test
    void getAllTasksShouldReturnEmptyCollectionWhenTasksWereNotAdded(){
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void removeAllTasksWhenSomeTasksWereAdded() {
        taskManager.addTask(task1);
        taskManager.removeAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void removeAllTasksWhenNoTasksWereAdded() {
        taskManager.removeAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void getTaskShouldReturnTask1WhenItWereAdded() {
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTask(1));
    }

    @Test
    void getTaskShouldReturnNullWhenNoTasksInIt() {
        assertNull(taskManager.getTask(1));
    }

    @Test
    void getTaskShouldReturnNullWhenIdIsIncorrect() {
        taskManager.addTask(task1);
        assertNull(taskManager.getTask(2));
    }

    @Test
    void addTaskShoudlReturnTrueWhenTaskWithNoTime() {
        assertTrue(taskManager.addTask(task1));
    }

    @Test
    void addTaskShouldReturnTrueWhenTaskWithTimeAndNoInterSection() {
        task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        task2 = new Task("Task2", "Task2Description","24.01.2023 15:45", "PT20M");
        taskManager.addTask(task2);
        assertTrue(taskManager.addTask(task1));
    }

    @Test
    void addTaskShouldReturnFalseWhenTaskWithTimeAndInterSection() {
        task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        task2 = new Task("Task2", "Task2Description","23.01.2023 15:45", "PT20M");
        taskManager.addTask(task2);
        assertFalse(taskManager.addTask(task1));
    }

    @Test
    void updateTaskShouldReturnTrueWhenIdCorrectAndNoIntersection() {
        task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        task2 = new Task("Task2", "Task2Description","23.01.2023 15:45", "PT20M");
        taskManager.addTask(task1);
        assertTrue(taskManager.updateTask(1,task1));
    }

    @Test
    void updateTaskShouldReturnFalseWhenNoTasks() {
        task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        assertFalse(taskManager.updateTask(1,task1));
    }

    @Test
    void updateTaskShouldReturnFalseWhenIncorrectIdentificator() {
        task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        task2 = new Task("Task2", "Task2Description","23.01.2023 15:45", "PT20M");
        taskManager.addTask(task1);
        assertFalse(taskManager.updateTask(2,task1));
    }

    @Test
    void removeTaskShouldReturnTrueWhenIdentificatorIsCorrect() {
        task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        taskManager.addTask(task1);
        assertTrue(taskManager.removeTask(1));
    }

    @Test
    void removeTaskShouldReturnFalseNoTasks() {
        assertFalse(taskManager.removeTask(1));
    }

    @Test
    void removeTaskShouldReturnFalseIdentificatorIsIncorrect() {
        task1 = new Task("Task1", "Task1Description","23.01.2023 15:45", "PT20M");
        taskManager.addTask(task1);
        assertFalse(taskManager.removeTask(2));
    }

    @Test
    void getAllSubTasksShouldReturnCollectionWithSubTasksWhenSomeWereAdded() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1,1);
        Collection<SubTask> testList = Collections.singletonList(subTask1);
        assertArrayEquals(testList.toArray(),taskManager.getAllSubTasks().toArray());
    }

    @Test
    void getAllSubTasksShouldReturnEmptyCollectionWhenSubTasksWereNotAdded() {
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void removeAllSubTasksWhenSubtaskWereAdded() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1,1);
        taskManager.removeAllSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void removeAllSubTasksWhenSubtaskSomeWereNotAdded() {
        taskManager.removeAllSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void getSubTaskShouldReturnSubtask1WhenItWereAdded() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1,1);
        assertEquals(subTask1,taskManager.getSubTask(2));
    }

    @Test
    void getSubTaskShouldReturnNullWhenItWereNotAdded() {
        assertNull(taskManager.getSubTask(2));
    }

    @Test
    void getSubTaskShouldReturnNullWhenIdentificatorIsIncorrect() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1,1);
        assertNull(taskManager.getSubTask(3));
    }

    @Test
    void addSubTaskShouldReturnTrueWhenSubTaskWithNoTimeAndEpicExist() {
        taskManager.addEpic(epic1);
        assertTrue(taskManager.addSubTask(subTask1,1));
    }

    @Test
    void addSubTaskShouldReturnFalseWhenSubTaskWithNoTimeAndEpicNotExist() {
        taskManager.addEpic(epic1);
        assertFalse(taskManager.addSubTask(subTask1,2));
    }

    @Test
    void addSubTaskShouldReturnTrueWhenSubTaskWithTimeAndNoTimeInterSection() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description","23.01.2023 15:45", "PT20M");
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2Description","24.01.2023 15:45", "PT20M");
        taskManager.addSubTask(subTask1,1);
        assertTrue(taskManager.addSubTask(subTask2,1));
    }

    @Test
    void addSubTaskShouldReturnFalseWhenSubTaskWithTimeAndTimeInterSection() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description","23.01.2023 15:45", "PT20M");
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2Description","23.01.2023 15:45", "PT20M");
        taskManager.addSubTask(subTask1,1);
        assertFalse(taskManager.addSubTask(subTask2,1));
    }

    @Test
    void addSubTaskShouldReturnFalseWhenSubTaskWithTimeAndNoTimeInterSectionButEpicNotExist() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description","23.01.2023 15:45", "PT20M");
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2Description","23.01.2023 15:45", "PT20M");
        taskManager.addSubTask(subTask1,1);
        assertFalse(taskManager.addSubTask(subTask2,2));
    }

    @Test
    void updateSubTaskShouldReturnTrueWhenIdCorrectAndNoTimeInterSection() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description","23.01.2023 15:45", "PT20M");
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2Description","24.01.2023 15:45", "PT20M");
        taskManager.addSubTask(subTask1,1);
        assertTrue(taskManager.updateSubTask(2,subTask2));
    }

    @Test
    void updateSubTaskShouldReturnFalseWhenIdIncorrectAndNoTimeInterSection() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description","23.01.2023 15:45", "PT20M");
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2Description","23.01.2023 15:45", "PT20M");
        taskManager.addSubTask(subTask1,1);
        assertFalse(taskManager.updateSubTask(3,subTask2));
    }

    @Test
    void removeSubTaskShouldReturnTrueWhenIdentificatorIsCorrect() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description","23.01.2023 15:45", "PT20M");
        taskManager.addSubTask(subTask1,1);
        assertTrue(taskManager.removeSubTask(2));
    }

    @Test
    void removeSubTaskShouldReturnFalseWhenIdentificatorIsIncorrect() {
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("SubTask1", "SubTask1Description","23.01.2023 15:45", "PT20M");
        taskManager.addSubTask(subTask1,1);
        assertFalse(taskManager.removeSubTask(3));
    }

    @Test
    void getAllEpicsShouldReturnCollectionWithEpicsWhenSomeWereAdded() {
        taskManager.addEpic(epic1);
        Collection<Epic> testList = Collections.singletonList(epic1);
        assertArrayEquals(testList.toArray(), taskManager.getAllEpics().toArray());
    }

    @Test
    void getAllEpicsShouldReturnEmptyCollectionWhenSomeWereNotAdded() {
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void removeAllEpicsWhenSomeEpicsWereAdded() {
        taskManager.addEpic(epic1);
        taskManager.removeAllEpics();
        Collection<Epic> testList = Collections.emptyList();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void removeAllEpicsWhenSomeEpicsWereNotAdded() {
        taskManager.removeAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void getEpicShouldReturnEpic1WhenItWasAdded() {
        taskManager.addEpic(epic1);
        assertEquals(epic1,taskManager.getEpic(1));
    }

    @Test
    void getEpicShouldReturnNullWhenItWasNotAdded() {
        assertNull(taskManager.getEpic(1));
    }

    @Test
    void getEpicShouldReturnNullWhenIdentificatorIncorrect() {
        taskManager.addEpic(epic1);
        assertNull(taskManager.getEpic(2));
    }

    @Test
    void addEpic() {
        taskManager.addEpic(epic1);
        Collection<Epic> testList = Collections.emptyList();
        assertNotEquals(testList,taskManager.getAllEpics());
    }

    @Test
    void updateEpicShouldReturnTrueWhenIdentificatorIsCorrect() {
        taskManager.addEpic(epic1);
        assertTrue(taskManager.updateEpic(1, epic2));
    }

    @Test
    void updateEpicShouldReturnFalseWhenIdentificatorIsCorrect() {
        taskManager.addEpic(epic1);
        assertFalse(taskManager.updateEpic(2, epic2));
    }

    @Test
    void removeEpicShouldReturnTrueWhenIdentificatorIsCorrect() {
        taskManager.addEpic(epic1);
        assertTrue(taskManager.removeEpic(1));
    }

    @Test
    void removeEpicShouldReturnFalseWhenIdentificatorIsIncorrect() {
        taskManager.addEpic(epic1);
        assertFalse(taskManager.removeEpic(2));
    }

    @Test
    void getEpicSubTasksShouldReturnSubtasksWhenTheyWereAddedToEpic() {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1,1);
        taskManager.addSubTask(subTask2,1);
        Collection<SubTask> testList = List.of(subTask1, subTask2);
        assertEquals(testList, taskManager.getEpicSubTasks(1));
    }

    @Test
    void getEpicSubTasksShouldEmptyListWhereNothingWereAdded() {
        taskManager.addEpic(epic1);
        Collection<SubTask> testList = Collections.emptyList();
        assertEquals(testList, taskManager.getEpicSubTasks(1));
    }

    @Test
    void getHistoryShouldReturnEmptyListWhenNothingWereGet() {
        taskManager.addTask(task1);
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1,2);
        Collection<SubTask> testList = Collections.emptyList();
        assertEquals(testList, taskManager.getHistory());
    }

    @Test
    void getHistoryShouldReturnListWhenNothingWereGet() {
        taskManager.addTask(task1);
        taskManager.getTask(1);
        Collection<Task> testList = Collections.singletonList(task1);
        assertEquals(testList, taskManager.getHistory());
    }

}
