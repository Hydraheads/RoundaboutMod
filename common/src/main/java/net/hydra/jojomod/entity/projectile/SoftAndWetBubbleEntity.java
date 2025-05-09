package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class SoftAndWetBubbleEntity extends AbstractHurtingProjectile implements UnburnableProjectile {
    public SoftAndWetBubbleEntity(EntityType<? extends SoftAndWetBubbleEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public LivingEntity standUser;
    public UUID standUserUUID;
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(SoftAndWetBubbleEntity.class, EntityDataSerializers.INT);
    protected SoftAndWetBubbleEntity(EntityType<? extends SoftAndWetBubbleEntity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }
    public void setUserID(int idd) {
        this.getEntityData().set(USER_ID, idd);
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            this.standUser = LE;
            if (!this.level().isClientSide()){
                standUserUUID = LE.getUUID();
            }
        }
    }
    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    @Override
    public float getPickRadius() {
        return 0.0F;
    }
    @Override
    protected ParticleOptions getTrailParticle() {
        return new BlockParticleOption(ParticleTypes.BLOCK, Blocks.AIR.defaultBlockState());
    }
    public static float eWidth=0.7f;
    public static float eHeight=0.7f;
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(eWidth, eHeight); // Width, Height
    }
    @Override
    protected float getInertia() {
        return 1F;
    }
    @Override
    public boolean isPickable() {
        if (this.level().isClientSide() && ClientUtil.getPlayer() != null && ClientUtil.getPlayer().getId() == getUserID()){
            return false;
        }
        return true;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        popBubble();
    }
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        popBubble();
    }
    public void popBubble(){
        this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                SoundSource.PLAYERS, 2F, (float)(0.98+(Math.random()*0.04)));
        this.discard();
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(USER_ID, -1);
    }
    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
    }
}
