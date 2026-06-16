package io.sparkycreepster.client.mixin;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.gui.hud.PlayerListHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.sparkycreepster.Stained;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(PlayerListHud.class)
public abstract class PlayerListModifier {
    @Inject(method = "collectPlayerEntries", at = @At("RETURN"), cancellable = true)
    private void filterBannedPlayers(CallbackInfoReturnable<List<PlayerListEntry>> cir) {
        List<PlayerListEntry> originalList = cir.getReturnValue();
        if (originalList == null) {

            return;
        }
        List<PlayerListEntry> filteredList = originalList.stream()
                .filter(entry -> !Stained.bannedUuids.contains(entry.getProfile().getId
                        ())).collect(Collectors.toList());
        cir.setReturnValue(filteredList);
    }

}
