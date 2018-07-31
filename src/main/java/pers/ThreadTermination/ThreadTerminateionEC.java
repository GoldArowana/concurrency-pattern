package pers.ThreadTermination;

import pers.ThreadAlarm.AlarmType;

public class ThreadTerminateionEC {

    public static void main(String[] args) {
        AlarmMgr alarmMgr = AlarmMgr.getInstance();
        alarmMgr.init();
        int a1 = alarmMgr.sentAlarm(AlarmType.RESUME, "100001", "警报1");
        int a2 = alarmMgr.sentAlarm(AlarmType.RESUME, "100002", "警报2");
        int a3 = alarmMgr.sentAlarm(AlarmType.RESUME, "100003", "警报3");
        alarmMgr.shundowm();
        alarmMgr.sentAlarm(AlarmType.RESUME, "100004", "警报4");
    }

}
