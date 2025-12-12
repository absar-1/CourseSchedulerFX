package com.example.courseschedulerfx.datastructures;

public class Queuee<T> {
    LinkedListt<T> list;

    public Queuee(){
        list = new LinkedListt<>();
    }

    public void enqueue(T data){
        list.addLast(data);
    }

    public T dequeue(){
        return list.removeFirst();
    }

    public T peek(){
        return list.getHead();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    public void display(){
        list.display();
    }

}
