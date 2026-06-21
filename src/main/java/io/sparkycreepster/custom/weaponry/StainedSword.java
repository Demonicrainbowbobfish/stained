package io.sparkycreepster.custom.weaponry;

import io.sparkycreepster.custom.dataclasses.BloodmarkData;
import io.sparkycreepster.custom.generateRandomBetween10;
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

import java.util.*;

public class StainedSword extends SwordItem {
    private static final Map<UUID, BloodmarkData> BLOODMARKS = new HashMap<>();
    private static final Map<UUID, List<Long>> HITS = new HashMap<>();

    public StainedSword(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {
        if (user.isSneaking()) {
            // shift
        }
        else {
            // idk
        }
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
                Text.literal("§c§k 32" + requiredHits + "§c§k 32"),
                true
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

        if (mark.hasExpired(currentTime)) {
            BLOODMARKS.remove(id);
            HITS.remove(id);
            return super.postHit(stack, target, attacker);
        }
        List<Long> hits = HITS.computeIfAbsent(id, k -> new ArrayList<>());
        hits.removeIf(time -> currentTime - time > 1200);
        hits.add(currentTime);
        int hitCount = hits.size();
        if (hitCount >= mark.getRequiredHits() && !mark.isComplete()) {
            // this runs when blood thing is fufilled
            System.out.println("BLOODMARK COMPLETE");
            mark.completeMark(currentTime);
            if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
                // for later
                player.sendMessage(
                        Text.literal("BLOODMARK FULFILLED"),
                        true
                );
            }
        }


        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            player.sendMessage(
                    Text.literal(hitCount + "§c§k 20"),
                    true
            );
        }
        return super.postHit(stack, target, attacker);

    }


}