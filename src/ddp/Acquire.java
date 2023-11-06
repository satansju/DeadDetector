package ddp;

import event.Lock;
import event.Thread;
import util.vectorclock.VectorClock;

import java.util.HashSet;
/**
 * Acquire is used for storing meta data for deadlock pattern detection.
 *
 * @project Bachelor in Computer Science at Aarhus University
 * title: Deadlock Dependently Precedes
 *
 * @author Simon Sataa-Yu Larsen on 7/5/2020
 */
public class Acquire {
    long id;
    Thread t;
    Lock l;
    VectorClock D_t;
    HashSet<Lock> history;

    public Acquire(long id, Thread t, Lock l, VectorClock D_t, HashSet<Lock> history) {
        this.id = id;
        this.t = t;
        this.l = l;
        this.D_t = D_t;
        this.history = history;
    }

    public long getId() {
        return id;
    }

    public Thread getT() {
        return t;
    }

    public Lock getL() {
        return l;
    }

    public VectorClock getD_t() {
        return D_t;
    }

    public HashSet<Lock> getHistory() {
        return history;
    }
}
