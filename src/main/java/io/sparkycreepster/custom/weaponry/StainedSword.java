package io.sparkycreepster.custom.weaponry;

import io.sparkycreepster.custom.abilities.Decent;
import io.sparkycreepster.custom.dataclasses.BloodmarkData;
import io.sparkycreepster.custom.generateRandomBetween10;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.*;

public class    StainedSword extends SwordItem {
    private static final Map<UUID, BloodmarkData> BLOODMARKS = new HashMap<>();
    private static final Map<UUID, List<Long>> HITS = new HashMap<>();
    private UUID getFulfilledTarget(PlayerEntity player) {
        for (Map.Entry<UUID, BloodmarkData> entry : BLOODMARKS.entrySet()) {
            BloodmarkData mark1 = entry.getValue();

            if (mark1.isComplete() && mark1.getOwnerUuid().equals(player.getUuid())) {
                return entry.getKey();
            }
        }

        return null;

    }
    public StainedSword(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }
    private LivingEntity getFulfilledTargetEntity(PlayerEntity player) {
        UUID target = getFulfilledTarget(player);

        if (target == null) {
            return null;
        }

        Box searchBox = player.getBoundingBox().expand(200);
        List<Entity> entities = player.getWorld().getOtherEntities(
                player,
                searchBox
        );
        for (Entity entity : entities) {
            if (entity.getUuid().equals(target)) {
                if (entity instanceof LivingEntity livingTarget) {
                    return livingTarget;
                }
            }
        }

        return null;
    }
    // Ability swtiching with NBT data!!
    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.isSneaking()) {
            int ability = stack.getOrCreateNbt().getInt("SelectedAbility");
            ability++;

            if (ability > 1) {
                ability = 0;
            }

            stack.getOrCreateNbt().putInt("SelectedAbility", ability);

            user.sendMessage(
                    Text.literal(ability == 0 ? "Selected: Decent" : "Selected: Burst"), true
            );


            return TypedActionResult.success(stack);
        } else if (hand == Hand.OFF_HAND) {
            LivingEntity target = getFulfilledTargetEntity(user);
            if (target == null) {
                user.sendMessage(Text.literal("No targets found nearby."), true);
            }
            else {
                user.sendMessage(Text.literal(target.getName().getString()), true);
                int ability = stack.getOrCreateNbt().getInt("SelectedAbility");
                if (ability == 0) {
                    // decent abil
                    if (target.isOnGround()) {
                        // do nothing
                    }
                    else {
                        // do something


                        if (!world.isClient()) {
                            new Decent().use(user, target);
                        }
                    }
                }
                if (ability == 1) {
                    // burst
                }
            }



            
        }
        return TypedActionResult.success(stack);
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
                currentTime,
                user.getUuid()
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