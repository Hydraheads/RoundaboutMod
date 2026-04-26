package net.hydra.jojomod.entity.zombie_minion;

import net.hydra.jojomod.event.index.Tactics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class DogMinion extends BaseMinion{
    public DogMinion(EntityType<? extends DogMinion> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FallenZombieAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.33).add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.ATTACK_DAMAGE, 8).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    public void handleEntityEvent(byte $$0) {
        if ($$0 == 8) {
            this.isShaking = true;
            this.shakeAnim = 0.0F;
            this.shakeAnimO = 0.0F;
        } else if ($$0 == 56) {
            this.cancelShake();
        } else {
            super.handleEntityEvent($$0);
        }

    }

    public void aiStep() {
        super.aiStep();

    }
    private float interestedAngle;
    private float interestedAngleO;
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.interestedAngleO = this.interestedAngle;
            this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;

            if ((this.isShaking) && this.isShaking) {
                if (this.shakeAnim == 0.0F) {
                    this.playSound(SoundEvents.WOLF_SHAKE, this.getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.gameEvent(GameEvent.ENTITY_SHAKE);
                }

                this.shakeAnimO = this.shakeAnim;
                this.shakeAnim += 0.05F;
                if (this.shakeAnimO >= 2.0F) {
                    this.isShaking = false;
                    this.shakeAnimO = 0.0F;
                    this.shakeAnim = 0.0F;
                }

                if (this.shakeAnim > 0.4F) {
                    float $$0 = (float)this.getY();
                    int $$1 = (int)(Mth.sin((this.shakeAnim - 0.4F) * (float)Math.PI) * 7.0F);
                    Vec3 $$2 = this.getDeltaMovement();

                    for(int $$3 = 0; $$3 < $$1; ++$$3) {
                        float $$4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        float $$5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
                        this.level().addParticle(ParticleTypes.SPLASH, this.getX() + (double)$$4, (double)($$0 + 0.8F), this.getZ() + (double)$$5, $$2.x, $$2.y, $$2.z);
                    }
                }
            }

        }
    }
    private void cancelShake() {
        this.isShaking = false;
        this.shakeAnim = 0.0F;
        this.shakeAnimO = 0.0F;
    }
    private boolean isShaking;
    private float shakeAnim;
    private float shakeAnimO;
    public float getTailAngle() {
        return (0.55F - (this.getMaxHealth() - this.getHealth()) * 0.02F) * (float)Math.PI;

    }
    public boolean isInSittingPose() {
        return getMovementTactic() == Tactics.HOLD.id;
    }

    public float getBodyRollAngle(float $$0, float $$1) {
        float $$2 = (Mth.lerp($$0, this.shakeAnimO, this.shakeAnim) + $$1) / 1.8F;
        if ($$2 < 0.0F) {
            $$2 = 0.0F;
        } else if ($$2 > 1.0F) {
            $$2 = 1.0F;
        }

        return Mth.sin($$2 * (float)Math.PI) * Mth.sin($$2 * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
    }

    public float getHeadRollAngle(float $$0) {
        return Mth.lerp($$0, this.interestedAngleO, this.interestedAngle) * 0.15F * (float)Math.PI;
    }
}
