//
// Created by Johanna Vo on 4/26/24.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <limits>
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

double bruteForce(const std::vector<Point>& points, int left, int right) {
    double minDist = std::numeric_limits<double>::max();
    for (int i = left; i <= right; ++i) {
        for (int j = i + 1; j <= right; ++j) {
            minDist = std::min(minDist, dist(points[i], points[j]));
        }
    }
    return minDist;
}

double closestSplitPair(const std::vector<Point>& px, const std::vector<Point>& py, double delta, int mid) {
    double minDist = delta;
    std::vector<Point> stripe;

    for (const Point& p : py) {
        if (fabs(p.x - px[mid].x) < delta) {
            stripe.push_back(p);
        }
    }

    for (size_t i = 0; i < stripe.size(); ++i) {
        for (size_t j = i + 1; j < stripe.size() && stripe[j].y - stripe[i].y < delta; ++j) {
            double distance = dist(stripe[i], stripe[j]);
            minDist = std::min(minDist, distance);
        }
    }
    return minDist;
}

double closestPairRecursive(std::vector<Point>& px, std::vector<Point>& py, int left, int right) {
    if (right - left <= 3) {
        // Print the current range and the computed distance for brute force calculation.
        double distance = bruteForce(px, left, right);
        std::cout << "D[" << left << "," << right << "]: " << std::fixed << std::setprecision(4) << distance << std::endl;
        return distance;
    }

    int mid = left + (right - left) / 2;
    double dl = closestPairRecursive(px, py, left, mid);
    double dr = closestPairRecursive(px, py, mid + 1, right);
    double d = std::min(dl, dr);

    std::vector<Point> pyLeft, pyRight;
    for (const Point& p : py) {
        if (p.x <= px[mid].x) pyLeft.push_back(p);
        else pyRight.push_back(p);
    }

    double dSplit = closestSplitPair(px, py, d, mid);
    double finalMin = std::min(d, dSplit);

    // Print the current range and the computed minimum distance.
    std::cout << "D[" << left << "," << right << "]: " << std::fixed << std::setprecision(4) << finalMin << std::endl;
    return finalMin;
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
    for (int i = 0; i < n; ++i) {
        double x, y;
        file >> x >> y;
        points.emplace_back(x, y);
    }

    closestPair(points); // This will print all the intermediate distances.

    return 0;
}
