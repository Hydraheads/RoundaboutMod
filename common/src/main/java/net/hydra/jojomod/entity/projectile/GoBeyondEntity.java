package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class GoBeyondEntity extends SoftAndWetBubbleEntity {
    public GoBeyondEntity(EntityType<? extends SoftAndWetBubbleEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public GoBeyondEntity(LivingEntity $$1, Level $$2) {
        super(ModEntities.GO_BEYOND, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }


    public Entity chasing = null;
    public Entity getChasing(){
        return chasing;
    }
    public void setChasing(Entity chasing){
        this.chasing = chasing;
    }
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
    }
    public void tick() {
        if (!this.level().isClientSide()) {
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
            }
        }
    }
}
