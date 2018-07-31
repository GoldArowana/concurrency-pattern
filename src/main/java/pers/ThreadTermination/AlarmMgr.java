package pers.ThreadTermination;

import pers.ThreadAlarm.AlarmInfo;
import pers.ThreadAlarm.AlarmType;

/**
 * 类名：AlarmMgr
 * 为线程拥有者。对外提供shutdown()方法，执行线程停止操作。
 *
 * @author WJL
 */
public class AlarmMgr {

    private static final AlarmMgr instance = new AlarmMgr();
    // 目标线程。定义为私有属性，不暴露给外部，防止被意外停止。
    private final AlarmSendingThread alarmSendingThread;
    private volatile boolean shutdownRequested = false;

    private AlarmMgr() {
        alarmSendingThread = new AlarmSendingThread();
    }

    public static AlarmMgr getInstance() {
        return instance;
    }

    public void init() {
        alarmSendingThread.start();
    }

    /**
     * 功能：发送警报
     *
     * @param type
     * @param id
     * @param extraInfo
     * @return 由type+id+extraInfo唯一确定的警报信息被提交的次数。-1表示警报管理器已经被关闭
     */
    public int sentAlarm(AlarmType type, String id, String extraInfo) {
        // System.err.println("Trigger alarm "+type+","+id+","+extraInfo);
        int duplicateSubmissionCount = 0;
        try {
            AlarmInfo alarmInfo = new AlarmInfo(id, type);
            alarmInfo.setExtraInfo(extraInfo);
            duplicateSubmissionCount = alarmSendingThread.sendAlarm(alarmInfo);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return duplicateSubmissionCount;
    }

    public synchronized void shundowm() {
        System.err.println("线程准备停止.");
        if (shutdownRequested) {
            throw new IllegalStateException("shutdown already requested!");
        }
        alarmSendingThread.terminate();
        shutdownRequested = true;
    }
}
