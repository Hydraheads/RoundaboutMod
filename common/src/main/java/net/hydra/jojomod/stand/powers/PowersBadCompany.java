package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class PowersBadCompany extends NewDashPreset {
    @Override public boolean isWip(){return true;}
    @Override public Component ifWipListDevStatus(){ return Component.translatable(  "roundabout.dev_status.paused").withStyle(ChatFormatting.AQUA);}
    @Override public Component ifWipListDev(){ return Component.literal("DOGael Arts").withStyle(ChatFormatting.AQUA);}

    public PowersBadCompany(LivingEntity self) { super(self); }
    @Override public StandPowers generateStandPowers(LivingEntity entity) { return new PowersBadCompany(entity); }

    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        super.renderIcons(context, x, y);
    }
    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_3_NORMAL, SKILL_3_GUARD, SKILL_3_CROUCH, SKILL_3_CROUCH_GUARD -> {
                dash();
            }
        }
    }
}
