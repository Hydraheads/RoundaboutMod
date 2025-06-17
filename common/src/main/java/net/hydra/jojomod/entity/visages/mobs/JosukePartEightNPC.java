package net.hydra.jojomod.entity.visages.mobs;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.StandUsingNPC;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class JosukePartEightNPC extends StandUsingNPC {
    public JosukePartEightNPC(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }


    @Override
    public StandDiscItem getDisc(){
        return ((StandDiscItem)ModItems.STAND_DISC_SOFT_AND_WET);
    }
}
