package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EvilAuraProjectile extends RoundaboutGeneralProjectile{
    public EvilAuraProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected EvilAuraProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    protected EvilAuraProjectile(EntityType<RoundaboutGeneralProjectile> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public EvilAuraProjectile(LivingEntity $$1, Level $$2) {
        this(ModEntities.EVIL_AURA_PROJECTILE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    @Override
    public int getMaxLifeSpan(){
        return 20;
    }

    public List<Entity> alreadyHitEntities = new ArrayList<>();

    public void blastEntity(Entity entity){
        entity.setDeltaMovement(entity.getDeltaMovement().add(getDeltaMovement().normalize().scale(1.4F)));
        entity.hasImpulse = true;
        entity.hurtMarked = true;

        //Roundabout.LOGGER.info("1");
        ((ServerLevel) level()).sendParticles(ModParticles.VAMPIRE_AURA,
                entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z,
                0, 0, 0, 0, 0.8);
        alreadyHitEntities.add(entity);
    }
    public boolean alreadyHitEntity(Entity entity){
        return alreadyHitEntities.contains(entity);
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!level().isClientSide()) {
            Entity ent = $$0.getEntity();
            if (ent != null && ent.isAlive()) {
                if (!alreadyHitEntity($$0.getEntity())) {
                    blastEntity(ent);
                }
            }
        }
    }

    public void tick(){
        if (!level().isClientSide()){
            Vec3 windDir = getDeltaMovement().normalize();
            ((ServerLevel) level()).sendParticles(ParticleTypes.CLOUD,
                    getX(), getY()+0.2F, getZ(),
                    0, windDir.x, windDir.y, windDir.z, 0.05);
        }
        super.tick();
    }

}
