package ddp;

import event.Lock;
import event.Thread;
import parser.ParseStandard;
import util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
/**
 * Engine class for analysing traces for deadlocks
 * @project Bachelor in Computer Science at Aarhus University
 * title: Deadlock Dependently Precedes
 *
 * @author Simon Sataa-Yu Larsen on 7/5/2020
 */
public class DDPEngine {
    public boolean deadLockDetected;
    HashSet<Thread> threadSet;
    DDPState state;
    ParseStandard parser;
    DDPHandler handler;
    String path;
    boolean print_events;
    public Long eventCount = (long) 0;

    public DDPEngine(String path, boolean print_events) {
        this.path = path;
        this.print_events = print_events;
        this.threadSet = new HashSet<>();
        initializeReader(path);
        this.state = new DDPState(this.threadSet);
        this.handler = new DDPHandler(print_events);
        this.deadLockDetected = false;
    }

    protected void initializeReader(String trace_file) {
        this.parser = new ParseStandard(trace_file, true);
        this.threadSet = parser.getThreadSet();
    }
    
    public void analyzeEvent(DDPHandler handler, long eventCount) {
        try{
            if(handler.handle(state)) {
                deadLockDetected = true;
            }
            handler.increment(state);
        }
        catch(OutOfMemoryError oome){
            oome.printStackTrace();
            System.err.println("Number of events = " + eventCount);
            state.printMemory();
        }
    }

    public void analyzeTrace() {
        if(print_events) {
            System.out.println(" - Beginning Analysis - ");
            System.out.println("\nTrace:");
        }

        while(parser.hasNext()){
            eventCount = eventCount + 1;
            parser.getNextEvent(handler);
            analyzeEvent(handler, eventCount);
        }
        if(print_events) {
            printStats();
            state.printMemory();
        }
    }

    private void printStats() {
        for (String s: getStats()) {
            System.out.println(s);
        }
    }

    private ArrayList<String> getStats() {
        ArrayList<String> list = new ArrayList<>();
        list.add(" - Analysis complete  - \n");
        list.add("Number of events found = " + eventCount);
        list.add("Threads   = " + state.threadSet.size()); //state.threadSet.toString());
        list.add("Locks     = " + state.lockSet.size()); //state.lockSet.toString());
        list.add("Variables = " + state.variableSet.size()); //state.variableSet.toString());
        return list;
    }

    public void writeResults(long timeAnalysis, long numberOfEvents) {
        try {
            String folderName = "deadlock_analysis_output";
            File theDir = new File(folderName);
            if (!theDir.exists()) {
                theDir.mkdir();
            }
            Writer w = new FileWriter(getNewFileName(folderName));

            ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> list = handler.deadLocks;
            w.write("Processed: " + numberOfEvents + " events");
            w.write("Detected " + list.size() + " deadlock" + (list.size() == 1 ? ":" : "s:") + System.lineSeparator());
            for(Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair: list) {
                String deadlockDescription = "\n* Deadlock Detected with events: e" + pair.first.first + " on lock " + pair.first.second + " and e" + pair.second.first + " on lock " + pair.second.second;
                w.write(deadlockDescription + System.lineSeparator());
            }
            if(timeAnalysis != 0) {
                w.write("Time for full analysis = " + timeAnalysis + " milliseconds" + System.lineSeparator());
                w.write("                       = " + timeAnalysis/1000 + " seconds" + System.lineSeparator());
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private String getNewFileName(String folderName) {
        String fileName = new File(path).getName();
        String parentDir = path.substring(0, path.length() - fileName.length());
        parentDir = new File(parentDir).getParent();
        fileName = fileName.substring(0, fileName.length()-4) + "_out.txt";
        String newFileName = parentDir + File.separator + folderName + File.separator + fileName;
        return newFileName;
    }

    public void printDeadlock() {
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> list = handler.deadLocks;
        System.out.println("Detected " + list.size() + " deadlock" + (list.size() == 1 ? ":" : "s:"));
        for(Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair: list) {
            String deadlockDescription = "\n* Deadlock Detected with events: e"  + pair.first.first + " on lock " + pair.first.second + " and e" + pair.second.first + " on lock " + pair.second.second;
            System.out.println(deadlockDescription);
        }

    }

    public ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> getDeadlocks() {
        return handler.deadLocks;
    }
}
