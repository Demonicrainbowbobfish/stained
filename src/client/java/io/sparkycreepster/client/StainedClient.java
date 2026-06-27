package io.sparkycreepster.client;

import io.sparkycreepster.Stained;
import io.sparkycreepster.custom.particles.CustomLodestoneParticles;
import io.sparkycreepster.general.Items;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class StainedClient implements ClientModInitializer {
	// Register particles!



	private static KeyBinding toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.stained.toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_V,
			"category.stained"
	));

	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(
				CustomLodestoneParticles.BLOOD1,
				LodestoneWorldParticleType.Factory::new
		);

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModelPredicateProviderRegistry.register(
				Items.BLOOD_VIAL,
				new Identifier("filled"),
				(stack, world, entity, seed) -> {
					if (stack.hasNbt() &&
							stack.getNbt().getBoolean("Filled")) {
						return 1.0F;
					}
					return 0.0F;
				}
		);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKey.wasPressed()) {
				ClientPlayNetworking.send(
						Stained.TOGGLE_GHOST,
						PacketByteBufs.empty()
				);
			}
		});


		ClientPlayNetworking.registerGlobalReceiver(
				Stained.GHOST_UPDATE,
				(client, handler, buf, responseSender) -> {
					boolean enabled = buf.readBoolean();
					client.execute(() -> {
						Stained.vanishEnabled = enabled;
					});

				}
		);
	}
}