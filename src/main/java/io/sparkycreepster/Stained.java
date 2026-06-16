package io.sparkycreepster;

import io.sparkycreepster.general.Items;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

public class Stained implements ModInitializer {
	// ghosty shit
	public static final ArrayList<UUID> bannedUuids = new ArrayList<>();
	public static boolean vanishEnabled = true;
	// regular shit
	public static final String MOD_ID = "stained";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		bannedUuids.add(UUID.fromString("5c35627a-2d28-49c4-82fc-e64ec85be5c4"));
		Items.registerModItems();
		LOGGER.info("chaos maybe");
	}
}