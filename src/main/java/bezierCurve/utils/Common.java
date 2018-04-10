package bezierCurve.utils;

public class Common {

    /**
     * Check if the two double numbers given are equal (in approximation of 1^(-9) ).
     * @param d1
     * @param d2
     * @return
     */
    public static boolean Equal(double d1, double d2) {
        return Math.abs(d1 - d2) <= 1e-9;
    }
}
