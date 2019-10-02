package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;

public class KDTreeTest {
    private static Random r = new Random(500);

    private KDTree buildLecture() {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);
        Point p8 = new Point(2, 7);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7, p8));
        return kd;
    }

    @Test
    public void testNearestDemo() {
        KDTree kd = buildLecture();
        Point actual = kd.nearest(0, 7);
        Point expected = new Point(2, 7);
        assertEquals(expected, actual);
    }

    private Point rPoint() {
        double x = r.nextDouble();
        //x = StdRandom.uniform(500);
        double y = r.nextDouble();
        //  y = StdRandom.uniform(500);
        return new Point(x, y);
    }

    private List<Point> randomPoints(int N) {
        List<Point> l = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            l.add(rPoint());
        }
        return l;
    }

    @Test
    public void test1000Points() {
        List<Point> points = randomPoints(10000);
        NaivePointSet nps = new NaivePointSet(points);
        KDTree kd = new KDTree(points);

        List<Point> point200 = randomPoints(2000);
        for (Point p : point200) {
            Point expected = nps.nearest(p.getX(), p.getY());
            Point actual = kd.nearest(p.getX(), p.getY());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void timeTestKD() {
        List<Point> points = randomPoints(100000);
        KDTree kd = new KDTree(points);

        Stopwatch sw = new Stopwatch();
        List<Point> point200 = randomPoints(20000);
        for (Point p : point200) {
            Point actual = kd.nearest(p.getX(), p.getY());
        }
        System.out.println(sw.elapsedTime());
    }

    @Test
    public void timeTestNaive() {
        List<Point> points = randomPoints(100000);
        NaivePointSet nps = new NaivePointSet(points);

        Stopwatch sw = new Stopwatch();
        List<Point> point200 = randomPoints(20000);
        for (Point p : point200) {
            Point actual = nps.nearest(p.getX(), p.getY());
        }
        System.out.println(sw.elapsedTime());
    }
}
