package io.sparkycreepster;

// A note to whoever is reading this - I poured blood, sweat, and tears into this project, so whatever you do, don't steal it.
// And arathain, I thank you in particular for getting me started on my journey. I really have no idea if you remember me, but if so,
// I've come a long way since we talked. I give my sincerest thanks! Text me on discord if you do see this and wany to talk! (Discord username: SparkyCreepster)

import io.sparkycreepster.custom.particles.Particles;
import io.sparkycreepster.general.Blocks;
import io.sparkycreepster.general.Items;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
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

	public static final Identifier TOGGLE_GHOST =
			new Identifier(MOD_ID, "toggle_ghost");
	public static final Identifier GHOST_UPDATE =
			new Identifier(MOD_ID, "ghost_update");
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	// particles




	@Override
	public void onInitialize() {

		Blocks.registerModBlocks();
		Particles.registerParticles();
		bannedUuids.add(UUID.fromString("5c35627a-2d28-49c4-82fc-e64ec85be5c4"));
		Items.registerModItems();
		ServerPlayNetworking.registerGlobalReceiver(
				TOGGLE_GHOST,
				(server, player, handler, buf, responseSender) -> {
					server.execute(() -> {
						if (Stained.bannedUuids.contains(player.getUuid())) {
							vanishEnabled = !vanishEnabled;
							PacketByteBuf buf1 = PacketByteBufs.create();
							buf1.writeBoolean(vanishEnabled);
							for (ServerPlayerEntity target : server.getPlayerManager().getPlayerList()) {
								ServerPlayNetworking.send(
										target,
										GHOST_UPDATE,
										buf1
								);
							}
						}
					});
				}
		);
		LOGGER.info("chaos maybe");
	}
}