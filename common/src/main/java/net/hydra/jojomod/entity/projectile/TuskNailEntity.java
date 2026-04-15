package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.IEnderMan;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.pathfinding.TuskHoleEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TuskNailEntity extends AbstractArrow {

    @Override
    public void shootFromRotation(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * ((float)Math.PI / 180F)) * Mth.cos($$1 * ((float)Math.PI / 180F));
        float $$7 = -Mth.sin(($$1 + $$3) * ((float)Math.PI / 180F));
        float $$8 = Mth.cos($$2 * ((float)Math.PI / 180F)) * Mth.cos($$1 * ((float)Math.PI / 180F));
        this.shoot($$6, $$7, $$8, $$4, $$5);
    }


    private int life = 0;
    public TuskNailEntity(EntityType<? extends TuskNailEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public TuskNailEntity(LivingEntity $$1, Level $$2, byte type) {
        super(ModEntities.TUSK_NAIL, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
        this.setAct(type);
    }

    public static final byte
        NONE = (byte) 0,
        GUARD_BREAK = (byte) 1,
        REDUCED = (byte) 2;


    private static final EntityDataAccessor<Integer> ROUNDABOUT$ACT = SynchedEntityData.defineId(TuskNailEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> ROUNDABOUT$BONUS = SynchedEntityData.defineId(TuskNailEntity.class, EntityDataSerializers.BYTE);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(ROUNDABOUT$ACT)) {
            this.entityData.define(ROUNDABOUT$ACT, 1);
            this.entityData.define(ROUNDABOUT$BONUS, NONE);
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        if ($$0.contains("act")) {this.entityData.set(ROUNDABOUT$ACT,$$0.getInt("act"));}
        if ($$0.contains("rb_extra")) {this.entityData.set(ROUNDABOUT$BONUS,$$0.getByte("rb_extra"));}
        super.readAdditionalSaveData($$0);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        if (this.getEntityData().hasItem(ROUNDABOUT$ACT)) {$$0.putInt("act",this.getEntityData().get(ROUNDABOUT$ACT));}
        if (this.getEntityData().hasItem(ROUNDABOUT$BONUS)) {$$0.putByte("rb_extra",this.getEntityData().get(ROUNDABOUT$BONUS));}
        super.addAdditionalSaveData($$0);
    }

    public int getAct() {return this.entityData.get(ROUNDABOUT$ACT);}
    public void setAct(int act) {this.entityData.set(ROUNDABOUT$ACT,Mth.clamp(act,1,4));}
    public byte getExtra() {return this.entityData.get(ROUNDABOUT$BONUS);}
    public void setExtra(byte i) {this.entityData.set(ROUNDABOUT$BONUS,i);}

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void tick() {
        super.tick();
        this.life += 1;
        if (this.life > 200) {
            this.discard();
        }
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_TRAIL,
                    this.getX(), this.getY() + this.getBbHeight() / 2, this.getZ(),
                    0, 0, 0, 0, 0.015);



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


    @SuppressWarnings("deprecation")
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState($$0.getBlockPos())),
                    $$0.getLocation().x, $$0.getLocation().y, $$0.getLocation().z,
                    30, 0.2, 0.05, 0.2, 0.3);


            BlockState bs = this.level().getBlockState($$0.getBlockPos());
            if (this.getAct() > 1) {
                createHole();
            }
            this.discard();
        }
    }

    private TuskHoleEntity createHole() {return createHole(this.getPosition(0));}
    private TuskHoleEntity createHole(Vec3 pos) {
        if (this.getOwner() instanceof LivingEntity LE) {
            TuskHoleEntity tuskHoleEntity = new TuskHoleEntity(this.level(), LE);
            tuskHoleEntity.setPos(pos);
            this.level().addFreshEntity(tuskHoleEntity);
            return tuskHoleEntity;
        }
        return null;
    }

    @Override
    protected ItemStack getPickupItem() {return ItemStack.EMPTY;}




    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide()) {

            if ($$0.getEntity() instanceof EnderMan em) {
                ((IEnderMan) em).roundabout$teleport();
                return;
            }
            Entity ent = $$0.getEntity();
            if (!(ent instanceof TuskNailEntity)) {
                if (this.getOwner() instanceof LivingEntity LE && ((StandUser) LE).roundabout$getStandPowers() instanceof PowersTusk PT) {
                    if (!(MainUtil.isMobOrItsMounts(ent, getOwner())) && !MainUtil.isCreativeOrInvincible(ent)) {
                        float str = PT.getNailDamage(ent,this.getAct());

                        if (this.getExtra() == REDUCED) {
                            str = 0.1F;
                        }

                        if (ent.hurt(ModDamageTypes.of(ent.level(), ModDamageTypes.EXPLOSIVE_STAND, this.getOwner()), str)) {
                            float knockbackStrength = this.getAct() == 1 ? 0.1F : 0.3F;
                            PowersTusk.takeDeterminedKnockbackWithY2(LE, ent, knockbackStrength);

                            if (this.getAct() > 1) {
                                TuskHoleEntity tuskHole = createHole(ent.getPosition(0));
                                if (tuskHole != null) {
                                    tuskHole.doHurtTarget(ent);
                                }
                            }

                            if (ent instanceof LivingEntity LIVE) {
                                if (this.getAct() == 2) {
                                    LIVE.addEffect(new MobEffectInstance(ModEffects.UNBALANCED, 140));
                                }
                                LE.setLastHurtMob(LIVE);
                                PT.addEXP(this.getAct() == 1 ? 1 : 2);
                            }
                        } else if (ent instanceof LivingEntity LIVE) {

                            if (LIVE.isBlocking() && this.getExtra() == GUARD_BREAK) {
                                MainUtil.knockShieldPlusStand(LIVE,60);
                                for(TuskNailEntity tuskNailEntity : this.level().getEntitiesOfClass(TuskNailEntity.class,this.getBoundingBox().inflate(2))) {
                                    tuskNailEntity.setExtra(REDUCED);
                                }
                            }

                        }

                    }
                }
                this.discard();
            }
        }
    }


}
