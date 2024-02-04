package h13.rubric;

import h13.noise.AbstractPerlinNoise;
import h13.noise.PerlinNoise;

import java.util.Random;

public class TutorPerlinNoise extends AbstractPerlinNoise implements PerlinNoise {

    public TutorPerlinNoise() {
        super(Integer.MAX_VALUE, Integer.MAX_VALUE, new Random(0));
    }

    @Override
    public double compute(int x, int y) {
        return compute((double) x, y);
    }

    @Override
    public double compute(double x, double y) {
        return x * y % 2 == 0 ? 1 : -1;
    }

    @Override
    public double fade(double t) {
        return 0;
    }

    @Override
    public double interpolate(double x1, double x2, double alpha) {
        return 0;
    }
}
