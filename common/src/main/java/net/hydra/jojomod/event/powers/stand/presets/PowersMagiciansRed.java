package net.hydra.jojomod.event.powers.stand.presets;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class PowersMagiciansRed extends PunchingStand{
    public PowersMagiciansRed(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersMagiciansRed(entity);
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 3, StandIcons.NONE, PowerIndex.NO_CD);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
        }
        setSkillIcon(context, x, y, 1, StandIcons.NONE, PowerIndex.NO_CD);
        setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.NO_CD);
        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.NO_CD);
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.MAGICIANS_RED.create(this.getSelf().level());
    }
    @Override
    public Component getSkinName(byte skinId){
        return JusticeEntity.getSkinNameT(skinId);
    }
}
