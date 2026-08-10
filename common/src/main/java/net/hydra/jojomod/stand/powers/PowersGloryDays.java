package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class PowersGloryDays extends NewDashPreset {
    public PowersGloryDays(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersGloryDays(entity);
    }

    //defining the stand's most basic attributes, you can find these in StandPowers.java
    @Override
    public boolean isSecondaryStand() {
        return true;
    }

    @Override
    public boolean canSummonStandAsEntity() {
        return false;
    }

    @Override
    public boolean rendersPlayer() {
        return true;
    }

    //makes stand appear as wip, with my name and stuff, also found in StandPowers.Java
    public boolean isWip() {
        return true;
    }

    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.GOLD);
    }

    public Component ifWipListDev() {
        return Component.literal("Finn_Nefarious").withStyle(ChatFormatting.WHITE);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 1, StandIcons.GLORY_DAYS_SPEED_DOWN, PowerIndex.POWER_1);

        setSkillIcon(context, x, y, 2, StandIcons.GLORY_DAYS_SPEED_UP, PowerIndex.POWER_2);

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.GLORY_DAYS_FRACTATION, PowerIndex.SKILL_3);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }

        if (autoResume){
            setSkillIcon(context, x, y, 4, StandIcons.GLORY_DAYS_AUTO_MODE_OFF, PowerIndex.POWER_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.GLORY_DAYS_AUTO_MODE_ON, PowerIndex.POWER_4);
        }

        super.renderIcons(context, x, y);
    }

    private int GDSpeed = 3;

    private int getGDSpeed(){
        return GDSpeed;
    }

    public void GDSpeedUp(){
        GDSpeed = Mth.clamp(GDSpeed + 1, 0, 3);
    }

    public void GDSpeedDown(){
        GDSpeed = Mth.clamp(GDSpeed - 1, 0 ,3);
    }

    public float GDSpeedMultiplier(){
        return switch(GDSpeed){
            case 1 -> 0.001F;
            case 2 -> 0.5F;
            case 3 -> 1.0F;

            default -> 1.0F;
        };
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL -> {
                GDSpeedUp();
            }
            case SKILL_2_NORMAL -> {
                GDSpeedDown();
            }
            case SKILL_3_NORMAL -> {
                dash();
            }
            case SKILL_4_NORMAL -> {
                if (!autoResume) {
                    autoResume = true;
                } else if (autoResume) {
                    autoResume = false;
                }

            }
        }
    }
    public boolean autoResume = false;
}
