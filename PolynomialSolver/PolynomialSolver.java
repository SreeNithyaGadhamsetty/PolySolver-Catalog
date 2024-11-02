import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class PolynomialSolver {

    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    // Decode the base-encoded value
    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    // Parse JSON file and extract points
    private static List<Point> parseJson(String filePath) throws Exception {
        List<Point> points = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            for (String key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    int x = Integer.parseInt(key);
                    JSONObject pointData = jsonObject.getJSONObject(key);
                    int base = pointData.getInt("base");
                    String yEncoded = pointData.getString("value");
                    BigInteger y = decodeValue(yEncoded, base);
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    // Lagrange interpolation to find the constant term (y-intercept) of the polynomial
    private static BigInteger findConstantTerm(List<Point> points) {
        BigInteger constantTerm = BigInteger.ZERO;

        for (int i = 0; i < points.size(); i++) {
            BigInteger term = points.get(i).y;
            BigInteger denom = BigInteger.ONE;

            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    term = term.multiply(BigInteger.valueOf(-points.get(j).x));
                    denom = denom.multiply(BigInteger.valueOf(points.get(i).x - points.get(j).x));
                }
            }
            constantTerm = constantTerm.add(term.divide(denom));
        }
        return constantTerm;
    }

    public static void main(String[] args) {
        try {
            // Replace these paths with the actual paths to your JSON test case files
            String filePath1 = "testcase1.json";
            String filePath2 = "testcase2.json";

            // First Test Case
            List<Point> points1 = parseJson(filePath1);
            BigInteger constantTerm1 = findConstantTerm(points1);
            System.out.println("Constant term for Test Case 1: " + constantTerm1);

            // Second Test Case
            List<Point> points2 = parseJson(filePath2);
            BigInteger constantTerm2 = findConstantTerm(points2);
            System.out.println("Constant term for Test Case 2: " + constantTerm2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
