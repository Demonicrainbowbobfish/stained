package io.sparkycreepster.custom;

import io.sparkycreepster.custom.dataclasses.BloodmarkData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.*;

public class StainedSwordMaterialorsmt extends SwordItem {
    private static final Map<UUID, BloodmarkData> BLOODMARKS = new HashMap<>();
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

        long currentTime = user.getWorld().getTime();
        UUID id = entity.getUuid();
        // BLOODMARKS.containsKey(id);
        if (BLOODMARKS.containsKey(id)) {
            return ActionResult.SUCCESS;
        }
        generateRandomBetween10 random = new generateRandomBetween10();
        int requiredHits = random.getRangedInt(10, 20);
        BloodmarkData mark = new BloodmarkData(
                requiredHits,
                currentTime
        );
        BLOODMARKS.put(id, mark);
        user.sendMessage(
                Text.literal("Bloodmarked: " + requiredHits + " hits required"),
                false
        );
        return ActionResult.SUCCESS;
    }
    // Bloodmark detection thingy
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        // Now I need to figure this shit out bruh
        long currentTime = attacker.getWorld().getTime();
        // UUID storing
        UUID id = target.getUuid();
        if (!BLOODMARKS.containsKey(id)) {
            return super.postHit(stack, target, attacker);
        }
        BloodmarkData mark = BLOODMARKS.get(id);


        List<Long> hits = HITS.computeIfAbsent(id, k -> new ArrayList<>());
        hits.removeIf(time -> currentTime - time > 1200);
        hits.add(currentTime);
        int hitCount = hits.size();
        if (hitCount >= mark.getRequiredHits()) {
            // this runs when blood thing is fufilled
            if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
                player.sendMessage(
                        Text.literal("Shit should work now"),
                        false
                );
            }
        }
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            player.sendMessage(
                    Text.literal("Hit count (last 60s): " + hitCount),
                    false
            );
        }
        return super.postHit(stack, target, attacker);

    }


}