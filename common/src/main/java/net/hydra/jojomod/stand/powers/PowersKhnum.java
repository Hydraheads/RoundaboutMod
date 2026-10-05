package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import java.util.List;
import com.google.common.collect.Lists;

public class PowersKhnum extends NewDashPreset {
    private static final byte NEUTRAL = 0;
    private static final byte TALL_LEGS = 1;
    private static final byte WIDE = 2;
    private static final byte SMALL = 3;

    public PowersKhnum(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKhnum(entity);
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().khnumSettings.enableKhnum;
    }

    @Override
    public boolean isSecondaryStand() {
        return true;
    }

    @Override
    public boolean canSummonStandAsEntity() {
        return false;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1,
                isHoldingSneak() ? StandIcons.CINDERELLA_VISAGES : StandIcons.WHITESNAKE_HALLUCINATORY_DISGUISE,
                PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 2,
                isHoldingSneak() ? StandIcons.CINDERELLA_MASK : StandIcons.CINDERELLA_SCALP,
                PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3,
                isHoldingSneak() ? StandIcons.CINDERELLA_MASK : StandIcons.DODGE,
                isHoldingSneak() ? PowerIndex.SKILL_3 : PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.CINDERELLA_VISAGES, PowerIndex.SKILL_4);
        super.renderIcons(context, x, y);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY,
                                                   int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> icons = Lists.newArrayList();
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0,
                "ability.roundabout.khnum_disguise", "instruction.roundabout.press_skill",
                StandIcons.WHITESNAKE_HALLUCINATORY_DISGUISE, 1, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0,
                "ability.roundabout.khnum_visage", "instruction.roundabout.press_skill_crouch",
                StandIcons.CINDERELLA_VISAGES, 1, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0,
                "ability.roundabout.khnum_tall", "instruction.roundabout.press_skill",
                StandIcons.CINDERELLA_SCALP, 2, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0,
                "ability.roundabout.khnum_wide", "instruction.roundabout.press_skill_crouch",
                StandIcons.CINDERELLA_MASK, 2, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0,
                "ability.roundabout.khnum_dash", "instruction.roundabout.press_skill",
                StandIcons.DODGE, 3, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0,
                "ability.roundabout.khnum_small", "instruction.roundabout.press_skill_crouch",
                StandIcons.CINDERELLA_MASK, 3, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 0,
                "ability.roundabout.khnum_reset", "instruction.roundabout.press_skill",
                StandIcons.CINDERELLA_VISAGES, 4, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0,
                "ability.roundabout.khnum_passive", "instruction.roundabout.passive",
                StandIcons.WHITESNAKE_HALLUCINATORY_DISGUISE, 0, level, bypass));
        return icons;
    }

    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.DARK_PURPLE);
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("Kalee").withStyle(ChatFormatting.WHITE);
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL -> ClientUtil.openKhnumDisguiseScreen();
            case SKILL_1_CROUCH -> ClientUtil.openKhnumVisageScreen();
            case SKILL_2_NORMAL -> setFormClient(TALL_LEGS);
            case SKILL_2_CROUCH -> setFormClient(WIDE);
            case SKILL_3_NORMAL -> dash();
            case SKILL_3_CROUCH -> setFormClient(SMALL);
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> resetClient();
        }
    }

    private void setFormClient(byte form) {
        byte move = switch (form) {
            case TALL_LEGS -> PowerIndex.POWER_2;
            case WIDE -> PowerIndex.POWER_2_SNEAK;
            case SMALL -> PowerIndex.POWER_3_SNEAK;
            default -> PowerIndex.POWER_4;
        };
        ((StandUser) self).roundabout$tryPower(move, true);
        tryPowerPacket(move);
    }

    private void resetClient() {
        ((StandUser) self).roundabout$tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
    }

    private void resetKhnum() {
        StandUser user = (StandUser) self;
        user.roundabout$setKhnumForm(NEUTRAL);
        user.roundabout$setKhnumVisage(237, 135, 135);
        user.roundabout$clearDisguise();
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_2 || move == PowerIndex.POWER_2_SNEAK || move == PowerIndex.POWER_3_SNEAK) {
            byte form = switch (move) {
                case PowerIndex.POWER_2 -> TALL_LEGS;
                case PowerIndex.POWER_2_SNEAK -> WIDE;
                default -> SMALL;
            };
            ((StandUser) self).roundabout$setKhnumForm(form);
            return true;
        }
        if (move == PowerIndex.POWER_4) {
            resetKhnum();
            return true;
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        if (self instanceof Mob && !self.level().isClientSide) {
            byte desiredForm = attackTarget == null ? SMALL : WIDE;
            if (((StandUser) self).roundabout$getKhnumForm() != desiredForm) {
                ((StandUser) self).roundabout$setKhnumForm(desiredForm);
            }
        }
        super.tickMobAI(attackTarget);
    }
}
