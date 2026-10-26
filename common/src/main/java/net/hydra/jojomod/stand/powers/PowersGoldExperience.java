package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.BlockGrabPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Objects;

public class PowersGoldExperience extends BlockGrabPreset {
    // move bytes
    //private static final byte

    // basic stand registry stuff
    public PowersGoldExperience(LivingEntity self) {super(self);}
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersGoldExperience(entity);}
    //TODO: replace this with the GE enable config
    @Override
    public boolean isStandEnabled(){return true;}

    // start of move methods

    /**
     * Gets the input for the power based on the buttons pressed.
     * Note, this is client side only. DO NOT FORGET!!!!
     *
     * @param context What button combination was pressed.
     */
    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            // life punch
            case SKILL_1_CROUCH -> {
                tryLifePunch();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1_SNEAK) {
            return this.impale(); // this is basically just a reskinned impale that does less damage and does fancy stuff
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            updateLifePunch();
        }
        super.updateUniqueMoves();
    }

    @Override
    public void handleStandAttack(Player player, Entity target) {
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            // this is basically a reskinned impale so...
            impaleImpact(target);
        }
    }

    // life punch start

    private void tryLifePunch() {
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            //if (canExecuteMoveWithLevel(getImpaleLevel())) {
                if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                } else {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                    tryPowerPacket(PowerIndex.POWER_1_SNEAK);
                }
            //}
        }
    }

    private void updateLifePunch() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 24) {
                this.standLifePunch();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    // oooooh i'm sooo menacing ooooh
                    if (this.attackTimeDuring % 4 == 0) {
                        sendParticlesIfPossible(self.level(), ModParticles.MENACING,
                                this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }
        }
    }

    private void standLifePunch() {
        if (this.self instanceof Player) {
            if (isPacketPlayer()) {
                this.setAttackTimeDuring(-20);
                impaleTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, getTargetEntityId2(impaleRange));
            }
        } else {
            // mob ai stuff i guess? it makes sense because this is outside the play check.
            Entity targetEntity = getTargetEntity(this.self, impaleRange);
            //this is basically a reskinned impale
            impaleImpact(targetEntity);
        }
    }

    @Override
    public void impaleImpact(Entity entity) {
        super.impaleImpact(entity);
        //eventually, i'll have to do the life punch stuff here, but for now this works fine.
    }

    //overriding this to change the animation
    @Override
    public boolean impale(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){

            airTriggered = (((StandUser) this.getSelf()).roundabout$getLeapTicks() > 0);
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1_SNEAK);
            playSoundsIfNearby(IMPALE_NOISE, 27, false);
            this.animateStand(GoldExperienceEntity.LIFE_PUNCH);
            this.poseStand(OffsetIndex.GUARD);

            return true;
        }
        return false;
    }

    @Override
    public SoundEvent getImpaleSound() {
        return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT;
    }

    // life punch end

    // end of move methods

    // sounds start

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_GOLD_EXPERIENCE_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_WINDUP_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public void playBarrageEndNoise(float mod, Entity entity) {
        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT, SoundSource.PLAYERS, 0.85F + mod, 1F);
        }
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 2 == 0)
                playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GOLD_EXPERIENCE_HIT_EVENT, SoundSource.PLAYERS, 0.8F, 1F);
        }
    }

    @Override
    public float getPunchLandPitch() {
        return 1F + 0.05F * activePowerPhase;
    }

    @Override
    public float getPunchLandLastPitch() {
        return 1F;
    }

    @Override
    public SoundEvent getPunchLandSound() {
        return ModSounds.GOLD_EXPERIENCE_HIT_EVENT;
    }

    @Override
    public SoundEvent getPunchLandLastSound() {
        return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT;
    }

    // sounds end

    // skins and entity start

    @Override
    public Component getSkinName(byte skinId) {
        switch (skinId) {
            default -> {
                return Component.translatable("skins.roundabout.gold_experience.base");
            }
        }
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> l = Lists.newArrayList();
        l.add(GoldExperienceEntity.PART_5);
        /*if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass) {
            }
            if (Level > 2 || bypass) {
            }
            if (Level > 3 || bypass) {
            }
            if (Level > 4 || bypass) {
            }
            if (Level > 5 || bypass) {
            }
            if (Level > 6 || bypass) {
            }
            if (((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() || bypass) {
            }
        }*/

        return l;
    }

    @Override
    public StandEntity getNewStandEntity() {
        byte skin = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        return ModEntities.GOLD_EXPERIENCE.create(this.getSelf().level());
    }

    // skins and entity end

    // icons and abilities start

    // stand icons and info screen start

    /**
     * Renders the icons for the stand's abilities on the GUI. Comments will specify
     * if icon is for courching, blocking, or normal.
     */
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, PowerIndex.SKILL_4);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
                                                  int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        return $$1;
    }

    // icons and abilities end

    //WIP tags, comment out when done

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
