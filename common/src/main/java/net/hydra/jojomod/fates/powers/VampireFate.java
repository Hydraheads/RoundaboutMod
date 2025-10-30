package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
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
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_2_NORMAL -> {
                suckBlood();
            }
        }
        super.powerActivate(context);
    };

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.FLESH_BUD, PowerIndex.FATE_1_SNEAK);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.HYPNOTISM, PowerIndex.FATE_1);
        }

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.REGENERATE, PowerIndex.FATE_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.BLOOD_DRINK, PowerIndex.FATE_2);
        }

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.CHEETAH_SPEED, PowerIndex.FATE_3_SNEAK);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 4, StandIcons.HEARING_MODE, PowerIndex.FATE_4_SNEAK);
        } else {
            if (isVisionOn()){
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_ON, PowerIndex.FATE_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_OFF, PowerIndex.FATE_4);
            }
        }
    }

    @Override
    public void tick(){
    }
}
