package com.king.concurrency.mtpattern.ch5.tpt.example;

public enum AlarmType {
    FAULT("fault"),
    RESUME("resume");

    private final String name;

    private AlarmType(String name) {
        this.name = name;
    }

    public String toString() {

        return name;
    }
}
