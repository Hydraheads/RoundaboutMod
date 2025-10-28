package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.fates.FatePowers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;

public class VampireFate extends VampiricFate {


    public VampireFate() {
        super();
    }
    public VampireFate(LivingEntity self) {
        super(self);
    }

    public FatePowers generateFatePowers(LivingEntity entity){
        return new VampireFate(entity);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.CINDERELLA_MASK, PowerIndex.FATE_1);
        setSkillIcon(context, x, y, 2, StandIcons.BLOOD_DRINK, PowerIndex.FATE_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.DODGE, PowerIndex.FATE_3);
    }

    @Override
    public void tick(){
    }
}
