package net.hydra.jojomod.block;

import net.hydra.jojomod.menu.GamblingTableMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GamblingTableBlockEntity extends BlockEntity implements Container, MenuProvider {
    // byte list for all the games, when adding a game, be sure to update the bytes here.
    /* list of all classes you need to change whenever adding a game:
    This one
    GamblingTableMenu
    GamblingGameSelectionScreen
    en_us json
     */
    public static final byte
            WAITING = 0,
            COIN_DROP = 1;

    private int selectedGame = WAITING;

    private NonNullList<ItemStack> items = NonNullList.withSize(GamblingTableMenu.BET_SLOT_COUNT, ItemStack.EMPTY);
    // UUIDs are used for tracking the players
    private UUID hostUUID;
    private UUID challengerUUID;
    private final Set<UUID> activeViewers = new HashSet<>();
    private boolean gameInProgress = false;
    private boolean hostAccepted = false;
    private boolean challengerAccepted = false;

    public enum Role { HOST, CHALLENGER, SPECTATOR }

    public GamblingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.GAMBLING_TABLE_BLOCK_ENTITY, pos, state);
    }


    // Tracks when a player open the GUI
    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.activeViewers.add(player.getUUID());
        }
    }

    // Tracks when a player closes the GUI, resets the game if both the challenger and host are gone
    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.activeViewers.remove(player.getUUID());

            // If nobody is interacting with the table at the time of betting
            if (this.activeViewers.isEmpty() && !this.gameInProgress && this.level != null && !this.level.isClientSide()) {
                returnAllItems();
                resetGame();
            }
        }
    }

    // Returns items for both players
    public void returnAllItems() {
        if (this.level == null || this.level.isClientSide()) return;

        // Return Host items (slots 0-8)
        returnSlotRange(0, 9, this.hostUUID);

        // Return Challenger items (slots 9-17)
        returnSlotRange(9, 18, this.challengerUUID);

        this.setChanged();
    }

    private void returnSlotRange(int startSlot, int endSlot, UUID playerUUID) {
        Player player = playerUUID != null ? this.level.getPlayerByUUID(playerUUID) : null;

        for (int i = startSlot; i < endSlot; i++) {
            ItemStack stack = this.items.get(i);
            if (!stack.isEmpty()) {
                if (player != null && player.isAlive()) {
                    // Add to player inventory;, drops at feet if inventory is full
                    if (!player.getInventory().add(stack)) {
                        player.drop(stack, false);
                    }
                } else {
                    // Drops at table if player leaves the server/gets killed/whatever
                    Containers.dropItemStack(this.level,
                            this.worldPosition.getX() + 0.5,
                            this.worldPosition.getY() + 1.0,
                            this.worldPosition.getZ() + 0.5,
                            stack);
                }
                this.items.set(i, ItemStack.EMPTY);
            }
        }
    }

    /* Getter method for checking the role of the player based on their UUID
     * Right now, the gambling table only supports players, so when villager gambling AI is added,
     * you can easily make it so that the villager's UUID is tracked as well.
     *
     * Other players are assigned as spectators. I might also be able to implement the "return items"
     * feature of the gambling table by tracking the UUID of whoever broke the table as well.
     */
    public Role getPlayerRole(Player player) {
        if (this.hostUUID == null || this.hostUUID.equals(player.getUUID())) {
            this.hostUUID = player.getUUID();
            this.setChanged();
            return Role.HOST;
        }
        if (this.challengerUUID == null || this.challengerUUID.equals(player.getUUID())) {
            this.challengerUUID = player.getUUID();
            this.setChanged();
            return Role.CHALLENGER;
        }
        return Role.SPECTATOR;
    }

    // resets the game
    public void resetGame() {
        this.hostUUID = null;
        this.challengerUUID = null;
        this.selectedGame = WAITING;
        setChanged();
    }

    public void setGameInProgress(boolean inProgress) {
        this.gameInProgress = inProgress;
        this.setChanged();
    }

    // For game selection

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? selectedGame : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                selectedGame = value;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    // creates the gambling menu
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        Role role = getPlayerRole(player);
        ContainerData menuData = new ContainerData() {
            @Override
            public int get(int index) {
                if (index == 0) return selectedGame;
                if (index == 1) return role == Role.HOST ? 1 : 0;
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) selectedGame = value;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        return new GamblingTableMenu(containerId, playerInventory, this, ContainerLevelAccess.create(this.level, this.worldPosition), menuData);
    }

    @Override
    public int getContainerSize() {
        return GamblingTableMenu.BET_SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        return result;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.setChanged();
    }

    // accepting and starting the game

    public void setPlayerAccepted(Player player, boolean accepted) {
        Role role = getPlayerRole(player);
        if (role == Role.HOST) {
            this.hostAccepted = accepted;
        } else if (role == Role.CHALLENGER) {
            this.challengerAccepted = accepted;
        }
        this.setChanged();

        if (this.hostAccepted && this.challengerAccepted) {
            startGamble();
        }
    }

    private void startGamble() {
        this.gameInProgress = true;
        // IT'S GAMBLING TIME!!!!
        this.level.getServer().getPlayerList().broadcastSystemMessage(Component.literal("IT'S GAMBLING TIME!!!!! game: " + this.selectedGame), false);
    }

    // saves and loads whatever is in storage
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    // simple getters and setters

    public int getSelectedGame() {
        return this.selectedGame;
    }

    public void setSelectedGame(int game) {
        this.selectedGame = game;
        this.setChanged();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.items.get(slot);
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.roundabout.gambling_table");
    }
}