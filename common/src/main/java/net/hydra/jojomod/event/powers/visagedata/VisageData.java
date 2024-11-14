package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class VisageData {

    public final Player self;
    public VisageData(Player self) {
        this.self = self;
    }
    public VisageData generateVisageData(Player entity){
        return new VisageData(entity);
    }
    public JojoNPC getModelNPC(Player pl){
        return null;
    }
}
