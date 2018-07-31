package pers.ThreadTermination;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类名：TerminationToken
 * 功能：toShutdown属性用于标志准备阶段标志符；
 * reservation属性用于标志还有多少数量的任务未完成。
 *
 */
public class TerminationToken {

    public final AtomicInteger reservation = new AtomicInteger(0);
    /*
     * 在多个可停止线程实例共享一个TerminationToken实例的情况下，该队列用于记录那些共享TerminationToken实例
     * 的可停止线程，以便尽可能减少锁的使用的情况下，实现这些线程的停止。
     */
    private final Queue<WeakReference<Termintable>> coordinatedThreads;
    // 使用volatile修饰，以保证无需显示锁的情况下该变量的内存可见性
    protected volatile boolean toShutdown = false;

    public TerminationToken()

    {
        coordinatedThreads = new ConcurrentLinkedQueue<>();
    }

    public boolean isToShutdown() {
        return toShutdown;
    }

    protected void setToShutdown(boolean toShutdown) {
        this.toShutdown = toShutdown;
    }

    protected void register(Termintable thread) {
        coordinatedThreads.add(new WeakReference<>(thread));
    }

    /*
     * 通知TerminationToken实例：共享该实例的所有可停止线程中的一个线程停止了，以便其停止其他未被停止的线程。
     * @param thread 已停止的线程
     */
    protected void notifyThreadTermination(Termintable thread) {
        WeakReference<Termintable> wrThread;
        Termintable otherThread;
        while (null != (wrThread = coordinatedThreads.poll())) {
            otherThread = wrThread.get();
            if (null != otherThread && otherThread != thread)
                otherThread.terminate();
        }
    }
}
