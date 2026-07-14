package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class BlackSabbathPlayerInventoryMenu extends AbstractContainerMenu {
   // private final Container blackSabbathContainer;
    private final ResultContainer resultSlots = new ResultContainer();
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 2, 2);
    public final boolean active;
    private final Player owner;

    public BlackSabbathPlayerInventoryMenu(Inventory $$0, boolean $$1, final Player $$2, int cid) {
        super(null, cid);
        this.active = $$1;
        this.owner = $$2;
        IPlayerEntity play = ((IPlayerEntity)this.owner);
        BlackSabbathPlayerInventory bsinv = play.roundabout$getBlckSabbathPlayerInventory();

        this.addSlot(new Slot(bsinv, 0, 62, 36) {
            @Override
            public int getMaxStackSize() {
                return super.getMaxStackSize();
            }

            @Override
            public boolean mayPlace(ItemStack $$0) {
                return (!($$0.getItem() instanceof BlockItem BI && BI.getBlock() instanceof ShulkerBoxBlock));
            }

        });


        for (int $$10 = 0; $$10 < 2; $$10++) {
            for (int $$9 = 0; $$9 < 4; $$9++) {
                this.addSlot(new Slot(bsinv,1 + $$9 + $$10 * 4, 98 + $$9 * 18, 18 * ($$10 + 1) + 9) {

                    @Override
                    public boolean mayPlace(ItemStack $$0) {
                        return (!($$0.getItem() instanceof BlockItem BI && BI.getBlock() instanceof ShulkerBoxBlock));
                    }

                });
            }
        }

        for (int $$8 = 0; $$8 < 3; $$8++) {
            for (int $$9 = 0; $$9 < 9; $$9++) {
                this.addSlot(new Slot($$0, $$9 + $$8 * 9 + 9, 8 + $$9 * 18, 102 + $$8 * 18 + -18));
            }
        }

        for (int $$10 = 0; $$10 < 9; $$10++) {
            this.addSlot(new Slot($$0, $$10, 8 + $$10 * 18, 142));
        }
    }

    public void setData(int p_39044_, int p_39045_) {
        super.setData(p_39044_, p_39045_);
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player $$0, int $$1) {
        ItemStack $$2 = ItemStack.EMPTY;
        return $$2;
    }

    @Override
    public void slotsChanged(Container $$0) {
        super.slotsChanged($$0);
    }

    @Override
    public boolean stillValid(Player $$0) {
        return true;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack $$0, Slot $$1) {
        return $$1.container != this.resultSlots && super.canTakeItemForPickAll($$0, $$1);
    }

    @Override
    public void removed(Player $$0) {
        super.removed($$0);
        this.resultSlots.clearContent();
        if (!$$0.level().isClientSide) {
            this.clearContainer($$0, this.craftSlots);
        }
    }

    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }
}
