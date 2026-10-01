package net.hydra.jojomod.menu;

import net.hydra.jojomod.block.GamblingTableBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GamblingTableMenu extends AbstractContainerMenu {
    public static final int BET_SLOT_COUNT = 18;
    private final Container betContainer;
    private final ContainerLevelAccess access;
    private final Player player;

    // Client-side constructor called by MenuType
    public GamblingTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(BET_SLOT_COUNT), ContainerLevelAccess.NULL);
    }

    // Server-side constructor called by GamblingTableBlockEntity
    public GamblingTableMenu(int containerId, Inventory playerInventory, Container betContainer, ContainerLevelAccess access) {
        super(ModMenus.GAMBLING_TABLE, containerId);
        this.access = access;
        this.betContainer = betContainer;
        this.player = playerInventory.player;
        checkContainerSize(betContainer, BET_SLOT_COUNT);
        betContainer.startOpen(playerInventory.player);

        // HAVE YOU EVER PLAYED GAMBLING GAMES
        // WITH YOUR LIFE ON THE LINE?? (Betting slots)
        // Top row is for the host bet slots
        for (int col = 0; col < 9; ++col) {
            int slotIndex = col;
            this.addSlot(new Slot(this.betContainer, slotIndex, 8 + col * 18, 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return isHost(player);
                }
                @Override
                public boolean mayPickup(Player player) {
                    return isHost(player);
                }
            });
        }

        // Bottom rows are for challenger betting slots
        for (int col = 0; col < 9; ++col) {
            int slotIndex = col + 9;
            this.addSlot(new Slot(this.betContainer, slotIndex, 8 + col * 18, 54) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return isChallenger(player);
                }
                @Override
                public boolean mayPickup(Player player) {
                    return isChallenger(player);
                }
            });
        }

        // Player's inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player's hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.betContainer.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();
            if (index < BET_SLOT_COUNT) {
                if (!this.moveItemStackTo(stackInSlot, BET_SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, BET_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.betContainer.stopOpen(player);
    }

    public Container getBetContainer() {
        return this.betContainer;
    }


    public boolean isHost(Player p) {
        if (this.betContainer instanceof GamblingTableBlockEntity table) {
            return table.getPlayerRole(p) == GamblingTableBlockEntity.Role.HOST;
        }
        return true;
    }

    public boolean isChallenger(Player p) {
        if (this.betContainer instanceof GamblingTableBlockEntity table) {
            return table.getPlayerRole(p) == GamblingTableBlockEntity.Role.CHALLENGER;
        }
        return true;
    }
}