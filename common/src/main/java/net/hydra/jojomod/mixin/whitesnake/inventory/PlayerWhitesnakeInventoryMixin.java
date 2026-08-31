package net.hydra.jojomod.mixin.whitesnake.inventory;

import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.hydra.jojomod.access.WhitesnakeInventoryAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ContainerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerWhitesnakeInventoryMixin implements WhitesnakeInventoryAccess {
    @Unique
    private static final EntityDataAccessor<CompoundTag> WHITESNAKE_INVENTORY =
            SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
    @Unique
    private NonNullList<ItemStack> roundaboutWhitesnake$inventory =
            NonNullList.withSize(WhitesnakeControlInventory.SIZE, ItemStack.EMPTY);
    @Unique
    private CompoundTag roundaboutWhitesnake$lastSync = new CompoundTag();

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void roundaboutWhitesnake$defineInventory(CallbackInfo ci) {
        ((Player) (Object) this).getEntityData().define(WHITESNAKE_INVENTORY, new CompoundTag());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void roundaboutWhitesnake$saveInventory(CompoundTag tag, CallbackInfo ci) {
        tag.put("WhitesnakeInventory", roundaboutWhitesnake$serialize());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void roundaboutWhitesnake$loadInventory(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("WhitesnakeInventory", Tag.TAG_COMPOUND)) {
            roundaboutWhitesnake$read(tag.getCompound("WhitesnakeInventory"));
        }
        roundaboutWhitesnake$syncInventory();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundaboutWhitesnake$tickInventory(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (WhitesnakeControlInventory.isActive(player)) {
            for (int i = 0; i < roundaboutWhitesnake$inventory.size(); i++) {
                ItemStack stack = roundaboutWhitesnake$inventory.get(i);
                if (!stack.isEmpty()) stack.inventoryTick(player.level(), player, i, i == player.getInventory().selected);
            }
            if (!player.level().isClientSide()) WhitesnakeControlInventory.pickupNearby(player);
        }
        if (!player.level().isClientSide()) {
            CompoundTag current = roundaboutWhitesnake$serialize();
            if (!current.equals(roundaboutWhitesnake$lastSync)) roundaboutWhitesnake$syncInventory();
        }
    }

    @Override
    public NonNullList<ItemStack> roundaboutWhitesnake$getInventory() {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide()) {
            CompoundTag synced = player.getEntityData().get(WHITESNAKE_INVENTORY);
            if (!synced.equals(roundaboutWhitesnake$lastSync)) roundaboutWhitesnake$read(synced);
        }
        return roundaboutWhitesnake$inventory;
    }

    @Override
    public void roundaboutWhitesnake$setInventory(NonNullList<ItemStack> inventory) {
        roundaboutWhitesnake$inventory = NonNullList.withSize(WhitesnakeControlInventory.SIZE, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(inventory.size(), roundaboutWhitesnake$inventory.size()); i++) {
            roundaboutWhitesnake$inventory.set(i, inventory.get(i).copy());
        }
        roundaboutWhitesnake$inventory.set(0, ItemStack.EMPTY);
        roundaboutWhitesnake$syncInventory();
    }

    @Override
    public void roundaboutWhitesnake$syncInventory() {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide()) return;
        CompoundTag tag = roundaboutWhitesnake$serialize();
        roundaboutWhitesnake$lastSync = tag.copy();
        player.getEntityData().set(WHITESNAKE_INVENTORY, tag, true);
    }

    @Unique
    private CompoundTag roundaboutWhitesnake$serialize() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, roundaboutWhitesnake$inventory);
        return tag;
    }

    @Unique
    private void roundaboutWhitesnake$read(CompoundTag tag) {
        NonNullList<ItemStack> loaded = NonNullList.withSize(WhitesnakeControlInventory.SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, loaded);
        loaded.set(0, ItemStack.EMPTY);
        roundaboutWhitesnake$inventory = loaded;
        roundaboutWhitesnake$lastSync = tag.copy();
    }
}
