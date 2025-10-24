package net.hydra.jojomod.item;

import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class RoadRollerItem extends Item {
    public RoadRollerItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level dimension, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!dimension.isClientSide) {
            double x = player.getX() + player.getLookAngle().x * 2.0;
            double y = player.getY();
            double z = player.getZ() + player.getLookAngle().z * 2.0;

            RoadRollerEntity roadRoller = ModEntities.ROAD_ROLLER_ENTITY.create(dimension);
            if (roadRoller != null) {
                roadRoller.moveTo(x, y, z, player.getYRot(), 0.0F);
                dimension.addFreshEntity(roadRoller);

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, dimension.isClientSide);
    }
}