package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class SilverChariotRapierPlatformBlockEntity extends BlockEntity {
    public UUID ownerUUID;
    public Direction facing = Direction.NORTH;
    public byte standSkin = 0;
    public int clientAge = 0;

    public SilverChariotRapierPlatformBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlocks.SC_RAPIER_PLATFORM_BLOCK_ENTITY, $$1, $$2);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Facing", facing.get3DDataValue());
        tag.putByte("StandSkin", standSkin);
        if (this.ownerUUID != null) {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Facing")) {
            this.facing = Direction.from3DDataValue(tag.getInt("Facing"));
        }
        if (tag.contains("StandSkin")) {
            this.standSkin = tag.getByte("StandSkin");
        }
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUUID = tag.getUUID("OwnerUUID");
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SilverChariotRapierPlatformBlockEntity rapier) {
        if (level.isClientSide()) {
            rapier.clientAge++;
            return;
        }
        if (!level.isClientSide()) {
            if (rapier.ownerUUID == null) {
                level.removeBlock(blockPos, false);
                return;
            }
            if (level.getServer() != null && level.getGameTime() % 20 == 0) {
                BlockPos attachedPos = blockPos.relative(rapier.facing);
                if (level.getBlockState(attachedPos).isAir() || level.getBlockState(attachedPos).canBeReplaced()) {
                    level.removeBlock(blockPos, false);
                    return;
                }
                if (rapier.ownerUUID != null) {
                    ServerPlayer owner = level.getServer().getPlayerList().getPlayer(rapier.ownerUUID);

                    if (owner == null
                            || !owner.isAlive()
                            || owner.isRemoved()
                            || !(((StandUser) owner).roundabout$getStandPowers() instanceof PowersSilverChariot)
                    ) {
                        level.removeBlock(blockPos, false);
                    }
                }
            }
        }
    }
}
