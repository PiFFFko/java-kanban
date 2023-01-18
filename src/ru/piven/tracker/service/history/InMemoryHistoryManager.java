package ru.piven.tracker.service.history;

import ru.piven.tracker.model.Task;
import ru.piven.tracker.service.linkedList.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> mapForRemoving = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        Integer taskId = task.getId();
        if (mapForRemoving.containsKey(taskId)) {
            removeNode(mapForRemoving.get(taskId));
        }
        Node<Task> node = linkLast(task);
        mapForRemoving.put(taskId, node);
    }

    @Override
    public void remove(int id) {
        removeNode(mapForRemoving.get(id));
        mapForRemoving.remove(id);
    }

    private void removeNode(Node<Task> node) {
        Node<Task> prevNode = node.getPrev();
        Node<Task> nextNode = node.getNext();
        //Если слева по списку, стоял элемент, то переназначаем его next, на элемент стоявший спереди
        //Иначе, наш элемент был головой и теперь головной элемент тот, что стоит спереди
        //Ссылку на предыдущий элемент у следующего элемента изменим исходя из результат следующего ветвления
        if (prevNode != null) {
            prevNode.setNext(nextNode);
        } else {
            head = nextNode;
        }
        //Если спереди был элемент, то меняем его ссылку на предыдущий элемент на тот, что стоял перед нашим элементом
        //Иначе наш элемент был хвостом и теперь предыдущий элемент становится хвостом
        //Если наш элемент был единственным в списке, то head и tail станут null и список очистится
        if (nextNode != null) {
            nextNode.setPrev(prevNode);
        } else {
            tail = prevNode;
        }
    }

    private Node<Task> linkLast(Task task) {
        //запомнили элемент который был на конце
        Node<Task> oldTail = tail;
        //создали новую ноду, которая теперь будет на конце
        //ее предыдущим элементов теперь стал тот, который был на конце до этого, т.е. oldTail
        //впереди его никого нет, поэтому next = null
        Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        //Если до этого ничего не добавляли, то наш новый элемент является первым элементом в списке
        //Соответственно он теперь и голова, и хвост
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        return newNode;
    }

    private List<Task> getTasks() {
        Node<Task> node = head;
        List<Task> elems = new ArrayList<>();
        while (node != null) {
            elems.add(node.getData());
            node = node.getNext();
        }
        return elems;
    }
}
