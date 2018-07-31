package pers.ThreadAlarm;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * GuardedObject对象
 * GuardedMethod保护方法： sendAlarm方法
 * BooleanSupplier保护条件：AlarmAgent实例与报警服务器已经建立网络连接，即connectedToServer的值为true
 */
public class AlarmAgent {

    private final Blocker blocker = new ConditionVarBlocker();
    // 心跳定时器
    private final Timer heartbeatTimer = new Timer(true);
    private volatile boolean connectedToServer = false;
    private final BooleanSupplier agentConnected = () -> connectedToServer;

    /**
     * GuardedMethod保护方法： sendAlarm方法
     *
     * @param alarm 报警信息
     * @throws Exception
     */
    public void sendAlarm(final AlarmInfo alarm) throws Exception {
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            public Void call() throws Exception {
                doSendAlarm(alarm);
                return null;
            }
        };
        blocker.callWithGuard(guardedAction);
    }

    private void doSendAlarm(AlarmInfo alarm) {
        System.out.println("sending alarm" + alarm);

        // 模拟发送劲报警至服务器的耗时
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    public void init() {
        Thread connectingThread = new Thread(new ConnectingTask());
        connectingThread.start();
        heartbeatTimer.schedule(new HeartbeatTask(), 60000, 2000);
    }

    protected void onConnected() {
        try {
            blocker.signalAfter(() -> {
                connectedToServer = true;
                System.out.println("Connected to server.");
                return Boolean.TRUE;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDisconnected() {
        connectedToServer = false;
    }

    private boolean testConnection() {
        return true;
    }

    private void reconnect() {
        ConnectingTask connectingThread = new ConnectingTask();
        // 直接在心跳定时器线程中执行
        connectingThread.run();
    }

    public void disconnect() {
        System.out.println("disconnected from alarm server.");
        connectedToServer = false;
    }

    /**
     * 负责与警报服务器建立网络连接
     */
    private class ConnectingTask implements Runnable {

        @Override
        public void run() {
            // 模拟连接耗时
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
            onConnected();
        }
    }

    /**
     * 心跳定时任务：定时检查与报警服务器的连接是否正常，发现连接异常后自动重新连接
     */
    private class HeartbeatTask extends TimerTask {

        @Override
        public void run() {
            if (!testConnection()) {
                onDisconnected();
                reconnect();
            }
        }
    }
}
