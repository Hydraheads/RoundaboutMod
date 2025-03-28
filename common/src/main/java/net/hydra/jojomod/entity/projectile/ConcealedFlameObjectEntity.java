package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

public class ConcealedFlameObjectEntity extends ThrowableItemProjectile {
    private void initDataTrackerRoundabout(CallbackInfo ci) {
    }
    @Override
    protected float getGravity() {
        return 0.0F;
    }

    public ConcealedFlameObjectEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ConcealedFlameObjectEntity(LivingEntity living, Level $$1) {
        super(ModEntities.CONCEALED_FLAME_OBJECT, living, $$1);
    }
    public LivingEntity getUser(){
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            return LE;
        }
        return null;
    }
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(ConcealedFlameObjectEntity.class, EntityDataSerializers.INT);
    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }
    public ConcealedFlameObjectEntity(LivingEntity living, Level $$1, ItemStack itemStack) {
        super(ModEntities.CONCEALED_FLAME_OBJECT, living, $$1);
        this.setUser(living);
        this.setItem(itemStack);
    }

    public LivingEntity standUser;
    public UUID standUserUUID;
    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
    }

    public ConcealedFlameObjectEntity(Level world, double p_36862_, double p_36863_, double p_36864_, ItemStack itemStack) {
        super(ModEntities.CONCEALED_FLAME_OBJECT, p_36862_, p_36863_, p_36864_, world);
        this.setItem(itemStack);
    }

    public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }
    @Override
    public void tick(){
        boolean client = this.level().isClientSide();
        if (!client){
            if (isEffectivelyInWater()){
                this.discard();
            }
            if (this.getUser() != null){
                if (MainUtil.cheapDistanceTo2(this.getX(),this.getZ(),this.standUser.getX(),this.standUser.getZ()) > 80
                        || !this.getUser().isAlive() || this.getUser().isRemoved()){
                    this.discard();
                }
            } else {
                this.discard();
            }
        }
        Entity $$0 = this.getUser();
            Vec3 delta = this.getDeltaMovement();
            super.tick();
            this.setDeltaMovement(delta);

            if (!client && this.tickCount %2 == 0) {
                if ($$0 instanceof LivingEntity LE && ((StandUser) LE).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                    Vec3 $$2 = this.getDeltaMovement();
                    double $$3 = this.getX() + $$2.x;
                    double $$4 = this.getY() + $$2.y;
                    double $$5 = this.getZ() + $$2.z;
                    this.level().addParticle(PMR.getFlameParticle(), $$3, $$4 + 0.5, $$5, 0.0, 0.0, 0.0);

                    ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), $$3,
                            $$4 + 0.5, $$5,
                            0,
                            0,
                            0,
                            0,
                            0.15);
                }
            }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("roundabout.HeldItem",this.getItem().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("roundabout.HeldItem");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setItem(itemstack);
        super.readAdditionalSaveData($$0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 0);
        this.entityData.define(USER_ID, -1);
    }
    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }

    @Override
    public boolean isPickable() {
        return true;
    }
    Direction tempDirection = Direction.UP;

    public boolean tryHitBlock(BlockHitResult $$0, BlockPos pos, BlockState state){

        if ((state.isAir() || state.canBeReplaced()) && (this.getOwner() != null && !((this.getOwner() instanceof Player &&
                (((Player) this.getOwner()).blockActionRestricted(this.getOwner().level(), pos, ((ServerPlayer)
                        this.getOwner()).gameMode.getGameModeForPlayer()))) ||
                !this.getOwner().level().mayInteract(((Player) this.getOwner()), pos)))){

            if (this.getItem().getItem() instanceof BlockItem) {
                Direction direction = this.getDirection();
                if (direction.getAxis() == Direction.Axis.X){
                    direction = direction.getOpposite();
                }
                if (((BlockItem) this.getItem().getItem()).getBlock() instanceof RotatedPillarBlock){
                    direction = $$0.getDirection();
                }

                if (((BlockItem)this.getItem().getItem()).place(new DirectionalPlaceContext(this.level(),
                        pos,
                        direction, this.getItem(),
                        direction)) != InteractionResult.FAIL){
                    this.tempDirection = direction;
                    return true;
                }
            }

        }
        return false;
    }

    //@SuppressWarnings("deprecation")
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);


        if (!this.level().isClientSide) {
            if (!this.getItem().isEmpty()) {
                BlockPos pos = $$0.getBlockPos().relative($$0.getDirection());
                dropItem(pos);
            }
            this.discard();
        }

    }

    public void dropItem(BlockPos pos){
        ItemEntity $$4 = new ItemEntity(this.level(), pos.getX() + 0.5F,
                pos.getY() + 0.25F, pos.getZ() + 0.5F,
                this.getItem());
        $$4.setPickUpDelay(40);
        $$4.setThrower(this.getUUID());
        $$4.setDeltaMovement(Math.random() * 0.1F - 0.05F, 0.2F, Math.random() * 0.18F - 0.05F);
        this.level().addFreshEntity($$4);
    }


    public void blockBreakParticles(Block block, Vec3 pos){
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                            block.defaultBlockState()),
                    pos.x, pos.y, pos.z,
                    100, 0, 0, 0, 0.5);
        }
    }

    public void setSize(int idd) {
        this.getEntityData().set(SIZE, idd);
    }
    public int getSize() {
        return this.getEntityData().get(SIZE);
    }
    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(ConcealedFlameObjectEntity.class, EntityDataSerializers.INT);
    @Override
    public boolean hurt(DamageSource source, float amount) {
        LivingEntity user = this.getUser();

        if (source.getEntity() != null && user != null &&
                ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            if (source.getEntity().is(getUser())) {
                burst(PMR);
                this.discard();
            } else {
                burst(PMR);
                CrossfireHurricaneEntity.blastEntity(source.getEntity(), this,
                        this.getSize(), user, true, PMR);
                this.discard();
            }
        }
        return true;
    }

    public void burst(PowersMagiciansRed PMR){
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.blockPosition(), ModSounds.CROSSFIRE_EXPLODE_EVENT,
                    SoundSource.PLAYERS, 2F, 1F);
            ((ServerLevel) this.level()).sendParticles(PMR.getFlameParticle(), this.getX(),
                    this.getY()+0.5, this.getZ(),
                    200,
                    0.01, 0.01, 0.01,
                    0.1);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        if ($$1 instanceof LivingEntity LE){
        }

        if (!this.level().isClientSide) {
            Entity $$4 = this.getOwner();

            DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.THROWN_OBJECT, $$4);


            if ($$1.hurt($$5, 10)) {
                if ($$1.getType() == EntityType.ENDERMAN) {
                    return;
                }
            }
            this.discard();
        }

    }


    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }


    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }
}
