package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.access.ISuperThrownAbstractArrow;
import net.hydra.jojomod.entity.objects.GentlyWeepsEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersCatchTheRainbow;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import static net.hydra.jojomod.util.MainUtil.*;

public class CTRbarrierEntity extends StandEntity {

    public CTRbarrierEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean isValid(boolean userActive, LivingEntity thisStand, LivingEntity userEntity){
        return userEntity.isAlive() && !userEntity.isRemoved() && (!needsActive() || userActive) && validatePowers(userEntity);
    }

    public final AnimationState spin = new AnimationState();

    int lifespan = 160;

    public int followTimer = 1;

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            if (User != null && ((StandUser) User).roundabout$getStandPowers() instanceof PowersCatchTheRainbow
                    && (((PowersCatchTheRainbow) ((StandUser) User).roundabout$getStandPowers()).isInRain())
                    && User.isAlive()) {
                if (followTimer != 10) {
                    this.setPos(User.getX(), User.getY(), User.getZ());
                    followTimer++;
                }
            } else {
                this.discard();
            }

            if (lifespan != 0) {
                lifespan --;
            }else{
                this.discard();
            }

            AABB wallBox = this.getBoundingBox();

            for (Entity mob : level().getEntitiesOfClass(
                    Entity.class,
                    wallBox)) {

                if (mob.getBoundingBox().intersects(wallBox)) {
                    if (PowerTypes.isInADifferentExistence(mob,this)){
                        continue;
                    }
                    if (mob instanceof Projectile pj){
                        dealWithProjectile(pj,this);
                    } else if (mob instanceof LivingEntity && mob != User){
                        MainUtil.makeBleed(mob, 1, 40, User);
                        mob.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.SHARP_RAINDROP), 2);
                    }
                }
            }
            double randX = Roundabout.RANDOM.nextDouble(-2, 2);
            double randZ = Roundabout.RANDOM.nextDouble(-2, 2);
            double randY = Roundabout.RANDOM.nextDouble(0, 4);
            sendParticlesIfPossible(this, level(), ParticleTypes.DRIPPING_DRIPSTONE_WATER,
                    this.getX() + randX,
                    this.getY() + randY,
                    this.getZ() + randZ,
                    1, 0, 0, 0, 0);
        }
        super.tick();
    }

    public static void dealWithProjectile(Projectile pj, CTRbarrierEntity this2){
        if (pj.level() instanceof ServerLevel sl) {
            if (!(pj.getOwner() instanceof LivingEntity LE &&
                    ((StandUser) LE).roundabout$getStandPowers() instanceof PowersCatchTheRainbow)) {
                {
                    if (pj instanceof AbstractArrow || pj instanceof IronBallEntity || pj instanceof ThrownObjectEntity ||
                            pj instanceof RipperEyesProjectile ||
                            pj instanceof MatchEntity) {
                        if (!(pj instanceof AbstractArrow aa && ((IAbstractArrowAccess) aa).roundabout$GetIsManhattan())
                                && !(pj instanceof RoundaboutBulletEntity abe && abe.isHattan)) {
                            IProjectileAccess ipa = (IProjectileAccess) pj;
                            if (!ipa.roundabout$getIsDeflected()) {
                                if (pj instanceof ThrownObjectEntity ttt){
                                    ttt.disableThrow();
                                }
                                if (pj instanceof AbstractArrow ttt && ((ISuperThrownAbstractArrow)ttt).roundabout$getSuperThrow()){
                                    ((ISuperThrownAbstractArrow)ttt).roundabout$cancelSuperThrow();
                                }
                                ((ServerLevel) this2.level()).sendParticles(ParticleTypes.SPLASH,
                                        pj.getX(),
                                        pj.getY(),
                                        pj.getZ(),
                                        10, 0, 0, 0, 0.2);
                                pj.level().playSound(null, pj.blockPosition(), ModSounds.ICE_BREAKER_EVENT,
                                        SoundSource.PLAYERS, 1.0F, 2.0F);
                                if (pj instanceof RoundaboutBulletEntity) {
                                    pj.setOwner(this2);
                                    ((RoundaboutBulletEntity) pj).setSuperThrown(false);
                                }
                                ipa.roundabout$setIsDeflected(true);
                                ((IEntityAndData) pj).rdbt$forceDeltaMovement(pj.getDeltaMovement().scale(-0.4));
                                pj.setYRot(pj.getYRot() + 180.0F);
                                pj.yRotO += 180.0F;
                            }
                        }
                    }
                }
            }
        }
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0F).add(Attributes.MAX_HEALTH, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0);
    }

    @Override
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox();
    }

    @Override
    public boolean hasNoPhysics() {return true;}

    @Override
    public boolean isNoGravity() {return true;}

    @Override
    public boolean canBeCollidedWith(){
            return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource $$0) {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }
}
