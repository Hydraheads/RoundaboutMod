package net.hydra.jojomod.client.gui.diverdown.custom_workbench_code;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.server.level.ServerLevel;

public class DiverDownCraftingMenu extends RecipeBookMenu<CraftingContainer> {
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    public DiverDownCraftingMenu(
            int containerId,
            Inventory inventory,
            ContainerLevelAccess access
    ) {
        super(ModMenus.DIVER_DOWN_CRAFTING, containerId);
        this.access = access;
        this.player = inventory.player;

        this.addSlot(new ResultSlot(inventory.player, this.craftSlots, this.resultSlots, 0, 125, 35));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                this.addSlot(new Slot(this.craftSlots, column + row * 3, 30 + column * 18, 17 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }

    public DiverDownCraftingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        this.access.execute((level, blockPos) -> {
            if (level instanceof ServerLevel serverLevel) {
                ItemStack result = ItemStack.EMPTY;
                CraftingRecipe recipe = serverLevel.getRecipeManager()
                        .getRecipeFor(RecipeType.CRAFTING, this.craftSlots, serverLevel)
                        .orElse(null);
                if (recipe != null) {
                    result = recipe.assemble(this.craftSlots, serverLevel.registryAccess());
                }
                this.resultSlots.setItem(0, result);
                this.broadcastChanges();
            }
        });
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents contents) {
        this.craftSlots.fillStackedContents(contents);
    }

    @Override
    public void clearCraftingContent() {
        this.resultSlots.clearContent();
        this.craftSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.craftSlots, this.player.level());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            movedStack = slotStack.copy();

            if (slotIndex == 0) {
                if (!this.moveItemStackTo(slotStack, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, movedStack);
            } else if (slotIndex >= 1 && slotIndex < 10) {
                if (!this.moveItemStackTo(slotStack, 10, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 10 && slotIndex < 37) {
                if (!this.moveItemStackTo(slotStack, 37, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 37 && slotIndex < 46 && !this.moveItemStackTo(slotStack, 10, 37, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == movedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return movedStack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @Override
    public int getSize() {
        return 10;
    }

    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int slotIndex) {
        return slotIndex != this.getResultSlotIndex();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.resultSlots.clearContent();
        if (!player.level().isClientSide) {
            this.clearContainer(player, this.craftSlots);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}