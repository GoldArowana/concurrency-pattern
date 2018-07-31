package pers.ThreadTermination;

public abstract class AbstractTerminatableThread extends Thread implements Termintable {

    public final TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }

    /**
     * @param terminationToken 线程间共享的线程终止标志实例
     */
    public AbstractTerminatableThread(TerminationToken terminationToken) {
        super();
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    /**
     * 留给子类实现其线程处理逻辑。
     *
     * @throws Exception
     */
    protected abstract void doRun() throws Exception;

    /**
     * 留给子类实现。用于实现线程停止后的一些清理动作。
     *
     * @param cause
     */
    protected void doCleanup(Exception cause) {
    }

    /**
     * 留给子类实现。用于实现线程停止所需的操作。
     * 处理当线程处于某些特定状况下无法响应interrupt方法时，所必须执行的一些操作。
     * 如：线程包含Socket I/O,则必须在此方法中实现关闭Socket
     */
    protected void doTerminate() {
    }

    /**
     * 线程执行方法，真正的执行方法在doRun()方法中。
     * 功能：不断从警报队列取出警报消息并发送。
     * 停止条件： 1、执行了terminate方法。
     * 线程停止标志为true（准备阶段）；
     * 且警报消息发送完毕，terminationToken.reservation属性为0（执行阶段）。
     * 2、执行了线程的super.interrupt()方法。
     */
    public void run() {
        System.out.println("已进入线程.");
        Exception ex = null;
        try {
            for (; ; ) {
                // 在执行线程的处理逻辑前，先判断线程停止的标志。
                if (terminationToken.isToShutdown() && terminationToken.reservation.get() <= 0)
                    break;
                doRun();
            }
        } catch (Exception e) {
            // 使线程能够响应interrupt调用而退出
            ex = e;
        } finally {
            System.err.println("最后的任务已完成，执行停止.");
            try {
                doCleanup(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }

    }

    public void interrupt() {
        terminate();
    }

    /**
     * 终止线程的方法。
     * 首先处理doTerminate()方法，处理关闭线程之前的必要操作;
     * 然后判断是否有待处理的任务terminationToken?若有，则继续处理任务；否则直接执行interrupt方法终止线程。
     */
    public void terminate() {
        terminationToken.setToShutdown(true);
        try {
            doTerminate();
        } finally {
            // 若无待处理的任务，则试图强制终止线程
            if (terminationToken.reservation.get() <= 0)
                super.interrupt();
        }
    }

    public void terminate(boolean waitUtilThreadTerminated) {
        terminate();
        if (waitUtilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
