package bearmaps;


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {
    @Test
    public void testAdd() {
        ArrayHeapMinPQ h = new ArrayHeapMinPQ();
        for (int i = 2; i <= 1000001; i++) {
            h.add(i, i);
        }
        assertEquals(h.size(), 1000000);
        h.add("hi", 1);
        assertEquals(h.getSmallest(), "hi");
    }

    @Test
    public void testRemove() {
        ArrayHeapMinPQ h = new ArrayHeapMinPQ();
        NaiveMinPQ n = new NaiveMinPQ();
        for (int i = 0; i < 100000; i++) {
            int a = StdRandom.uniform(100000);
            while (h.contains(a)) {
                a = StdRandom.uniform(100000);
            }
            int b = StdRandom.uniform(100000);
            h.add(a, a);
            n.add(a, a);
        }

        for (int i = 0; i < 100; i++) {
            Object a = h.removeSmallest();
            Object b = n.removeSmallest();
            assertEquals(a, b);
        }

    }

    @Test
    public void testChange() {
        ArrayHeapMinPQ h = new ArrayHeapMinPQ();
        NaiveMinPQ n = new NaiveMinPQ();
        for (int i = 0; i < 2000; i++) {
            int a = StdRandom.uniform(200000);
            while (h.contains(a)) {
                a = StdRandom.uniform(20000);
            }
            int b = StdRandom.uniform(20000);
            h.add(a, a);
            n.add(a, a);
        }
        Stopwatch s = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            int a = StdRandom.uniform(200000);
            h.changePriority(i, a);
            // n.changePriority(i, a);
        }
        System.out.println(s.elapsedTime());
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 1000; i++) {
            int a = StdRandom.uniform(200000);
            n.changePriority(i, a);
            // n.changePriority(i, a);
        }
        System.out.println(sw.elapsedTime());


    }
}
