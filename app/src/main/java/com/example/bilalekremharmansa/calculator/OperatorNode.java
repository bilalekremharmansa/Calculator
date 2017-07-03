package com.example.bilalekremharmansa.calculator;

/**
 * Created by bilalekremharmansa on 29.6.2017.
 */

public class OperatorNode {
    private char item;
    private OperatorNode nextNode;

    public OperatorNode(char item) {
        this.item = item;
    }

    public char getItem() {
        return item;
    }

    public void setItem(char item) {
        this.item = item;
    }

    public OperatorNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(OperatorNode nextNode) {
        this.nextNode = nextNode;
    }
}
