package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class ProtectionBlockEntity extends BlockEntity {
    private int radius = 64;
    private UUID owner;

    public ProtectionBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.PROTECTION_BLOCK_ENTITY, pos, state);
        ProtectionSavedData sd = ProtectionSavedData.get((ServerLevel) level);
        if (!sd.isProtected(pos)) {
            sd.add(pos);
        }
    }

    public int getRadius() {
        return radius;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setData(int radius, UUID owner) {
        this.radius = radius;
        this.owner = owner;
        setChanged();
    }

    @Override
    public void setRemoved() {
        if (level != null && !level.isClientSide) {
            ProtectionSavedData.get((ServerLevel) level).remove(this.worldPosition);
        }
        super.setRemoved();
    }
}
