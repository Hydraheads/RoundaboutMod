package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.UUID;

public class DiverLimbBlockEntity extends BlockEntity {
    //REALLY IMPORTANT so that other diver down users don't end up with the same limbs.
    public UUID ownerUUID;
    // 0 = Right Arm, 1 = Left Arm, 2 = Right Leg, 3 = Left Leg
    // The move should cycle through both arms, then both legs. Repeat if used again.
    public Direction facing = Direction.NORTH;
    public int limbIndex = 0;
    public byte standSkin = 0;

    public DiverLimbBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DIVER_LIMB_BLOCK_ENTITY, pos, state);
    }

    //all of the bellow are so that the limbs correctly cycle and match the server
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Facing", facing.get3DDataValue());
        tag.putInt("LimbIndex", limbIndex);
        tag.putByte("StandSkin", standSkin);
        if (ownerUUID != null) {
            tag.putUUID("OwnerUUID", ownerUUID);
        }
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Facing")) {
            this.facing = Direction.from3DDataValue(tag.getInt("Facing"));
        }
        if (tag.contains("LimbIndex")) {
            this.limbIndex = tag.getInt("LimbIndex");
        }
        if (tag.contains("StandSkin")) {
            this.standSkin = tag.getByte("StandSkin");
        }
        if (tag.contains("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
    }
}
