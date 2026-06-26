package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class GentlyWeepsEntity extends WhiteAlbumFreezingEntity {
    public static final float height = 3f;
    public static final float width = 3f;

    public GentlyWeepsEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
    public GentlyWeepsEntity(Level $$2, Vec3 pos) {
        this(ModEntities.GENTLY_WEEPS, pos.x(), pos.y(), pos.z(), $$2);
    }

    protected GentlyWeepsEntity(EntityType<? extends Entity> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }

    public void tick(){
        if (!level().isClientSide()){
            if (lifeSpan > -1){
                lifeSpan--;
                if (lifeSpan == 0){
                    discard();
                }
            } else {
                discard();
            }


                AABB wallBox = this.getBoundingBox();

                for (Entity mob : level().getEntitiesOfClass(
                        Entity.class,
                        wallBox.inflate(0.1))) {

                    if (mob.getBoundingBox().intersects(wallBox)) {
                        if (mob instanceof Projectile pj){
                            if (!getBled()) {
                                if (pj instanceof BloodSplatterEntity ||
                                        (pj instanceof SoftAndWetPlunderBubbleEntity pb && pb.getLiquidStolen() == 4)
                                ) {
                                    setBled(true);
                                } else {

                                }
                            }
                        } else if (MainUtil.canFreeze(mob)) {
                            if (this.tickCount > 10) {
                                if (mob instanceof Player pl) {
                                    int amt = -3;
                                    if (this.tickCount > 20) {
                                        amt = -4;
                                    }
                                    HeatUtil.addHeat(mob, amt);
                                } else {
                                    HeatUtil.addHeat(mob, -2);
                                }
                            } else {
                                if (mob instanceof Player pl) {
                                    HeatUtil.addHeat(mob, -1);
                                } else {
                                    HeatUtil.addHeat(mob, -1);
                                }
                            }
                        }
                    }
                }

            if (tickCount > 7) {
                int range = 0;
                if (tickCount > 12) {
                    range = 1;
                }
                for (int y = 1; y < 4; y++) {
                    for (int x = -range; x <= range; x++) {
                        for (int z = -range; z <= range; z++) {
                            BlockPos targetPos = getOnPos().offset(x, y, z);
                            BlockState iceState = ModBlocks.COLD_AIR.defaultBlockState();

                            if (canFreeze(targetPos)
                                    && iceState.canSurvive(level(), targetPos)) {
                                level().setBlockAndUpdate(targetPos, iceState);
                                level().scheduleTick(targetPos, ModBlocks.COLD_AIR, Mth.nextInt(level().getRandom(), lifeSpan, lifeSpan+1));
                            }
                            // placement logic
                        }
                    }
                }
            }

        } else {
            if (renderCold < 10) {
                renderCold++;
            }
            if (!started && !((TimeStop) level()).inTimeStopRange(this)) {
                started = true;
                ClientUtil.handleWeepsSound(this);
            }
        }
        super.tick();
    }

    public boolean getBled() {
        return this.getEntityData().get(BLED);
    }

    public void setBled(boolean bleed){
        this.entityData.set(BLED, bleed);
    }

    private static final EntityDataAccessor<Boolean> BLED =
            SynchedEntityData.defineId(GentlyWeepsEntity.class, EntityDataSerializers.BOOLEAN);
    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        $$0.putBoolean("bled",getBled());
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        this.setBled($$0.getBoolean("bled"));
        super.readAdditionalSaveData($$0);
    }

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(BLED)) {
            super.defineSynchedData();
            this.entityData.define(BLED, false);
        }
    }
}
