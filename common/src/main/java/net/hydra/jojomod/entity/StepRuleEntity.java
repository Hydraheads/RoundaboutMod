package net.hydra.jojomod.entity;

import com.mojang.logging.LogUtils;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class StepRuleEntity extends Entity {

    public int time;
    public boolean dropItem;
    public int timing = -1;
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;
    protected static final EntityDataAccessor<Vector3f> DATA_FINAL_POS;

    public StepRuleEntity(EntityType<? extends StepRuleEntity> $$0, Level $$1) {
        super($$0, $$1);
        this.dropItem = true;
        this.blocksBuilding = true;
    }

    public static final float dimensions = 1F;

    public StepRuleEntity(Level $$0, double $$1, double $$2, double $$3, BlockState $$4) {
        this(ModEntities.STEP_RULE, $$0);
        this.setPos($$1, $$2, $$3);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = $$1;
        this.yo = $$2;
        this.zo = $$3;
        this.setStartPos(this.blockPosition());
    }

    @Override
    public void push(Entity $$0) {
    }

    public void setStartPos(BlockPos $$0) {
        this.entityData.set(DATA_START_POS, $$0);
    }
    public void setDataFinalPos(Vector3f $$0) {
        this.entityData.set(DATA_FINAL_POS, $$0);
    }

    public BlockPos getStartPos() {
        return (BlockPos)this.entityData.get(DATA_START_POS);
    }
    public Vector3f getFinalPos() {
        return (Vector3f) this.entityData.get(DATA_FINAL_POS);
    }

    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
        this.entityData.define(DATA_FINAL_POS, new Vector3f(0,0,0));
    }

    public boolean isPickable() {
        return false;
    }

    public float distanceToClearWhileTicked(){
        return 0.3f;
    }
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;

    public boolean updated = false;
    @Override
    public void lerpTo(double $$0, double $$1, double $$2, float $$3, float $$4, int $$5, boolean $$6) {
        this.lerpX = $$0;
        this.lerpY = $$1;
        this.lerpZ = $$2;
        this.setRot($$3, $$4);
        this.lerpSteps = $$5;
        updated = true;
    }

    public void breakAndDiscard(){
        discard();
    }


    @Override
    public boolean hurt(DamageSource $$0, float damage) {
        if (this.isInvulnerableTo($$0)) {
            return false;
        } else {
            return true;
        }
    }

    Vec3 finPos = Vec3.ZERO;
    @Override
    public void tick() {

        if (!level().isClientSide()) {
            if (timing > -1 && !((TimeStop)level()).inTimeStopRange(this)){
                timing--;
                if (timing <= 0){
                    breakAndDiscard();
                }
            }
        }
        super.tick();
        refreshDimensions();

        AABB wallBox = this.getBoundingBox();

        for (LivingEntity mob : level().getEntitiesOfClass(
                LivingEntity.class,
                wallBox.inflate(0.1))) {

            if (mob.getBoundingBox().intersects(wallBox)) {
                mob.push(0, 0.2, 0);
            }
        }

    }


    protected void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putInt("Time", this.time);
        $$0.putInt("DeathTimer", this.timing);
    }

    protected void readAdditionalSaveData(CompoundTag $$0) {
        this.time = $$0.getInt("Time");
        this.timing = $$0.getInt("DeathTimer");
    }


    public boolean displayFireAnimation() {
        return false;
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public void recreateFromPacket(ClientboundAddEntityPacket $$0) {
        super.recreateFromPacket($$0);
        this.blocksBuilding = true;
        double $$1 = $$0.getX();
        double $$2 = $$0.getY();
        double $$3 = $$0.getZ();
        this.setPos($$1, $$2, $$3);
        this.setStartPos(this.blockPosition());
    }

    static {
        DATA_START_POS = SynchedEntityData.defineId(StepRuleEntity.class, EntityDataSerializers.BLOCK_POS);
        DATA_FINAL_POS = SynchedEntityData.defineId(StepRuleEntity.class, EntityDataSerializers.VECTOR3);
    }

    public boolean fireImmune() {
        return true;
    }
    public boolean isOnFire() {
        return false;
    }

}
