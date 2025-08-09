import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SecretSharingSolver {

    // Convert a number from any base to decimal (BigInteger for large values)
    public static BigInteger convertFromBase(String value, int base) {
        return new BigInteger(value, base);
    }

    // Class to represent a point (x, y)
    static class Coordinate {
        long x;
        BigInteger y;

        Coordinate(long x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    static class PolynomialCalculator {
        private List<Coordinate> coordinates = new ArrayList<>();

        // Lagrange interpolation to find f(0)
        private BigInteger lagrangeInterpolation() {
            BigInteger result = BigInteger.ZERO;
            int totalPoints = coordinates.size();

            for (int i = 0; i < totalPoints; i++) {
                BigInteger numerator = coordinates.get(i).y;
                BigInteger denominator = BigInteger.ONE;

                for (int j = 0; j < totalPoints; j++) {
                    if (i != j) {
                        numerator = numerator.multiply(BigInteger.valueOf(-coordinates.get(j).x));
                        denominator = denominator.multiply(
                                BigInteger.valueOf(coordinates.get(i).x - coordinates.get(j).x)
                        );
                    }
                }

                // Adjust sign if denominator is negative
                if (denominator.compareTo(BigInteger.ZERO) < 0) {
                    numerator = numerator.negate();
                    denominator = denominator.negate();
                }

                result = result.add(numerator.divide(denominator));
            }

            return result;
        }

        public void addCoordinate(long x, BigInteger y) {
            coordinates.add(new Coordinate(x, y));
        }

        public void clearCoordinates() {
            coordinates.clear();
        }

        public BigInteger findSecret() {
            if (coordinates.isEmpty()) {
                System.err.println("Error: No coordinates loaded");
                return BigInteger.ZERO;
            }
            return lagrangeInterpolation();
        }

        public void displayCoordinates() {
            System.out.println("Decoded coordinates:");
            for (Coordinate c : coordinates) {
                System.out.println("(" + c.x + ", " + c.y + ")");
            }
        }
    }

    // Test Case 1
    public static void loadTestCase1(PolynomialCalculator calculator) {
        calculator.clearCoordinates();
        calculator.addCoordinate(1, BigInteger.valueOf(4));  // base 10, value 4
        calculator.addCoordinate(2, BigInteger.valueOf(7));  // base 2, value 111
        calculator.addCoordinate(3, BigInteger.valueOf(12)); // base 10, value 12
        calculator.addCoordinate(6, BigInteger.valueOf(39)); // base 4, value 213
    }

    // Test Case 2
    public static void loadTestCase2(PolynomialCalculator calculator) {
        calculator.clearCoordinates();
        calculator.addCoordinate(1, convertFromBase("13444211440455345511", 6));
        calculator.addCoordinate(2, convertFromBase("aed7015a346d63", 15));
        calculator.addCoordinate(3, convertFromBase("6aeeb69631c227c", 15));
        calculator.addCoordinate(4, convertFromBase("e1b5e05623d881f", 16));
        calculator.addCoordinate(5, convertFromBase("316034514573652620673", 8));
        calculator.addCoordinate(6, convertFromBase("2122212201122002221120200210011020220200", 3));
        calculator.addCoordinate(7, convertFromBase("20120221122211000100210021102001201112121", 3));
    }

    public static void main(String[] args) {
        System.out.println("=== Polynomial Solver - Secret Sharing ===");
        System.out.println("Using Lagrange interpolation to find secret C");

        PolynomialCalculator calculator = new PolynomialCalculator();

        // Test Case 1
        System.out.println("\nTest Case 1 (k=3, using first 3 points):");
        loadTestCase1(calculator);
        calculator.displayCoordinates();
        BigInteger secret1 = calculator.findSecret();
        System.out.println("Secret C: " + secret1);

        // Test Case 2
        System.out.println("\nTest Case 2 (k=7, using first 7 points):");
        loadTestCase2(calculator);
        calculator.displayCoordinates();
        BigInteger secret2 = calculator.findSecret();
        System.out.println("Secret C: " + secret2);
    }
}
