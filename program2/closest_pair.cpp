//
// Created by Johanna Vo on 4/25/24.
//

#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <iomanip>
#include "Point.h"

using namespace std;
class PointSet {
public:
    std::vector<Point> points;

    void readPoints(const std::string& filename) {
        std::ifstream file(filename);
        if (!file.is_open()) {
            std::cerr << "Error opening file" << std::endl;
            return;
        }
        int n;
        double x, y;
        file >> n;
        while (n-- && file >> x >> y) {
            points.emplace_back(x, y);
        }
        file.close();
    }

    static double distance(const Point& p1, const Point& p2) {
        return sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    double closestPairUtil(int left, int right) {
        if (right - left <= 3) {
            return bruteForce(left, right);
        }

        int mid = left + (right - left) / 2;
        double dLeft = closestPairUtil(left, mid);
        double dRight = closestPairUtil(mid + 1, right);
        double d = std::min(dLeft, dRight);

        std::vector<Point> strip;
        for (int i = left; i <= right; ++i) {
            if (std::abs(points[mid].x - points[i].x) < d) {
                strip.push_back(points[i]);
            }
        }

        std::sort(strip.begin(), strip.end(), [](const Point& a, const Point& b) {
            return a.y < b.y;
        });

        double dMinStrip = d;
        for (size_t i = 0; i < strip.size(); ++i) {
            for (size_t j = i + 1; j < strip.size() && (strip[j].y - strip[i].y) < d; ++j) {
                double dist = distance(strip[i], strip[j]);
                dMinStrip = std::min(dMinStrip, dist);
            }
        }

        return std::min(d, dMinStrip);
    }

    double bruteForce(int left, int right) {
        double minDist = std::numeric_limits<double>::max();
        for (int i = left; i <= right; ++i) {
            for (int j = i + 1; j <= right; ++j) {
                double d = distance(points[i], points[j]);
                minDist = std::min(minDist, d);
            }
        }
        return minDist;
    }


    PointSet(const std::string& filename) {
        readPoints(filename);
        std::sort(points.begin(), points.end(), [](const Point& a, const Point& b) {
            return a.x < b.x;
        });
    }

    double findClosestPair() {
        return closestPairUtil(0, points.size() - 1);
    }
    // Public method to get the number of points
    size_t size() const {
        return points.size();
    }
};

int main() {
    PointSet ps("program2trivialdata.txt");
    double closestDistance = ps.findClosestPair();
    std::cout << std::fixed << std::setprecision(4) << "D[0," << ps.points.size() - 1 << "]: " << closestDistance << std::endl;
    return 0;
}
