package ru.piven.tracker.service.history;

import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.linkedList.CustomDoubleLinkedList;
import ru.piven.tracker.service.linkedList.Node;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();
    private CustomDoubleLinkedList<Task> tasks = new CustomDoubleLinkedList<>();
    private HashMap<Integer, Node> mapForRemoving = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return tasks.getElems();
    }

    @Override
    public void add(Task task) {
        Node<Task> node = new Node<>(null,task,null);
        Integer taskId = task.getId();
        if (mapForRemoving.containsKey(taskId)){
            tasks.removeNode(mapForRemoving.get(taskId));
        }
        tasks.linkLast(node);
        mapForRemoving.put(taskId,node);
    }

    @Override
    public void remove(int id) {
        tasks.removeNode(mapForRemoving.get(id));
        mapForRemoving.remove(id);
    }
}
