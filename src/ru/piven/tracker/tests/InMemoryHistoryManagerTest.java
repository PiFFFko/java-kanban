package ru.piven.tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.history.InMemoryHistoryManager;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1,"Task1", "Task1Description");
        task2 = new Task(2,"Task2", "Task2Description");
        task3 = new Task(3,"Task2", "Task2Description");
    }
}
