package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.client.models.layers.PreRenderEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersCinderella;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CinderellaVisageDisplayEntity extends ThrowableItemProjectile implements UnburnableProjectile, PreRenderEntity {

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    public CinderellaVisageDisplayEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public CinderellaVisageDisplayEntity(LivingEntity living, Level $$1) {
        super(ModEntities.CINDERELLA_VISAGE_DISPLAY, living, $$1);
    }
    public LivingEntity getUser(){
        if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            return LE;
        }
        return null;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
    @Override
    public boolean isAttackable() {
        return false;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }
    public int getMaxSize() {
        return this.getEntityData().get(MAX_SIZE);
    }
    public void setMaxSize(int idd) {
        this.getEntityData().set(MAX_SIZE, idd);
    }
    private static final EntityDataAccessor<Integer> USER_ID = SynchedEntityData.defineId(CinderellaVisageDisplayEntity.class, EntityDataSerializers.INT);
    public int getUserID() {
        return this.getEntityData().get(USER_ID);
    }
    public CinderellaVisageDisplayEntity(LivingEntity living, Level $$1, ItemStack itemStack) {
        super(ModEntities.CINDERELLA_VISAGE_DISPLAY, living, $$1);
        this.setUser(living);
        this.setItem(itemStack);
    }

    public LivingEntity standUser;
    public UUID standUserUUID;

    public boolean fireStormCreated = false;
    public void setUser(LivingEntity User) {
        standUser = User;
        this.getEntityData().set(USER_ID, User.getId());
        if (!this.level().isClientSide()){
            standUserUUID = User.getUUID();
        }
    }
    public Vec3 storeVec;
    public void shootFromRotationDeltaAgnostic(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shoot((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
    }

    public CinderellaVisageDisplayEntity(Level world, double p_36862_, double p_36863_, double p_36864_, ItemStack itemStack) {
        super(ModEntities.CINDERELLA_VISAGE_DISPLAY, p_36862_, p_36863_, p_36864_, world);
        this.setItem(itemStack);
    }
    public boolean isTickable = false;
    public void actuallyTick(){
        isTickable = true;
        tick();
        isTickable = false;
    }
    public int saneAgeTicking;
    public boolean initialized = false;
    @Override
    public void tick() {
        boolean client = this.level().isClientSide();
        if (!client){
            if (this.getStandUser() != null){
                if (MainUtil.cheapDistanceTo2(this.getX(),this.getZ(),this.standUser.getX(),this.standUser.getZ()) > 80
                        || !this.getStandUser().isAlive() || this.getStandUser().isRemoved()){
                    this.discard();
                }
            } else {
                this.discard();
            }
        }

        LivingEntity le = this.getStandUser();
        if (le != null){
            if (((StandUser)this.getStandUser()).roundabout$getStandPowers() instanceof PowersCinderella PCR) {
                int crossnum = this.getCrossNumber();
                if (crossnum > 0) {
                    this.setDeltaMovement(Vec3.ZERO);
                    if (client) {

                        if (!initialized) {
                            initialized = true;
                            PCR.addFloatingVisage(this);
                        }
                    }
                    if (!isTickable) {
                        return;
                    }
                }
            } else {
                if (!client) {
                    this.discard();
                    return;
                }
            }
        }
        if (client && this.saneAgeTicking != this.tickCount){
            if (lastRenderSize < getMaxSize()) {
                lastRenderSize += getAccrualRate();
            } else {
                lastRenderSize = getMaxSize();
            }
        } else {
        }
        saneAgeTicking = this.tickCount;

    }


    public double getRandomY(double $$0) {
        return this.getY((2.0 * this.random.nextDouble() - 1.0) * $$0);
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

    private static final EntityDataAccessor<Integer> CROSS_NUMBER = SynchedEntityData.defineId(CinderellaVisageDisplayEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MAX_SIZE = SynchedEntityData.defineId(CinderellaVisageDisplayEntity.class, EntityDataSerializers.INT);
    public int getCrossNumber() {
        return this.getEntityData().get(CROSS_NUMBER);
    }
    public void setCrossNumber(int idd) {
        this.getEntityData().set(CROSS_NUMBER, idd);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 0);
        this.entityData.define(USER_ID, -1);
        this.entityData.define(CROSS_NUMBER, 0);
        this.entityData.define(MAX_SIZE, 0);
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


    //@SuppressWarnings("deprecation")
    @Override
    protected void onHitBlock(BlockHitResult $$0) {

    }




    public void setSize(int idd) {
        this.getEntityData().set(SIZE, idd);
    }
    public int getSize() {
        return this.getEntityData().get(SIZE);
    }
    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(CinderellaVisageDisplayEntity.class, EntityDataSerializers.INT);
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {

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


    public void setRenderSize(float renderSize) {
        this.renderSize = renderSize;
    }
    public float getLastRenderSize() {
        return lastRenderSize;
    }
    public void setLastRenderSize(float renderSize) {
        this.lastRenderSize = renderSize;
    }
    public float renderSize = 0;
    public float lastRenderSize = 0;

    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
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
    public LivingEntity getStandUser(){
        if (standUser != null){
            return standUser;
        } else if (standUserUUID != null && !this.level().isClientSide()){
            Entity ett = ((ServerLevel)this.level()).getEntity(standUserUUID);
            if (ett instanceof LivingEntity lett){
                standUser = lett;
                this.setUserID(lett.getId());
            }
        } else if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            standUser = LE;
        }
        return standUser;
    }

    public float getRenderSize() {
        return renderSize;
    }

    public float getAccrualRate(){
        return 1;
    }
    @Override
    public boolean preRender(Entity ent, double $$1, double $$2, double $$3, float $$4, PoseStack pose, MultiBufferSource $$6) {
        if (ent instanceof CinderellaVisageDisplayEntity cfhe) {

            if (((TimeStop)ent.level()).inTimeStopRange(ent)){
                $$4 = 0;
            }
            LivingEntity user = cfhe.getStandUser();
            if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersCinderella PCR) {
                        if (PCR.floatingVisages == null) {
                            PCR.floatingVisages = new ArrayList<>();
                        }
                        List<CinderellaVisageDisplayEntity> hurricaneSpecial2 = new ArrayList<>(PCR.floatingVisages) {
                        };
                        if (!hurricaneSpecial2.isEmpty()) {
                            PCR.spinint = PCR.lastSpinInt + ($$4 * PCR.maxSpinint);
                            int totalnumber = hurricaneSpecial2.size();
                            double lerpX = (user.getX() * $$4) + (user.xOld * (1.0f - $$4));
                            double lerpY = (user.getY() * $$4) + (user.yOld * (1.0f - $$4));
                            double lerpZ = (user.getZ() * $$4) + (user.zOld * (1.0f - $$4));
                            PCR.transformFloatingVisages(cfhe, totalnumber, lerpX,
                                    lerpY, lerpZ, getRenderSize());
                        }
            }
        }
        return false;
    }
}
