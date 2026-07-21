package net.hydra.jojomod.util;

import net.hydra.jojomod.access.IPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;

import javax.annotation.Nullable;

public class BlackSabbathPlayerInventory extends SimpleContainer {

    private final Player player;

    public BlackSabbathPlayerInventory(Player player) {
            super(9);
            this.player = player;
    }

    @Override
    public void fromTag(ListTag $$0) {
        for (int $$1 = 0; $$1 < this.getContainerSize(); $$1++) {
            this.setItem($$1, ItemStack.EMPTY);
        }

        for (int $$2 = 0; $$2 < $$0.size(); $$2++) {
            CompoundTag $$3 = $$0.getCompound($$2);
            int $$4 = $$3.getByte("Slot") & 255;
            if ($$4 >= 0 && $$4 < this.getContainerSize()) {
                this.setItem($$4, ItemStack.of($$3));
            }
        }
    }

    @Override
    public ListTag createTag() {
        ListTag $$0 = new ListTag();

        for (int $$1 = 0; $$1 < this.getContainerSize(); $$1++) {
            ItemStack $$2 = this.getItem($$1);
            if (!$$2.isEmpty()) {
                CompoundTag $$3 = new CompoundTag();
                $$3.putByte("Slot", (byte)$$1);
                $$2.save($$3);
                $$0.add($$3);
            }
        }

        return $$0;
    }


    @Override
    public void setChanged() {

        super.setChanged();
    }


    public void update(){
        IPlayerEntity play = ((IPlayerEntity)this.player);
       // play.roundabout$setBlackSabbathAttackSlot(this.getItem(0));
    }


    public void replaceWith(BlackSabbathPlayerInventory $$0) {
        for (int $$1 = 0; $$1 < this.getContainerSize(); $$1++) {
            this.setItem($$1, $$0.getItem($$1));
        }
    }

}
