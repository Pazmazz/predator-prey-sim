/*
 * @written 4/2/2025
 */
package classes.abstracts;

/**
 * A more specific sub-category of BaseVector which includes implementation that
 * does not assume that it's components are necessarily strictly int values.
 */
public abstract class Vector<T extends Vector<T>> extends BaseVector<T> {

    private Double magnitude;
    private T unit;

    /**
     * Divide the components of this Vector<T> by some other Vector<T>
     *
     * @param v the Vector<T> to divide the components of
     * @return the Vector<T> quotient
     */
    public T divide(T v) {
        return newVector(computeComponents(
                v,
                "divide(Vector<T>)",
                (args) -> (Double) args[0] / (Double) args[1]));
    }

    /**
     * Overload: {@code divide}
     *
     * Divide the components of this Vector<T> with some common divisor
     *
     * @param dec the common divisor to divide into the components of this
     * Vector<T>
     * @return ...
     */
    public T divide(double dec) {
        return newVector(computeComponents(
                (args) -> (Double) args[0] / dec));
    }

    /**
     * Overload: {@code divide}
     *
     * @param scalar the common integer divisor to divide into the components of
     * this Vector<T>
     * @return ...
     */
    public T divide(int scalar) {
        return newVector(computeComponents(
                (args) -> (Double) args[0] / scalar));
    }

    /**
     * Overload: {@code multiply}
     *
     * @param dec the common double to multiply the components of this Vector<T>
     * by
     * @return ...
     */
    public T multiply(double dec) {
        return newVector(computeComponents(
                (args) -> (Double) args[0] * dec));
    }

    /**
     * Overload: {@code add}
     *
     * @param dec
     * @return ...
     */
    public T add(double dec) {
        return newVector(computeComponents(
                (args) -> (Double) args[0] + dec));
    }

    /**
     * Overload: {@code subtract}
     *
     * @param dec
     * @return ...
     */
    public T subtract(double dec) {
        return newVector(computeComponents(
                (args) -> (Double) args[0] - dec));
    }

    /**
     * Floor the components of this Vector<T>
     *
     * @return the new Vector<T> with floored components of this Vector<T>
     */
    public T floor() {
        return newVector(computeComponents(
                (args) -> Math.floor((Double) args[0])));
    }

    /**
     * Ceil the components of this Vector<T>
     *
     * @return the new Vector<T> with ceiled components of this Vector<T>
     */
    public T ceil() {
        return newVector(computeComponents(
                (args) -> Math.ceil((Double) args[0])));
    }

    /**
     * Get the magnitude of this Vector<T>
     *
     * @return the magnitude of this Vector<T>
     */
    public double magnitude() {
        if (this.magnitude != null) {
            return this.magnitude;
        }

        this.magnitude = 0.0;
        this.components.forEach(cn -> this.magnitude += cn * cn);
        this.magnitude = Math.sqrt(this.magnitude);

        return this.magnitude;
    }

    /**
     * Get the midpoint between this Vector<T> and some other Vector<T>
     *
     * @param v the Vector<T> to get the midpoint between
     * @return the resulting Vector<T> representing the midpoint
     */
    public T midpoint(T v) {
        return newVector(computeComponents(
                v,
                "midpoint(Vector<T>)",
                (args) -> (((Double) args[0] + (Double) args[1]) / 2)));
    }

    /**
     * Get the unit vector of this Vector<T>
     *
     * @return the unit vector of this Vector<T>
     */
    public T unit() {
        if (this.unit != null) {
            return this.unit;
        }
        this.unit = divide(magnitude());
        return this.unit;
    }
}
