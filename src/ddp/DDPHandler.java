package ddp;

import event.Event;
import event.Lock;
import event.Thread;
import event.Variable;
import util.Pair;
import util.vectorclock.VectorClock;

import java.util.*;

/**
 * Class for handling processing of events.
 * This class is the one implementing the functionality of Algorithm 1.
 *
 * @project Bachelor in Computer Science at Aarhus University
 * title: Deadlock Dependently Precedes
 *
 * @author Simon Sataa-Yu Larsen on 7/5/2020
 */
public class DDPHandler extends Event {
    private boolean print_events;
    long eventCount = 1;
    ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadLocks;

    public DDPHandler(boolean print_events) {
        super();
        this.print_events = print_events;
        this.deadLocks = new ArrayList<>();
    }

    /**
     * Parent method for handling events
     * @param state data structure for dpp ordering
     * @return true if deadlock is detected otherwise false
     */
    public boolean handle(DDPState state) {
        if(print_events) {
            System.out.println("Event: e" + eventCount + "");
        }
        eventCount++;
        // Delegating to sub handling methods
        boolean deadlockDetected = this.handleSub(state);
        if(print_events) {
            printEvents(state);
        }
        return deadlockDetected;
    }

    /**
     * Printing partial orders and other meta data for each event
     * Used for debugging
     * @param state data structure for dpp ordering
     */
    private void printEvents(DDPState state) {
        //System.out.println("Acq = " + state.Acq_t_l);
        //System.out.println("Rel = " + state.Rel_t_l + "");
        //System.out.println("P" + " = " + state.P_t);
        //System.out.println("H_" + state.threadToIndex.get(getThread()) + " = " + state.H_t);
        //System.out.println("T_" + state.threadToIndex.get(getThread()) + " = " + state.T_t);
        //System.out.println("D" + " = " + state.D_t + "");
        //System.out.println("A = " + state.acquires + "\n");
        System.out.println("LocksHeld = " + state.locksHeld + "");
        System.out.println("Acq = " + state.acqMap.get(getThread()));
        //System.out.println("ThreadToLockSet = " + state.threadToLockSet + "\n");
        //System.out.println();
    }

    /**
     * Delegating method for invoking correct handling method for an event
     * @param state data structure for dpp ordering
     * @return deadlockDetected
     */
    private boolean handleSub(DDPState state) {
        boolean deadlockDetected = false;
        if(this.getType().isAcquire()) 	deadlockDetected = this.handleSubAcquire(state);
        if(this.getType().isRelease()) 	this.handleSubRelease(state);
        if(this.getType().isRead())		this.handleSubRead(state);
        if(this.getType().isWrite())	this.handleSubWrite(state);
        if(this.getType().isFork()) 	this.handleSubFork(state);
        if(this.getType().isJoin())		this.handleSubJoin(state);
        if(this.getType().isBegin()) 	this.handleSubBegin(state);
        if(this.getType().isEnd())		this.handleSubEnd(state);
        return deadlockDetected;
    }

