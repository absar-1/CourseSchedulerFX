package com.example.courseschedulerfx.datastructures;

class Node<T> {
    Node<T> prev;
    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
    }
}

public class LinkedListt<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedListt() {
        this.size = 0;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            T data = head.data;
            head = tail = null;
            size--;
            return data;
        } else {
            T data = head.data;
            head = head.next;
            head.prev = null;
            size--;
            return data;
        }
    }

    public T removeLast() {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            T data = head.data;
            head = tail = null;
            size--;
            return data;
        } else {
            T data = tail.data;
            tail = tail.prev;
            tail.next = null;
            size--;
            return data;
        }
    }

    public void display() {
        if (head == null) {
            System.out.println("List is empty");
            return;
        }
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(" ");
            }
            current = current.next;
        }
    }

    public T getHead() {
        if (head == null) {
            return null;
        }
        return head.data;
    }

    public T getTail() {
        if (tail == null) {
            return null;
        }
        return tail.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid Index!!!");
        }

        Node<T> current;

        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        return current.data;
    }

}
