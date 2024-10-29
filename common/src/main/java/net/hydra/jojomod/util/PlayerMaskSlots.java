package net.hydra.jojomod.util;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class PlayerMaskSlots extends SimpleContainer {
    private final Player player;
    public PlayerMaskSlots(Player player) {
        super(2);
        this.player = player;
    }
    @Override
    public void setChanged() {
        IPlayerEntity play = ((IPlayerEntity)this.player);
        if (!this.getItem(0).equals(play.roundabout$getMaskSlot())) {
            if (!this.player.level().isClientSide()) {
                play.roundabout$setMaskSlot(this.getItem(0));
            }
        }
        if (!this.getItem(1).equals(play.roundabout$getMaskVoiceSlot())){
            if (!this.player.level().isClientSide()) {
                play.roundabout$setMaskVoiceSlot(this.getItem(1));
            }
        }
        super.setChanged();
    }
    public void update(){
        IPlayerEntity play = ((IPlayerEntity)this.player);
            play.roundabout$setMaskSlot(this.getItem(0));
            play.roundabout$setMaskVoiceSlot(this.getItem(1));
    }


    public void replaceWith(PlayerMaskSlots $$0) {
        for (int $$1 = 0; $$1 < this.getContainerSize(); $$1++) {
            this.setItem($$1, $$0.getItem($$1));
        }
    }
}