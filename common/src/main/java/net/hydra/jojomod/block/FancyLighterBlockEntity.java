package net.hydra.jojomod.block;

import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class FancyLighterBlockEntity extends BlockEntity {
    public FancyLighterBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlocks.FANCY_LIGHTER_BLOCK_ENTITY, $$1, $$2);
    }

    private ItemStack storedStack = ItemStack.EMPTY;

    public void setStoredStack(ItemStack stack) {
        this.storedStack = stack.copy();
        setChanged();
    }
    public ItemStack getStoredStack() {
        return storedStack;
    }
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    /*This is the integer in which the owner's id gets saved*/
    private UUID owner;

    public UUID getOwner(){
        return owner;
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
                SoundEvents.METAL_HIT, SoundSource.PLAYERS, 1F,
                (float) (1.00f + Math.random() * 0.01f));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setValue(UUID thisowner){
        if(owner == null){
            owner = thisowner;
        }
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        if(owner != null) {
            tag.putUUID("owner", owner);
        }
        if (!storedStack.isEmpty()) {
            tag.put("StoredStack", storedStack.save(new CompoundTag()));
        }
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if(tag.contains("owner")) {
            owner = tag.getUUID("owner");
        }
        if (tag.contains("StoredStack")) {
            storedStack = ItemStack.of(tag.getCompound("StoredStack"));
        } else {
            storedStack = ItemStack.EMPTY;
        }
    }
}
