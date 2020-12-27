package tfcr.data;

public class BellCurve {
    public int min;
    public int max;
    public double mean;
    public double stdDev;

    /**
     * Holds the bell curve for a given range of values without needing to recompute them.
     * This curve is not normalized; instead it is set so that the value at the mean is 1,
     * rather than the area under the curve being equal to one.
     *
     * @param min Two standard deviations left.
     * @param max Two standard deviations right.
     */
    public BellCurve(int min, int max) {
        this.min = min;
        this.max = max;
        this.stdDev = Math.sqrt((max - min) / 2.0);
        this.mean = (max + min) / 2.0;
    }

    /**
     * Compute the value of the bell curve at the point x.
     *
     * If the value is outside of the range of [min, max], this function returns 0.
     *
     * TODO: possibly implement a taylor series expansion to make this faster.
     * @return
     */
    public double getValue(double x) {
        if (x < min || x > max) {
            return 0;
        }

        if (x < mean) {
            return (2.0 / (max - min)) * (x - min);
        } else {
            return ((-2.0 / (max - min)) * (x - mean)) + 1;
        }


//        double value = Math.pow(Math.E, -((x - mean) * (x - mean) / (2 * stdDev * stdDev)));
//        if (value < 0.001) {
//            return 0;
//        }
//        return value;
    }
}
