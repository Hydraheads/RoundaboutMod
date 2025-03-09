package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class JotaroVisage extends VisageData {
    public JotaroVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new JotaroVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.JOTARO.create(pl.level());
    }
    @Override
    public Vec3 sizeModifier(){
        return new Vec3(1.09F,1.09F,1.09F);
    }
}
