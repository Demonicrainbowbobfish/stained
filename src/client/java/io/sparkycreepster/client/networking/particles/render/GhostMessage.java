package io.sparkycreepster.client.networking.particles.render;

public class GhostMessage {

    public final String message;

    public final double x;
    public final double y;
    public final double z;

    public final float yaw;
    public final float pitch;

    public int age = 0;

    public static final int FADE_IN = 20;
    public static final int HOLD = 240;
    public static final int FADE_OUT = 20;

    public static final int MAX_AGE =
            FADE_IN + HOLD + FADE_OUT;

    public GhostMessage(
            String message,
            double x,
            double y,
            double z,
            float yaw,
            float pitch
    ) {
        this.message = message;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void tick() {
        age++;
    }

    public boolean isDead() {
        return age >= MAX_AGE;
    }

    public float getAlpha() {

        if (age < FADE_IN) {
            return age / (float) FADE_IN;
        }

        if (age < FADE_IN + HOLD) {
            return 1.0F;
        }

        int fadeAge = age - FADE_IN - HOLD;

        return 1.0F - fadeAge / (float) FADE_OUT;
    }
}