package net.hydra.jojomod.block;

import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;

public class ChessPieceBlockEntity extends BlockEntity {

    public ChessPieceBlockEntity(BlockPos $$0, BlockState $$1) {
        super(getBE($$0,$$1), $$0, $$1);
    }
    private ItemStack storedStack = ItemStack.EMPTY;

    public void setStoredStack(ItemStack stack) {
        this.storedStack = stack.copy();
        setChanged();
    }

    public ItemStack getStoredStack() {
        return storedStack;
    }
    public static BlockEntityType getBE(BlockPos $$0, BlockState $$1){
        return ModBlocks.CHESS_PIECE_BLOCK_ENTITY;
    }
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    public void popOutRecord() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        BlockPos blockPos = this.getBlockPos();
        ItemStack itemStack = this.getStoredStack();
        if (itemStack.isEmpty()) {
            return;
        }

        storedStack = null;
        Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 0.3, 0.5);
        ItemStack itemStack2 = itemStack.copy();
        ItemEntity itemEntity = new ItemEntity(this.level, vec3.x(), vec3.y(), vec3.z(), itemStack2);
        itemEntity.setPickUpDelay(2);
        itemEntity.setDeltaMovement(0,0.1F,0);
        this.level.addFreshEntity(itemEntity);
        level.playSound(null, vec3.x(), vec3.y(), vec3.z(),
                ModSounds.CHESS_BREAK_EVENT, SoundSource.PLAYERS, 1F,
                (float) (1.00f + Math.random() * 0.01f));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    private static Direction getNeighbourDirection(BedPart $$0, Direction $$1) {
        return $$0 == BedPart.FOOT ? $$1 : $$1.getOpposite();
    }
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (!storedStack.isEmpty()) {
            tag.put("StoredStack", storedStack.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("StoredStack")) {
            storedStack = ItemStack.of(tag.getCompound("StoredStack"));
        } else {
            storedStack = ItemStack.EMPTY;
        }
    }
}
