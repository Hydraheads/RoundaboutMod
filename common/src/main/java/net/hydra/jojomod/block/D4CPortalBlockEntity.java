package net.hydra.jojomod.block;

import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class D4CPortalBlockEntity extends BlockEntity {
    public int ticksUntilRestore = 100; // 5 seconds at 20 tps

    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public D4CPortalBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.D4C_PORTAL_BLOCK_ENTITY, $$0, $$1);
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("TicksLeft", ticksUntilRestore);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("TicksLeft")) {
            ticksUntilRestore = tag.getInt("TicksLeft");
        }
    }
    public void tick() {
        if (!level.isClientSide && --ticksUntilRestore <= 0) {

        }
    }
    public static void tickBlockEnt(Level lvl, BlockPos bp, BlockState bs, D4CPortalBlockEntity invisiBlockEntity) {
        invisiBlockEntity.tick();
    }

}
