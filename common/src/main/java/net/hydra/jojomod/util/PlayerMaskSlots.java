package net.hydra.jojomod.util;

import net.hydra.jojomod.access.IPlayerEntity;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
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
        if (!this.getItem(0).equals(play.roundabout$getMaskSlot())){
            play.roundabout$setMaskSlot(this.getItem(0));
        }
        if (!this.getItem(1).equals(play.roundabout$getMaskVoiceSlot())){
            play.roundabout$setMaskVoiceSlot(this.getItem(1));
        }
        super.setChanged();
    }
}