    /**
     * Handle Acq events
     * Line 19-24 in algorithm
     * @param state data structure for ddp partial order
     * @return true if a deadlock has been detected
     */
    private boolean handleSubAcquire(DDPState state) {
        // Get t and l
        Thread t = getThread();
        Lock l = getLock();

        // Check if they exists and if not add t and l to state
        checkAndAddThread(state, t);
        checkAndAddLock(state, l);
        //checkAndAddToLocksHeld(state);

        // Line 20
        VectorClock H_t = state.getVectorClock(state.H_t, t);
        VectorClock H_l = state.getVectorClock(state.H_l, l);
        H_t.updateWithMax(H_l);

        // Line 21
        VectorClock P_t = state.getVectorClock(state.P_t, t);
        VectorClock P_l = state.getVectorClock(state.P_l, l);
        P_t.updateWithMax(P_l);

        // Line 22
        VectorClock D_t = state.getVectorClock(state.D_t, t);
        D_t.updateWithMax(P_l);

        // Line 23-24
        VectorClock T_t = state.getVectorClock(state.T_t, t);
        for(Thread t_prime: state.threadSet) {
            if(!state.Acq_t_l.containsKey(t_prime)){
                initializeAcqQueue(state, l, t_prime);
            }
            if(!state.Acq_t_l.get(t_prime).containsKey(l)) {
                state.Acq_t_l.get(t_prime).put(l, new ArrayDeque<>());
            }
            if(!state.Rel_t_l.get(t_prime).containsKey(l)){
                state.Rel_t_l.get(t_prime).put(l, new ArrayDeque<>());
            }
            if(!state.Rel_t_l.get(t_prime).containsKey(l)){
                initializeAcqQueue(state, l, t_prime);
            }
            VectorClock newT_t = new VectorClock(T_t);
            VectorClock newD_t = new VectorClock(D_t);
            Pair<VectorClock, VectorClock> pair = new Pair<>(newT_t, newD_t);
            state.Acq_t_l.get(t_prime).get(l).add(pair);
        }
        if(print_events) {
            System.out.println("Acq|" + t + "|" + l);
        }

        state.locksHeld.get(t).add(l);

        /*if(state.locksHeld.get(t).size() == 1) {
            return false;
        }*/

        boolean deadlockDetected = detectDeadLock(state, t, l);

        VectorClock newD_t = new VectorClock(state.getVectorClock(state.D_t, t));
        HashSet<Lock> history = new HashSet<>(state.locksHeld.get(t));
        state.acqMap.get(t).add(new Acquire(eventCount-1, t, l, newD_t, history));

        // Check for dpp Deadlock
        return deadlockDetected;
    }

    /**
     * Helper method for initialising Acq FIFO queue
     * @param state data structure for ddp partial order
     * @param l lock
     * @param t thread
     */
    private void initializeAcqQueue(DDPState state, Lock l, Thread t) {
        ArrayDeque<Pair<VectorClock, VectorClock>> arrayDeque = new ArrayDeque<>();
        HashMap<Lock, ArrayDeque<Pair<VectorClock, VectorClock>>> map = new HashMap<>();
        map.put(l, arrayDeque);
        state.Acq_t_l.put(t, map);
    }

    /**
     * Handle Rel events
     * Line 25-39 in algorithm
     * @param state data structure for ddp partial order
     */
    private void handleSubRelease(DDPState state) {
        // Get t and l
        Thread t = getThread();
        Lock l = getLock();

        // Check if they exists and if not add t and l to state
        checkAndAddThread(state, t);
        checkAndAddLock(state, l);

        // Checks if AcqQueue has been initialized
        if(!state.Acq_t_l.containsKey(t)) {
            initializeAcqQueue(state, l, t);
        }

        if(!state.Acq_t_l.get(t).containsKey(l)) {
            ArrayDeque<Pair<VectorClock, VectorClock>> arrayDeque = new ArrayDeque<>();
            state.Acq_t_l.get(t).put(l, arrayDeque);
        }

        // Line 26
        while(!state.Acq_t_l.get(t).get(l).isEmpty()) {
            // Line 27
            Pair<VectorClock, VectorClock> p = state.Acq_t_l.get(t).get(l).poll();
            // Line 28-29
            VectorClock P_t = state.getVectorClock(state.P_t, t);
            assert p != null;
            if(!p.second.isLessThanOrEqual(P_t)){
                break;
            }
            // Line 30
            VectorClock H_prime = state.Rel_t_l.get(t).get(l).poll();
            // Line 31
            if(!p.first.isLessThanOrEqual(state.getVectorClock(state.T_t, t))){
                // Line 32
                P_t.updateWithMax(H_prime);

                // Line 33
                VectorClock D_t = state.getVectorClock(state.D_t, t);
                D_t.updateWithMax(H_prime);
            }
            // Line 34-35
            if (!state.Acq_t_l.get(t).get(l).isEmpty()) {
                state.Acq_t_l.get(t).get(l).remove();
            }
            if(!state.Rel_t_l.get(t).get(l).isEmpty()) {
                state.Rel_t_l.get(t).get(l).remove();
            }
        }

        // Line 36
        VectorClock H_t = state.getVectorClock(state.H_t, t);
        state.H_l.replace(l, H_t);

        // Line 37
        VectorClock P_t = state.getVectorClock(state.P_t, t);
        state.P_l.replace(l, P_t);

        // Line 38-39
        for(Thread t_prime: state.threadSet) {
            checkAndAddToRelQueue(state, l, t_prime);
            VectorClock newH_t = new VectorClock(H_t);
            state.Rel_t_l.get(t_prime).get(l).add(newH_t);
        }
        if(print_events) {
            System.out.println("Rel|" + t + "|" + l);
        }

        // Update threadToLockSet such that thread t no longer holds lock l
        state.locksHeld.get(t).remove(l);
    }

