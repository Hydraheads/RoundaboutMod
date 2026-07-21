package net.hydra.jojomod.block;

import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import javax.annotation.Nullable;
import java.util.UUID;

public class KingBedBlockEntity extends BlockEntity {
    public KingBedBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.KING_BED_BLOCK_ENTITY, $$0, $$1);
    }

    public KingBedBlockEntity(BlockPos $$0, BlockState $$1, DyeColor $$2) {
        super(ModBlocks.KING_BED_BLOCK_ENTITY, $$0, $$1);
    }

    private UUID standUUID;
    private UUID bedUUID;

    public void setStandUUID(UUID uuid) {
        this.standUUID = uuid;
        setChanged();
    }
    public void setBedUUID(UUID uuid) {
        this.bedUUID = uuid;
        setChanged();
    }

    @Nullable
    public UUID getBedUUID() {
        return bedUUID;
    }

    @Nullable
    public UUID getStandUUID() {
        return standUUID;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (standUUID != null) {
            tag.putUUID("StandUUID", standUUID);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.hasUUID("StandUUID")) {
            standUUID = tag.getUUID("StandUUID");
        } else {
            standUUID = null;
        }
    }

    public static void tickThis(Level level, BlockPos pos, BlockState state, KingBedBlockEntity bed) {
        if (level.isClientSide) {
            return;
        }

        UUID uuid = bed.getStandUUID();
        if (uuid == null) {
            removeBed(level, pos, state);
            return;
        }

        Entity entity = ((ServerLevel) level).getEntity(uuid);

        if (entity instanceof CaliforniaKingBedEntity ckb){
            if (ckb.bedUUID == null || !ckb.bedUUID.equals(bed.getBedUUID())){
                removeBed(level, pos, state);
            }
        } else {
            removeBed(level, pos, state);
        }

    }
    private static void removeBed(Level level, BlockPos pos, BlockState state) {
        BedPart part = state.getValue(BedBlock.PART);
        Direction facing = state.getValue(BedBlock.FACING);

        BlockPos otherPos = part == BedPart.FOOT
                ? pos.relative(facing)
                : pos.relative(facing.getOpposite());

        level.removeBlock(pos, false);

        if (level.getBlockState(otherPos).getBlock() instanceof BedBlock) {
            level.removeBlock(otherPos, false);
        }
    }
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
