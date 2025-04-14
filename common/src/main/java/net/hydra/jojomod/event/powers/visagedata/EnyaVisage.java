package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EnyaVisage extends VisageData {
    public EnyaVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new EnyaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.ENYA.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
    }
}
