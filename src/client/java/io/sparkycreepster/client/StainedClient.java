package io.sparkycreepster.client;

import io.sparkycreepster.Stained;
import io.sparkycreepster.client.endStopper.GoldenCubeRenderer;
import io.sparkycreepster.client.endStopper.GoldenWorldBorderRenderer;
import io.sparkycreepster.client.networking.particles.networked.ModClientPackets;
import io.sparkycreepster.client.networking.particles.render.GhostMessageRenderer;
import io.sparkycreepster.client.networking.particles.render.GhostMessageScreen;
import io.sparkycreepster.client.networking.particles.render.flipside.FlipsideParticleRenderer;
import io.sparkycreepster.client.networking.particles.render.shaders.FlipsideShaderManager;
import io.sparkycreepster.client.networking.particles.render.shaders.registerModShaders;
import io.sparkycreepster.custom.networking.packets.Packets;
import io.sparkycreepster.custom.particles.CustomLodestoneParticles;
import io.sparkycreepster.general.Items;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class StainedClient implements ClientModInitializer {

	// =========================
	// Keybind
	// =========================

	private static final KeyBinding toggleKey =
			KeyBindingHelper.registerKeyBinding(new KeyBinding(
					"key.stained.toggle",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_V,
					"category.stained"
			));

	// =========================
	// Golden cube test position
	// =========================

	private static final BlockPos TEST_POSITION =
			new BlockPos(0, 100, 0);

	@Override
	public void onInitializeClient() {

		// =========================
		// Ghost message renderer
		// =========================

		GhostMessageRenderer.register();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			GhostMessageRenderer.tick();
			FlipsideShaderManager.tick();
			FlipsideParticleRenderer.tick();
		});

		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
			GhostMessageRenderer.render(context);
		});

		// =========================
		// Flipside shader
		// =========================

		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			if (FlipsideShaderManager.isTransitioning()) {
				registerModShaders.flipsideShader.render(tickDelta);
			}
		});

		// =========================
		// Golden cube animation
		// =========================

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			GoldenCubeRenderer.tick();
		});

		// =========================
		// Golden cube rendering
		// =========================

		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
			GoldenCubeRenderer.render(context);
		});

		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {

			// World-border surface
			GoldenWorldBorderRenderer.render(context);

			// Golden geometry lines
			GoldenCubeRenderer.render(context);
		});

		// =========================
		// Networking
		// =========================

		ModClientPackets.registerClientPackets();

		// =========================
		// Particles
		// =========================

		ParticleFactoryRegistry.getInstance().register(
				CustomLodestoneParticles.BLOOD1,
				LodestoneWorldParticleType.Factory::new
		);

		ParticleFactoryRegistry.getInstance().register(
				CustomLodestoneParticles.SHOCKWAVE,
				LodestoneWorldParticleType.Factory::new
		);

		// =========================
		// Blood vial model predicate
		// =========================

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

		// =========================
		// Ghost toggle key
		// =========================

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			while (toggleKey.wasPressed()) {

				ClientPlayNetworking.send(
						Stained.TOGGLE_GHOST,
						PacketByteBufs.empty()
				);
			}
		});

		// =========================
		// Ghost update packet
		// =========================

		ClientPlayNetworking.registerGlobalReceiver(
				Stained.GHOST_UPDATE,
				(client, handler, buf, responseSender) -> {

					boolean enabled = buf.readBoolean();

					client.execute(() -> {

						if (client.player == null) {
							Stained.vanishEnabled = enabled;
							return;
						}

						/*
						 * Start both visual effects.
						 *
						 * FlipsideParticleRenderer will delay
						 * the actual vanishEnabled state change
						 * until its animation finishes.
						 */
						FlipsideParticleRenderer.startTransition(
								client.player,
								enabled
						);

						FlipsideShaderManager.startTransition();
					});
				}
		);

		// =========================
		// Ghost message screen
		// =========================

		ClientPlayNetworking.registerGlobalReceiver(
				Packets.OPEN_GHOST_MESSAGE,
				(client, handler, buf, responseSender) -> {
					client.execute(() -> {
						client.setScreen(new GhostMessageScreen());
					});
				}
		);
	}
}

