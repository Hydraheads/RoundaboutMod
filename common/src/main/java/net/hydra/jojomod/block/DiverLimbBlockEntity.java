package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.UUID;

public class DiverLimbBlockEntity extends BlockEntity {
    // REALLY IMPORTANT so that other diver down users don't end up with the same
    // limbs.
    public UUID ownerUUID;
    // 0 = Right Arm, 1 = Left Arm, 2 = Right Leg, 3 = Left Leg
    // The move should cycle through both arms, then both legs. Repeat if used
    // again.
    public Direction facing = Direction.NORTH;
    public int limbIndex = 0;
    public byte standSkin = 0;

    public DiverLimbBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.DIVER_LIMB_BLOCK_ENTITY, pos, state);
    }

    // all of the bellow are so that the limbs correctly cycle and match the server
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
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DiverLimbBlockEntity limb) {
        if (!level.isClientSide()) {
            // Additional checks for a null UUID, just in case
            if (limb.ownerUUID == null) {
                level.removeBlock(pos, false);
                return;
            }
            if (level.getServer() != null && level.getGameTime() % 20 == 0) {
                // Break limb if the supporting block is gone
                BlockPos attachedPos = pos.relative(limb.facing);
                if (level.getBlockState(attachedPos).isAir() || level.getBlockState(attachedPos).canBeReplaced()) {
                    level.removeBlock(pos, false);
                    return;
                }
                // checks if they player stil exists
                if (limb.ownerUUID != null) {
                    ServerPlayer owner = level.getServer().getPlayerList().getPlayer(limb.ownerUUID);

                    // Checks if player is null (left server / logged off),
                    // or player is dead, or player no longer has Diver Down
                    if (owner == null
                            || !owner.isAlive()
                            || owner.isRemoved()
                            || !(((StandUser) owner).roundabout$getStandPowers() instanceof PowersDiverDown)) {
                        level.removeBlock(pos, false);
                    }
                }
            }
        }
    }
}
