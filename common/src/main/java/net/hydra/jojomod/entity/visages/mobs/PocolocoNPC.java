package net.hydra.jojomod.entity.visages.mobs;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.StandUsingNPC;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PocolocoNPC extends StandUsingNPC {
    public PocolocoNPC(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }

    @Override
    public boolean runsIfLow(){
        return true;
    }
    public boolean canSummonStandThroughFightOrFlightActive(){
        return true;
    }
    @Override
    public StandDiscItem getDisc(){
        return ((StandDiscItem)ModItems.STAND_DISC_HEY_YA);
    }

    public ItemStack getBasis(){
        return ModItems.POCOLOCO_MASK.getDefaultInstance();
    }
}
