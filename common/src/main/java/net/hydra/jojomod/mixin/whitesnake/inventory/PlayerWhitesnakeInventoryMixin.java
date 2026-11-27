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
    private NonNullList<ItemStack> sbaWhitesnake$inventory =
            NonNullList.withSize(WhitesnakeControlInventory.SIZE, ItemStack.EMPTY);
    @Unique
    private final NonNullList<ItemStack> sbaWhitesnake$lastItems =
            NonNullList.withSize(WhitesnakeControlInventory.SIZE, ItemStack.EMPTY);
    @Unique
    private CompoundTag sbaWhitesnake$lastSync = new CompoundTag();

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void sbaWhitesnake$defineInventory(CallbackInfo ci) {
        ((Player) (Object) this).getEntityData().define(WHITESNAKE_INVENTORY, new CompoundTag());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void sbaWhitesnake$saveInventory(CompoundTag tag, CallbackInfo ci) {
        tag.put("WhitesnakeInventory", sbaWhitesnake$serialize());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void sbaWhitesnake$loadInventory(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("WhitesnakeInventory", Tag.TAG_COMPOUND)) {
            sbaWhitesnake$read(tag.getCompound("WhitesnakeInventory"));
        }
        sbaWhitesnake$syncInventory();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void sbaWhitesnake$tickInventory(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (WhitesnakeControlInventory.isActive(player)) {
            for (int i = 0; i < sbaWhitesnake$inventory.size(); i++) {
                ItemStack stack = sbaWhitesnake$inventory.get(i);
                if (!stack.isEmpty()) stack.inventoryTick(player.level(), player, i, i == player.getInventory().selected);
            }
            if (!player.level().isClientSide()) WhitesnakeControlInventory.pickupNearby(player);
        }
        if (!player.level().isClientSide()) {
            sbaWhitesnake$syncInventory();
        }
    }

    @Override
    public NonNullList<ItemStack> sbaWhitesnake$getInventory() {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide()) {
            CompoundTag synced = player.getEntityData().get(WHITESNAKE_INVENTORY);
            if (!synced.equals(sbaWhitesnake$lastSync)) sbaWhitesnake$read(synced);
        }
        return sbaWhitesnake$inventory;
    }

    @Override
    public void sbaWhitesnake$setInventory(NonNullList<ItemStack> inventory) {
        sbaWhitesnake$inventory = NonNullList.withSize(WhitesnakeControlInventory.SIZE, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(inventory.size(), sbaWhitesnake$inventory.size()); i++) {
            sbaWhitesnake$inventory.set(i, inventory.get(i).copy());
        }
        sbaWhitesnake$inventory.set(0, ItemStack.EMPTY);
        sbaWhitesnake$syncInventory();
    }

    @Override
    public void sbaWhitesnake$syncInventory() {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide()) return;
        boolean changed = false;
        for (int i = 0; i < sbaWhitesnake$inventory.size(); i++) {
            if (!ItemStack.matches(sbaWhitesnake$inventory.get(i), sbaWhitesnake$lastItems.get(i))) {
                changed = true;
                break;
            }
        }
        if (!changed) return;
        CompoundTag tag = sbaWhitesnake$serialize();
        sbaWhitesnake$lastSync = tag.copy();
        for (int i = 0; i < sbaWhitesnake$inventory.size(); i++) {
            sbaWhitesnake$lastItems.set(i, sbaWhitesnake$inventory.get(i).copy());
        }
        player.getEntityData().set(WHITESNAKE_INVENTORY, tag);
    }

    @Unique
    private CompoundTag sbaWhitesnake$serialize() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, sbaWhitesnake$inventory);
        return tag;
    }

    @Unique
    private void sbaWhitesnake$read(CompoundTag tag) {
        NonNullList<ItemStack> loaded = NonNullList.withSize(WhitesnakeControlInventory.SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, loaded);
        loaded.set(0, ItemStack.EMPTY);
        sbaWhitesnake$inventory = loaded;
        sbaWhitesnake$lastSync = tag.copy();
    }
}
