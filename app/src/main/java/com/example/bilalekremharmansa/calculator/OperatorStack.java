package com.example.bilalekremharmansa.calculator;

import java.util.EmptyStackException;

/**
 * Created by bilalekremharmansa on 29.6.2017.
 */

public class OperatorStack {
    private OperatorNode top;
    private int size;

    public OperatorStack() {

    }

    public OperatorStack(OperatorNode top) {
        this.top = top;
        size++;
    }

    public void push(char item){
        if(empty()){
            top = new OperatorNode(item);
            size ++;
        }
        else{
            OperatorNode newNode = new OperatorNode(item);
            newNode.setNextNode(top);
            top = newNode;
            size++;
        }
    }
    public char peek() throws EmptyStackException {
        if(empty()){
            throw new EmptyStackException();
        }

        return top.getItem();

    }
    public char pop() throws EmptyStackException{
        if(empty()){
            throw new EmptyStackException();
        }
        char poppedItem = top.getItem();
        top = top.getNextNode();
        size--;
        return poppedItem;
    }
    public boolean empty(){
        return size == 0 ? true : false;
    }
    public int size(){
        return size;
    }





}
