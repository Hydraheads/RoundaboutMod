package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.REGENERATE, PowerIndex.FATE_2_SNEAK);
            setSkillIcon(context, x, y, 3, StandIcons.CHEETAH_SPEED, PowerIndex.FATE_3_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.BLOOD_DRINK, PowerIndex.FATE_2);
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        setSkillIcon(context, x, y, 4, StandIcons.DODGE, PowerIndex.FATE_3);
    }

    @Override
    public void tick(){
    }
}
