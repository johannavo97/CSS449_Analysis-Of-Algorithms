//
// Created by Johanna Vo on 4/26/24.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <iomanip>

class Point {
public:
    double x, y;

    Point(double x, double y) : x(x), y(y) {}
};

bool compareX(const Point& a, const Point& b) {
    return a.x < b.x;
}

bool compareY(const Point& a, const Point& b) {
    return a.y < b.y;
}

double dist(const Point& p1, const Point& p2) {
    return sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
}

double bruteForce(std::vector<Point>& points, int left, int right) {
    double minDist = std::numeric_limits<double>::max();
    for (int i = left; i <= right; ++i) {
        for (int j = i + 1; j <= right; ++j) {
            minDist = std::min(minDist, dist(points[i], points[j]));
        }
    }
    return minDist;
}

double closestSplitPair(std::vector<Point>& px, std::vector<Point>& py, double delta, int left, int right) {
    double minDist = delta;
    Point midPoint = px[(left + right) / 2];
    std::vector<Point> stripe;

    for (int i = 0; i < py.size(); ++i) {
        if (fabs(py[i].x - midPoint.x) < delta) {
            stripe.push_back(py[i]);
        }
    }

    for (int i = 0; i < stripe.size(); ++i) {
        for (int j = i + 1; j < stripe.size() && (stripe[j].y - stripe[i].y) < delta; ++j) {
            minDist = std::min(minDist, dist(stripe[i], stripe[j]));
        }
    }
    return minDist;
}

double closestPairRecursive(std::vector<Point>& px, std::vector<Point>& py, int left, int right) {
    // Base case: If there are 2 or 3 points, use the brute force approach
    if (right - left <= 3) {
        return bruteForce(px, left, right);
    }

    // Find the middle index to split the points into two halves
    int mid = left + (right - left) / 2;

    // Adjust for odd number of points, giving one more to the left side
    if ((right - left + 1) % 2 == 1) {
        mid++;
    }

    // Recursively find the closest pairs in the left and right subsets
    double dl = closestPairRecursive(px, py, left, mid);
    double dr = closestPairRecursive(px, py, mid + 1, right);

    // Find the closest distance seen so far
    double d = std::min(dl, dr);

    // Find the closest split pair distance
    double dSplit = closestSplitPair(px, py, d, left, right);

    // The smallest distance is the minimum of d and dSplit
    double dMin = std::min(d, dSplit);

    // Output the results with four digits of precision
    std::cout << std::fixed << std::setprecision(4) << "D[" << left << "," << right << "]: " << dMin << std::endl;

    return dMin;
}


double closestPair(std::vector<Point>& points) {
    std::vector<Point> px(points), py(points);
    std::sort(px.begin(), px.end(), compareX);
    std::sort(py.begin(), py.end(), compareY);
    return closestPairRecursive(px, py, 0, px.size() - 1);
}

int main() {
    std::ifstream file("program2trivialdata.txt");
    if (!file) {
        std::cerr << "Cannot open the file!" << std::endl;
        return 1;
    }

    int n;
    file >> n;
    std::vector<Point> points;
    double x, y;

    while (file >> x >> y) {
        points.emplace_back(x, y);
    }

    // Sort points according to their x-coordinate
    std::vector<Point> px(points), py(points);
    std::sort(px.begin(), px.end(), compareX);
    std::sort(py.begin(), py.end(), compareY);

    // Call the recursive closest pair function and start the process
    std::cout << "Closest pair distances for each recursive call:" << std::endl;
    double closestDistance = closestPairRecursive(px, py, 0, px.size() - 1);

    return 0;
}

