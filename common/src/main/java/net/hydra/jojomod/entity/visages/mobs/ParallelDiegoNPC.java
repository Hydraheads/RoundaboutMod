package net.hydra.jojomod.entity.visages.mobs;

import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.StandUsingNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ParallelDiegoNPC extends DiegoNPC {
    public ParallelDiegoNPC(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }
    @Override
    public StandDiscItem getDisc(){
        return ((StandDiscItem) ModItems.STAND_DISC_THE_WORLD);
    }

    @Override
    public void applySkin(){
        ((StandUser)this).roundabout$setStandSkin(TheWorldEntity.PART_7_SKIN);
    }
}
