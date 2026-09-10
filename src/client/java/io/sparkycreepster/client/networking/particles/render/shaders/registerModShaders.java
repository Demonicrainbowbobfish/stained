package io.sparkycreepster.client.networking.particles.render.shaders;

import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

public class registerModShaders {
    public static ManagedShaderEffect flipsideShader = registerShaders(
            "stained",
            "shaders/post/flipside.json"
    );
    public static ManagedShaderEffect registerShaders(String name, String shaderLocation) {
        return ShaderEffectManager.getInstance().manage(new Identifier(name, shaderLocation));
    }


}
