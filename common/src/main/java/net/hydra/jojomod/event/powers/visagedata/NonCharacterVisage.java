package net.hydra.jojomod.event.powers.visagedata;

import net.minecraft.world.entity.player.Player;

public class NonCharacterVisage extends VisageData{
    public NonCharacterVisage(Player self) {
        super(self);
    }

    public boolean isCharacterVisage(){
        return false;
    }
}
