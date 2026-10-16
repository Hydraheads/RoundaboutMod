package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class PowersLyinEyes extends NewDashPreset {
    // basic stand registry stuff
    public PowersLyinEyes(LivingEntity self) {super(self);}
    @Override
    public boolean canSummonStandAsEntity() {return false;}
    @Override
    public boolean isSecondaryStand(){return true;}
    @Override
    public boolean rendersPlayer(){return true;}
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersLyinEyes(entity);
    }

    // stand icons and info screen

    /**
     * Renders the icons for the stand's abilities on the GUI. Comments will specify
     * if icon is for courching, blocking, or normal.
     */
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.LYIN_EYES_PLACEHOLDER, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 2, StandIcons.LYIN_EYES_PLACEHOLDER, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.LYIN_EYES_PLACEHOLDER, PowerIndex.SKILL_4);
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
                                                  int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.lyin_eyes_placeholder",
                "instruction.roundabout.press_attack", StandIcons.LYIN_EYES_PLACEHOLDER, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.lyin_eyes_placeholder",
                "instruction.roundabout.hold_block", StandIcons.LYIN_EYES_PLACEHOLDER, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.hold_attack_crouch", StandIcons.DODGE, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.lyin_eyes_placeholder",
                "instruction.roundabout.barrage", StandIcons.LYIN_EYES_PLACEHOLDER, 0, level, bypas));
        return $$1;
    }

    // WIP FEATURES BELOW HERE, DELETE WHEN DONE
    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("88superguy").withStyle(ChatFormatting.GOLD);
    }
}
