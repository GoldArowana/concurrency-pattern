package pers.ThreadAlarm;

import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;

public abstract class GuardedAction<V> implements Callable<V> {

    protected final BooleanSupplier guard;

    public GuardedAction(BooleanSupplier guard) {
        this.guard = guard;
    }
}
