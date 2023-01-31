package ru.piven.tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.history.HistoryManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {

    protected HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;

    @Test
    void addShouldReturnNotEmptyListAfterTaskWasAdded() {
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertFalse(history.isEmpty());
    }

    @Test
    void addShouldReturnEmptyListWhenTaskWereNotAdded() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void addShouldNotWorkForDuplicate() {
        historyManager.add(task1);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1,history.size());
    }

    @Test
    void removeFromTail() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        assertEquals(2,history.size());
    }

    @Test
    void removeFromMiddle() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        List<Task> history = historyManager.getHistory();
        assertEquals(2,history.size());
    }

    @Test
    void removeFromHead() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        List<Task> history = historyManager.getHistory();
        assertEquals(2,history.size());
    }

    @Test
    void getHistory() {
        historyManager.add(task1);
        Collection<Task> taskList = Collections.singletonList(task1);
        List<Task> history = historyManager.getHistory();
        assertArrayEquals(taskList.toArray(), history.toArray());
    }
}