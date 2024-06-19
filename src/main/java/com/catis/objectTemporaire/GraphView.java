package com.catis.objectTemporaire;

import java.io.Serializable;

public class GraphView implements Serializable {

    private String name;
    private int value;


    public GraphView() {

    }


    public GraphView(String name, int value) {
        super();
        this.name = name;
        this.value = value;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
