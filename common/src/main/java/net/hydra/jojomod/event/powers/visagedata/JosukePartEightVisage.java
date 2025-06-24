package net.hydra.jojomod.event.powers.visagedata;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class JosukePartEightVisage extends VisageData {
    public JosukePartEightVisage(Player self) {
        super(self);
    }
    public VisageData generateVisageData(Player entity){
        return new JosukePartEightVisage(entity);
    }
    @Override
    public JojoNPC getModelNPC(Player pl){
        return ModEntities.JOSUKE_PART_EIGHT.create(pl.level());
    }


    public String getSkinPath(){
        return "josuke_part_eight";
    }
}
