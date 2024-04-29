public class Point {
    double x, y;

    /**
     * Constructs a point given its x and y coordinates.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the point.
     *
     * @return A string in the format (x, y).
     */
    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    /**
     * Returns the x-coordinate of this point.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of this point.
     *
     * @param x The new x-coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the y-coordinate of this point.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of this point.
     *
     * @param y The new y-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }


}