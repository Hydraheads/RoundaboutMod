package net.hydra.jojomod.block;

import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InvisiBlockEntity extends BlockEntity {
    private BlockState originalState = Blocks.AIR.defaultBlockState();

    public BlockState getOriginalState(){
        return originalState;
    }

    private CompoundTag originalTag = null;
    public int ticksUntilRestore = 100; // 5 seconds at 20 tps

    public LivingEntity standuser = null;

    public List<Vec3> bubbleList = new ArrayList<>();

    public void setOriginal(BlockState replacedState, @Nullable CompoundTag tag, Level level) {
        this.originalState = replacedState;
        this.originalTag = tag;
        /*** This just does not work and is inneficient
        if (level instanceof ServerLevel SL){
            CompoundTag $$0 = new CompoundTag();
            $$0.put("OriginalState", NbtUtils.writeBlockState(originalState));
            MainUtil.spreadRadialClientPacket(level,getBlockPos(),100, ServerToClientPackets.S2CPackets.MESSAGES.INVIS_BLOCK_STATE.value,
                    getBlockPos(),$$0);
        }
         **/
    }
    public void setOriginal2(BlockState replacedState) {
        this.originalState = replacedState;
    }
    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public void restoreNow() {
        if (level != null && !level.isClientSide && level.getBlockEntity(worldPosition) == this) {
            level.setBlock(worldPosition, originalState, 3);
            level.scheduleTick(worldPosition, originalState.getBlock(), 1);

            if (originalTag != null) {
                BlockEntity restored = level.getBlockEntity(worldPosition);
                if (restored != null) {
                    restored.load(originalTag);
                }
            }
            restoreParticles();
        }
    }

    public void restoreParticles(){

        if (level instanceof ServerLevel SL) {
            SL.sendParticles(
                    ModParticles.MAGIC_DUST, // use whatever type you like
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 0.5,
                    worldPosition.getZ() + 0.5,
                    7, // count
                    0.3, 0.3, 0.3, // spread
                    0.05 // speed
            );
        }
    }

    public void restoreParticlesLesser(){

        if (level instanceof ServerLevel SL) {
            SL.sendParticles(
                    ModParticles.MAGIC_DUST, // use whatever type you like
                    worldPosition.getX() + 0.45,
                    worldPosition.getY() + 0.45,
                    worldPosition.getZ() + 0.45,
                    10, // count
                    0.3, 0.3, 0.3, // spread
                    0.01 // speed
            );
        }
    }

    public InvisiBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.INVISIBLE_BLOCK_ENTITY, $$0, $$1);
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("OriginalState", NbtUtils.writeBlockState(originalState));
        tag.putInt("TicksLeft", ticksUntilRestore);
        if (originalTag != null) {
            tag.put("OriginalBlockEntity", originalTag);
        }
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("OriginalState")) {
            originalState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("OriginalState"));
        }
        if (tag.contains("TicksLeft")) {
            ticksUntilRestore = tag.getInt("TicksLeft");
        }
        if (tag.contains("OriginalBlockEntity")) {
            originalTag = tag.getCompound("OriginalBlockEntity");
        }
    }
    public void tick() {
        if (!level.isClientSide && --ticksUntilRestore <= 0) {
            if (level != null && level.getBlockEntity(worldPosition) == this) {
                level.setBlock(worldPosition, originalState, 3);
                restoreParticles();
                if (originalTag != null) {
                    BlockEntity restored = level.getBlockEntity(worldPosition);
                    level.scheduleTick(worldPosition, originalState.getBlock(), 1);
                    if (restored != null) {
                        restored.load(originalTag);
                    }
                }
            }
        }
    }
    public static void tickBlockEnt(Level lvl, BlockPos bp, BlockState bs, InvisiBlockEntity invisiBlockEntity) {
        invisiBlockEntity.tick();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("stored_state", NbtUtils.writeBlockState(this.originalState));
        return tag;
    }

}
