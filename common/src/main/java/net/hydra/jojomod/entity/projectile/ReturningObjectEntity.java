package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;


// It doesn't need to be a ThrowableItemProjectile but tbh it just made the deltaMovement work, call me lazy
public class ReturningObjectEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<BlockPos> RETURN_POS = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<BlockPos> BOX_START = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockPos> BOX_END = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.BLOCK_POS);


    public ReturningObjectEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ReturningObjectEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1, BlockPos ret, BlockPos start, BlockPos end) {
        super($$0, $$1);
        this.entityData.set(RETURN_POS,ret);
        this.entityData.set(BOX_START,start);
        this.entityData.set(BOX_END,end);
    }



    public int getLifetime() {return this.entityData.get(LIFETIME);}
    public void setLifetime(int i) {this.entityData.set(LIFETIME,i);}

    public BlockState getState() {return this.entityData.get(BLOCK_STATE);}
    public void setState(BlockState s) {this.entityData.set(BLOCK_STATE,s);}

    public BlockPos getReturnPos() {return this.entityData.get(RETURN_POS);}
    public BlockPos getBoxStart() {return this.entityData.get(BOX_START);}
    public BlockPos getBoxEnd() {return this.entityData.get(BOX_END);}

    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }

    @Override
    protected void defineSynchedData() {
        if (!this.getEntityData().hasItem(RETURN_POS)) {
            this.getEntityData().define(BLOCK_STATE, Blocks.DIRT.defaultBlockState());
            this.getEntityData().define(RETURN_POS, BlockPos.ZERO);
            this.getEntityData().define(BOX_START, BlockPos.ZERO);
            this.getEntityData().define(BOX_END, BlockPos.ZERO);
            this.getEntityData().define(LIFETIME, 0);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("return")) {
            int[] array = compoundTag.getIntArray("return");
            this.entityData.set(RETURN_POS,new BlockPos(array[0],array[1],array[2]));
        }
        if (compoundTag.contains("lifetime")) {
            this.setLifetime(compoundTag.getInt("lifetime"));
        }
        if (compoundTag.contains("state")) {
            this.setState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compoundTag.getCompound("state")));
        }
        if (compoundTag.contains("box_start")) {
            int[] array = compoundTag.getIntArray("box_start");
            this.entityData.set(BOX_START,new BlockPos(array[0],array[1],array[2]));
        }
        if (compoundTag.contains("box_end")) {
            int[] array = compoundTag.getIntArray("box_end");
            this.entityData.set(BOX_END,new BlockPos(array[0],array[1],array[2]));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("lifetime",this.getLifetime());
        compoundTag.put("state", NbtUtils.writeBlockState(this.getState()));

        BlockPos returnPos = this.getReturnPos();
        int[] Return = {
                returnPos.getX(),
                returnPos.getY(),
                returnPos.getZ()
        };
        compoundTag.putIntArray("return",Return);

        BlockPos startPos = getBoxStart();
        int[] start = {
                startPos.getX(),
                startPos.getY(),
                startPos.getZ()
        };
        BlockPos endPos = getBoxStart();
        int[] end = {
                endPos.getX(),
                endPos.getY(),
                endPos.getZ()
        };
        compoundTag.putIntArray("box_start",start );
        compoundTag.putIntArray("box_end",end );
    }



    @Override
    public boolean isAttackable() {return false;}
    @Override
    public boolean isInvulnerable() {return true;}
    @Override
    public boolean isPickable() {return false;}
    @Override
    public boolean isPushedByFluid() {return false;}
    @Override
    public boolean isNoGravity() {return true;}

    private int soundDelay = 0;

    /// to whomever uses this next, add different byte types for CD, Tusk, etc, unless you're okay with them all sounding the same/having the same particles

    @Override
    public void tick() {
        super.tick();

        if (soundDelay == 0) {
            this.soundDelay = 22;
            this.level().playSound(null,this.blockPosition(), ModSounds.TUSK_HOLE_MOVE_EVENT, SoundSource.PLAYERS,0.2F,0.75F);
        } else if (soundDelay > 0) {
            soundDelay--;
        }

        if (!this.level().isClientSide() && this.tickCount % 3 == 0) {
            ((ServerLevel)this.level()).sendParticles(new DustParticleOptions(new Vector3f(1, 1, 1
            ), 1f),this.getX(), this.getY()+0.5F, this.getZ(),
                    0, 0, 0, 0, 0);
        }

        if (this.getLifetime() > 0) {
            this.setLifetime(this.getLifetime() - 1);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.85F));
            if (this.getLifetime() == 0) {
                this.setDeltaMovement(this.getDeltaMovement().reverse());
                this.hasImpulse = true;
            }
        } else {
            BlockPos ret = this.getReturnPos();
            if (ret != null) {
                Vec3 center = ret.getCenter();
                if (!this.level().isClientSide()) {
                    BlockPos start = getBoxStart();
                    BlockPos end = getBoxEnd();
                    List<Entity> entities = this.level().getEntities(null, new AABB(
                            start.getX(), start.getY(), start.getZ(),
                            end.getX(), end.getY(), end.getZ()).inflate(1.05F));
                    entities.removeIf(entity -> !entity.isPickable());
                    if (entities.isEmpty() && this.level().getBlockState(this.getReturnPos()).isAir() ) {
                        Vec3 dir = center.subtract(this.getPosition(0)).normalize();
                        this.setDeltaMovement(dir);
                        if (this.getPosition(0).distanceTo(center) <= 1) {
                            this.level().setBlock(ret, this.getState(), 3);
                            this.discard();
                        }
                    }
                }
            }
        }
    }
}
