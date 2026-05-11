package net.hydra.jojomod.entity.projectile;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class IronBallEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> IRON_BALL = SynchedEntityData.defineId(IronBallEntity.class,
                EntityDataSerializers.ITEM_STACK);

    public IronBallEntity(Level $$0, LivingEntity $$1) {
        super(ModEntities.IRON_BALL, $$1, $$0);
        this.pickup = Pickup.DISALLOWED;
    }
    public IronBallEntity(EntityType<? extends IronBallEntity> $$0, Level $$1) {
        super($$0, $$1);
        this.pickup = Pickup.DISALLOWED;
    }

    public IronBallEntity(Level $$0, double $$1, double $$2, double $$3) {
        super(ModEntities.IRON_BALL, $$1, $$2, $$3, $$0);
        this.pickup = Pickup.DISALLOWED;
    }

    public IronBallEntity(Level $$0, LivingEntity $$1, ItemStack $$2, double p_36862_, double p_36863_, double p_36864_) {
        super(ModEntities.IRON_BALL, p_36862_, p_36863_, p_36864_, $$0);
        this.setArrow($$2.copy());
        this.pickup = Pickup.DISALLOWED;
    }

    int falloff = 12;
    @Override
    public void tick() {
        this.pickup = Pickup.DISALLOWED;
        Vec3 deltamovement = getDeltaMovement();
        this.inGround = false;
        int bounceC = bounceCount;
        super.tick();
        if (!level().isClientSide()){
            if (inGround){
                discard();
            }
        }
        falloff--;
        if (falloff > 0 && bounceC == bounceCount) {
            setDeltaMovement(deltamovement);
        }
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


    private static final double baseDamage2 = 2f;

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        DamageSource damageSource;
        Entity entity2;
        Entity entity = entityHitResult.getEntity();
        float f = (float)this.getDeltaMovement().length();
        IAbstractArrowAccess iarrow = ((IAbstractArrowAccess) this);
        if (this.getPierceLevel() > 0) {
            if (iarrow.rdbt$piercingIgnoreEntityIds() == null) {
                iarrow.rdbt$piercingIgnoreEntityIds(new IntOpenHashSet(5));
            }
            if (iarrow.rdbt$piercedAndKilledEntities() == null) {
                iarrow.rdbt$piercedAndKilledEntities(Lists.newArrayListWithCapacity(5));
            }
            if (iarrow.rdbt$piercingIgnoreEntityIds().size() < this.getPierceLevel() + 1) {
                iarrow.rdbt$piercingIgnoreEntityIds().add(entity.getId());
            } else {
                this.discard();
                return;
            }
        }
        float pow = 9;
        if (this.isCritArrow()) {
            pow = 11;
        }
        if ((entity2 = this.getOwner()) == null) {
            damageSource = ModDamageTypes.of(this.level(), ModDamageTypes.IRON_BALL, this, this);
        } else {
            damageSource = ModDamageTypes.of(this.level(), ModDamageTypes.IRON_BALL, this, entity2);
            if (entity2 instanceof LivingEntity) {
                ((LivingEntity)entity2).setLastHurtMob(entity);
            }
        }
        boolean bl = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getRemainingFireTicks();
        if (this.isOnFire() && !bl) {
            entity.setSecondsOnFire(5);
        }
        byte pl = getPierceLevel();
        setPierceLevel((byte) 0);

        Vec3 between = this.getPosition(1).subtract(entity.getPosition(1f)).normalize();

        if (entity.hurt(damageSource, pow)) {
            if (bl) {
                return;
            }

            this.level().playSound(null, this.blockPosition(), ModSounds.IRON_BALL_IMPACT_EVENT, SoundSource.PLAYERS,
                    2F, (float)(1.03F+ Math.random()*0.2F));
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;


                if (iarrow.rdbt$knockback() > 0) {
                    double $$11 = Math.max((double)0.0F, (double)1.0F - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                    Vec3 $$12 = this.getDeltaMovement().multiply((double)1.0F, (double)0.0F, (double)1.0F).normalize().scale((double)iarrow.rdbt$knockback() * 0.6 * $$11);
                    if ($$12.lengthSqr() > (double)0.0F) {
                        livingEntity.push($$12.x, 0.1, $$12.z);
                    }
                }

                if (!this.level().isClientSide && entity2 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, entity2);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity2, livingEntity);
                }

                this.doPostHurtEffects(livingEntity);
                if (entity2 != null && livingEntity != entity2 && livingEntity instanceof Player && entity2 instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)entity2).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0f));
                }

                if (!entity.isAlive() && iarrow.rdbt$piercedAndKilledEntities() != null) {
                    iarrow.rdbt$piercedAndKilledEntities().add(livingEntity);
                }
                if (!this.level().isClientSide && entity2 instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer)entity2;
                    if (iarrow.rdbt$piercedAndKilledEntities() != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, iarrow.rdbt$piercedAndKilledEntities());
                    } else if (!entity.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, Arrays.asList(entity));
                    }
                }
            }

            float knockbackStrength = 1.65F;
            if (getOwner() instanceof Player PE && PE.distanceTo(this) < 5.5){
                knockbackStrength = 3.3F;
            }
            MainUtil.takeKnockbackWithY(entity, knockbackStrength,
                    between.x,
                    -0.1F,
                    between.z);

            setPierceLevel(pl);
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            if (pl > 0 && !bl) {
                //reduced kb

                float knockbackStrength = 0.75F;
                if (getOwner() instanceof Player PE && PE.distanceTo(this) < 5.5){
                    knockbackStrength = 1.5F;
                }
                MainUtil.takeKnockbackWithY(entity, knockbackStrength,
                        between.x,
                        -0.1F,
                        between.z);
            }
            entity.setRemainingFireTicks(j);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1));
            this.setYRot(this.getYRot() + 180.0f);
            this.yRotO += 180.0f;
            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7) {
                if (this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1f);
                }
                this.discard();
            }
        }
        if (!this.isRemoved()){
            setPierceLevel(pl);
        }
    }

    private int bounceCount = 0;
    private int critBounceCount = 0;
    private static final int max_bounces = 3;

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.blockPosition(), ModSounds.IRON_BALL_BOUNCE_EVENT, SoundSource.PLAYERS,
                    2F, (float)(1.2F / (this.random.nextFloat() * 0.2F + 0.9F)));

            BlockState $$1 = this.level().getBlockState(hitResult.getBlockPos());
            $$1.onProjectileHit(this.level(), $$1, hitResult, this);

            if (bounceCount >= max_bounces) {
                this.discard();
                return;
            }
            Vec3 velocity = this.getDeltaMovement();
            Direction hitDir = hitResult.getDirection();
            Vec3 normal = Vec3.atLowerCornerOf(hitDir.getNormal());

            // Makes it bounce
            Vec3 reflected = velocity.subtract(normal.scale(2 * velocity.dot(normal)));

            // Slowly stops it bouncing
            reflected = reflected.scale(0.5); // less bounce / more bounce :)

            this.setDeltaMovement(reflected);

            Vec3 hitLoc = hitResult.getLocation();
            Vec3 pushOut = normal.scale(0.5);
            this.setPos(hitLoc.x + pushOut.x, hitLoc.y + pushOut.y, hitLoc.z + pushOut.z);

            this.inGround = false;
            this.shakeTime = 0;

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
    }

    @Override
    public void remove(Entity.RemovalReason $$0) {
        if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
            this.spawnAtLocation(this.getPickupItem().copy(), 0.1F);
        }
        this.setRemoved($$0);
    }
    public IronBallEntity(Level $$0, LivingEntity $$1, ItemStack stack) {
        super(ModEntities.IRON_BALL, $$1, $$0);
        setArrow(stack.copy());
    }

    public ItemStack getArrow() {
        return this.entityData.get(IRON_BALL);
    }
    public void setArrow(ItemStack arrow) {
        this.entityData.set(IRON_BALL,arrow);
    }
    @Override
    protected ItemStack getPickupItem() {
        return getArrow();
    }


    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("ironBall",this.getPickupItem().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("ironBall");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setArrow(itemstack);
        super.readAdditionalSaveData($$0);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IRON_BALL, ItemStack.EMPTY);
    }
}
