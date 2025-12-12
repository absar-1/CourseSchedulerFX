package com.example.courseschedulerfx.datastructures;

public class Stackk<T> {
    LinkedListt<T> list;

    public Stackk() {
        list = new LinkedListt<>();
    }

    public void push(T data) {
        list.addFirst(data);
    }

    public T pop(){
        if (list.isEmpty()) {
            return null;
        }
        return list.removeFirst();
    }

    public T peek(){
        if (list.isEmpty()) {
            return null;
        }
        return list.getHead();
    }

    public void display(){
        list.display();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

}
