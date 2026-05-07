package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RoadRollerItem extends Item {
    public RoadRollerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        BlockPos abovePos = blockPos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (!aboveState.isAir()) return InteractionResult.FAIL;

        if (!level.isClientSide) {
            Vec3 spawnPos = Vec3.atCenterOf(abovePos);

            RoadRollerEntity roadRoller = ModEntities.ROAD_ROLLER_ENTITY.create(level);
            if (roadRoller != null) {
                roadRoller.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), 0.0F);
                level.addFreshEntity(roadRoller);

                if (!player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
