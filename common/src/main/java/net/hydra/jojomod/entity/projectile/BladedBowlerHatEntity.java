package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.ISuperThrownAbstractArrow;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenPhantom;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class BladedBowlerHatEntity extends AbstractArrow {
        private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(BladedBowlerHatEntity.class, EntityDataSerializers.BYTE);
        private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(BladedBowlerHatEntity.class, EntityDataSerializers.BOOLEAN);
        private ItemStack bowlerHatItem = ItemStack.EMPTY;
        private boolean dealtDamage;

        public boolean isThrown = false;
        public int clientSideReturnTridentTickCount;

    public void setItem(ItemStack stack){
        this.bowlerHatItem = stack == null ? ItemStack.EMPTY : stack.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.bowlerHatItem));
        this.entityData.set(ID_FOIL, this.bowlerHatItem.hasFoil());
    }

    public BladedBowlerHatEntity(EntityType<? extends BladedBowlerHatEntity> type, Level level) {
        super(type, level);
    }

    public BladedBowlerHatEntity(Level level, LivingEntity livingEntity, ItemStack itemStack) {
            super(ModEntities.BLADED_BOWLER_HAT, livingEntity, level);
            bowlerHatItem = itemStack;
            this.bowlerHatItem = itemStack.copy();
            this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(itemStack));
            this.entityData.set(ID_FOIL, itemStack.hasFoil());
        }
    public BladedBowlerHatEntity(Level $$0, LivingEntity $$1, ItemStack $$2, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.BLADED_BOWLER_HAT, p_36862_, p_36863_, p_36864_, $$0);
        this.bowlerHatItem = $$2.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty($$2));
        this.entityData.set(ID_FOIL, $$2.hasFoil());
    }


        @Override
        protected void defineSynchedData() {
            super.defineSynchedData();
            this.entityData.define(ID_LOYALTY, (byte)0);
            this.entityData.define(ID_FOIL, false);
        }

    boolean isHoldingItem;
    int holdingCheck;

    @Override
        public void tick() {
            Vec3 $$26 = this.getDeltaMovement();
            this.setDeltaMovement($$26.x, $$26.y + (double)0.01F, $$26.z);
            super.tick();

            if (level().isClientSide) {
                boolean isFlying = getDeltaMovement().lengthSqr() > 0.01;

                if (isFlying) {
                    if (this.tickCount%80 ==1) {
                        if (!((TimeStop) this.level()).inTimeStopRange(this)) {
                            ClientUtil.handleBowlerHatFlySound(this);
                        }
                    }
                }
            }

            if (holdingCheck < 1) {
                holdingCheck += 1;
                isHoldingItem = false;
            }

            // Timer that calls back the hat when it's been flying for too long
            if (!level().isClientSide && this.tickCount >= 200) {
                Entity entity = this.getOwner();
                if (entity instanceof Player player && player.isAlive()) {
                    if (!player.getInventory().add(this.getPickupItem())) {
                        player.drop(this.getPickupItem(), false);
                    }
                } else {
                    ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                            this.getY() + this.getEyeHeight(), this.getZ(),
                            this.getPickupItem());
                    $$4.setPickUpDelay(0);
                    this.level().addFreshEntity($$4);
                }
                this.discard();
                return;
            }

            if (!this.level().isClientSide) {
                if (!this.isHoldingItem) {
                    double pickupRadius = 1.0D;
                    double tntPickupRadius = 2.0D;

                    List<ItemEntity> itemsNearby = this.level().getEntitiesOfClass(
                            ItemEntity.class,
                            this.getBoundingBox().inflate(pickupRadius),
                            item -> !item.isRemoved() && !item.hasPickUpDelay()
                    );

                    if (!itemsNearby.isEmpty()) {
                        // Pick up the first available item
                        ItemEntity itemEntity = itemsNearby.get(0);
                        itemEntity.startRiding(this, true);
                        this.isHoldingItem = true;
                    }

                    List<net.minecraft.world.entity.item.PrimedTnt> tntNearby = this.level().getEntitiesOfClass(
                            net.minecraft.world.entity.item.PrimedTnt.class,
                            this.getBoundingBox().inflate(tntPickupRadius),
                            tnt -> !tnt.isRemoved()
                    );

                    if (!tntNearby.isEmpty()) {
                        net.minecraft.world.entity.item.PrimedTnt tntEntity = tntNearby.get(0);
                        tntEntity.startRiding(this, true);
                        this.isHoldingItem = true;
                    }
                }
            }

            if (!this.level().isClientSide) {

                if (this.inGroundTime > 0.1F) {
                    this.dealtDamage = true;
                }

                Entity $$0 = this.getOwner();
                int $$1 = 2;
                if ($$1 > 0 && (this.dealtDamage || this.isNoPhysics()) && $$0 != null) {
                    if (!this.isAcceptibleReturnOwner()) {
                        if (this.pickup == Pickup.DISALLOWED) {
                            this.spawnAtLocation(this.getPickupItem(), 0.1F);
                        }
                        this.discard();
                    } else {
                        this.setNoPhysics(true);
                        Vec3 $$2 = $$0.getEyePosition().subtract(this.position());
                        this.setPosRaw(this.getX(), this.getY() + $$2.y * 0.015 * (double) $$1, this.getZ());

                        double $$3 = 0.05 * (double) $$1;
                        this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add($$2.normalize().scale($$3)));
                        if (this.clientSideReturnTridentTickCount == 0 && this.level().isClientSide) {
                            this.playSound(ModSounds.HARPOON_RETURN_EVENT, 10.0F, 1.0F);
                        }

                        this.clientSideReturnTridentTickCount++;
                    }
                }
            }
        }

    private boolean isAcceptibleReturnOwner() {
            Entity $$0 = this.getOwner();
            return $$0 == null ? false : !($$0 instanceof ServerPlayer) || !$$0.isSpectator();
        }

    @Override
    protected ItemStack getPickupItem() {
        if (this.bowlerHatItem.isEmpty()) {
            return ItemStack.EMPTY;
        }

        Entity owner = this.getOwner();
        ItemStack stack = this.bowlerHatItem.copy();

        if (owner instanceof LivingEntity livingEntity) {
            stack.hurtAndBreak(1, livingEntity, e -> {

            });
        }
        return stack;
    }

        public boolean isFoil() {
            return this.entityData.get(ID_FOIL);
        }

        @Nullable
        @Override
        protected EntityHitResult findHitEntity(Vec3 $$0, Vec3 $$1) {
            return this.dealtDamage ? null : super.findHitEntity($$0, $$1);
        }

    private int bounceCount = 0;
    private int critBounceCount = 0;
    private static final int max_bounces = 3;

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        this.playSound(ModSounds.HARPOON_GROUND_EVENT, 1.0F,
                1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

        if (bounceCount >= max_bounces) {
            super.onHitBlock(hitResult);
            this.inGround = true;
            return;
        }

        Vec3 velocity = this.getDeltaMovement();
        Direction hitDir = hitResult.getDirection();
        Vec3 normal = Vec3.atLowerCornerOf(hitDir.getNormal());

        // Makes it bounce
        Vec3 reflected = velocity.subtract(normal.scale(2 * velocity.dot(normal)));

        // Slowly stops it bouncing
        reflected = reflected.scale(0.7); // less bounce / more bounce :)

        this.setDeltaMovement(reflected);

        Vec3 hitLoc = hitResult.getLocation();
        Vec3 pushOut = normal.scale(0.5);
        this.setPos(hitLoc.x + pushOut.x, hitLoc.y + pushOut.y, hitLoc.z + pushOut.z);

        this.inGround = false;
        this.shakeTime = 0;

        this.dealtDamage = false;

        bounceCount++;
        if (hitResult.getDirection() != net.minecraft.core.Direction.UP) {
            critBounceCount++;
        }

        if (!this.level().isClientSide) {
            Vec3 start = this.position();
            Vec3 end = start.add(reflected);
            BlockHitResult extraHit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (extraHit.getType() == HitResult.Type.BLOCK) {
                Vec3 n2 = Vec3.atLowerCornerOf(extraHit.getDirection().getNormal());
                Vec3 reflected2 = reflected.subtract(n2.scale(2 * reflected.dot(n2))).scale(0.6);
                this.setDeltaMovement(reflected2);

                Vec3 pushOut2 = n2.scale(0.5);
                this.setPos(extraHit.getLocation().x + pushOut2.x,
                        extraHit.getLocation().y + pushOut2.y,
                        extraHit.getLocation().z + pushOut2.z);
            }
        }

        if (!this.level().isClientSide) {
            Vec3 start = this.position();
            Vec3 end = start.add(reflected);
            EntityHitResult entityHitResult = this.findHitEntity(start, end);
            if (entityHitResult != null) {
                this.onHitEntity(entityHitResult);
            }
        }
    }

        public boolean skyHit = false;
        @Override
        protected void onHitEntity(EntityHitResult $$0) {
            Entity $$1 = $$0.getEntity();
            if ($$1 instanceof SoftAndWetBubbleEntity)
                return;
            float $$2 = 4.0F;

            Entity $$4 = this.getOwner();
            DamageSource $$5 = ModDamageTypes.of(this.level(),ModDamageTypes.BLADED_BOWLER_HAT,this, (Entity)($$4 == null ? this : $$4));
            this.dealtDamage = true;

            skyHit = false;
            $$2 = addSkyAndBounceDamage($$1,$$2);

            if (isThrown){
                if (((ISuperThrownAbstractArrow)this).roundabout$getSuperThrow()){
                    $$2*=2F;
                } else {
                    $$2*=1.5F;
                }
            }

            if ($$1.hurt($$5, $$2)) {
                if ($$1.getType() == EntityType.ENDERMAN) {
                    return;
                }

                if ($$1 instanceof LivingEntity $$7) {
                    if ($$4 instanceof LivingEntity) {
                        EnchantmentHelper.doPostHurtEffects($$7, $$4);
                        EnchantmentHelper.doPostDamageEffects((LivingEntity)$$4, $$7);
                    }

                    this.doPostHurtEffects($$7);
                }
            }

            ((ISuperThrownAbstractArrow)this).roundabout$cancelSuperThrow();
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
            float $$8 = 1.0F;
            if (skyHit){
                this.playSound(ModSounds.HARPOON_CRIT_EVENT, $$8, 1.0F);
            } else {
                this.playSound(ModSounds.HARPOON_HIT_EVENT, $$8, 1.0F);
            }
        }

        public float addSkyAndBounceDamage(Entity target, float damage){
            if (target instanceof Player){
                if (((Player)target).isFallFlying()){
                    skyHit = true;
                    damage += 3;
                }

            } else if (target instanceof Phantom
                    || target instanceof FallenPhantom
                    || target instanceof Bat){
                skyHit = true;
                damage += 3;
            }

            if (!target.onGround() && !target.isInWater() && !target.isSwimming() && !target.isPassenger()){
                damage += 2;
                skyHit = true;
            }

            if (critBounceCount == 1) {
                damage += 6;
                skyHit = true;
            } else if (critBounceCount == 2) {
                damage += 8;
                skyHit = true;
            } else if (critBounceCount == 3) {
                damage += 12;
                skyHit = true;
            }

            if (MainUtil.getMobBleed(target)){
                ((StandUser)target).roundabout$setBleedLevel(1);
                ((LivingEntity)target).addEffect(new MobEffectInstance(ModEffects.BLEED, 400, 0), this);
            }

            return damage;
        }

        public boolean isChanneling() {
            return EnchantmentHelper.hasChanneling(this.bowlerHatItem);
        }

        @Override
        protected boolean tryPickup(Player $$0) {
            return super.tryPickup($$0) || this.isNoPhysics() && this.ownedBy($$0) && $$0.getInventory().add(this.getPickupItem());
        }

        @Override
        public void playerTouch(Player $$0) {
            if (this.ownedBy($$0) || this.getOwner() == null) {
                super.playerTouch($$0);
            }
        }

        @Override
        public void readAdditionalSaveData(CompoundTag $$0) {
            super.readAdditionalSaveData($$0);
            if ($$0.contains("BladedBowlerHat", 10)) {
                this.bowlerHatItem = ItemStack.of($$0.getCompound("BladedBowlerHat"));
            }

            this.dealtDamage = $$0.getBoolean("DealtDamage");
            this.entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(this.bowlerHatItem));
        }

        @Override
        public void addAdditionalSaveData(CompoundTag $$0) {
            super.addAdditionalSaveData($$0);
            if (!this.bowlerHatItem.isEmpty()) {
                $$0.put("BladedBowlerHat", this.bowlerHatItem.save(new CompoundTag()));
            }
            $$0.putBoolean("DealtDamage", this.dealtDamage);
        }

        @Override
        public void tickDespawn() {
            //int $$0 = this.entityData.get(ID_LOYALTY);
            int $$0 = 1;
            if (this.pickup != Pickup.ALLOWED || $$0 <= 0) {
                super.tickDespawn();
            }
        }

        @Override
        protected float getWaterInertia() {
            return 0.99F;
        }

        @Override
        public boolean shouldRender(double $$0, double $$1, double $$2) {
            return true;
        }
}
