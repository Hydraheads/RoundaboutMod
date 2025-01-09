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

    public FogInventoryMenu(Inventory $$0, boolean $$1, final Player $$2) {
        super(null, CONTAINER_ID);
        this.active = $$1;
        this.owner = $$2;
        this.addSlot(new ResultSlot($$0.player, this.craftSlots, this.resultSlots, 0, 154, 28));

        for (int $$3 = 0; $$3 < 2; $$3++) {
            for (int $$4 = 0; $$4 < 2; $$4++) {
                this.addSlot(new Slot(this.craftSlots, $$4 + $$3 * 2, 98 + $$4 * 18, 18 + $$3 * 18));
            }
        }

        for (int $$5 = 0; $$5 < 4; $$5++) {
            final EquipmentSlot $$6 = SLOT_IDS[$$5];
            this.addSlot(new Slot($$0, 39 - $$5, 8, 8 + $$5 * 18) {
                @Override
                public void setByPlayer(ItemStack $$0) {
                    FogInventoryMenu.onEquipItem($$2, $$6, $$0, this.getItem());
                    super.setByPlayer($$0);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack $$0) {
                    return $$6 == Mob.getEquipmentSlotForItem($$0);
                }

                @Override
                public boolean mayPickup(Player $$0) {
                    ItemStack $$1 = this.getItem();
                    return !$$1.isEmpty() && !$$0.isCreative() && EnchantmentHelper.hasBindingCurse($$1) ? false : super.mayPickup($$0);
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(FogInventoryMenu.BLOCK_ATLAS, FogInventoryMenu.TEXTURE_EMPTY_SLOTS[$$6.getIndex()]);
                }
            });
        }

        for (int $$7 = 0; $$7 < 3; $$7++) {
            for (int $$8 = 0; $$8 < 9; $$8++) {
                this.addSlot(new Slot($$0, $$8 + ($$7 + 1) * 9, 8 + $$8 * 18, 84 + $$7 * 18));
            }
        }

        for (int $$9 = 0; $$9 < 9; $$9++) {
            this.addSlot(new Slot($$0, $$9, 8 + $$9 * 18, 142));
        }

        this.addSlot(new Slot($$0, 40, 77, 62) {
            @Override
            public void setByPlayer(ItemStack $$0) {
                FogInventoryMenu.onEquipItem($$2, EquipmentSlot.OFFHAND, $$0, this.getItem());
                super.setByPlayer($$0);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(FogInventoryMenu.BLOCK_ATLAS, FogInventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    static void onEquipItem(Player $$0, EquipmentSlot $$1, ItemStack $$2, ItemStack $$3) {
        Equipable $$4 = Equipable.get($$2);
        if ($$4 != null) {
            $$0.onEquipItem($$1, $$3, $$2);
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
