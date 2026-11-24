
package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GinkgoBoatItem extends Item {

    private final boolean hasChest;

    public GinkgoBoatItem(boolean hasChest, Properties properties) {
        super(properties);
        this.hasChest = hasChest;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        HitResult hit = getPlayerPOVHitResult(
                level, player, ClipContext.Fluid.ANY
        );

        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        Vec3 view = player.getViewVector(1.0F);

        for (Entity entity : level.getEntities(
                player,
                player.getBoundingBox()
                        .expandTowards(view.scale(5.0))
                        .inflate(1.0),
                EntitySelector.NO_SPECTATORS.and(Entity::isPickable))) {

            if (entity.getBoundingBox()
                    .inflate(entity.getPickRadius())
                    .contains(player.getEyePosition())) {

                return InteractionResultHolder.pass(stack);
            }
        }

        if (hit.getType() == HitResult.Type.BLOCK) {

            Boat boat = hasChest
                    ? ModEntities.GINKGO_CHEST_BOAT.create(level)
                    : ModEntities.GINKGO_BOAT.create(level);

            if (boat == null) {
                return InteractionResultHolder.fail(stack);
            }

            boat.setPos(
                    hit.getLocation().x,
                    hit.getLocation().y,
                    hit.getLocation().z
            );

            boat.setYRot(player.getYRot());

            if (!level.noCollision(boat, boat.getBoundingBox())) {
                return InteractionResultHolder.fail(stack);
            }

            if (!level.isClientSide) {
                level.addFreshEntity(boat);

                level.gameEvent(
                        player,
                        GameEvent.ENTITY_PLACE,
                        hit.getLocation()
                );

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));

            return InteractionResultHolder.sidedSuccess(
                    stack, level.isClientSide
            );
        }

        return InteractionResultHolder.pass(stack);
    }
}
