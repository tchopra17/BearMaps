package bearmaps.hw4;

import bearmaps.proj2ab.DoubleMapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    double time;
    int explored;
    LinkedList<Vertex> solutions;
    double totalWeight;
    SolverOutcome solved = SolverOutcome.UNSOLVABLE;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        DoubleMapPQ<Vertex> solver = new DoubleMapPQ<>();
        HashMap<Vertex, Double> distTo = new HashMap<>();
        HashMap<Vertex, Vertex> edgeTo = new HashMap<>();
        solutions = new LinkedList<>();
        solver.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);
        while (solver.size() != 0
                && !solver.getSmallest().equals(end)
                && sw.elapsedTime() <= timeout) {
            Vertex p = solver.removeSmallest();
            explored++;
            if (input.neighbors(p).size() == 0) {
                solved = SolverOutcome.UNSOLVABLE;
            }
            for (WeightedEdge<Vertex> v : input.neighbors(p)) {
                Vertex f = v.from();
                Vertex t = v.to();
                double w = v.weight();

                if (!distTo.containsKey(t)) {
                    distTo.put(t, Double.MAX_VALUE);
                }
                if (distTo.get(f) + w < distTo.get(t)) {
                    edgeTo.put(t, f);
                    distTo.put(t, distTo.get(f) + w);
                    if (solver.contains(t)) {
                        double h = input.estimatedDistanceToGoal(t, end);
                        solver.changePriority(t, distTo.get(t) + h);
                    } else {
                        solver.add(t, distTo.get(t) + input.estimatedDistanceToGoal(t, end));
                    }
                }
            }
        }
        time = sw.elapsedTime();
        if (time > timeout) {
            solved = SolverOutcome.TIMEOUT;
        }
         if (solver.contains(end)) {
            solved = SolverOutcome.SOLVED;
        }

        if (!solved.equals(SolverOutcome.UNSOLVABLE)) {
            Vertex v = edgeTo.get(end);
            if (edgeTo.size() == 0) {
                solutions.addFirst(end);
            } else if (v != null) {
                solutions.addFirst(end);
                while (!v.equals(start)) {
                    solutions.addFirst(v);
                    v = edgeTo.get(v);
                }
                solutions.addFirst(start);
            }
            totalWeight = distTo.get(end);
        }



    }

    @Override
    public SolverOutcome outcome() {
        return solved;
    }

    @Override
    public List<Vertex> solution() {
        return solutions;
    }

    @Override
    public double solutionWeight() {
        return totalWeight;
    }

    @Override
    public int numStatesExplored() {
        return explored;
    }

    @Override
    public double explorationTime() {
        return time;
    }
}
