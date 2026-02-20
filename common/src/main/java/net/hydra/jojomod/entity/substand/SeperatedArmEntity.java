package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SeperatedArmEntity extends StandEntity {

    String context = "left_hand";
    public final AnimationState floating = new AnimationState();

    public static final byte
            IDLE=11;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == IDLE) {
                this.floating.startIfStopped(this.tickCount);
            } else {
                this.floating.stop();
            }

        }
    }

    public SeperatedArmEntity(EntityType<SeperatedArmEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public void jump(Vec3 jumpT0Pos){
        Vec3 location = new Vec3(this.getX(),this.getY(),this.getZ());
        ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD_DUST, location.x,
                location.y, location.z,
                24,
                0.005, 0.005, 0.005,
                0.1);
        //this.setDeltaMovement(jumpT0Pos);
        this.lookAt(EntityAnchorArgument.Anchor.EYES,jumpT0Pos);
        this.setDeltaMovement(this.getLookAngle().multiply(1.5,1.5,1.5));
    }


    @Override
    public void tick() {
        this.setFadeOut((byte)1);
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        if (!client) {
            if(user == null){
                this.discard();
            }

            for(int i = 0; i < 2; i = i + 1) {
                double randX = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
                double randY = Roundabout.RANDOM.nextDouble(-0.1, 0.1);
                double randZ = Roundabout.RANDOM.nextDouble(-0.2, 0.2);
                ((ServerLevel) this.level()).sendParticles(ModParticles.MOLD,
                        this.getX()+randX,
                        this.getY()+randY + 0.15 ,
                        this.getZ() + randZ,
                        1,0,0,0,0);
            }
        }

        super.tick();
    }


    @Override
    public boolean isInvulnerableTo(DamageSource $$0) {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }


    /**USER_ID is the mob id of the stand's user. Needs to be stored as an int,
     * because clients do not have access to UUIDS.*/
    protected static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SeperatedLegsEntity.class,
            EntityDataSerializers.INT);

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity $$0) {
        return true;
    }

    @Override
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox();
    }

    @Override
    public boolean hasNoPhysics() {
        return false;
    }

    @Override
    protected boolean isHorizontalCollisionMinor(Vec3 $$0) {
        return super.isHorizontalCollisionMinor($$0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return true;
    }
}