    /**
     * Helper method for adding to Rel FIFO queue
     * @param state data structure for ddp partial order
     * @param l lock
     * @param t_prime thread
     */
    private void checkAndAddToRelQueue(DDPState state, Lock l, Thread t_prime) {
        if(!state.Rel_t_l.containsKey(t_prime)){
            ArrayDeque<VectorClock> arrayDeque = new ArrayDeque<>();
            HashMap<Lock, ArrayDeque<VectorClock>> map = new HashMap<>();
            map.put(l, arrayDeque);
            state.Rel_t_l.put(t_prime, map);
        }

        if(!state.Rel_t_l.get(t_prime).containsKey(l)){
            ArrayDeque<VectorClock> arrayDeque = new ArrayDeque<>();
            state.Rel_t_l.get(t_prime).put(l, arrayDeque);
        }
    }

    /**
     * Handle Read events
     * @param state data structure for ddp partial order
     */
    private void handleSubRead(DDPState state) {
        // Get params t and x
        Thread t = getThread();
        Variable x = getVariable();

        // Check if they exists and if not add t and x to state
        checkAndAddThread(state, t);
        checkAndAddVariable(state, x);

        // Line 41
        for(Thread t_prime: state.threadSet) {
            // Line 42
            VectorClock TWrite_t_prime_x = state.TWrite_t_x.get(t_prime).get(x);
            VectorClock T_t = state.getVectorClock(state.T_t, t);
            if(!TWrite_t_prime_x.isLessThanOrEqual(T_t)) {
                // Line 43
                int index = state.threadToIndex.get(t);
                VectorClock HWrite_t_prime_x = state.getVectorClock(state.HWrite_t_x.get(t_prime), x);
                VectorClock P_t = new VectorClock(state.numThreads);
                P_t.copyFrom(state.getVectorClock(state.P_t, t));
                P_t.updateWithMax(HWrite_t_prime_x);
                state.P_t.set(index, P_t);

                // Line 44
                VectorClock H_t = new VectorClock(state.numThreads);
                H_t.copyFrom(state.getVectorClock(state.H_t, t));
                H_t.updateWithMax(HWrite_t_prime_x);
                state.H_t.set(index, H_t);

                // Line 45
                VectorClock D_t = state.getVectorClock(state.D_t, t);
                D_t.updateWithMax(HWrite_t_prime_x);
                state.D_t.set(index, D_t);
            }
        }
        // Line 46
        VectorClock H_t = state.getVectorClock(state.H_t, t);
        state.HRead_t_x.get(t).replace(x, H_t);

        // Line 47
        VectorClock T_t = state.getVectorClock(state.T_t, t);
        state.TRead_t_x.get(t).replace(x, T_t);


        if(print_events) {
            System.out.println("Read|" + t + "|" + x);
        }
    }

