package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.block.StickyIceCoatingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class WhiteAlbumFreezingEntity extends Entity {
    public final AnimationState twisterSpin = new AnimationState();
    public int lifeSpan = -1;
    public int renderCold = 1;

    public WhiteAlbumFreezingEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    public boolean fireImmune() {
        return true;
    }
    public boolean isOnFire() {
        return false;
    }

    public boolean started = false;

    public boolean canFreeze(BlockPos pos) {
        BlockState state = level().getBlockState(pos);

        if (!state.canBeReplaced())
            return false;

        if (!state.getFluidState().isEmpty())
            return false;

        if (state.getBlock() instanceof LiquidBlockContainer)
            return false;

        if (state.getBlock() instanceof FireBlock)
            return false;

        if (state.getBlock() instanceof StickyIceCoatingBlock)
            return false;

        if (state.getBlock() instanceof StandFireBlock)
            return false;

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)
                && state.getValue(BlockStateProperties.WATERLOGGED))
            return false;

        return true;
    }
    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        lifeSpan = compoundTag.getInt("lifespan");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("lifespan", lifeSpan);
    }
}
