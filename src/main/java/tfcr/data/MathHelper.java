package tfcr.data;

public class MathHelper {
    public static int clamp(int min, int max, int value) {
        return Math.min(max, Math.max(min, value));
    }

    public static float clamp(float min, float max, float value) {
        return Math.min(max, Math.max(min, value));
    }
}