    /**
     * Handle Write events
     * @param state data structure for ddp partial order
     */
    private void handleSubWrite(DDPState state) {
        // Get and add params t and x if they does not exist in state
        Thread t = getThread();
        Variable x = getVariable();

        // Check if they exists and if not add t and x to state
        checkAndAddThread(state, t);
        checkAndAddVariable(state, x);

        // Line 49
        for(Thread t_prime: state.threadSet) {
            // Line 50
            VectorClock TWrite_t_prime_x = state.getVectorClock(state.TWrite_t_x.get(t_prime), x);
            VectorClock T_t = state.getVectorClock(state.T_t, t);
            if(!TWrite_t_prime_x.isLessThanOrEqual(T_t)) {
                // Line 51
                int index = state.threadToIndex.get(t);
                VectorClock P_t = state.getVectorClock(state.P_t, t);
                VectorClock PWrite_t_prime_x = state.getVectorClock(state.PWrite_t_x.get(t_prime), x);
                P_t.updateWithMax(PWrite_t_prime_x);
                state.P_t.set(index, P_t);

                // Line 52
                VectorClock H_t = state.getVectorClock(state.H_t, t);
                VectorClock HWrite_t_prime_x = state.getVectorClock(state.HWrite_t_x.get(t_prime), x);
                H_t.updateWithMax(HWrite_t_prime_x);
                state.H_t.set(index, H_t);

                // Line 53
                VectorClock D_t = state.getVectorClock(state.D_t, t);
                D_t.updateWithMax(PWrite_t_prime_x);
                state.D_t.set(index, D_t);
            }
            // Line 54
            VectorClock TRead_t_prime_x = state.getVectorClock(state.TRead_t_x.get(t_prime), x);
            if(!TRead_t_prime_x.isLessThanOrEqual(T_t)) {
                // Line 55
                int index = state.threadToIndex.get(t);
                VectorClock P_t = state.getVectorClock(state.P_t, t);
                VectorClock HRead_t_prime_x = state.getVectorClock(state.HRead_t_x.get(t_prime), x);
                P_t.updateWithMax(HRead_t_prime_x);
                state.P_t.set(index, P_t);

                // Line 56
                VectorClock H_t = state.getVectorClock(state.H_t, t);
                H_t.updateWithMax(HRead_t_prime_x);
                state.H_t.set(index, H_t);

                // Line 57
                VectorClock D_t = state.getVectorClock(state.D_t, t);
                D_t.updateWithMax(HRead_t_prime_x);
                state.D_t.set(index, D_t);
            }
        }
        // Line 58
        VectorClock H_t = new VectorClock(state.getVectorClock(state.H_t, t));
        state.HWrite_t_x.get(t).replace(x, H_t);

        // Line 59
        VectorClock T_t = new VectorClock(state.getVectorClock(state.T_t, t));
        state.TWrite_t_x.get(t).replace(x, T_t);

        // Line 60
        VectorClock P_t = new VectorClock(state.getVectorClock(state.P_t, t));
        state.PWrite_t_x.get(t).replace(x, P_t);

        if(print_events) {
            System.out.println("Write|" + t + "|" + x);
        }
    }

    /**
     * Handle Fork events
     * @param state data structure for ddp partial order
     */
    private void handleSubFork(DDPState state) {
        // Get params t and u
        Thread t = getThread();
        Thread u = getTarget();

        // Check and add thread t and thread u if they don't exist
        checkAndAddThread(state, t);
        checkAndAddThread(state, u);

        // Line 62-65
        int index = state.threadToIndex.get(u);
        componentAssignment(state, t, u, state.H_t, index);
        componentAssignment(state, t, u, state.T_t, index);
        componentAssignment(state, t, u, state.D_t, index);
        state.P_t.set(index, state.getVectorClock(state.P_t, t));

        if(print_events) {
            System.out.println("Fork|" + t + "|" + u);
        }

        // LocksHeld update
        HashSet<Lock> locksInT = state.locksHeld.get(t);
        HashSet<Lock> locksInU = new HashSet<>(locksInT);
        state.locksHeld.put(u, locksInU);
    }

    /**
     * Helper method for assigning C_u:=C_t[1/u]
     * @param state data structure for ddp partial order
     * @param t thread
     * @param u thread
     * @param target vector clock array
     * @param index index of vector clock to be set
     */
    private void componentAssignment(DDPState state, Thread t, Thread u, ArrayList<VectorClock> target, int index) {
        VectorClock t_clock = state.getVectorClock(target, t);
        VectorClock u_clock = state.getVectorClock(target, u);
        u_clock.copyFrom(t_clock);
        u_clock.setClockIndex(index, 1);
    }

