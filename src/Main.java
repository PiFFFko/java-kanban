import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.taskmanagers.FileBackedTaskManager;
import ru.piven.tracker.service.taskmanagers.InMemoryTaskManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        testSprint7();
    }

    public static void testSprint7(){
        Path file = Paths.get(System.getProperty("user.dir"), "data", "data.csv");
        FileBackedTaskManager fm = new FileBackedTaskManager(file);
        Task task1 = new Task("Убраться в квартире", "Подмести и помыть полы, убрать мусор");
        Task task2 = new Task("Покормить кису", "Корм в кладовке насыпать жвотине в миску", "23.01.2023 15:45", "PT20M");
        SubTask subTask1 = new SubTask("Почистить зубы", "Чисто, чисто, чисто","30.01.2023 08:00", "PT10M");
        SubTask subTask2 = new SubTask("Позавтракать", "Яишенка с беконом и помидорами - топ!","20.01.2023 09:00", "PT40M");
        SubTask subTask3 = new SubTask("Я не знаю какие задачи еще придумать", "Пусто", "25.01.2023 10:00", "PT10M");
        Epic epic1 = new Epic("Утренний ритуал", "То что делаешь каждое утро");
        Epic epic2 = new Epic("Тест", "Тест");
        fm.addTask(task1);
        fm.addTask(task2);
        fm.addEpic(epic1);
        fm.addEpic(epic2);
        fm.addSubTask(subTask1, 3);
        fm.addSubTask(subTask2, 3);
        fm.addSubTask(subTask3, 3);
        fm.getTask(1);
        fm.getTask(2);
        fm.getTask(1);
        fm.getTask(2);
        fm.getTask(1);
        fm.getSubTask(5);
        fm.getSubTask(6);
        fm.getSubTask(7);
        fm.getSubTask(7);
        fm.getSubTask(6);
        fm.getSubTask(5);
        fm.getEpic(3);
        fm.getEpic(4);
        fm.getEpic(4);
        fm.getEpic(3);
        System.out.println(fm.getAllTasks());
        System.out.println(fm.getAllSubTasks());
        System.out.println(fm.getAllEpics());
        System.out.println("История:" + fm.getHistory() + "\n");
        FileBackedTaskManager fm2 = FileBackedTaskManager.loadFromFile(file);
        System.out.println(fm2.getAllTasks());
        System.out.println(fm2.getAllSubTasks());
        System.out.println(fm2.getAllEpics());
        System.out.println("История:" + fm2.getHistory() + "\n");
        System.out.println(fm2.getPrioritizedTasks());
    }
    public static void testSprint6() throws IOException {
        Path file = Paths.get(System.getProperty("user.dir"), "data", "data.csv");
        FileBackedTaskManager fm = new FileBackedTaskManager(file);
        Task task1 = new Task("Убраться в квартире", "Подмести и помыть полы, убрать мусор");
        Task task2 = new Task("Покормить кису", "Корм в кладовке насыпать жвотине в миску");
        SubTask subTask1 = new SubTask("Почистить зубы", "Чисто, чисто, чисто");
        SubTask subTask2 = new SubTask("Позавтракать", "Яишенка с беконом и помидорами - топ!");
        SubTask subTask3 = new SubTask("Я не знаю какие задачи еще придумать", "Пусто");
        Epic epic1 = new Epic("Утренний ритуал", "То что делаешь каждое утро");
        Epic epic2 = new Epic("Тест", "Тест");
        fm.addTask(task1);
        fm.addTask(task2);
        fm.addEpic(epic1);
        fm.addEpic(epic2);
        fm.addSubTask(subTask1, 3);
        fm.addSubTask(subTask2, 3);
        fm.addSubTask(subTask3, 3);
        fm.getTask(1);
        fm.getTask(2);
        fm.getTask(1);
        fm.getTask(2);
        fm.getTask(1);
        fm.getSubTask(5);
        fm.getSubTask(6);
        fm.getSubTask(7);
        fm.getSubTask(7);
        fm.getSubTask(6);
        fm.getSubTask(5);
        fm.getEpic(3);
        fm.getEpic(4);
        fm.getEpic(4);
        fm.getEpic(3);
        System.out.println(fm.getAllTasks());
        System.out.println(fm.getAllSubTasks());
        System.out.println(fm.getAllEpics());
        System.out.println("История:" + fm.getHistory() + "\n");
        FileBackedTaskManager fm2 = FileBackedTaskManager.loadFromFile(file);
        System.out.println(fm2.getAllTasks());
        System.out.println(fm2.getAllSubTasks());
        System.out.println(fm2.getAllEpics());
        System.out.println("История:" + fm2.getHistory() + "\n");
    }

    public static void testSprint5() {
        Task task1 = new Task("Убраться в квартире", "Подмести и помыть полы, убрать мусор");
        Task task2 = new Task("Покормить кису", "Корм в кладовке насыпать жвотине в миску");
        SubTask subTask1 = new SubTask("Почистить зубы", "Чисто, чисто, чисто");
        SubTask subTask2 = new SubTask("Позавтракать", "Яишенка с беконом и помидорами - топ!");
        SubTask subTask3 = new SubTask("Я не знаю какие задачи еще придумать", "Пусто");
        Epic epic1 = new Epic("Утренний ритуал", "То что делаешь каждое утро");
        Epic epic2 = new Epic("Тест", "Тест");
        InMemoryTaskManager tm = new InMemoryTaskManager();
        System.out.println("\n" + tm.getHistory());
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addEpic(epic2);
        tm.addSubTask(subTask1, 3);
        tm.addSubTask(subTask2, 3);
        tm.addSubTask(subTask3, 3);
        tm.getTask(1);
        tm.getTask(2);
        tm.getTask(1);
        tm.getTask(2);
        tm.getTask(1);
        System.out.println("\n" + tm.getHistory());
        tm.getSubTask(5);
        tm.getSubTask(6);
        tm.getSubTask(7);
        tm.getSubTask(7);
        tm.getSubTask(6);
        tm.getSubTask(5);
        System.out.println("\n" + tm.getHistory());
        tm.getEpic(3);
        tm.getEpic(4);
        tm.getEpic(4);
        tm.getEpic(3);
        System.out.println("\n" + tm.getHistory());
        tm.removeTask(1);
        tm.removeSubTask(5);
        tm.removeEpic(4);
        System.out.println("\n" + tm.getHistory());
        tm.removeEpic(3);
        System.out.println("\n" + tm.getHistory());
        tm.removeTask(2);
        System.out.println("\n" + tm.getHistory());
    }

    public static void testSprint4GetHistory() {
        Task task1 = new Task("Убраться в квартире", "Подмести и помыть полы, убрать мусор");
        SubTask subTask1 = new SubTask("Почистить зубы", "Чисто, чисто, чисто");
        Epic epic1 = new Epic("Утренний ритуал", "То что делаешь каждое утро");
        InMemoryTaskManager tm = new InMemoryTaskManager();
        tm.addTask(task1);
        tm.addEpic(epic1);
        tm.addSubTask(subTask1, 2);
        //последовательность вызова 1,3,2,1,2,3
        tm.getTask(1);
        tm.getSubTask(3);
        tm.getEpic(2);
        tm.getTask(1);
        tm.getEpic(2);
        tm.getSubTask(3);
        //шесть записей находится в истории
        System.out.println(tm.getHistory());
        //добавляем еще шесть записей в историю
        //итоговая последовательность должна быть 1,3,2,1,2,3,2,1,3,3,1,2
        //Отсекая последние десять должно получиться 2,1,2,3,2,1,3,3,1,2
        tm.getEpic(2);
        tm.getTask(1);
        tm.getSubTask(3);
        tm.getSubTask(3);
        tm.getTask(1);
        tm.getEpic(2);
        System.out.println(tm.getHistory());

    }

    public static void testSprint3() {
        Task task1 = new Task("Убраться в квартире", "Подмести и помыть полы, убрать мусор");
        Task task2 = new Task("Покормить кису", "Корм в кладовке насыпать жвотине в миску");
        SubTask subTask1 = new SubTask("Почистить зубы", "Чисто, чисто, чисто");
        SubTask subTask2 = new SubTask("Позавтракать", "Яишенка с беконом и помидорами - топ!");
        SubTask subTask3 = new SubTask("Я не знаю какие задачи еще придумать", "Пусто");
        Epic epic1 = new Epic("Утренний ритуал", "То что делаешь каждое утро");
        Epic epic2 = new Epic("Тест", "Тест");
        InMemoryTaskManager tm = new InMemoryTaskManager();
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addEpic(epic2);
        tm.addSubTask(subTask1, 3);
        tm.addSubTask(subTask2, 3);
        tm.addSubTask(subTask3, 4);
        System.out.println(tm.getTasks());
        System.out.println(tm.getEpics());
        System.out.println(tm.getSubTasks());
        System.out.println();
        task1 = new Task("Убраться в квартире", "Подмести и помыть полы, убрать мусор", Status.IN_PROGRESS);
        task2 = new Task("Покормить кису", "Корм в кладовке насыпать жвотине в миску", Status.IN_PROGRESS);
        subTask1 = new SubTask("Почистить зубы", "Чисто, чисто, чисто", Status.NEW);
        subTask2 = new SubTask("Позавтракать", "Яишенка с беконом и помидорами - топ!", Status.DONE);
        subTask3 = new SubTask("Я не знаю какие задачи еще придумать", "Пусто", Status.DONE);
        tm.updateTask(1, task1);
        tm.updateTask(2, task2);
        tm.updateSubTask(5, subTask1);
        tm.updateSubTask(6, subTask2);
        tm.updateSubTask(7, subTask3);
        System.out.println(tm.getTasks());
        System.out.println(tm.getEpics());
        System.out.println(tm.getSubTasks());
    }


}
