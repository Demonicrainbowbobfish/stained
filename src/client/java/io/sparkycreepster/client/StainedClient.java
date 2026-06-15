package io.sparkycreepster.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class StainedClient implements ClientModInitializer {
	public static KeyBinding toggleGhostModeBinding;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		toggleGhostModeBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.stained.special",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_APOSTROPHE,
				"category.stained.general"
		));


	}
}