    /**
     * Handle Join events
     * @param state data structure for ddp partial order
     */
    private void handleSubJoin(DDPState state) {
        // Get params t and u
        Thread t = getThread();
        Thread u = getTarget();

        // Check and add thread t and thread u if they don't exist
        checkAndAddThread(state, t);
        checkAndAddThread(state, u);

        // Line 67-70
        VectorClock H_t = state.getVectorClock(state.H_t, t);
        VectorClock H_u = state.getVectorClock(state.H_t, u);
        H_t.updateWithMax(H_u);

        VectorClock T_t = state.getVectorClock(state.T_t, t);
        VectorClock T_u = state.getVectorClock(state.T_t, u);
        T_t.updateWithMax(T_u);

        VectorClock D_t = state.getVectorClock(state.D_t, t);
        VectorClock D_u = state.getVectorClock(state.D_t, u);
        D_t.updateWithMax(D_u);

        VectorClock P_t = state.getVectorClock(state.P_t, t);
        VectorClock P_u = state.getVectorClock(state.P_t, u);
        P_t.updateWithMax(P_u);


        if(print_events) {
            System.out.println("Join|" + t + "|" + u);
        }

        // LocksHeld update
        HashSet<Lock> emptySet = new HashSet<>();
        state.locksHeld.put(u, emptySet);
    }

    private void handleSubBegin(DDPState state) {
        if(print_events) {
            System.out.println("Begin");
        }
    }

    private void handleSubEnd(DDPState state) {
        if(print_events) {
            System.out.println("End");
        }
    }

    /**
     * Check if lock l exists in state and if not initialize it in state
     * @param state data structure for ddp partial order
     * @param l lock
     */
    private void checkAndAddLock(DDPState state, Lock l) {
        // Check if lock l exists and add it data structure and H_l and P_l
        if(!state.lockSet.contains(l)) {
            state.lockSet.add(l);
            state.H_l.put(l, new VectorClock(state.numThreads));
            state.P_l.put(l, new VectorClock(state.numThreads));
        }
        // Loop over all threads and initialize Acq and Rel Queues if they are not already
        for(Thread t: state.threadSet) {
            boolean initializedAcg =  state.Acq_t_l.containsKey(t);
            if(!initializedAcg) {
                ArrayDeque<Pair<VectorClock, VectorClock>> pairArrayDeque = new ArrayDeque<>();
                HashMap<Lock, ArrayDeque<Pair<VectorClock, VectorClock>>> acqMap = new HashMap<>();
                acqMap.put(l, pairArrayDeque);
                state.Acq_t_l.put(t, acqMap);
            }
            boolean initializedRel = state.Rel_t_l.containsKey(t);
            if(!initializedRel) {
                ArrayDeque<VectorClock> arrayDeque = new ArrayDeque<>();
                HashMap<Lock, ArrayDeque<VectorClock>> readMap = new HashMap<>();
                readMap.put(l, arrayDeque);
                state.Rel_t_l.put(t, readMap);
            }
        }
    }

    /**
     * Check if Thread t exists in state and if not initialize it in state
     * @param state data structure for ddp partial order
     * @param t thread
     */
    private void checkAndAddThread(DDPState state, Thread t) {
        if(!state.threadSet.contains(t)) {
            state.threadSet.add(t);
            state.threadToIndex.put(t, state.threadSet.size() - 1);
            int index = state.threadToIndex.get(t);
            state.P_t.add(index, new VectorClock(state.numThreads));

            state.H_t.add(index, new VectorClock(state.numThreads));
            state.H_t.get(index).setClockIndex(index, 1);

            state.T_t.add(index, new VectorClock(state.numThreads));
            state.T_t.get(index).setClockIndex(index, 1);

            state.D_t.add(index, new VectorClock(state.numThreads));
            state.D_t.get(index).setClockIndex(index, 1);

            HashMap<Lock, ArrayDeque<Pair<VectorClock, VectorClock>>> acqMap = new HashMap<>();
            state.Acq_t_l.put(t, acqMap);

            HashMap<Lock, ArrayDeque<VectorClock>> relMap = new HashMap<>();
            state.Rel_t_l.put(t, relMap);

            for(Lock l: state.lockSet) {
                state.Acq_t_l.get(t).put(l, new ArrayDeque<>());
                state.Rel_t_l.get(t).put(l, new ArrayDeque<>());
            }
        }
    }

