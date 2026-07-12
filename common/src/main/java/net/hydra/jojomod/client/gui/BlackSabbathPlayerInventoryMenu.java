package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.hydra.jojomod.util.PlayerMaskSlots;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;

public class BlackSabbathPlayerInventoryMenu extends AbstractContainerMenu {

    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 2, 2);
    private final ResultContainer resultSlots = new ResultContainer();
    public final boolean active;
    private final Player owner;

    public BlackSabbathPlayerInventoryMenu(Inventory $$0, boolean $$1, final Player $$2, int cid) {
        super(null, cid);
        this.active = $$1;
        this.owner = $$2;
        IPlayerEntity play = ((IPlayerEntity)this.owner);
      //  BlackSabbathPlayerInventory bsi = play.roundabout$getBlackSabbathInventory();
        PlayerMaskSlots bsi = play.roundabout$getMaskInventory();
        this.addSlot(new Slot(bsi, 0, 8, 28) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPlace(ItemStack $$0) {
                return ($$0.getItem() instanceof MaskItem);
            }

        });

        this.addSlot(new Slot(bsi, 1, 8, 28) {
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
