package bearmaps;

import java.util.List;

public class KDTree implements PointSet {
    public static final boolean HORIZONTAL = false;
    private Node root;
    private Point nearest;

    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = add(p, root, HORIZONTAL);
        }
    }

    private Node add(Point p, Node n, boolean orientation) {
        if (n == null) {
            return new Node(p, orientation);
        }
        if (p.equals(n.p)) {
            return n;
        }
        int compare = comparePoints(p, n.p, orientation);
        if (compare <= 0) {
            n.left = add(p, n.left, !orientation);
        } else if (compare > 0) {
            n.right = add(p, n.right, !orientation);
        }
        return n;
    }

    private int comparePoints(Point a, Point b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(a.getX(), b.getX());
        } else {
            return Double.compare(a.getY(), b.getY());
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point toCheck = new Point(x, y);
        nearestHelper(root, toCheck, HORIZONTAL);
        return nearest;
    }

    private void nearestHelper(Node n, Point toCheck, boolean orientation) {
        if (n == null) {
            return;
        }

        if (nearest == null) {
            nearest = n.p;
        } else {
            double bestDistance = Math.sqrt(Point.distance(nearest, toCheck));
            double currentDistance = Math.sqrt(Point.distance(n.p, toCheck));

            if (currentDistance < bestDistance) {
                nearest = n.p;
            }
        }

        // checking left right
        if (orientation == HORIZONTAL) {
            // if point is to the left of node
            if (n.p.getX() > toCheck.getX()) {
                nearestHelper(n.left, toCheck, !orientation);
                if (n.right != null) {
                    double closestDistance = n.p.getX() - toCheck.getX();
                    if (closestDistance < Math.sqrt(Point.distance(nearest, toCheck))) {
                        nearestHelper(n.right, toCheck, !orientation);
                    }
                }
            } else {
                // if point is to the right of node
                nearestHelper(n.right, toCheck, !orientation);
                if (n.left != null) {
                    double closestDistance = toCheck.getX() - n.p.getX();
                    if (closestDistance < Math.sqrt(Point.distance(nearest, toCheck))) {
                        nearestHelper(n.left, toCheck, !orientation);
                    }
                }
            }
        } else {
            // checking up down
            if (n.p.getY() > toCheck.getY()) {
                // if point is to the bottom of node
                nearestHelper(n.left, toCheck, !orientation);
                if (n.right != null) {
                    double closestDistance = n.p.getY() - toCheck.getY();
                    if (closestDistance < Math.sqrt(Point.distance(nearest, toCheck))) {
                        nearestHelper(n.right, toCheck, !orientation);
                    }
                }
            } else {
                // if point is to the top of node
                nearestHelper(n.right, toCheck, !orientation);
                if (n.left != null) {
                    double closestDistance = toCheck.getY() - n.p.getY();
                    if (closestDistance < Math.sqrt(Point.distance(nearest, toCheck))) {
                        nearestHelper(n.left, toCheck, !orientation);
                    }
                }
            }
        }
    }

    /*private Point checkLeft(Node n, Point toCheck, double bestDistance){
        if (n.left == null){
            return n.p;
        }
        Node left = n.left;
        double leftDistance = Math.sqrt(Point.distance(left.p, toCheck));
        if (leftDistance < bestDistance){
            return n.left.p;
        }
        return n.p;
    }
    private Point checkRight(Node n, Point toCheck, double bestDistance){
        if (n.right == null){
            return n.p;
        }
        Node right = n.right;
        double rightDistance = Math.sqrt(Point.distance(right.p, toCheck));
        if (rightDistance < bestDistance){
            return n.right.p;
        }
        return n.p;
    }
*/

    private class Node {
        private Point p;
        private boolean orientation;
        private Node left; // down
        private Node right; // up

        Node(Point p, boolean orientation) {
            this.p = p;
            this.orientation = orientation;
        }
    }

}
