package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.item.BodyBagItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class FallenMob extends Mob {
    public boolean isActivated = false;
    public int ticksThroughPhases = 0;
    public int ticksThroughPlacer = 0;
    public Entity placer;
    public Entity controller;
    private static final EntityDataAccessor<Boolean> TICKS_THROUGH_PLACER =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CONTROLLER =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> FORCED_ROTATION =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> PHASES_FULL =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BOOLEAN);
    public float getForcedRotation() {
        return this.getEntityData().get(FORCED_ROTATION);
    }
    public void setForcedRotation(float fr){
        this.entityData.set(FORCED_ROTATION, fr);
    }
    public boolean getPhasesFull() {
        return this.getEntityData().get(PHASES_FULL);
    }

    public void setPhasesFull(boolean bool){
        this.entityData.set(PHASES_FULL, bool);
    }
    public int getController() {
        return this.getEntityData().get(CONTROLLER);
    }

    public void setController(int controller){
        this.entityData.set(CONTROLLER, controller);
    }
    public void setController(Entity controller){
        this.controller = controller;
        this.entityData.set(CONTROLLER, controller.getId());
    }
    public boolean getTicksThroughPlacer() {
        return this.getEntityData().get(TICKS_THROUGH_PLACER);
    }
    public int getPlacer() {
        return this.getEntityData().get(CONTROLLER);
    }
    public void setPlacer(int controller){
        this.entityData.set(CONTROLLER, controller);
    }
    public void setPlacer(Entity controller){
        this.placer = controller;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        $$0.putBoolean("IsActivated",isActivated);
        $$0.putInt("TicksThroughPhases",ticksThroughPhases);
        if (this.placer != null) {
            $$0.putUUID("Placer", this.placer.getUUID());
        }
        if (this.controller != null) {
            $$0.putUUID("Controller", this.controller.getUUID());
        }
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        this.isActivated = $$0.getBoolean("IsActivated");
        this.ticksThroughPhases = $$0.getInt("TicksThroughPhases");
        UUID $$1;
        UUID $$2;
        if ($$0.hasUUID("Placer")) {
            $$1 = $$0.getUUID("Placer");
            if (this.level() instanceof ServerLevel SE){
                this.placer = SE.getEntity($$1);
            }
        }
        if ($$0.hasUUID("Controller")) {
            $$2 = $$0.getUUID("Controller");
            if (this.level() instanceof ServerLevel SE){
                this.controller = SE.getEntity($$2);
            }
        }
        super.readAdditionalSaveData($$0);
    }
    public void tickThroughPlacerStart(){
        this.entityData.set(TICKS_THROUGH_PLACER, true);
        this.ticksThroughPlacer = 50;
    }
    @Override
    public void tick(){
        if (ticksThroughPlacer > 0){
            ticksThroughPlacer--;
            if (ticksThroughPlacer <= 0){
                this.entityData.set(TICKS_THROUGH_PLACER, false);
            }
        }

        if (!getPhasesFull()){
            if (ticksThroughPhases < 10){
                ticksThroughPhases++;
            } else {
                if (!this.level().isClientSide()) {
                    setPhasesFull(true);
                }
            }
        } else {
            if (!isActivated && !getTicksThroughPlacer()){
                if (this.level().isClientSide){

                    float fr = this.getForcedRotation();
                    if (fr != 0){
                        this.setYBodyRot(fr);
                        this.yBodyRotO = fr;
                        this.setYRot(fr);
                        this.xRotO = fr;
                    }

                    if (ClientUtil.checkIfClientHoldingBag()) {
                        if (this.tickCount % 5 == 0) {
                            for (int i = 0; i < ConfigManager.getClientConfig().particleSettings.bodyBagHoldingParticlesPerFiveTicks; i++) {
                                this.level()
                                        .addParticle(
                                                ParticleTypes.HAPPY_VILLAGER,
                                                this.getRandomX(1.3),
                                                this.getY() + this.getBbHeight() / 6,
                                                this.getRandomZ(1.3),
                                                0,
                                                0.15,
                                                0
                                        );
                            }
                        }
                    }
                } else {
                    float fr = this.getForcedRotation();
                    if (fr != 0){
                        this.setYBodyRot(fr);
                        this.yBodyRotO = fr;
                        this.setYRot(fr);
                        this.xRotO = fr;
                    }
                }
            }
        }
        super.tick();
    }

    public String getData(){
        return "zombie";
    }

    @Override
    public void playerTouch(Player $$0) {
        if (!isActivated && this.isAlive() && !this.isRemoved() && !getTicksThroughPlacer()) {
            if (!this.level().isClientSide) {
                if ($$0.getMainHandItem().getItem() instanceof BodyBagItem BB){
                    if (BB.fillWithBody($$0.getMainHandItem(),this)){
                        this.level().playSound(null, this.blockPosition(), ModSounds.BODY_BAG_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));

                        this.discard();
                    }
                } else if ($$0.getOffhandItem().getItem() instanceof BodyBagItem BB){
                    if (BB.fillWithBody($$0.getOffhandItem(),this)){
                        this.level().playSound(null, this.blockPosition(), ModSounds.BODY_BAG_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CONTROLLER, -1);
        this.entityData.define(TICKS_THROUGH_PLACER, false);
        this.entityData.define(PHASES_FULL, false);
        this.entityData.define(FORCED_ROTATION, 0F);
    }
    protected FallenMob(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }
}
