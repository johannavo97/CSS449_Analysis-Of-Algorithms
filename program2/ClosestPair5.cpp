//
// Created by Johanna Vo on 4/26/24.
//
#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <iomanip>

using namespace std;

struct Point {
    double x, y;
};

// Helper function to calculate Euclidean distance between two points
double distance(const Point& a, const Point& b) {
    return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
}

// Sort points by x-coordinate
bool compareX(const Point& a, const Point& b) {
    return a.x < b.x;
}

// Sort points by y-coordinate
bool compareY(const Point& a, const Point& b) {
    return a.y < b.y;
}

// Brute-force calculation of the closest distance for small subsets
double bruteForce(vector<Point>& points, int left, int right) {
    double minDist = std::numeric_limits<double>::max();
    for (int i = left; i <= right; ++i) {
        for (int j = i + 1; j <= right; ++j) {
            minDist = min(minDist, distance(points[i], points[j]));
        }
    }
    return minDist;
}

// Utilizes a divide and conquer approach to find the closest pair of points
double closestPairUtil(vector<Point>& pointsX, vector<Point>& pointsY, int left, int right) {
    // Handling small subproblems directly
    if (right - left <= 3) {
        return bruteForce(pointsX, left, right);
    }

    int mid = left + (right - left) / 2;
    Point midPoint = pointsX[mid];

    vector<Point> pointsYL, pointsYR;
    for (int i = 0; i < pointsY.size(); ++i) {
        if (pointsY[i].x <= midPoint.x)
            pointsYL.push_back(pointsY[i]);
        else
            pointsYR.push_back(pointsY[i]);
    }

    double dLeft = closestPairUtil(pointsX, pointsYL, left, mid);
    double dRight = closestPairUtil(pointsX, pointsYR, mid + 1, right);
    double d = min(dLeft, dRight);

    vector<Point> strip;
    for (int i = 0; i < pointsY.size(); ++i) {
        if (abs(pointsY[i].x - midPoint.x) < d)
            strip.push_back(pointsY[i]);
    }

    // Find the closest points in the strip
    double minStripDist = d;
    for (int i = 0; i < strip.size(); ++i) {
        for (int j = i + 1; j < strip.size() && (strip[j].y - strip[i].y) < d; ++j) {
            minStripDist = min(minStripDist, distance(strip[i], strip[j]));
        }
    }

    return minStripDist;
}

// Main function to find the closest pair of points
double closestPair(vector<Point>& points) {
    vector<Point> pointsX = points;
    vector<Point> pointsY = points;
    sort(pointsX.begin(), pointsX.end(), compareX);
    sort(pointsY.begin(), pointsY.end(), compareY);
    return closestPairUtil(pointsX, pointsY, 0, points.size() - 1);
}

int main() {
    ifstream inputFile("program2data.txt");
    int n;
    inputFile >> n;

    vector<Point> points(n);
    for (int i = 0; i < n; ++i) {
        inputFile >> points[i].x >> points[i].y;
    }
    inputFile.close();

    double result = closestPair(points);
    cout << fixed << setprecision(4) << result << endl;

    return 0;
}
