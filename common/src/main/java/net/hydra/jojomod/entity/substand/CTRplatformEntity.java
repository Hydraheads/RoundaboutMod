package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersCatchTheRainbow;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static net.hydra.jojomod.util.MainUtil.sendParticlesIfPossible;

public class CTRplatformEntity extends StandEntity {

    public CTRplatformEntity(EntityType<? extends StandEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean isValid(boolean userActive, LivingEntity thisStand, LivingEntity userEntity){
        return userEntity.isAlive() && !userEntity.isRemoved() && (!needsActive() || userActive) && validatePowers(userEntity);
    }

    @Override
    public void tick() {
        double randX = Roundabout.RANDOM.nextDouble(-1, 1);
        double randZ = Roundabout.RANDOM.nextDouble(-1, 1);
        sendParticlesIfPossible(this, level(), ParticleTypes.SPLASH,
                this.getX() + randX,
                this.getY(),
                this.getZ() + randZ,
                1, 0, 0, 0, 0);

        if(!this.level().isClientSide) {
            if (User != null) {
                this.setPos(User.getX(), this.getY(), User.getZ());
                if (MainUtil.cheapDistanceTo(this.getX(), this.getY(), this.getZ(), User.getX(), User.getY(), User.getZ()) > 1 || User.isCrouching()) {
                    this.discard();
                }
            }
        }

        super.tick();
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.0F).add(Attributes.MAX_HEALTH, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0);
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
        return true;
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
