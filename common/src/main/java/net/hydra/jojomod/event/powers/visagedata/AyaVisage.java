package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class AyaVisage extends VisageData {
    public AyaVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new AyaVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.AYA.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(0.95F,0.95F,0.95F);
    }
}
