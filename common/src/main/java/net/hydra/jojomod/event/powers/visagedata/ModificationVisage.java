package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;

public class ModificationVisage extends NonCharacterVisage {
    @Override
    public VisageData generateVisageData(Player entity){
        return new ModificationVisage(entity);
    }
    public ModificationVisage(Player self) {
        super(self);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.DIO.create(pl.level());
    }
}
