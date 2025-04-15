package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class AvdolVisage extends VisageData {
    public AvdolVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new AvdolVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.AVDOL.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.0675F,1.0675F,1.0675F);
    }
}
