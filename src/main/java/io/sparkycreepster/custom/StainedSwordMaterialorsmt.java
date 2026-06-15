package io.sparkycreepster.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

public class StainedSwordMaterialorsmt extends SwordItem {
    private static final Map<UUID, List<Long>> HITS = new HashMap<>();
    public StainedSwordMaterialorsmt(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
    // Mark with incomplete blood mark on right click
    @Override
    public ActionResult useOnEntity(
            ItemStack stack,
            PlayerEntity user,
            LivingEntity entity,
            Hand hand
    ) {
        if (!user.getWorld().isClient()) {
            user.sendMessage(
                    Text.literal("Marked " + entity.getName().getString()),
                    false
            );
        }

        return ActionResult.SUCCESS;

    }
    // Bloodmark detection thingy
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Now I need to figure this shit out bruh
        long currentTime = attacker.getWorld().getTime();
        // UUID storing
        UUID id = target.getUuid();
        List<Long> hits = HITS.computeIfAbsent(id, k -> new ArrayList<>());
        hits.removeIf(time -> currentTime - time > 1200);
        hits.add(currentTime);
        int hitCount = hits.size();
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            player.sendMessage(
                    Text.literal("Hit count (last 60s): " + hitCount),
                    false
            );
        }
        return super.postHit(stack, target, attacker);

    }


}