package io.sparkycreepster;

import io.sparkycreepster.general.Items;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stained implements ModInitializer {
	public static final String MOD_ID = "stained";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {

		Items.registerModItems();
		LOGGER.info("chaos maybe");
	}
}