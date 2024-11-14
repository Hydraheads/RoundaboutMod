package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;

public class DIOVisage extends VisageData {
    public DIOVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new DIOVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.JOTARO.create(pl.level());
    }
}
