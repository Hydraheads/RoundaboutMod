package net.hydra.jojomod.client.gui;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;


public class FogInventoryMenu extends RecipeBookMenu<CraftingContainer> {
    public static final int CONTAINER_ID = 42357;
    public static final int RESULT_SLOT = 0;
    public static final int CRAFT_SLOT_START = 1;
    public static final int CRAFT_SLOT_END = 5;
    public static final int ARMOR_SLOT_START = 5;
    public static final int ARMOR_SLOT_END = 9;
    public static final int INV_SLOT_START = 9;
    public static final int INV_SLOT_END = 36;
    public static final int USE_ROW_SLOT_START = 36;
    public static final int USE_ROW_SLOT_END = 45;
    public static final int SHIELD_SLOT = 45;
    public static final ResourceLocation BLOCK_ATLAS = new ResourceLocation("textures/atlas/blocks.png");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
    public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = new ResourceLocation("item/empty_armor_slot_shield");
    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
            EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET
    };
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 2, 2);
    private final ResultContainer resultSlots = new ResultContainer();
    public final boolean active;
    private final Player owner;

    public FogInventoryMenu(Inventory inventory, boolean isActive, final Player player) {
        super(null, CONTAINER_ID);
        this.active = isActive;
        this.owner = player;
        this.addSlot(new ResultSlot(inventory.player, this.craftSlots, this.resultSlots, 0, 154, 28));

        // 2x2 crafting slot grid
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                this.addSlot(new Slot(this.craftSlots, y + x * 2, 98 + y * 18, 18 + x * 18));
            }
        }

        for (int slotIndex = 0; slotIndex < 4; slotIndex++) {
            final EquipmentSlot slot = SLOT_IDS[slotIndex];
            this.addSlot(new Slot(inventory, 39 - slotIndex, 8, 8 + slotIndex * 18) {
                @Override
                public void setByPlayer(ItemStack stack) {
                    FogInventoryMenu.onEquipItem(player, slot, stack, this.getItem());
                    super.setByPlayer(stack);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return slot == Mob.getEquipmentSlotForItem(stack);
                }

                @Override
                public boolean mayPickup(Player p) {
                    ItemStack stack = this.getItem();
                    return !stack.isEmpty() && !p.isCreative() && EnchantmentHelper.hasBindingCurse(stack) ? false : super.mayPickup(p);
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(FogInventoryMenu.BLOCK_ATLAS, FogInventoryMenu.TEXTURE_EMPTY_SLOTS[slot.getIndex()]);
                }
            });
        }

        // 3x9 grid of slots
        // do not ask me what the magic numbers are for
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 9; y++) {
                this.addSlot(new Slot(inventory, y + (x + 1) * 9, 8 + y * 18, 84 + x * 18));
            }
        }

        // hotbar slots (9x1)
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }

        this.addSlot(new Slot(inventory, 40, 77, 62) {
            @Override
            public void setByPlayer(ItemStack stack) {
                FogInventoryMenu.onEquipItem(player, EquipmentSlot.OFFHAND, stack, this.getItem());
                super.setByPlayer(stack);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(FogInventoryMenu.BLOCK_ATLAS, FogInventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    static void onEquipItem(Player p, EquipmentSlot slot, ItemStack newStack, ItemStack oldStack) {
        Equipable toEquip = Equipable.get(newStack);
        if (toEquip != null) {
            p.onEquipItem(slot, oldStack, newStack);
        }
    }

    public static boolean isHotbarSlot(int $$0) {
        return $$0 >= 36 && $$0 < 45 || $$0 == 45;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents $$0) {
        this.craftSlots.fillStackedContents($$0);
    }

    @Override
    public void clearCraftingContent() {
        this.resultSlots.clearContent();
        this.craftSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> $$0) {
        return $$0.matches(this.craftSlots, this.owner.level());
    }

    @Override
    public void slotsChanged(Container $$0) {
    }

    @Override
    public void removed(Player $$0) {
        super.removed($$0);
        this.resultSlots.clearContent();
        if (!$$0.level().isClientSide) {
            this.clearContainer($$0, this.craftSlots);
        }
    }

    @Override
    public boolean stillValid(Player $$0) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player $$0, int $$1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = this.slots.get($$1);
        if ($$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            EquipmentSlot $$5 = Mob.getEquipmentSlotForItem($$2);
            if ($$1 == 0) {
                if (!this.moveItemStackTo($$4, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                $$3.onQuickCraft($$4, $$2);
            } else if ($$1 >= 1 && $$1 < 5) {
                if (!this.moveItemStackTo($$4, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$1 >= 5 && $$1 < 9) {
                if (!this.moveItemStackTo($$4, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$5.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(8 - $$5.getIndex()).hasItem()) {
                int $$6 = 8 - $$5.getIndex();
                if (!this.moveItemStackTo($$4, $$6, $$6 + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$5 == EquipmentSlot.OFFHAND && !this.slots.get(45).hasItem()) {
                if (!this.moveItemStackTo($$4, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$1 >= 9 && $$1 < 36) {
                if (!this.moveItemStackTo($$4, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$1 >= 36 && $$1 < 45) {
                if (!this.moveItemStackTo($$4, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo($$4, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            } else {
                $$3.setChanged();
            }

            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }

            $$3.onTake($$0, $$4);
            if ($$1 == 0) {
                $$0.drop($$4, false);
            }
        }

        return $$2;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack $$0, Slot $$1) {
        return $$1.container != this.resultSlots && super.canTakeItemForPickAll($$0, $$1);
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
        return 5;
    }

    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int $$0) {
        return $$0 != this.getResultSlotIndex();
    }
}
