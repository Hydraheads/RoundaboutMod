package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GEBushBlockEntity extends BlockEntity {
    private UUID ownerUUID = null;
    private int age = 0;
    // in ticks, so 600 ticks = 30 seconds
    private final int maxAge = 600;
    private final Map<UUID, Double> accumulatedDistances = new HashMap<>();

    public GEBushBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.GE_BUSH_BLOCK_ENTITY, pos, state);
    }

    public void setOwner(UUID owner) {
        this.ownerUUID = owner;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public boolean accumulateDistanceAndCheck(UUID entityId, double deltaDistance) {
        double total = accumulatedDistances.getOrDefault(entityId, 0.0D) + deltaDistance;
        if (total >= 0.5D) {
            accumulatedDistances.put(entityId, total - 0.5D); // Retain remainder for smooth progression
            return true;
        }
        accumulatedDistances.put(entityId, total);
        return false;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GEBushBlockEntity entity) {
        entity.age++;
        if (entity.age >= entity.maxAge) {
            level.destroyBlock(pos, false);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }
}