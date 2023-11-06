import ddp.DDPEngine;

import java.io.File;
/**
 * @project Bachelor in Computer Science at Aarhus University
 * title: Deadlock Dependently Precedes
 *
 * @author Simon Sataa-Yu Larsen on 7/5/2020
 */
public class DeadDetector {

    public static void main(String[] args) {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("algokiller.std");

        // Default path
        //path = "C:\\Users\\Simon\\Downloads\\rapid-master\\rapid-master\\diningphilosophers.std";
        if(args != null) {
            if(args.length != 0) {
                // overwrite path
                path = args[0];
            }
        }

        boolean time_reporting = true;
        boolean print_events = false;

        long startTimeAnalysis = 0;
        if(time_reporting){
            startTimeAnalysis = System.currentTimeMillis(); //System.nanoTime();
        }

        DDPEngine engine = new DDPEngine(path, print_events);
        engine.analyzeTrace();
        long timeAnalysis = 0;
        if(time_reporting){
            long stopTimeAnalysis = System.currentTimeMillis(); //System.nanoTime();
            timeAnalysis = stopTimeAnalysis - startTimeAnalysis;
            System.out.println("Time for full analysis = " + timeAnalysis + " milliseconds");
            System.out.println("                       = " + timeAnalysis/1000 + " seconds");
        }
        if(engine.deadLockDetected) {
            engine.printDeadlock();
        }
        engine.writeResults(timeAnalysis, engine.eventCount);
    }
}
