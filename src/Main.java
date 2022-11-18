import ru.piven.tracker.model.Epic;
import ru.piven.tracker.model.SubTask;
import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.Status;
import ru.piven.tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("Убраться в квартире","Подмести и помыть полы, убрать мусор", Status.NEW);
        Task task2 = new Task("Покормить кису","Корм в кладовке насыпать жвотине в миску", Status.NEW);
        SubTask subTask1 = new SubTask("Почистить зубы","Чисто, чисто, чисто",Status.NEW);
        SubTask subTask2 = new SubTask("Позавтракать","Яишенка с беконом и помидорами - топ!",Status.NEW);
        SubTask subTask3 = new SubTask("Я не знаю какие задачи еще придумать","Пусто",Status.NEW);
        Epic epic1 = new Epic("Утренний ритуал","То что делаешь каждое утро",Status.NEW);
        Epic epic2 = new Epic("Тест","Тест", Status.NEW);
        TaskManager tm = new TaskManager();
        tm.addTask(task1);
        tm.addTask(task2);
        tm.addEpic(epic1);
        tm.addEpic(epic2);
        tm.addSubTask(subTask1,3);
        tm.addSubTask(subTask2,3);
        tm.addSubTask(subTask3,4);
        System.out.println(tm.tasks);
        System.out.println(tm.epics);
        System.out.println(tm.subTasks);
        System.out.println();
        task1 = new Task("Убраться в квартире","Подмести и помыть полы, убрать мусор", Status.IN_PROGRESS);
        task2 = new Task("Покормить кису","Корм в кладовке насыпать жвотине в миску", Status.IN_PROGRESS);
        subTask1 = new SubTask("Почистить зубы","Чисто, чисто, чисто",Status.NEW);
        subTask2 = new SubTask("Позавтракать","Яишенка с беконом и помидорами - топ!",Status.DONE);
        subTask3 = new SubTask("Я не знаю какие задачи еще придумать","Пусто",Status.DONE);
        tm.updateTask(1,task1);
        tm.updateTask(2,task2);
        tm.updateSubTask(5,subTask1);
        tm.updateSubTask(6,subTask2);
        tm.updateSubTask(7,subTask3);
        System.out.println(tm.tasks);
        System.out.println(tm.epics);
        System.out.println(tm.subTasks);

    }
}
