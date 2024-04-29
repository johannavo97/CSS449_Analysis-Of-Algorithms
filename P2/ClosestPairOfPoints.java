import java.io.*;
import java.util.*;

/**
 * Implements the closest pair of points algorithm using a divide and conquer approach.
 * This class calculates the closest distance between pairs of points in a plane,
 * ensuring that the computation is efficient and outputs the results at each recursive call.
 * @author Johanna Vo
 * @Date 2024-04-26
 * @Course CSS 449
 * @Assignment Program 2
 */
public class ClosestPairOfPoints {

    /**
     * Computes the Euclidean distance between two points.
     * @param p1 The first point.
     * @param p2 The second point.
     * @return The distance between p1 and p2.
     */
    private static double distance(Point p1, Point p2) {
        return Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
    }

    /**
     * Computes the closest pair of points using brute force.
     * @param points A list of points to evaluate.
     * @return An array containing the closest pair of points and their distance.
     */
    private static Point[] closestBruteForce(List<Point> points, int low, int high ) {
        double minDist = Double.POSITIVE_INFINITY;
        Point p1 = null, p2 = null;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double d = distance(points.get(i), points.get(j));
                if (d < minDist) {
                    minDist = d;
                    p1 = points.get(i);
                    p2 = points.get(j);
                }
            }
        }
        System.out.printf("D[%d,%d]: %.4f\n", low, high - 1, minDist);
        return new Point[]{p1, p2, new Point(minDist, 0)};
    }

    /**
     * Recursively finds the closest pair of points in a set.
     * @param xSorted Points sorted by the x-coordinate.
     * @param ySorted Points sorted by the y-coordinate.
     * @param low The starting index.
     * @param high The ending index.
     * @return An array containing the closest pair of points found and their distance.
     */
    private static Point[] recursiveClosestPair(List<Point> xSorted, List<Point> ySorted, int low, int high) {
        int n = high - low;
        if (n <= 3) {
            return closestBruteForce(xSorted.subList(low, high), low, high);
        }

        int mid = low + (n + 1) / 2;
        Point[] leftPair = recursiveClosestPair(xSorted, ySorted, low, mid);
        Point[] rightPair = recursiveClosestPair(xSorted, ySorted, mid, high);

        double deltaLeft = leftPair[2].x;
        double deltaRight = rightPair[2].x;
        Point[] bestPair = deltaLeft < deltaRight ? leftPair : rightPair;
        double delta = Math.min(deltaLeft, deltaRight);

        List<Point> inBand = new ArrayList<>();
        for (Point point : ySorted) {
            if (Math.abs(point.x - xSorted.get(mid - 1).x) < delta) {
                inBand.add(point);
            }
        }

        for (int i = 0; i < inBand.size(); i++) {
            for (int j = i + 1; j < Math.min(i + 7, inBand.size()); j++) {
                double d = distance(inBand.get(i), inBand.get(j));
                if (d < delta) {
                    delta = d;
                    bestPair = new Point[]{inBand.get(i), inBand.get(j), new Point(d, 0)};
                }
            }
        }

        // Print the current range and the computed minimum distance.
        System.out.printf("D[%d,%d]: %.4f\n", low, high - 1, delta);
        return bestPair;
    }



    /**
     * Initiates the closest pair of points calculation.
     * @param points A list of points in two-dimensional space.
     * @return An array containing the closest pair of points and their distance.
     */
    public static Point[] closest(List<Point> points) {
        List<Point> xSorted = new ArrayList<>(points);
        xSorted.sort(Comparator.comparingDouble(p -> p.x));
        List<Point> ySorted = new ArrayList<>(points);
        ySorted.sort(Comparator.comparingDouble(p -> p.y));

        return recursiveClosestPair(xSorted, ySorted, 0, points.size());
    }

    /**
     * The main method to run the closest pair of points algorithm.
     * It reads points from a hardcoded file "program2data.txt" and prints the closest distances.
     * @param args Command line arguments (not used).
     * @throws FileNotFoundException If the specified file cannot be found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("program2trivialdata.txt"));
//        Scanner scanner = new Scanner(new File("program2data.txt"));
//        Scanner scanner = new Scanner(new File("program2data10000.txt"));
        List<Point> points = new ArrayList<>();
        int n = Integer.parseInt(scanner.nextLine().trim());
        while (scanner.hasNextDouble()) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            points.add(new Point(x, y));
        }
        scanner.close();
        closest(points);
    }
}
