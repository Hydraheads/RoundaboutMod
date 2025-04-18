package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
    public Vec3 sizeModifier(){
        return new Vec3(1.0F,1.0F,1.0F);
    }
}
