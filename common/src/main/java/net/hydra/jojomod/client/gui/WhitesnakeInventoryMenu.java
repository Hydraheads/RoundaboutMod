package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.access.WhitesnakeInventoryAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.hydra.jojomod.stand.powers.WhitesnakeControlInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class WhitesnakeInventoryMenu extends AbstractContainerMenu {
    private static final int WHITESNAKE_SLOTS = WhitesnakeControlInventory.SIZE;
    private final Container whitesnakeInventory;

    private WhitesnakeInventoryMenu(int containerId, Inventory playerInventory, Player owner) {
        super(MenuType.GENERIC_9x1, containerId);
        whitesnakeInventory = new ControlContainer(owner);
        checkContainerSize(whitesnakeInventory, WHITESNAKE_SLOTS);
        whitesnakeInventory.startOpen(owner);

        for (int slot = 0; slot < WHITESNAKE_SLOTS; slot++) {
            final int inventorySlot = slot;
            addSlot(new Slot(whitesnakeInventory, slot, 8 + slot * 18, 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return canPlaceInSlot(inventorySlot, stack);
                }
            });
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9,
                        8 + column * 18, 50 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 108));
        }
    }

    public static void open(ServerPlayer player) {
        if (!hasWhitesnake(player)) return;
        player.openMenu(new SimpleMenuProvider(
                (containerId, inventory, menuPlayer) ->
                        new WhitesnakeInventoryMenu(containerId, inventory, menuPlayer),
                Component.translatable("container.roundabout.whitesnake_inventory")));
        player.playNotifySound(ModSounds.WHITESNAKE_INVENTORY_OPEN_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static boolean hasWhitesnake(Player player) {
        return player.isAlive()
                && ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake;
    }

    private static boolean canPlaceInSlot(int slot, ItemStack stack) {
        if (slot == 0) return false;
        if (slot == WhitesnakeControlInventory.WEAPON_SLOT) {
            return WhitesnakeControlInventory.isWeapon(stack);
        }
        return slot >= WhitesnakeControlInventory.STORAGE_START
                && (WhitesnakeControlInventory.isAmmo(stack)
                || WhitesnakeControlInventory.isDisc(stack));
    }

    @Override
    public boolean stillValid(Player player) {
        return whitesnakeInventory.stillValid(player) && hasWhitesnake(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = getSlot(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < WHITESNAKE_SLOTS) {
            if (!moveItemStackTo(stack, WHITESNAKE_SLOTS, slots.size(), true)) return ItemStack.EMPTY;
        } else if (WhitesnakeControlInventory.isWeapon(stack)) {
            if (!moveItemStackTo(stack, WhitesnakeControlInventory.WEAPON_SLOT,
                    WhitesnakeControlInventory.WEAPON_SLOT + 1, false)) return ItemStack.EMPTY;
        } else if (WhitesnakeControlInventory.isAmmo(stack)
                || WhitesnakeControlInventory.isDisc(stack)) {
            if (!moveItemStackTo(stack, WhitesnakeControlInventory.STORAGE_START,
                    WHITESNAKE_SLOTS, false)) return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        whitesnakeInventory.stopOpen(player);
        whitesnakeInventory.setChanged();
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.playNotifySound(ModSounds.WHITESNAKE_INVENTORY_CLOSE_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private static final class ControlContainer implements Container {
        private final Player owner;
        private final NonNullList<ItemStack> items;

        private ControlContainer(Player owner) {
            this.owner = owner;
            items = WhitesnakeControlInventory.get(owner);
        }

        @Override
        public int getContainerSize() {
            return WHITESNAKE_SLOTS;
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack stack : items) if (!stack.isEmpty()) return false;
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            return items.get(slot);
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack removed = ContainerHelper.removeItem(items, slot, amount);
            if (!removed.isEmpty()) setChanged();
            return removed;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack removed = items.get(slot);
            items.set(slot, ItemStack.EMPTY);
            return removed;
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            if (!stack.isEmpty() && !canPlaceInSlot(slot, stack)) return;
            items.set(slot, stack);
            if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
                stack.setCount(getMaxStackSize());
            }
            setChanged();
        }

        @Override
        public void setChanged() {
            ((WhitesnakeInventoryAccess) owner).roundaboutWhitesnake$syncInventory();
        }

        @Override
        public boolean stillValid(Player player) {
            return player == owner;
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return canPlaceInSlot(slot, stack);
        }

        @Override
        public void clearContent() {
            for (int slot = 0; slot < items.size(); slot++) items.set(slot, ItemStack.EMPTY);
            setChanged();
        }
    }
}
