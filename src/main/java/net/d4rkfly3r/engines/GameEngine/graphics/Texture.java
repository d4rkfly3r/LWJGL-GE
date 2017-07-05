package net.d4rkfly3r.engines.GameEngine.graphics;

public class Texture {

    public final double u, v, u2, v2;

    public Texture(double u, double v, double u2, double v2) {
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return "Texture{" +
                "u=" + u +
                ", v=" + v +
                ", u2=" + u2 +
                ", v2=" + v2 +
                '}';
    }
}
