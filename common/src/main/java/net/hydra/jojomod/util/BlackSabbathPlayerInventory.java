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
    public void setChanged() {
     /*   IPlayerEntity play = ((IPlayerEntity)this.player);
        if (!this.getItem(0).equals(play.roundabout$getBlackSabbathAttackSlot())) {
            if (!this.player.level().isClientSide()) {
                play.roundabout$setBlackSabbathAttackSlot(this.getItem(0));
            }
        }
        super.setChanged();*/
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
