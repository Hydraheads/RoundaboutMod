package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**Harpoons use retooled/edited trident code, so they can function similarly, but they have anti-air properties
 * such as damaging airborne mobs more based on airtime, and sniping elytra users/phantoms.
 * The code for sniping and loyalty will be replaced by enchants in future versions*/
public class HarpoonEntity extends AbstractArrow {
        private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(HarpoonEntity.class, EntityDataSerializers.BYTE);
        private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(HarpoonEntity.class, EntityDataSerializers.BOOLEAN);
        private ItemStack harpoonItem = new ItemStack(ModItems.HARPOON);
        private boolean dealtDamage;
        public int clientSideReturnTridentTickCount;

    public HarpoonEntity(EntityType<? extends HarpoonEntity> $$0, Level $$1) {
            super($$0, $$1);
        }

    public HarpoonEntity(Level $$0, LivingEntity $$1, ItemStack $$2) {
            super(ModEntities.THROWN_HARPOON, $$1, $$0);
            this.harpoonItem = $$2.copy();
            this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty($$2));
            this.entityData.set(ID_FOIL, $$2.hasFoil());
        }
    public HarpoonEntity(Level $$0, LivingEntity $$1, ItemStack $$2, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.THROWN_HARPOON, p_36862_, p_36863_, p_36864_, $$0);
        this.harpoonItem = $$2.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty($$2));
        this.entityData.set(ID_FOIL, $$2.hasFoil());
    }


        @Override
        protected void defineSynchedData() {
            super.defineSynchedData();
            this.entityData.define(ID_LOYALTY, (byte)0);
            this.entityData.define(ID_FOIL, false);
        }

        @Override
        public void tick() {

            if (!this.level().isClientSide) {
                LivingEntity targetMob = MainUtil.homeOnFlier(this.level(), this.position(), 13, this.getOwner());
                if (targetMob != null && !(this.getOwner() != null && this.getOwner().getUUID() == targetMob.getUUID())) {
                    if (!this.isNoPhysics()) {
                        double ln = targetMob.getDeltaMovement().multiply(1.2F, 1.2F, 1.2F).length();
                        if (this.getDeltaMovement().length() > ln) {
                            ln = this.getDeltaMovement().multiply(1.2F, 1.2F, 1.2F).length();
                        }

                        this.setDeltaMovement(
                                targetMob.position().add(0, targetMob.getEyeHeight(), 0).subtract(this.position()).normalize().scale(ln)
                        );
                    }
                }
            }

            if (this.inGroundTime > 4) {
                this.dealtDamage = true;
            }

            Entity $$0 = this.getOwner();
            //int $$1 = this.entityData.get(ID_LOYALTY);
            int $$1 = 2;
            if ($$1 > 0 && (this.dealtDamage || this.isNoPhysics()) && $$0 != null) {
                if (!this.isAcceptibleReturnOwner()) {
                    if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }

                    this.discard();
                } else {
                    this.setNoPhysics(true);
                    Vec3 $$2 = $$0.getEyePosition().subtract(this.position());
                    this.setPosRaw(this.getX(), this.getY() + $$2.y * 0.015 * (double)$$1, this.getZ());
                    if (this.level().isClientSide) {
                        this.yOld = this.getY();
                    }

                    double $$3 = 0.05 * (double)$$1;
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add($$2.normalize().scale($$3)));
                    if (this.clientSideReturnTridentTickCount == 0) {
                        this.playSound(ModSounds.HARPOON_RETURN_EVENT, 10.0F, 1.0F);
                    }

                    this.clientSideReturnTridentTickCount++;
                }
            }

            super.tick();
        }

        private boolean isAcceptibleReturnOwner() {
            Entity $$0 = this.getOwner();
            return $$0 == null || !$$0.isAlive() ? false : !($$0 instanceof ServerPlayer) || !$$0.isSpectator();
        }

        @Override
        protected ItemStack getPickupItem() {
            return this.harpoonItem.copy();
        }

        public boolean isFoil() {
            return this.entityData.get(ID_FOIL);
        }

        @Nullable
        @Override
        protected EntityHitResult findHitEntity(Vec3 $$0, Vec3 $$1) {
            return this.dealtDamage ? null : super.findHitEntity($$0, $$1);
        }

        public boolean skyHit = false;
        @Override
        protected void onHitEntity(EntityHitResult $$0) {
            Entity $$1 = $$0.getEntity();
            float $$2 = 6.0F;
            if ($$1 instanceof LivingEntity $$3) {
                int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, $$3);
                $$2 = (float) ($$2 * (1-(f*0.03)));
                $$2 += EnchantmentHelper.getDamageBonus(this.harpoonItem, $$3.getMobType());
            }

            Entity $$4 = this.getOwner();
            DamageSource $$5 = ModDamageTypes.of(this.level(),ModDamageTypes.HARPOON,this, (Entity)($$4 == null ? this : $$4));
            this.dealtDamage = true;

            skyHit = false;
            $$2 = addSkyDamage($$1,$$2);

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
            ((IAbstractArrowAccess)this).roundabout$cancelSuperThrow();
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
            float $$8 = 1.0F;
            if (skyHit){
                this.playSound(ModSounds.HARPOON_CRIT_EVENT, $$8, 1.0F);
            } else {
                this.playSound(ModSounds.HARPOON_HIT_EVENT, $$8, 1.0F);
            }
        }

        public float addSkyDamage(Entity target, float damage){
            if (target instanceof Player){
                if (((Player)target).isFallFlying()){
                    skyHit = true;
                    damage += 10;
                }
                int airTime = ((IPlayerEntity)target).roundabout$getAirTime();
                if (airTime > 0){
                    /**the longer a player is in the air without levitation, the more damage the harpoon will do*/
                    damage+= Math.min(12F,((float) airTime /10));
                    skyHit = true;
                }

            } else if (target instanceof Phantom
                    || target instanceof Bat){
                skyHit = true;
                damage += 10;
            }

            if (!target.onGround() && !target.isInWater() && !target.isSwimming() && !target.isPassenger()){
                damage += 4;
                skyHit = true;
            }

            return damage;
        }

        public boolean isChanneling() {
            return EnchantmentHelper.hasChanneling(this.harpoonItem);
        }

        @Override
        protected boolean tryPickup(Player $$0) {
            return super.tryPickup($$0) || this.isNoPhysics() && this.ownedBy($$0) && $$0.getInventory().add(this.getPickupItem());
        }

        @Override
        protected SoundEvent getDefaultHitGroundSoundEvent() {
            return ModSounds.HARPOON_GROUND_EVENT;
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
            if ($$0.contains("Harpoon", 10)) {
                this.harpoonItem = ItemStack.of($$0.getCompound("Harpoon"));
            }

            this.dealtDamage = $$0.getBoolean("DealtDamage");
            this.entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(this.harpoonItem));
        }

        @Override
        public void addAdditionalSaveData(CompoundTag $$0) {
            super.addAdditionalSaveData($$0);
            $$0.put("Harpoon", this.harpoonItem.save(new CompoundTag()));
            $$0.putBoolean("DealtDamage", this.dealtDamage);
        }

        @Override
        public void tickDespawn() {
            //int $$0 = this.entityData.get(ID_LOYALTY);
            int $$0 = 1;
            if (this.pickup != AbstractArrow.Pickup.ALLOWED || $$0 <= 0) {
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
