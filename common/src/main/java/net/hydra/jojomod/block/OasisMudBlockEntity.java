package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;

public class OasisMudBlockEntity extends BlockEntity {

    private BlockState copiedState = Blocks.AIR.defaultBlockState();
    private int ticksRemaining = -1;

    public OasisMudBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.OASIS_MUD_BLOCK_ENTITY, blockPos, blockState);
    }

    public void tick() {
        if (level != null && !level.isClientSide && --ticksRemaining <= 0 && level.getBlockEntity(worldPosition) == this) {
            this.revert(this.getBlockPos());
        }
    }

    public static void tickBlockEnt(Level lvl, BlockPos bp, BlockState bs, OasisMudBlockEntity oasisMudBlockEntity) {
        oasisMudBlockEntity.tick();
    }



    public void revert(BlockPos pos) {
        if (level != null && !level.isClientSide) {
            BlockState restore = this.copiedState;
            if (restore.isAir()) {
                level.removeBlock(pos, false);
            } else {
                level.setBlock(pos, restore, Block.UPDATE_ALL);
            }
        }
    }


    public void initialize(BlockState copiedState, int durationTicks) {
        this.copiedState = copiedState;
        this.ticksRemaining = durationTicks;
        this.setChanged();
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("CopiedState")) {
            this.copiedState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("CopiedState"));
        }
        if (tag.contains("TicksRemaining")) {
            this.ticksRemaining = tag.getInt("TicksRemaining");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("CopiedState", NbtUtils.writeBlockState(this.copiedState));
        tag.putInt("TicksRemaining", this.ticksRemaining);
    }



    public BlockState getCopiedState() {
        return this.copiedState;
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

}
