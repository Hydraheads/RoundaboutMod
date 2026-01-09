package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class VampireGeneralPowers extends PunchingGeneralPowers {
    public VampireGeneralPowers(LivingEntity self) {
        super(self);
    }
    public VampireGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new VampireGeneralPowers(entity);
    }


    public static final byte POWER_DIVE = PowerIndex.SNEAK_ATTACK_CHARGE;
    public static final byte HIT = PowerIndex.SPECIAL_CHARGED;
    public static final byte POWER_SWEEP = PowerIndex.SNEAK_ATTACK;

    /**The text name of the fate*/
    public Component getPowerName(){
        return Component.translatable("text.roundabout.powers.vampire");
    }
    public Component getPowerTagName(){
        return Component.translatable("text.roundabout.powers.vampire_select");
    }
    public int getMaxGuardPoints(){
        return 13;
    }

    @Override
    public void powerActivate(PowerContext context) {
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vp) {
            switch (context) {
                case SKILL_3_NORMAL -> {
                    vp.dashOrWallWalk();
                }
                case SKILL_3_CROUCH -> {
                    vp.dashOrWallWalk();
                }
            }
        }
    };

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (self.isCrouching() && canUseAirAttack()) {
            if (keyIsDown) {
                if (activePowerPhase == 0){
                    this.tryPower(POWER_DIVE);
                    tryPowerPacket(POWER_DIVE);
                }
            }
        } else {
            super.buttonInputAttack(keyIsDown,options);
        }
    }

    @Override
    public void tickPower() {
        super.tickPower();
        if (self.level().isClientSide()) {
            if (getActivePower() == POWER_DIVE){
                if (attackTimeDuring > 20 || self.isInWater()){
                    xTryPower(PowerIndex.NONE,false);
                    tryPowerPacket(NONE);
                } else if (!self.onGround()) {
                    Entity hit = DamageHandler.damageMobBelow(self, 1.5, 1);
                    if (hit != null) {
                        //set cooldown

                        tryIntPowerPacket(HIT, hit.getId());
                        xTryPower(PowerIndex.NONE,false);
                        tryPowerPacket(NONE);
                        Vec3 lower = self.getDeltaMovement();
                        self.setDeltaMovement(lower.x(), 0, lower.z());
                    } else {
                        Vec3 lower = self.getDeltaMovement();
                        self.setDeltaMovement(lower.x(), -1.8, lower.z());
                    }
                } else {
                    xTryPower(PowerIndex.NONE,false);
                    tryPowerPacket(NONE);
                }
            }
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (!self.level().isClientSide()) {
            if (move == HIT) {
                attackTargetId = chargeTime;
            }
        }
        return super.tryIntPower(move,forced,chargeTime);
    }


    public float getPunchStrength(Entity entity){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)){
                return 0.75F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 2.1F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vp) {
            if ((vp.canLatchOntoWall() || (vp.isPlantedInWall() && !isHoldingSneak())) && vp.canWallWalkConfig()) {
                setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK_VAMP, PowerIndex.FATE_3);
            } else if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }
    }


    @Override
    /**Override this to set the special move*/
    public boolean setPowerOther(int move, int lastMove) {
        if (move == POWER_DIVE) {
            doDive();
        }if (move == HIT) {
            doDiveHit();
        }

        return super.setPowerOther(move,lastMove);
    }

    public void doDive(){
        this.attackTimeMax= 5;
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        setActivePowerPhase((byte) 1);
        setActivePower(POWER_DIVE);
        if (!self.level().isClientSide()) {
            for (int i = 0; i < 3; i++){


                Vec3 cvec = new Vec3(Math.random()*0.2 - 0.1,1,Math.random()*0.2 - 0.1);
                Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                if (gravD != Direction.DOWN){
                    cvec = RotationUtil.vecPlayerToWorld(cvec,gravD);
                }

                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CLOUD,
                        this.getSelf().getX()+cvec.x, this.getSelf().getY()+cvec.y, this.getSelf().getZ()+cvec.z,
                        0,
                        cvec.x,
                        cvec.y,
                        cvec.z,
                        0.8);
            }
            this.self.level().playSound(null, this.self.blockPosition(),ModSounds.VAMPIRE_DIVE_EVENT, SoundSource.PLAYERS, 1F, (float) (0.96f + Math.random() * 0.08f));
        } else {
            Vec3 lower = self.getDeltaMovement();
            self.setDeltaMovement(lower.x(),-1.8,lower.z());
        }
    }
    public void doDiveHit(){
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            diveImpact(target);
        }
    }

    @Override
    public boolean interceptDamageEvent(DamageSource $$0, float $$1) {
        if (getActivePower() == POWER_DIVE && $$0.is(DamageTypes.MOB_ATTACK)){
            return true;
        }
        return false;
    }


    public void diveImpact(Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (entity != null) {
                attackTargetId = 0;
                float pow;
                float knockbackStrength;
                pow = getPunchStrength(entity)*1.2F;
                pow = applyComboDamage(pow);
                knockbackStrength = 0.10F;

                if (DamageHandler.VampireDamageEntity(entity, pow, this.self)) {
                    if (entity instanceof LivingEntity livingEntity){
                        ((StandUser)livingEntity).roundabout$setDazed((byte) 10);
                    }
                    ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CRIT,
                            entity.getEyePosition().x,entity.getEyePosition().y,entity.getEyePosition().z,
                            10,
                            0.2,
                            0.2,
                            0.2,
                            0.01);
                    takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                    this.self.level().playSound(null, this.self.blockPosition(), getPunchSound(), SoundSource.PLAYERS, 1F, (float) (0.7f + Math.random() * 0.1f));
                    addToCombo();
                } else {
                    if (!this.self.level().isClientSide()) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                    }
                }
            }
        }
    }
}
