package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RattDartEntity extends AbstractArrow {

    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(RattDartEntity.class, EntityDataSerializers.BOOLEAN);
    private int superThrowTicks = -1;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.getEntityData().hasItem(ROUNDABOUT$SUPER_THROWN)) {
            this.getEntityData().define(ROUNDABOUT$SUPER_THROWN, false);
        }
    }

    int melting = 0;
    float damage = 0;
    int charged = 0;

    boolean particles = true;
    int bounces = 0;

    public RattDartEntity(EntityType<? extends RattDartEntity> entity,  Level world) {
        super(entity, world);
    }

    double ding() {
        double ding = 0.2;
        return Math.random()*ding-ding/2;
    }

    public void alignDart(LivingEntity player) {
        if ( ((StandUser) player).roundabout$getStandPowers() instanceof PowersRatt PR) {
            if (PR.getStandEntity(player) instanceof RattEntity RE) {
                Vec3 rots = PR.getRotations(PR.getShootTarget());
                Vec2 v = new Vec2((float) (-1*Math.cos(rots.y)),
                        (float) (-1*Math.sin(rots.y)) );
                this.setPos(RE.getEyeP(0).add(new Vec3(ding(), Mth.clamp(ding(),0,3), ding())));
            }
        }
    }

    public RattDartEntity(Level world, LivingEntity player,int m, float d) {
        super(ModEntities.RATT_DART, player, world);
        alignDart(player);
        this.melting = m;
        this.damage = d;
        this.charged = 51;
    }

    public RattDartEntity(Level world, LivingEntity player, int i) {
        super(ModEntities.RATT_DART, player, world);
       // alignDart(player);
        this.melting = i > 90 || i == -1 ? 0 : 1;
        this.damage = i < 90 ? 0.1F : 3.2F;
        this.charged = i;
        this.bounces = 1;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (bounces > 0) {
            bounces--;

            // yoinked from BladedBowlerHatEntity
            Vec3 velocity = this.getDeltaMovement();
            Direction hitDir = $$0.getDirection();
            Vec3 normal = Vec3.atLowerCornerOf(hitDir.getNormal());

            // Makes it bounce
            Vec3 reflected = velocity.subtract(normal.scale(2 * velocity.dot(normal)));

            // Slowly stops it bouncing
            reflected = reflected.scale(0.5); // less bounce / more bounce :)

            this.setDeltaMovement(reflected);

            Vec3 hitLoc = $$0.getLocation();
            Vec3 pushOut = normal.scale(0.5);
            this.setPos(hitLoc.x + pushOut.x, hitLoc.y + pushOut.y, hitLoc.z + pushOut.z);

        } else {
            this.DisableSuperThrow();
            particles = false;
            super.onHitBlock($$0);

        }

    }

    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void EnableSuperThrow() {
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, true);
        int ticks = 0;
        for (int b=PowersRatt.ShotThresholds.length-1;b>=0;b--) {
            if (this.charged >= PowersRatt.ShotThresholds[b]) {
                ticks = PowersRatt.ShotSuperthrowTicks[b];
                break;
            }
        }
        superThrowTicks = ticks;
    }
    public void DisableSuperThrow() {
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, false);
        superThrowTicks = 0;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.RATT_DART_THUNK_EVENT;
    }
    public void applyEffect(LivingEntity $$1) {
        int stack = -1;
        if ( $$1.getEffect(ModEffects.MELTING) != null) {
            stack = $$1.getEffect(ModEffects.MELTING).getAmplifier() + this.melting;
        } else { stack = melting -1;}
        if (stack != -1) {
            ((LivingEntity) $$1).addEffect(new MobEffectInstance(ModEffects.MELTING, 900, stack), this);
        }
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();

        if ($$1.equals(this.getOwner())) {return;}


        if ($$1 instanceof LivingEntity $$3) {
            StandPowers entityPowers = ((StandUser) $$3).roundabout$getStandPowers();
            if (entityPowers != null ) {
                if (entityPowers.dealWithProjectile(this, $$0)) {
                    this.discard();
                    return;
                }
            }

        }

        Entity $$4 = this.getOwner();
        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.STAND, $$4);
        SoundEvent $$6 = ModSounds.RATT_DART_IMPACT_EVENT;
        if ($$1.hurt($$5,this.damage + (($$1 instanceof Mob) ? 1F : 0) )) {


            if ($$4 instanceof LivingEntity LE) {
                if ( ((StandUser)$$4).roundabout$getStandPowers() instanceof PowersRatt PR ) {
                    if ($$1 instanceof LivingEntity l) {
                        PR.addEXP(1,l);
                    }
                }
                LE.setLastHurtMob($$1);
            }

            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }


            if ($$1 instanceof LivingEntity $$7) {
                applyEffect($$7);
                $$1.setDeltaMovement($$1.getDeltaMovement().multiply(0.4,0.4,0.4));
                if ($$4 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects($$7, $$4);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) $$4, $$7);
                }

                this.doPostHurtEffects($$7);
            }
            this.playSound($$6, 1.0F, (this.random.nextFloat() * 0.2F + 0.9F));
        }
        this.discard();

    }

    @Override
    public void tick(){
        Vec3 delta = this.getDeltaMovement();
        if (inGroundTime >= 160) {
            this.remove(RemovalReason.DISCARDED);
        }
        if(this.isInWater()) {
            this.entityData.set(ROUNDABOUT$SUPER_THROWN,false);
        }
        super.tick();
        if (this.getEntityData().get(ROUNDABOUT$SUPER_THROWN)) {
            this.setDeltaMovement(delta);
        }
        if (!this.level().isClientSide()) {
            if (this.particles) {
                ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.86F, 0.28F, 0.48F
                        ), 1f),
                        this.getX(), this.getY(), this.getZ(),
                        0, 0, 0, 0, 0);
            }
        }
    }


}
