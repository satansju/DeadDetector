package Test;
import ddp.DDPEngine;
import event.Lock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Pair;

import java.io.File;
import java.util.ArrayList;

public class EngineTest {

    public long timeStart() {
        return System.currentTimeMillis();
    }

    public long getTimeAnalysis(long startTimeAnalysis) {
        long stopTimeAnalysis = System.currentTimeMillis();
        return stopTimeAnalysis - startTimeAnalysis;
    }

    @Test
    public void testInitializationOfEngine() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("f1_2.std");
        DDPEngine engine = new DDPEngine(path, false);
        Assertions.assertNotNull(engine);
    }

    @Test
    public void testDeadlocksNotNull() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("f1_2.std");
        DDPEngine engine = new DDPEngine(path, false);
        engine.analyzeTrace();
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertNotNull(deadlocks);
    }

    public void assertEqualForDeadlocks(Pair<Pair<Long, Lock>, Pair<Long, Lock>> deadlock, Pair<Pair<Long, Lock>, Pair<Long, Lock>>result) {
        Assertions.assertEquals(deadlock.first.first, result.first.first);
        Assertions.assertEquals(deadlock.first.second.getName(), result.first.second.getName());
        Assertions.assertEquals(deadlock.second.first, result.second.first);
        Assertions.assertEquals(deadlock.second.second.getName(), result.second.second.getName());
    }

    @Test
    public void testFigure1_2() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("f1_2.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Long e1 = 2L;
        Lock l1 = new Lock("2");
        Long e2 = 7L;
        Lock l2 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        assertEqualForDeadlocks(pair, deadlocks.get(0));
    }

    @Test
    public void testFigure4_1() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("f4_1.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> results = new ArrayList<>();
        Long e1 = 2L;
        Lock l1 = new Lock("2");
        Long e2 = 8L;
        Lock l2 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        assertEqualForDeadlocks(pair, deadlocks.get(0));
    }

    @Test
    public void testFigure4_2() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("f4_2.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> results = new ArrayList<>();
        Long e1 = 2L;
        Lock l1 = new Lock("2");
        Long e2 = 11L;
        Lock l2 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        assertEqualForDeadlocks(pair, deadlocks.get(0));
    }

    @Test
    public void testFigure4_3() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("f4_3.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Long e1 = 4L;
        Lock l1 = new Lock("3");
        Long e2 = 12L;
        Lock l2 = new Lock("2");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        assertEqualForDeadlocks(pair, deadlocks.get(0));
    }


    @Test
    public void testFigure4_4() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("f4_4.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Long e1 = 6L;
        Lock l1 = new Lock("6");
        Long e2 = 16L;
        Lock l2 = new Lock("5");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        assertEqualForDeadlocks(pair, deadlocks.get(0));
    }

    @Test
    public void testZigZag() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("zigzag.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testIndeterminism() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("indeterminism.std");
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> results = new ArrayList<>();
        Long e1 = 6L;
        Lock l1 = new Lock("2");
        Long e2 = 14L;
        Lock l2 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        results.add(pair);

        Long e3 = 10L;
        Lock l3 = new Lock("2");
        Long e4 = 14L;
        Lock l4 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair2 = new Pair<>(new Pair<>(e3, l3), new Pair<>(e4, l4));
        results.add(pair2);

        Long e5 = 2L;
        Lock l5 = new Lock("2");
        Long e6 = 14L;
        Lock l6 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair3 = new Pair<>(new Pair<>(e5, l5), new Pair<>(e6, l6));
        results.add(pair3);

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(deadlocks.size(), 3);
        boolean mismatch;
        for (Pair<Pair<Long, Lock>, Pair<Long, Lock>> deadlock : deadlocks) {
            boolean containsEvent1 = false;
            boolean containsLock1 = false;
            boolean containsEvent2 = false;
            boolean containsLock2 = false;
            for (Pair<Pair<Long, Lock>, Pair<Long, Lock>> result : results) {
                if (deadlock.first.first.equals(result.first.first)) containsEvent1 = true;
                if (deadlock.first.second.getName().equals(result.first.second.getName())) containsLock1 = true;
                if (deadlock.second.first.equals(result.second.first)) containsEvent2 = true;
                if (deadlock.second.second.getName().equals(result.second.second.getName())) containsLock2 = true;
            }
            Assertions.assertTrue(containsEvent1);
            Assertions.assertTrue(containsLock1);
            Assertions.assertTrue(containsEvent2);
            Assertions.assertTrue(containsLock2);
        }
    }

    @Test
    public void testLock_w() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("lock_w.std");

        DDPEngine engine = new DDPEngine(path, false);
        engine.analyzeTrace();
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testNewLocks() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("newlocks.std");
        Long e1 = 6L;
        Lock l1 = new Lock("2");
        Long e2 = 10L;
        Lock l2 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(deadlocks.size(), 1);
        assertEqualForDeadlocks(pair, deadlocks.get(0));
    }

    @Test
    public void testNewLocks2() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("newlocks2.std");
        Long e1 = 2L;
        Lock l1 = new Lock("2");
        Long e2 = 10L;
        Lock l2 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(deadlocks.size(), 1);
        assertEqualForDeadlocks(pair, deadlocks.get(0));
    }

    @Test
    public void testNewLocks3() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("newlocks3.std");
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> results = new ArrayList<>();
        Long e1 = 6L;
        Lock l1 = new Lock("2");
        Long e2 = 10L;
        Lock l2 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        results.add(pair);

        Long e3 = 2L;
        Lock l3 = new Lock("2");
        Long e4 = 10L;
        Lock l4 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair2 = new Pair<>(new Pair<>(e3, l3), new Pair<>(e4, l4));
        results.add(pair2);

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(deadlocks.size(), 2);
        for(int i = 0; i < deadlocks.size(); i++) {
            assertEqualForDeadlocks(results.get(i), deadlocks.get(i));
        }
    }

    @Test
    public void testNewLocks4() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("newlocks4.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(4, deadlocks.size());
    }

    @Test
    public void testDoubleDeadlockPattern() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("nested.std");
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> results = new ArrayList<>();
        Long e1 = 3L;
        Lock l1 = new Lock("3");
        Long e2 = 8L;
        Lock l2 = new Lock("2");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair = new Pair<>(new Pair<>(e1, l1), new Pair<>(e2, l2));
        results.add(pair);

        Long e3 = 2L;
        Lock l3 = new Lock("2");
        Long e4 = 9L;
        Lock l4 = new Lock("1");
        Pair<Pair<Long, Lock>, Pair<Long, Lock>> pair2 = new Pair<>(new Pair<>(e3, l3), new Pair<>(e4, l4));
        results.add(pair2);


        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        for(int i = 0; i < deadlocks.size(); i++) {
            assertEqualForDeadlocks(results.get(i), deadlocks.get(i));
        }
    }


    @Test
    public void testAlgoKiller() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("algokiller.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testAlgoKiller2() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("algokiller2.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(12, deadlocks.size());
    }

    @Test
    public void testPMD() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("pmd.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testAccount() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("account.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testBoundedBuffer() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("boundedBuffer.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testBufWriter() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("bufwriter.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testClean() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("clean.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testDeadLock() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("deadlock.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testDiningPhilosophers() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("diningphilosophers.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(4, deadlocks.size());
    }

    @Test
    public void testFalseDeadlock() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("FalseDeadlock.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testTrueDeadlock() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("TrueDeadlock.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(1, deadlocks.size());
    }

    @Test
    public void testFOP() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("fop.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testGroovy() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("groovy.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testJython() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("jython.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testLongDeadlock() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("LongDeadlock.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(1, deadlocks.size());
    }

    @Test
    public void testPiper() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("piper.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testPool5() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("pool6.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testSleepingBarber() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("sleepingBarber.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }

    @Test
    public void testXALAN() {
        // Relative path to 'test traces' folder
        String path = new File("").getAbsolutePath().concat(File.separator + "test traces" + File.separator);

        // Set file name for test file in 'traces folder'
        path = path.concat("xalan.std");

        DDPEngine engine = new DDPEngine(path, false);
        long start = timeStart();
        engine.analyzeTrace();
        long timeAnalysis = getTimeAnalysis(start);
        engine.writeResults(timeAnalysis, engine.eventCount);
        ArrayList<Pair<Pair<Long, Lock>, Pair<Long, Lock>>> deadlocks = engine.getDeadlocks();
        Assertions.assertEquals(0, deadlocks.size());
    }
}
