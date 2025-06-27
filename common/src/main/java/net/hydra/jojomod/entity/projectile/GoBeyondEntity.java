package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.NoHitboxRendering;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GoBeyondEntity extends SoftAndWetBubbleEntity implements NoHitboxRendering {
    public GoBeyondEntity(EntityType<? extends SoftAndWetBubbleEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GoBeyondEntity(LivingEntity $$1, Level $$2) {
        super(ModEntities.GO_BEYOND, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    public boolean hurt(DamageSource $$0, float $$1) {
        if ($$0.is(DamageTypes.GENERIC_KILL)){
            return super.hurt($$0,$$1);
        }
        return false;
    }

    public int ticksUntilDamage = 0;
    public Entity chasing = null;
    public Entity getChasing(){
        return chasing;
    }
    public void setChasing(Entity chasing){
        this.chasing = chasing;
    }


    public boolean success = false;
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide()) {
            Entity ent = getChasing();
            if (ticksUntilDamage <= 0) {
                if (ent != null && $$0.getEntity().is(ent)) {
                    if (this.getOwner() instanceof LivingEntity LE && ((StandUser) LE).roundabout$getStandPowers() instanceof PowersSoftAndWet PW) {
                        if (ent.hurt(ModDamageTypes.of(ent.level(), ModDamageTypes.GO_BEYOND, this.getOwner()),
                                PW.getGoBeyondStrength(ent))) {
                            //You don't need to hurt them to launch them

                            if (MainUtil.getMobBleed(ent)) {
                                MainUtil.makeBleed(ent, 2, 400, LE);
                                MainUtil.makeMobBleed(ent);
                            }
                        }

                        Vec3 launchVec = this.getDeltaMovement();
                        Vec3 vec3d2 = launchVec.normalize().scale(3F);
                        vec3d2 = vec3d2.add(0, 0.8F, 0);

                        MainUtil.takeLiteralUnresistableKnockbackWithY(ent,
                                vec3d2.x,
                                vec3d2.y,
                                vec3d2.z);


                        this.level().playSound(null, this.blockPosition(), ModSounds.GO_BEYOND_HIT_EVENT,
                                SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));


                        ((ServerLevel) this.level()).sendParticles(ModParticles.ENERGY_DISTORTION,
                                this.getX(), this.getY(), this.getZ(),
                                0, 0, 0, 0, 0.2F);
                        for (int i = 0; i < 100; ++i) {
                            double randomX = (Math.random() * 0.5) - 0.25;
                            double randomY = (Math.random() * 0.5) - 0.25;
                            double randomZ = (Math.random() * 0.5) - 0.25;
                            Vec3 xvec = vec3d2.add(randomX, randomY, randomZ);
                            ((ServerLevel) this.level()).sendParticles(ModParticles.STAR,
                                    this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                                    0, xvec.x, xvec.y, xvec.z, 0.7F);
                        }
                        success = true;
                        popBubble();
                        super.onHitEntity($$0);
                    }
                }
            }
            Roundabout.LOGGER.info("A "+ticksUntilDamage);
        }
    }
    //2.8F;
    @Override

    public void popBubble(){
        if (!this.level().isClientSide()){
            if (!success) {
                this.level().playSound(null, this.blockPosition(), ModSounds.EXPLOSIVE_BUBBLE_POP_EVENT,
                        SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                ((ServerLevel) this.level()).sendParticles(ModParticles.STAR,
                        this.getX(), this.getY() + this.getBbHeight(), this.getZ(),
                        5, 0.2, 0.2, 0.2, 0.015);
            }
            this.discard();
        }
    }

    @Override
    public boolean distancePops(){
        return false;
    }
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
    }
    public void tick() {
        if (!this.level().isClientSide()) {
            if (ticksUntilDamage >= 0){
                ticksUntilDamage--;

            }
            lifeSpan--;
            if (lifeSpan <= 0 || (this.standUser == null || !(((StandUser) this.standUser).roundabout$getStandPowers() instanceof PowersSoftAndWet))) {
                popBubble();
                return;
            }
        }

        if (!this.level().isClientSide()) {
            if (this.getChasing() != null){
                this.setDeltaMovement(this.getPosition(1).subtract(this.getChasing().getEyePosition(1F)).normalize().reverse().scale(this.getSped()));
            } else {
                popBubble();
                return;
            }
        }


        super.tick();
        if (!this.level().isClientSide()) {
            if (this.tickCount % 40 == 9) {
                ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE,
                        this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(),
                        0, 0, 0, 0, 0.015);
            }

            if (!isRemoved()) {

                Vec3 currentPos = this.position();
                Vec3 nextPos = currentPos.add(this.getDeltaMovement());
                AABB sweptBox = this.getBoundingBox()
                        .expandTowards(this.getDeltaMovement())
                        .inflate(this.getBbWidth() * 1 + 0.1); // Adjust as needed

                EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                        this.level(), this, currentPos, nextPos, sweptBox,
                        this::canHitEntity
                );

                if (entityHitResult != null) {
                    this.onHitEntity(entityHitResult);
                }

                AABB box = this.getBoundingBox().inflate(0.1); // Slight growth
                for (Entity e : level().getEntities(this, box, this::canHitEntity)) {
                    this.onHitEntity(new EntityHitResult(e));
                }
            }
        }
    }
}
