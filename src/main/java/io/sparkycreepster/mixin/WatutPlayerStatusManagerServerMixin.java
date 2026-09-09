package io.sparkycreepster.mixin;

import io.sparkycreepster.Stained;
import com.corosus.watut.PlayerStatusManagerServer;
import com.corosus.watut.WatutNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerStatusManagerServer.class)
public class WatutPlayerStatusManagerServerMixin {

    @Redirect(
            method = "receiveAny",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/corosus/watut/WatutNetworking;serverSendToClientAll(Lnet/minecraft/nbt/NbtCompound;)V"
            )
    )
    private void stained$redirectWatutBroadcast(
            WatutNetworking networking,
            NbtCompound data,
            PlayerEntity player
    ) {
        Stained.LOGGER.info(
                "WATUT broadcast intercepted from {}",
                player.getName().getString()
        );

        if (Stained.vanishEnabled
                && Stained.bannedUuids.contains(player.getUuid())) {

            Stained.LOGGER.info("BLOCKING WATUT broadcast from vanished player");
            return;
        }

        networking.serverSendToClientAll(data);
    }
    @Redirect(
            method = "playerLoggedIn",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/corosus/watut/WatutNetworking;serverSendToClientPlayer(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
    )
    private void stained$redirectWatutPlayerStatus(
            WatutNetworking networking,
            NbtCompound data,
            PlayerEntity targetPlayer
    ) {
        String uuidString = data.getString(WatutNetworking.NBTDataPlayerUUID);

        if (Stained.vanishEnabled) {
            try {
                java.util.UUID uuid = java.util.UUID.fromString(uuidString);

                if (Stained.bannedUuids.contains(uuid)) {
                    Stained.LOGGER.info(
                            "BLOCKING cached WATUT status for vanished player {}",
                            uuid
                    );
                    return;
                }
            } catch (IllegalArgumentException ignored) {
            }
        }

        networking.serverSendToClientPlayer(data, targetPlayer);
    }
}