package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**Notice: This is for early fired ripper eyes. Full charge is not an entity.*/
public class RipperEyesProjectile extends RoundaboutGeneralProjectile{
    public RipperEyesProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public int charge = 0;
    public final AnimationState ripperEyes = new AnimationState();
    protected RipperEyesProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    protected RipperEyesProjectile(EntityType<RoundaboutGeneralProjectile> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public RipperEyesProjectile(LivingEntity $$1, Level $$2) {
        this(ModEntities.RIPPER_EYES_PROJECTILE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    @Override
    public int getMaxLifeSpan(){
        return 20;
    }

    public List<Entity> alreadyHitEntities = new ArrayList<>();

    public void blastEntity(Entity entity){
        //Add hurt code here
        if (getOwner() instanceof Player pl && ((IPowersPlayer)pl).rdbt$getPowers() instanceof VampireGeneralPowers vgp){
            float power = vgp.getRipperEyeStrayStrength(entity,charge);
            Roundabout.LOGGER.info("charge-> "+charge+" power-> "+power);
            if (DamageHandler.RipperEyesDamage(entity, power, pl)) {
                MainUtil.makeBleed(entity,0,400,pl);
            }
            vgp.addToCombo();
        }
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

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
    }

    public void tick(){
        super.tick();
        if (!level().isClientSide()){
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
