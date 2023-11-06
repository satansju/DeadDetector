package ddp;

import event.Lock;
import event.Thread;
import event.Variable;
import util.Pair;
import util.vectorclock.VectorClock;

import java.util.*;
/**
 * @project Bachelor in Computer Science at Aarhus University
 * title: Deadlock Dependently Precedes
 *
 * @author Simon Sataa-Yu Larsen on 7/5/2020
 *
 * The algorithm also store the TO, CHB, DCP and the less DCP partial orders and use
 * different clocks and queues for this purpose:
 * The Tt clock corresponds to the TO partial order.
 * Te is the value of clock Tt after processing event e on threat t.
 *
 * The Dt and Ht clocks corresponds to the DCP and the CHB partial orders.
 * Likewise De and He is the value of the clocks after processing event e.
 */

public class DDPState {
    public HashSet<Thread> threadSet;
    public HashSet<Lock> lockSet;
    public HashSet<Variable> variableSet;
    public HashMap<Thread, Integer> threadToIndex;
    public Integer numThreads;

    // - Partial Orders -
    // <DDP
    public ArrayList<VectorClock> P_t;
    // <=CHB
    public ArrayList<VectorClock> H_t;
    // <=TO
    public ArrayList<VectorClock> T_t;
    // <=DDP
    public ArrayList<VectorClock> D_t;

    // - Fifo Queues -
    // Vector Time
    public HashMap<Thread, HashMap<Lock, ArrayDeque<Pair<VectorClock, VectorClock>>>> Acq_t_l;
    // Pair of Vector Times
    public HashMap<Thread, HashMap<Lock, ArrayDeque<VectorClock>>> Rel_t_l;

    // CHB Time for Last Read(x) <t,r(x)>
    public HashMap<Thread, HashMap<Variable, VectorClock>> HRead_t_x;
    // CHB Time for Last Write(x) <t,w(x)>
    public HashMap<Thread, HashMap<Variable, VectorClock>> HWrite_t_x;
    // TO Time for Last Read(x) <t,r(x)>
    public HashMap<Thread, HashMap<Variable, VectorClock>> TRead_t_x;
    // TO Time for Last Write(x) <t,w(x)>
    public HashMap<Thread, HashMap<Variable, VectorClock>> TWrite_t_x;
    // DDP Time for Last Write(x) <t,w(x)>
    public HashMap<Thread, HashMap<Variable, VectorClock>> PWrite_t_x;

    // - VectorClock -
    // <=CHB
    public HashMap<Lock, VectorClock> H_l;
    // <DDP
    public HashMap<Lock, VectorClock> P_l;

    // locksHeld are used for knowing which locks are held in the locksHeldBy
    // when processing event with thread t and lock l
    //public HashMap<Thread, HashMap<Lock, HashSet<Lock>>> locksHeld;

    // locksHeld is used for containing the locks held by thread t after processing current event
    public HashMap<Thread, HashSet<Lock>> locksHeld;

    public HashMap<Thread, ArrayList<Acquire>> acqMap;

    // Contains P_t for each thread and for each lock at a given moment
    //public HashMap<Thread, HashMap<Lock, Pair<Long, VectorClock>>> acquires;


    public DDPState(HashSet<Thread> threadSet) {
        setup(threadSet);
        initialization();
    }

    /**
     * Initializing internal data structures
     * @param threadSet
     * threadsSet is the threads appearing in the trace.
     */
    private void setup(HashSet<Thread> threadSet) {
        this.threadSet = new HashSet<>();
        this.lockSet = new HashSet<>();
        this.variableSet = new HashSet<>();
        this.threadToIndex = new HashMap<>();
        this.numThreads = 0;
        //this.locksHeld = new HashMap<>();
        //this.acquires = new HashMap<>();
        this.acqMap = new HashMap<>();
        this.locksHeld = new HashMap<>();


        for (Thread thread : threadSet) {
            this.threadToIndex.put(thread, this.numThreads);
            //locksHeld.put(thread, new HashMap<>());
            locksHeld.put(thread, new HashSet<>());
            //acquires.put(thread, new HashMap<>());
            this.numThreads++;
            this.acqMap.put(thread, new ArrayList<>());
        }
    }

