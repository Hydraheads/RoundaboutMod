package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.compress.utils.Lists;

import java.util.Arrays;
import java.util.List;

public class PowersCatchTheRainbow extends NewDashPreset {
    public PowersCatchTheRainbow(LivingEntity self) {
        super(self);
    }

    /**
     * general definition stuff
     **/
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

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersCatchTheRainbow(entity);
    }

    //poses
    @Override
    public Component getPosName(byte posID) {
        switch (posID){
            case 1 -> {return Component.translatable("idle.roundabout.catch_the_rainbow2"); }
            case 2 -> {return Component.translatable("idle.roundabout.catch_the_rainbow3"); }
        }
        return Component.translatable("idle.roundabout.catch_the_rainbow1");
    }

    @Override
    public List<Byte> getPosList() {
        List<Byte> listy = Lists.newArrayList();
        listy.add((byte) 0);
        listy.add((byte) 1);
        listy.add((byte) 2);
        return listy;
    }

    //skins
    public static final byte
            BASE = 1,
            WARM = 2,
            GHAST = 3;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                BASE,
                WARM,
                GHAST
        );
    }

    @Override public Component getSkinName(byte skinId) {
        if (!isInRain()) {
            return switch (skinId) {
                case WARM -> Component.translatable("skins.roundabout.catch_the_rainbow.warm");
                case GHAST -> Component.translatable("skins.roundabout.catch_the_rainbow.ghast_dry");
                default -> Component.translatable("skins.roundabout.catch_the_rainbow.base");
            };
        } else {
            return switch (skinId) {
                case WARM -> Component.translatable("skins.roundabout.catch_the_rainbow.warm");
                case GHAST -> Component.translatable("skins.roundabout.catch_the_rainbow.ghast_happy");
                default -> Component.translatable("skins.roundabout.catch_the_rainbow.base");
            };
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (!isInRain())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else if (isHoldingSneak() && !self.onGround())
            setSkillIcon(context, x, y, 3, StandIcons.CATCH_THE_RAINBOW_DROP_DOWN, PowerIndex.GLOBAL_DASH);
        else
            setSkillIcon(context, x, y, 3, StandIcons.CATCH_THE_RAINBOW_RAIN_DASH, PowerIndex.GLOBAL_DASH);
        if (isHoldingSneak())
            setSkillIcon(context, x, y, 1, StandIcons.CATCH_THE_RAINBOW_RAIN_MEND, PowerIndex.SKILL_1_SNEAK);
        setSkillIcon(context, x, y, 2, StandIcons.CATCH_THE_RAINBOW_CHOKE, PowerIndex.SKILL_2);
        if (!canLifeClutch())
            setSkillIcon(context, x, y, 4, StandIcons.CATCH_THE_RAINBOW_FULL_DODGE, PowerIndex.SKILL_4);
        else
            setSkillIcon(context, x, y, 4, StandIcons.CATCH_THE_RAINBOW_FULL_DODGE_LIFE_CLUTCH, PowerIndex.SKILL_4);
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (!isInRain()) {
            if (slot == 1) {
                return true;

            }
            if (slot == 2) {
                return true;
            }
            if (slot == 4) {
                return true;
            }
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public void powerActivate(PowerContext context){
        switch (context){
            case SKILL_1_CROUCH -> useRainMend(this.self);

            case SKILL_3_NORMAL -> {
                if (!isInRain())
                {dash();}
                else
                {rainDash();}
            }

            case SKILL_3_CROUCH -> {
                if  (!this.getSelf().onGround() && isInRain())
                {dropDown();}
            }
        }
    }

    public boolean isInRain() {
        BlockPos $$0 = this.self.blockPosition();
        return this.self.level().isRainingAt($$0)
                || this.self.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.self.getBoundingBox().maxY, (double)$$0.getZ()));
    }

    //rain speed
    @Override
    public float inputSpeedModifiers(float basis) {
        if (isInRain() && PowerTypes.hasStandActive(self)){
            if (!(this.getSelf() instanceof Mob)) {
                basis *= this.getSelf().isSprinting() ? 1.35F : 1F;
            }
        }
        return super.inputSpeedModifiers(basis);
    }

    //life clutch
    int deathTimer;

    public boolean canLifeClutch() {
        return deathTimer == 0;
    }

    @Override
    public void tickPower() {
        super.tickPower();
        if (deathTimer != 0) {
            -- deathTimer;
        }
        if (isInRain() && hasStandActive(self)) {
            this.getSelf().resetFallDistance();
        }
    }

    @Override
    public boolean cheatDeath(DamageSource dsource){
        if (!dsource.is(ModDamageTypes.SUNLIGHT) && !dsource.is(DamageTypes.GENERIC_KILL)
                && self instanceof Player PE) {
            if (isInRain() && canLifeClutch() && hasStandActive(self)) {
                deathTimer = 1200;
                PE.setHealth(1);
                PE.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1), PE);
                PE.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 10), PE);
                getStandUserSelf().roundabout$setDazed((byte)0);
                playSoundIfPossible(self.level(), null, self.blockPosition(), ModSounds.VAMPIRE_AWAKEN_EVENT,
                        SoundSource.PLAYERS, 1F, 1F);
                return true;
            }
        }
        return false;
    }

    //the f###ing healing move that i hate

    public void useRainMend(LivingEntity entity) {
        if (!self.level().isClientSide() && !onCooldown(PowerIndex.SKILL_1_SNEAK)){
            Entity ent = self;

            if (ent instanceof LivingEntity LV && ent.isAlive()) {
                this.setCooldown(PowerIndex.SKILL_1_SNEAK, 400);
                LV.heal(2f);
                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.CINDERELLA_SPARKLE_EVENT, SoundSource.PLAYERS, 1F, 1.5F);

                sendParticlesIfPossible(self.level(),ModParticles.SMALL_EXPLOSION, LV.getEyePosition().x,
                        LV.getEyePosition().y, LV.getEyePosition().z,
                        0, 0, 0, 0, 0.2);

                playSoundIfPossible(self.level(),null, LV.blockPosition(), ModSounds.BUBBLE_CREATE_EVENT, SoundSource.PLAYERS, 1F, 0.8F);
                sendParticlesIfPossible(self.level(),ModParticles.PURPLE_STAR,
                        LV.getEyePosition().x, LV.getEyePosition().y, LV.getEyePosition().z,
                        10, 0.25F, 0.1F, 0.25F, 0.02);

                MobEffectInstance bleed = LV.getEffect(ModEffects.BLEED);
                if (bleed != null) {
                    if (bleed.getAmplifier() > 0) {
                        int amp = bleed.getAmplifier() - 1;
                        LV.removeEffect(bleed.getEffect());
                        LV.addEffect(new MobEffectInstance(ModEffects.BLEED, bleed.getDuration(), amp));
                    } else {
                        LV.removeEffect(bleed.getEffect());
                    }
                }
            }
        }
    }

    //ground pound (add damage)
    public void dropDown(){
        if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
            this.setCooldown(PowerIndex.GLOBAL_DASH, 20);
            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 1F,
                    0,
                    2,
                    0);
        }
    }


    //rain dodge
    public void rainDash(){
        Options options = Minecraft.getInstance().options;

        inputDash = true;
        if (this.getSelf().level().isClientSide && !this.isClashing()) {
            if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                    byte forward = 0;
                    byte strafe = 0;
                    if (options.keyUp.isDown()) forward++;
                    if (options.keyDown.isDown()) forward--;
                    if (options.keyLeft.isDown()) strafe++;
                    if (options.keyRight.isDown()) strafe--;
                    int degrees = (int) (this.getSelf().getYRot() % 360);
                    int backwards = 0;

                    if (strafe > 0 && forward == 0) {
                        degrees -= 90;
                        degrees = degrees % 360;
                        backwards = 1;
                    } else if (strafe > 0 && forward > 0) {
                        degrees -= 45;
                        degrees = degrees % 360;
                        backwards = 2;
                    } else if (strafe > 0) {
                        degrees -= 135;
                        degrees = degrees % 360;
                        backwards = -1;
                    } else if (strafe < 0 && forward == 0) {
                        degrees += 90;
                        degrees = degrees % 360;
                        backwards = 3;
                    } else if (strafe < 0 && forward > 0) {
                        degrees += 45;
                        degrees = degrees % 360;
                        backwards = 4;
                    } else if (strafe < 0) {
                        degrees += 135;
                        degrees = degrees % 360;
                        backwards = -2;
                    } else if (forward < 0) {
                        degrees += 180;
                        degrees = degrees % 360;
                        backwards = -3;
                    }

                    int buffer = 0;
                    int cdTime = ClientNetworking.getAppropriateConfig().generalStandSettings.dashCooldown / 6;
                    if (this.getSelf() instanceof Player) {
                        ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(0);
                    }
                    this.setCooldown(PowerIndex.GLOBAL_DASH, cdTime);
                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.91F,
                            Mth.sin(degrees * ((float) Math.PI / 180)),
                            Mth.sin(-20 * ((float) Math.PI / 180)),
                            -Mth.cos(degrees * ((float) Math.PI / 180)));

                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.MOVEMENT, true);
                    tryIntPowerPacket(PowerIndex.MOVEMENT, backwards+buffer);
                }
            }
        }
    }
}
