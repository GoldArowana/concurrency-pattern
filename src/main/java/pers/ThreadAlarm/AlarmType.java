package pers.ThreadAlarm;

public enum AlarmType {
    FAULT("fault"),
    RESUME("resume");

    private final String name;

    AlarmType(String name) {
        this.name = name;
    }

    public String toString() {

        return name;
    }
}