    /**
     * Initializing data structures used for DDP vector clock algorithm
     */
    private void initialization() {
        this.P_t = new ArrayList<>();
        this.H_t = new ArrayList<>();
        this.T_t = new ArrayList<>();
        this.D_t = new ArrayList<>();

        this.Acq_t_l = new HashMap<>();
        this.Rel_t_l = new HashMap<>();

        this.HRead_t_x  = new HashMap<>();
        this.HWrite_t_x = new HashMap<>();
        this.TRead_t_x  = new HashMap<>();
        this.TWrite_t_x = new HashMap<>();
        this.PWrite_t_x = new HashMap<>();

        this.H_l = new HashMap<>();
        this.P_l = new HashMap<>();
    }

    /**
     * Used for printing resulting clocks and queues in memory
     */
    public void printMemory(){
        System.err.println("Number of threads = " + this.numThreads);
        System.err.println("Number of locks = " + this.lockSet.size());
        System.err.println("Number of variables = " + this.variableSet.size());
        printError(getThreads());
        printError(getLocks());
        printError(getQueues());
        printError(getOrders());
    }

    /**
     * Printing as error msg instead of standard prompt
     * @param strings input array of strings to be printed
     */
    public void printError(ArrayList<String> strings) {
        for (String s: strings) {
            System.err.println(s);
        }
    }

    public ArrayList<String> getThreads() {
        ArrayList<String> s = new ArrayList<>();
        s.add("P_t = " + P_t);
        s.add("H_t = " + H_t);
        s.add("T_t = " + T_t);
        s.add("D_t = " + D_t);
        s.add("");
        return s;
    }

    public ArrayList<String>  getLocks() {
        ArrayList<String> s = new ArrayList<>();
        s.add("H_l = " + H_l);
        s.add("P_l = " + P_l);
        s.add("");
        return s;
    }

    public ArrayList<String>  getQueues() {
        ArrayList<String> s = new ArrayList<>();
        s.add("Acq = " + Acq_t_l);
        s.add("Rel = " + Rel_t_l);
        s.add("");
        return s;
    }

    public ArrayList<String>  getOrders() {
        ArrayList<String> s = new ArrayList<>();
        s.add("HRead_t_x  = " + HRead_t_x);
        s.add("HWrite_t_x = " + HWrite_t_x);
        s.add("TRead_t_x  = " + TRead_t_x);
        s.add("TWrite_t_x = " + TWrite_t_x);
        s.add("PWrite_t_x = " + PWrite_t_x);
        s.add("");
        return s;
    }

    public VectorClock getVectorClock(ArrayList<VectorClock> arrayList, Thread t) {
        int index = threadToIndex.get(t);
        return getVectorClockFromArray(arrayList, index);
    }

    public VectorClock getVectorClockFromArray(ArrayList<VectorClock> arrayList, int index) {
        if (index < 0 || index >= arrayList.size()) {
            throw new IllegalArgumentException("Illegal Out of Bound access");
        }
        return arrayList.get(index);
    }

    public VectorClock getVectorClock(HashMap<Variable, VectorClock> variableVectorClockHashMap, Variable x) {
        return variableVectorClockHashMap.get(x);
    }

    public VectorClock getVectorClock(HashMap<Lock, VectorClock> arrayList, Lock l) {
        return arrayList.get(l);
    }

    public ArrayList<String> getContent() {
        ArrayList<String> result = new ArrayList<>();
        result.addAll(getThreads());
        result.addAll(getLocks());
        result.addAll(getQueues());
        result.addAll(getOrders());
        return result;
    }
}
