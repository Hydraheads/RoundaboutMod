package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class PowersLyinEyes extends NewDashPreset {
    // stand summoning variable
    private int summonOverlayTicks = 0;
    private boolean isSummon = false;

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

    // stand summoning start

    @Override
    public void onStandSummon(boolean desummon) {
        if (!desummon && this.self.level().isClientSide()) {
            summonOverlayTicks = 1;
            isSummon = true;
        }
        if (desummon && this.self.level().isClientSide()) {
            isSummon = false;
        }
        super.onStandSummon(desummon);
    }

    // stand summoning end

    // tick power start

    @Override
    public void tickPower() {
        super.tickPower();
        if (isSummon) {
            summonOverlayTicks = Math.min(summonOverlayTicks + 1, 12);
        } else {
            summonOverlayTicks = Math.max(summonOverlayTicks - 1, 0);
        }
    }

    // tick power end

    // stand icons and info screen start

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

    // stand icons and info screen end

    // sounds start

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.LYIN_EYES_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    // sounds end

    // basic setters and getters here

    public int getSummonOverlayTicks() {return summonOverlayTicks;}

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
