package net.hydra.jojomod.entity.objects;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.block.StickyIceCoatingBlock;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class FallingBannerEntity extends Entity {
    public int lifeSpan = -1;
    public Entity user;


    public FallingBannerEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
    public FallingBannerEntity(Level $$2, Vec3 pos) {
        this(ModEntities.FALLING_BANNER, pos.x(), pos.y(), pos.z(), $$2);
    }
    protected FallingBannerEntity(EntityType<? extends Entity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    public boolean fireImmune() {
        return true;
    }
    public boolean isOnFire() {
        return false;
    }

    public boolean started = false;


    public static final float xwidth = 0.6f;
    public static final float xheight = 0.1f;

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        lifeSpan = compoundTag.getInt("lifespan");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("lifespan", lifeSpan);
    }

    public void tick() {
        super.tick();

        // Gravity
        if (!this.onGround()) {
            Vec3 velocity = this.getDeltaMovement();

            // Minecraft's normal-ish gravity
            velocity = velocity.add(0.0D, -0.04D, 0.0D);

            // Air resistance
            velocity = velocity.multiply(0.98D, 0.98D, 0.98D);

            this.setDeltaMovement(velocity);
        } else {
            // Stop once we hit the ground
            this.setDeltaMovement(
                    this.getDeltaMovement().x * 0.7D,
                    0.0D,
                    this.getDeltaMovement().z * 0.7D
            );
        }

        // Actually move and perform collision handling
        this.move(MoverType.SELF, this.getDeltaMovement());

        // If we hit the ground, stop falling
        if (this.onGround()) {
            Vec3 velocity = this.getDeltaMovement();

            this.setDeltaMovement(
                    velocity.x * 0.7D,
                    0.0D,
                    velocity.z * 0.7D
            );
        }

        // Your lifespan logic
        if (!level().isClientSide()) {
            if (lifeSpan > -1) {
                lifeSpan--;

                if (lifeSpan == 0) {
                    if (level() instanceof ServerLevel sl) {
                        MainUtil.sendParticlesIfPossible(this,this.level(),
                                ParticleTypes.CLOUD,
                                this.getEyePosition().x,
                                this.getEyePosition().y,
                                this.getEyePosition().z,
                                10,
                                0.3,
                                0.1,
                                0.3,
                                0.02F
                        );
                    }

                    discard();
                }
            } else {
                discard();
            }
        }
    }
    @Override
    public boolean isNoGravity() {
        return false;
    }

    protected static final EntityDataAccessor<ItemStack> BANNER = SynchedEntityData.defineId(FallingBannerEntity.class,
            EntityDataSerializers.ITEM_STACK);
    public final void setBanner(ItemStack stack) {
        this.entityData.set(BANNER, stack);
    }

    public final ItemStack getBanner() {
        return this.entityData.get(BANNER);
    }
    @Override
    public void defineSynchedData() {
        if (!this.entityData.hasItem(BANNER)) {
            this.entityData.define(BANNER, ItemStack.EMPTY);
        }
    }
}
