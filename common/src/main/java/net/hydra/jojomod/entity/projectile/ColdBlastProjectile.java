package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Zombiefish;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ColdBlastProjectile extends RoundaboutGeneralProjectile{
    public ColdBlastProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ColdBlastProjectile(LivingEntity $$1, Level $$2) {
        this(ModEntities.COLD_BLAST_PROJECTILE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    protected ColdBlastProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    public int getMaxLifeSpan(){
        return 20;
    }
    protected ColdBlastProjectile(EntityType<RoundaboutGeneralProjectile> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    public final AnimationState ripperEyes = new AnimationState();

    @Override
    public void tick() {
        super.tick();
    }

    public boolean alreadyHitEntity(Entity entity){
        return alreadyHitEntities.contains(entity);
    }

    public List<Entity> alreadyHitEntities = new ArrayList<>();

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
    public void blastEntity(Entity entity){
        //Add hurt code here
        //Roundabout.LOGGER.info("charge-> "+charge+" power-> "+power);
        if (entity instanceof LivingEntity lv && !(getOwner() != null && getOwner().getUUID() == entity.getUUID())) {

        }
        alreadyHitEntities.add(entity);
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!level().isClientSide()) {
            discard();
        }
    }
    @Override
    public boolean needsStandUser(){
        return false;
    }
    @Override
    public boolean killAtZero(){
        return false;
    }
}
