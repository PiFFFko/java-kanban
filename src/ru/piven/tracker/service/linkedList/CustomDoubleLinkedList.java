package ru.piven.tracker.service.linkedList;

import java.util.ArrayList;
import java.util.List;

public class CustomDoubleLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public void removeNode(Node<T> node) {
        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;
        //Если нода единственная в списке
        if (prevNode == null && nextNode == null) {
            node = null;
            head = null;
            tail = null;
        }
        //Если prevNode = null, значит нода является головой
        if (prevNode == null && nextNode != null) {
            nextNode.prev = null;
            head = nextNode;
        }
        //Если нода находится в хвосте (справа)
        if (prevNode != null && nextNode == null) {
            prevNode.next = null;
            tail = prevNode;
        }
        //Если нода находится в голове (слева)
        if (prevNode != null && nextNode != null) {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }

    public void linkLast(Node newNode) {
        //запомнили элемент который был на конце
        Node<T> oldTail = tail;
        //создали новую ноду, которая теперь будет на конце
        //ее предыдущим элементов теперь стал тот, который был на конце до этого, т.е. oldTail
        //впереди его никого нет, поэтому next = null
        newNode.prev = oldTail;
        tail = newNode;
        //Если до этого ничего не добавляли, то наш новый элемент является первым элементом в списке
        //Соответственно он теперь и голова, и хвост
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }


    public List<T> getElems() {
        Node<T> node = head;
        List<T> elems = new ArrayList<>();
        while (node!= null) {
            elems.add(node.data);
            node = node.next;
        }
        return elems;
    }

    public int size() {
        return this.size;
    }

}

