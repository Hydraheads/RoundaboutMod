package net.hydra.jojomod.client.gui;

import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ModItemBonusTextures;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.util.PlayerMaskSlots;
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

public class PowerInventoryMenu extends AbstractContainerMenu {

    public static final int CONTAINER_ID = 42358;
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

    public PowerInventoryMenu(Inventory $$0, boolean $$1, final Player $$2) {
        super(null, 42358);
        this.active = $$1;
        this.owner = $$2;
        //this.addSlot(new ResultSlot($$0.player, this.craftSlots, this.resultSlots, 0, 154, 28));
        IPlayerEntity play = ((IPlayerEntity)this.owner);
        PlayerMaskSlots plm = play.roundabout$getMaskInventory();
        this.addSlot(new Slot(plm, 0, 8, 8) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPlace(ItemStack $$0) {
                return ($$0.getItem() instanceof MaskItem);
            }

        });

        this.addSlot(new Slot(plm, 1, 8, 26) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPlace(ItemStack $$0) {
                return ($$0.getItem() instanceof MaskItem);
            }

        });

        for (int $$9 = 0; $$9 < 9; $$9++) {
            this.addSlot(new Slot($$0, $$9, 8 + $$9 * 18, 142));
        }

    }

    public void setData(int p_39044_, int p_39045_) {
        super.setData(p_39044_, p_39045_);
        this.broadcastChanges();
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
    public void slotsChanged(Container $$0) {
        super.slotsChanged($$0);
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
        return $$2;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack $$0, Slot $$1) {
        return $$1.container != this.resultSlots && super.canTakeItemForPickAll($$0, $$1);
    }


    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }
}
