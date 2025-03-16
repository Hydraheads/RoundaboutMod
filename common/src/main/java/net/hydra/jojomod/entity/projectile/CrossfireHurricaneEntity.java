package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.access.IMinecartTNT;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenCreeper;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class CrossfireHurricaneEntity extends AbstractHurtingProjectile {
    protected CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    protected CrossfireHurricaneEntity(EntityType<? extends CrossfireHurricaneEntity> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(MatchEntity.class, EntityDataSerializers.BOOLEAN);
    private int superThrowTicks = -1;
    public void starThrowInit(){
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, true);
        superThrowTicks = 50;
    }


    public boolean isBundle = false;
    public void tick() {
        super.tick();
    }
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {

    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ROUNDABOUT$SUPER_THROWN, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        super.readAdditionalSaveData($$0);
    }


}
