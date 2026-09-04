package net.hydra.jojomod.event.powers.whitesnake;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.WhitesnakeInventoryAccess;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ColtRevolverItem;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.SnubnoseAmmoItem;
import net.hydra.jojomod.item.SnubnoseRevolverItem;
import net.hydra.jojomod.item.CommandDiscItem;
import net.hydra.jojomod.item.AbstractBodyDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public final class WhitesnakeControlInventory {
    public static final int SIZE = 9;
    public static final int WEAPON_SLOT = 1;
    public static final int STORAGE_START = 2;

    private WhitesnakeControlInventory() {
    }

    public static boolean isActive(Player player) {
        return controlledStand(player) != null;
    }

    public static WhitesnakeEntity controlledStand(Player player) {
        StandEntity stand = ((StandUser) player).roundabout$getStand();
        if (!(stand instanceof WhitesnakeEntity whitesnake)
                || ((IPlayerEntity) player).roundabout$getControlling() != stand.getId()
                || !stand.isAlive() || stand.isRemoved()) return null;
        return whitesnake;
    }

    public static NonNullList<ItemStack> get(Player player) {
        return ((WhitesnakeInventoryAccess) player).roundaboutWhitesnake$getInventory();
    }

    public static ItemStack getSelected(Player player) {
        int slot = player.getInventory().selected;
        return slot >= 0 && slot < SIZE ? get(player).get(slot) : ItemStack.EMPTY;
    }

    public static boolean isWeapon(ItemStack stack) {
        return stack.getItem() instanceof ColtRevolverItem
                || stack.getItem() instanceof SnubnoseRevolverItem;
    }

    public static boolean isAmmo(ItemStack stack) {
        return stack.getItem() instanceof SnubnoseAmmoItem;
    }

    public static boolean isDisc(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.getItem() instanceof StandDiscItem
                || stack.getItem() instanceof AbstractBodyDiscItem
                || stack.getItem() instanceof CommandDiscItem
                || stack.getItem() instanceof RecordItem) return true;
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        return path.endsWith("disc") || path.endsWith("_disc") || path.contains("disc_");
    }

    public static boolean canPickUp(ItemStack stack) {
        return isWeapon(stack) || isAmmo(stack) || isDisc(stack);
    }

    public static boolean isHeldItem(ItemStack stack) {
        return isWeapon(stack) || isDisc(stack);
    }

    public static int insert(Player player, ItemStack source) {
        if (source.isEmpty()) return 0;
        int before = source.getCount();
        NonNullList<ItemStack> inventory = get(player);
        if (isWeapon(source)) {
            if (inventory.get(WEAPON_SLOT).isEmpty()) {
                int moved = Math.min(source.getCount(), source.getMaxStackSize());
                ItemStack placed = source.copy();
                placed.setCount(moved);
                inventory.set(WEAPON_SLOT, placed);
                source.shrink(moved);
            }
        } else if (isAmmo(source) || isDisc(source)) {
            for (int i = STORAGE_START; i < SIZE && !source.isEmpty(); i++) {
                ItemStack existing = inventory.get(i);
                if (!existing.isEmpty() && ItemStack.isSameItemSameTags(existing, source)) {
                    int moved = Math.min(source.getCount(), existing.getMaxStackSize() - existing.getCount());
                    if (moved > 0) {
                        existing.grow(moved);
                        source.shrink(moved);
                    }
                }
            }
            for (int i = STORAGE_START; i < SIZE && !source.isEmpty(); i++) {
                if (inventory.get(i).isEmpty()) {
                    int moved = Math.min(source.getCount(), source.getMaxStackSize());
                    ItemStack placed = source.copy();
                    placed.setCount(moved);
                    inventory.set(i, placed);
                    source.shrink(moved);
                }
            }
        }
        int inserted = before - source.getCount();
        if (inserted > 0) ((WhitesnakeInventoryAccess) player).roundaboutWhitesnake$syncInventory();
        return inserted;
    }

    public static void pickupNearby(Player player) {
        if (player.level().isClientSide()) return;
        WhitesnakeEntity stand = controlledStand(player);
        if (stand == null) return;
        for (ItemEntity item : player.level().getEntitiesOfClass(ItemEntity.class,
                stand.getBoundingBox().inflate(1.5D, 1.0D, 1.5D),
                entity -> entity.isAlive() && !entity.hasPickUpDelay()
                        && canPickUp(entity.getItem()))) {
            ItemStack remaining = item.getItem().copy();
            int inserted = insert(player, remaining);
            if (inserted <= 0) continue;
            stand.take(item, inserted);
            player.level().playSound(null, stand.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,
                    0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            if (remaining.isEmpty()) item.discard();
            else item.setItem(remaining);
        }
    }

    public static boolean hasAmmo(Player player) {
        if (player.isCreative()) return true;
        for (int i = STORAGE_START; i < SIZE; i++) {
            ItemStack stack = get(player).get(i);
            if (isAmmo(stack) && !stack.isEmpty()) return true;
        }
        return false;
    }

    public static int consumeAmmo(Player player, int amount) {
        if (player.isCreative()) return amount;
        int consumed = 0;
        for (int i = STORAGE_START; i < SIZE && amount > 0; i++) {
            ItemStack stack = get(player).get(i);
            if (!isAmmo(stack)) continue;
            int removed = Math.min(stack.getCount(), amount);
            stack.shrink(removed);
            consumed += removed;
            amount -= removed;
            if (stack.isEmpty()) get(player).set(i, ItemStack.EMPTY);
        }
        if (consumed > 0) ((WhitesnakeInventoryAccess) player).roundaboutWhitesnake$syncInventory();
        return consumed;
    }

    public static void reload(ServerPlayer player) {
        if (!isActive(player)) return;
        ItemStack weapon = getSelected(player);
        if (!(weapon.getItem() instanceof FirearmItem)) return;
        FirearmItem.cycleReload = true;
        try {
            weapon.use(player.level(), player, InteractionHand.MAIN_HAND);
        } finally {
            FirearmItem.cycleReload = false;
        }
    }
}
