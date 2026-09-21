package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ReturningObjectEntity extends Entity {
    private static final EntityDataAccessor<BlockPos> RETURN_POS = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Integer> LIFETIME = SynchedEntityData.defineId(ReturningObjectEntity.class, EntityDataSerializers.INT);

    public ReturningObjectEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }


    public BlockPos getReturnPos() {return this.entityData.get(RETURN_POS);}
    public void setReturnPos(BlockPos bp) {this.entityData.set(RETURN_POS,bp);}

    public int getLifetime() {return this.entityData.get(LIFETIME);}
    public void setLifetime(int i) {this.entityData.set(LIFETIME,i);}

    public BlockState getState() {return this.entityData.get(BLOCK_STATE);}
    public void setState(BlockState s) {this.entityData.set(BLOCK_STATE,s);}


    @Override
    protected void defineSynchedData() {
        if (!this.getEntityData().hasItem(RETURN_POS)) {
            this.getEntityData().define(BLOCK_STATE, Blocks.DIRT.defaultBlockState());
            this.getEntityData().define(RETURN_POS, BlockPos.ZERO);
            this.getEntityData().define(LIFETIME, 0);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("return")) {
            CompoundTag blockPos = compoundTag.getCompound("return");
            this.setReturnPos(new BlockPos(
                    blockPos.getInt("x"),
                    blockPos.getInt("y"),
                    blockPos.getInt("z")
                    ));
        }
        if (compoundTag.contains("lifetime")) {
            this.setLifetime(compoundTag.getInt("lifetime"));
        }
        if (compoundTag.contains("state")) {
            this.setState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compoundTag.getCompound("state")));
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        CompoundTag tag = compoundTag.getCompound("return");
        BlockPos returnPos = this.getReturnPos();
        tag.putInt("x",returnPos.getX());
        tag.putInt("y",returnPos.getY());
        tag.putInt("z",returnPos.getZ());
        compoundTag.putInt("lifetime",this.getLifetime());
        compoundTag.put("state", NbtUtils.writeBlockState(this.getState()));
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

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (this.getLifetime() > 0) {
                this.setLifetime(this.getLifetime() - 1);
             //   this.setDeltaMovement(this.getDeltaMovement().scale(0.98F));
            } else {
                BlockPos ret = this.getReturnPos();
                if (ret != null) {
                    Vec3 center = ret.getCenter();
                    List<Entity> entities = MainUtil.genHitbox(this.level(),center.x(),center.y(),center.z(),.6F,.6F,.6F);
                    entities.removeIf(entity -> !entity.isPickable());
                    if (entities.isEmpty()) {
                        Vec3 dir = center.subtract(this.getPosition(0)).normalize();
                        this.setPos(getPosition(0).add(dir));
                        if (this.getPosition(0).distanceTo(center) <= 1.3) {
                            Roundabout.LOGGER.info(""+this.getState());
                            this.level().setBlock(ret,this.getState(),3);
                            this.discard();
                        }
                    }
                }
            }
        }
    }
}
