package io.sparkycreepster.client;

import io.sparkycreepster.Stained;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class StainedClient implements ClientModInitializer {
	private static KeyBinding toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.stained.toggle",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_V,
			"category.stained"
	));
	@Override
	public void onInitializeClient() {

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

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