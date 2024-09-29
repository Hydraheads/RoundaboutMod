package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;


public class StandArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> STAND_ARROW = SynchedEntityData.defineId(StandArrowEntity.class,
            EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Boolean> ROUNDABOUT$SUPER_THROWN = SynchedEntityData.defineId(StandArrowEntity.class, EntityDataSerializers.BOOLEAN);

    public StandArrowEntity(EntityType<? extends StandArrowEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public StandArrowEntity(Level $$0, double $$1, double $$2, double $$3) {
        super(ModEntities.STAND_ARROW, $$1, $$2, $$3, $$0);
    }

    public StandArrowEntity(Level $$0, LivingEntity $$1, ItemStack $$2, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.STAND_ARROW, p_36862_, p_36863_, p_36864_, $$0);
        this.setArrow($$2.copy());
    }

    @Override
    public void tick() {
        Mob targetMob = MainUtil.homeOnWorthy(this.level(), this.position(), 5);
        if (targetMob != null){
            this.setDeltaMovement(
                    targetMob.position().add(0,targetMob.getEyeHeight(),0).subtract(this.position()).normalize().scale(this.getDeltaMovement().length())
            );

        }
        super.tick();
    }

    public void addItem(Entity target, ItemStack stack){
        if (target != null && !target.level().isClientSide()) {
            ItemEntity $$4 = new ItemEntity(target.level(), target.getX(),
                    target.getY() + target.getEyeHeight(), target.getZ(),
                    stack);
            $$4.setPickUpDelay(20);
            $$4.setThrower(target.getUUID());
            target.level().addFreshEntity($$4);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity $$1 = $$0.getEntity();
        float $$2 = (float)this.getDeltaMovement().length();
        int $$3 = Mth.ceil(Mth.clamp((double)$$2 * 2, 0.0, 2.147483647E9));

        if (this.isCritArrow()) {
            long $$4 = (long)this.random.nextInt($$3 / 2 + 2);
            $$3 = (int)Math.min($$4 + (long)$$3, 2147483647L);
        }

        Entity $$5 = this.getOwner();
        DamageSource $$6;
        if ($$5 == null) {
            $$6 = this.damageSources().arrow(this, this);
        } else {
            $$6 = this.damageSources().arrow(this, $$5);
            if ($$5 instanceof LivingEntity) {
                ((LivingEntity)$$5).setLastHurtMob($$1);
            }
        }

        boolean $$8 = $$1.getType() == EntityType.ENDERMAN;
        int $$9 = $$1.getRemainingFireTicks();
        if (this.isOnFire() && !$$8) {
            $$1.setSecondsOnFire(5);
        }
        boolean worthy = false;
        float X = $$3;
        if ($$1 instanceof Mob mob && MainUtil.canGrantStand(mob)){
            worthy = true;
            X = 0.2F;
            StandArrowItem.grantMobStand(this.getArrow(),this.level(),mob);
            if (this.getArrow().getItem() instanceof StandArrowItem && this.pickup == Pickup.ALLOWED){
                this.getArrow().hurt(1,this.level().getRandom(),null);
            }
            this.discard();
        }
        if (!worthy && $$1.hurt($$6, X)) {
            if ($$8) {
                return;
            }

            if ($$1 instanceof LivingEntity $$10) {
               if (MainUtil.canCauseRejection($$10)){
                   $$10.addEffect(new MobEffectInstance(ModEffects.STAND_VIRUS, 200, 0),this);
                   if (this.getArrow().getItem() instanceof StandArrowItem){
                       StandArrowItem.grantMobRejection(this.getArrow(),this.level(),$$10);
                   }
               }
                    if (this.getKnockback() > 0) {
                        double $$11 = Math.max(0.0, 1.0 - $$10.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        Vec3 $$12 = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale((double) this.getKnockback() * 0.6 * $$11);
                        if ($$12.lengthSqr() > 0.0) {
                            $$10.push($$12.x, 0.1, $$12.z);
                        }
                    }

                    if (!this.level().isClientSide && $$5 instanceof LivingEntity) {
                        EnchantmentHelper.doPostHurtEffects($$10, $$5);
                        EnchantmentHelper.doPostDamageEffects((LivingEntity) $$5, $$10);
                    }

                    this.doPostHurtEffects($$10);
            }

            this.playSound(this.getDefaultHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                if (this.getArrow().getItem() instanceof StandArrowItem && this.pickup == Pickup.ALLOWED){
                    this.getArrow().hurt(1,this.level().getRandom(),null);
                }
                this.discard();
            }
        } else {
            $$1.setRemainingFireTicks($$9);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1));
            this.setYRot(this.getYRot() + 180.0F);
            this.yRotO += 180.0F;
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {

                this.discard();
            }
        }
    }

    @Override
    public void remove(Entity.RemovalReason $$0) {
        if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
            this.spawnAtLocation(this.getPickupItem().copy(), 0.1F);
        }
        this.setRemoved($$0);
    }
    private int superThrowTicks = -1;

    public void starThrowInit(){
        this.entityData.set(ROUNDABOUT$SUPER_THROWN, true);
        superThrowTicks = 50;
    }

    public StandArrowEntity(Level $$0, LivingEntity $$1, ItemStack stack) {
        super(ModEntities.STAND_ARROW, $$1, $$0);
        setArrow(stack.copy());
    }

    public ItemStack getArrow() {
        return this.entityData.get(STAND_ARROW);
    }
    public void setArrow(ItemStack arrow) {
        this.entityData.set(STAND_ARROW,arrow);
    }
    @Override
    protected ItemStack getPickupItem() {
        return getArrow();
    }

    public boolean getSuperThrow() {
        return this.getEntityData().get(ROUNDABOUT$SUPER_THROWN);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("standArrow",this.getPickupItem().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("standArrow");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setArrow(itemstack);
        super.readAdditionalSaveData($$0);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ROUNDABOUT$SUPER_THROWN, false);
        this.entityData.define(STAND_ARROW, ItemStack.EMPTY);
    }
}
