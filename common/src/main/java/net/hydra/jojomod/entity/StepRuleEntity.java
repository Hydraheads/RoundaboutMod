package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersCalifornia;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class StepRuleEntity extends Entity {

    public int time;
    public boolean dropItem;
    public int timing = -1;
    public int timing2 = 0;
    public Entity userEntity;
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;

    public StepRuleEntity(EntityType<? extends StepRuleEntity> $$0, Level $$1) {
        super($$0, $$1);
        this.dropItem = true;
        this.blocksBuilding = true;
    }

    public boolean getTurnedBad() {
        return this.getEntityData().get(TURNED_BAD);
    }

    public void setTurnedBad(boolean bleed){
        this.entityData.set(TURNED_BAD, bleed);
    }

    private static final EntityDataAccessor<Boolean> TURNED_BAD =
            SynchedEntityData.defineId(StepRuleEntity.class, EntityDataSerializers.BOOLEAN);
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(TURNED_BAD)) {
            this.entityData.define(DATA_START_POS, BlockPos.ZERO);
            this.entityData.define(TURNED_BAD, false);
        }
    }

    public static final float dimensions = 1F;

    public StepRuleEntity(Level $$0, double $$1, double $$2, double $$3) {
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

    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
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

    public int renderFadeIn = 1;
    Vec3 finPos = Vec3.ZERO;
    @Override
    public void tick() {

        if (!level().isClientSide()) {
            if (userEntity instanceof LivingEntity LE &&
                    ((StandUser)LE).roundabout$getStandPowers() instanceof PowersCalifornia pca) {
                if (timing > -1 && !((TimeStop) level()).inTimeStopRange(this)) {
                    timing2++;
                    timing--;
                    if (timing <= 0) {
                        breakAndDiscard();
                    } else {
                        if (timing2 >= 15) {
                            setTurnedBad(true);
                        }
                    }
                }

                if (getTurnedBad() && isAlive() && !isRemoved()) {
                    AABB wallBox = this.getBoundingBox();

                    for (LivingEntity mob : level().getEntitiesOfClass(
                            LivingEntity.class,
                            wallBox)) {

                        if (!(mob instanceof StandEntity se && se.getUser().getUUID() == LE.getUUID())
                        && mob.isAlive()) {
                            if (mob.getBoundingBox().intersects(wallBox)) {
                                if (mob.getUUID() == userEntity.getUUID()) {
                                    pca.playUnfairSound();
                                    pca.clearListServer();
                                    discard();
                                    break;
                                } else {
                                    if (!pca.hurtEntities.containsKey(mob) && PowersCalifornia.canSteal(mob)) {
                                        pca.addToList(mob);
                                        pca.playGotchaSound();
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                discard();
            }
        } else {
            if (renderFadeIn < 20) {
                renderFadeIn++;
            }
        }
        super.tick();
        refreshDimensions();


    }


    protected void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putInt("Time", this.time);
        $$0.putInt("DeathTimer", this.timing);
        $$0.putBoolean("bled",getTurnedBad());
    }

    protected void readAdditionalSaveData(CompoundTag $$0) {
        this.time = $$0.getInt("Time");
        this.timing = $$0.getInt("DeathTimer");
        this.setTurnedBad($$0.getBoolean("bled"));
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
    }

    public boolean fireImmune() {
        return true;
    }
    public boolean isOnFire() {
        return false;
    }

}
