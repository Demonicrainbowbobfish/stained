package io.sparkycreepster.client.networking.particles.render.shaders;

import ladysnake.satin.api.managed.ManagedShaderEffect;

public class FlipsideShaderManager {

    private static int transitionTicks = 0;
    private static final int TRANSITION_LENGTH = 15;

    private static boolean transitioning = false;

    public static void startTransition() {
        transitionTicks = 0;
        transitioning = true;
    }

    public static boolean isTransitioning() {
        return transitioning;
    }

    public static void tick() {
        if (!transitioning) {
            return;
        }

        transitionTicks++;

        float progress =
                transitionTicks / (float) TRANSITION_LENGTH;

        ManagedShaderEffect shader =
                registerModShaders.flipsideShader;

        shader.setUniformValue("Progress", progress);

        if (transitionTicks >= TRANSITION_LENGTH) {
            transitioning = false;
            shader.setUniformValue("Progress", 0.0F);
        }
    }
}