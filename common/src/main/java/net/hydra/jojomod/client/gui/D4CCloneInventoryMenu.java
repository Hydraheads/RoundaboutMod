package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.mixin.PlayerEntityServer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class D4CCloneInventoryMenu extends AbstractContainerMenu {
    private final Container horseContainer;
    private final D4CCloneEntity horse;

    public D4CCloneInventoryMenu(int p_39656_, Inventory p_39657_, final D4CCloneEntity p_39659_, SimpleContainer container) {
        super((MenuType<?>)null, p_39656_);
        //this.horseContainer = new SimpleContainer(p_39659_.getInventorySize());
        this.horseContainer = container;
        this.horse = p_39659_;
        int i = 3;
        this.horseContainer.startOpen(p_39657_.player);
        int j = -18;
        /*this.addSlot(new Slot(p_39658_, 0, 8, 18) {
            public boolean mayPlace(ItemStack p_39677_) {
                return p_39677_.is(Items.SADDLE) && !this.hasItem() && p_39659_.isSaddleable();
            }

            public boolean isActive() {
                return p_39659_.isSaddleable();
            }
        });
        this.addSlot(new Slot(p_39658_, 1, 8, 36) {
            public boolean mayPlace(ItemStack p_39690_) {
                return p_39659_.isArmor(p_39690_);
            }

            public boolean isActive() {
                return p_39659_.canWearArmor();
            }

            public int getMaxStackSize() {
                return 1;
            }
        });
        if (this.hasChest(p_39659_)) { */
            for(int k = 0; k < 3; ++k) {
                for(int l = 0; l < (p_39659_).getInventoryColumns(); ++l) {
                    this.addSlot(new Slot(this.horseContainer, l + k * (p_39659_).getInventoryColumns(), 80 + l * 18, 18 + k * 18));
                }
            }
       // }

        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(p_39657_, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(p_39657_, j1, 8 + j1 * 18, 142));
        }

    }


    public boolean stillValid(Player p_39661_) {
        return !this.horse.hasInventoryChanged(this.horseContainer) && this.horseContainer.stillValid(p_39661_) && this.horse.isAlive() && this.horse.distanceTo(p_39661_) < 8.0F;
    }

    private boolean hasChest(AbstractHorse p_150578_) {
        return p_150578_ instanceof AbstractChestedHorse && ((AbstractChestedHorse)p_150578_).hasChest();
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            int containerSize = this.horseContainer.getContainerSize();

            if (index < containerSize) {
                if (!this.moveItemStackTo(stack, containerSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.getSlot(0).mayPlace(stack) && !this.getSlot(0).hasItem()) {
                    this.moveItemStackTo(stack, 0, 1, false);
                }

                if (!stack.isEmpty() && this.getSlot(1).mayPlace(stack)
                        && !this.getSlot(1).hasItem()) {
                    this.moveItemStackTo(stack, 1, 2, false);
                }

                if (!stack.isEmpty()) {
                    this.moveItemStackTo(stack, 2, containerSize, false);
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    public void removed(Player p_39663_) {
        super.removed(p_39663_);
        this.horseContainer.stopOpen(p_39663_);
    }
}