package net.hydra.jojomod.menu;

import net.hydra.jojomod.block.GamblingTableBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GamblingTableMenu extends AbstractContainerMenu {
    // for the games
    public static final byte
            BUTTON_COIN_DROP = 10;

    public static final int BET_SLOT_COUNT = 18;
    private final Container betContainer;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final Player player;
    public static final int BUTTON_ACCEPT = 0;
    public static final int BUTTON_DENY = 1;

    // Client-side constructor called by MenuType
    public GamblingTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(BET_SLOT_COUNT), ContainerLevelAccess.NULL, new SimpleContainerData(2));
    }

    // Server-side constructor called by GamblingTableBlockEntity
    public GamblingTableMenu(int containerId, Inventory playerInventory, Container betContainer, ContainerLevelAccess access, ContainerData data) {
        super(ModMenus.GAMBLING_TABLE, containerId);
        this.access = access;
        this.betContainer = betContainer;
        this.player = playerInventory.player;
        this.data = data;
        checkContainerSize(betContainer, BET_SLOT_COUNT);
        betContainer.startOpen(playerInventory.player);
        this.addDataSlots(data);

        // HAVE YOU EVER PLAYED GAMBLING GAMES
        // WITH YOUR LIFE ON THE LINE?? (Betting slots)
        // Top row is for the host bet slots
        for (int col = 0; col < 9; ++col) {
            int slotIndex = col;
            this.addSlot(new Slot(this.betContainer, slotIndex, 8 + col * 18, 16) {
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
            this.addSlot(new Slot(this.betContainer, slotIndex, 8 + col * 18, 52) {
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

    // for the text that shows up saying what game is about to be played.
    public Component getGameName() {
        int game = this.data.get(0);
        return switch (game) {
            case 1 -> Component.translatable("gui.roundabout.gambling_table.game.coin_drop");
            default -> Component.translatable("gui.roundabout.gambling_table.game.waiting");
        };
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

    // tells the table "hey table!! this player clicked accept!!"

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (this.betContainer instanceof GamblingTableBlockEntity table) {
            if (id == BUTTON_ACCEPT) {
                table.setPlayerAccepted(player, true);
                return true;
            } else if (id == BUTTON_DENY) {
                table.setPlayerAccepted(player, false);
                return true;
            } else if (id >= 10 && isHost(player)) {
                int gameId = id - 10;
                table.setSelectedGame(gameId);
                return true;
            }
        }
        return false;
    }

    public boolean isHost(Player p) {
        if (this.betContainer instanceof GamblingTableBlockEntity table) {
            return table.getPlayerRole(p) == GamblingTableBlockEntity.Role.HOST;
        }
        // Client side check
        return this.data.get(1) == 1;
    }

    public boolean isChallenger(Player p) {
        if (this.betContainer instanceof GamblingTableBlockEntity table) {
            return table.getPlayerRole(p) == GamblingTableBlockEntity.Role.CHALLENGER;
        }
        return true;
    }

    //literally all this is used for is to track if the host has selected a game that way the host gets auto sent to the selection menu
    public boolean hasGameStarted() {return this.data.get(0) > 0;}

    public void setSelectedGameClient(int gameId) {
        this.data.set(0, gameId);
    }
}