    /**
     * Check if variable x exists in state and if not initialize it in state
     * @param state data structure for ddp partial order
     * @param x variable
     */
    private void checkAndAddVariable(DDPState state, Variable x) {
        state.variableSet.add(x);
        for(Thread t: state.threadSet) {
            checkAndAddInitThreadMap(state, state.HRead_t_x, x, t);
            checkAndAddInitThreadMap(state, state.HWrite_t_x, x, t);
            checkAndAddInitThreadMap(state, state.TRead_t_x, x, t);
            checkAndAddInitThreadMap(state, state.TWrite_t_x, x, t);
            checkAndAddInitThreadMap(state, state.PWrite_t_x, x, t);
        }
    }

    /**
     * Initialise Hashmap threadMap for thread t and variable x
     * @param state data structure for ddp partial order
     * @param threadMap map to be checked if should initialized with x and t
     * @param x variable
     * @param t thread
     */
    private void checkAndAddInitThreadMap(DDPState state, HashMap<Thread, HashMap<Variable, VectorClock>> threadMap, Variable x, Thread t) {
        if(!threadMap.containsKey(t)) {
            HashMap<Variable, VectorClock> map = new HashMap<>();
            map.put(x, new VectorClock(state.numThreads));
            threadMap.put(t, map);
        } else if(!threadMap.get(t).containsKey(x)) {
            threadMap.get(t).put(x, new VectorClock(state.numThreads));
        }
    }

    /**
     * Used for updating the vector clocks H_t, T_t and D_t after analysis of event e
     * This is also described in the DCP paper
     * @param state data structure for ddp partial order
     */
    public void increment(DDPState state) {
        Thread t = getThread();
        int index = state.threadToIndex.get(t);

        // Increment H_t
        int curH = state.H_t.get(index).getClockIndex(index);
        state.H_t.get(index).setClockIndex(index, curH + 1);
        // Increment T_t
        int curT = state.T_t.get(index).getClockIndex(index);
        state.T_t.get(index).setClockIndex(index, curT + 1);
        // Increment D_t
        int curD = state.D_t.get(index).getClockIndex(index);
        state.D_t.get(index).setClockIndex(index, curD + 1);
    }

    /**
     * Method for detecting deadlocks used only for acquire events
     * @param state data structure for ddp partial order
     * @param t thread that does acquire
     * @param l lock that which is requested in acquire event
     * @return whether or not a deadlock has been detected
     */
    public boolean detectDeadLock(DDPState state, Thread t, Lock l) {
        boolean deadlockDetected = false;
        for(Thread t_prime: state.threadSet) {
            if(t_prime.equals(t)) {
                continue;
            }
            ArrayList<Acquire> acqList = state.acqMap.get(t_prime);
            VectorClock D_t = state.getVectorClock(state.D_t, t);

            for(int i = acqList.size()-1; i > 0; i--) {
                Acquire acq = acqList.get(i);

                boolean ddpOrdered = acq.getD_t().isLessThanOrEqual(D_t);
                if(ddpOrdered) {
                    break;
                }

                boolean sameLock = l == acq.getL();
                if(sameLock) {
                    continue;
                }

                HashSet<Lock> intersection = new HashSet<>(state.locksHeld.get(t));
                intersection.retainAll(acq.history);
                HashSet<Lock> resultLockSet = new HashSet<>();
                resultLockSet.add(l);
                resultLockSet.add(acq.getL());

                if(!resultLockSet.containsAll(intersection)) {
                    continue;
                }
                if(!intersection.containsAll(resultLockSet)) {
                    continue;
                }

                // We have discovered a DDP-deadlock
                deadLocks.add(new Pair<>(new Pair<>(acq.getId(), acq.getL()), new Pair<>((eventCount -1), l)));
                deadlockDetected = true;
            }
        }
        return deadlockDetected;
    }
}
