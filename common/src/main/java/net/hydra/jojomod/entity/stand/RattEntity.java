package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class RattEntity extends StandEntity {
    public RattEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            MELON_SKIN = 3,
            SAND_SKIN = 4,
            AZTEC_SKIN = 5,
            TOWER_SKIN = 6,
            SNOWY_SKIN = 7,
            GUARDIAN_SKIN = 8,
            ELDER_GUARDIAN_SKIN = 9,
            REDD_SKIN = 10,
            NETHER_SKIN = 11,
            CHAIR_RAT_SKIN = 12,
            KING_RAT_SKIN = 13,
            MECH_RAT_SKIN = 14,

            FIRE = 81,
            LOADING = 82;

    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                MELON_SKIN,
                SAND_SKIN,
                TOWER_SKIN,
                SNOWY_SKIN,
                REDD_SKIN,
                GUARDIAN_SKIN,
                ELDER_GUARDIAN_SKIN,
                CHAIR_RAT_SKIN,
                KING_RAT_SKIN,
                MECH_RAT_SKIN

        );
    }




    public Vec3 getEyeP(float d) {
        if (this.getSavedSkin() >= CHAIR_RAT_SKIN) {
            return this.getPosition(d).add(0,0.65,0);
        }
        return this.getPosition(d).add(0,0.15,0);
    }


    public LivingEntity Target;
    public LivingEntity getTarget() {
        if (this.level().isClientSide){
            return (LivingEntity) this.level().getEntity(this.entityData.get(TARGET_ID));
        } else {
            if (this.Target != null && this.Target.isRemoved()){
                this.setFollowing(null);
            }
            return this.Target;
        }
    }
    public void setRattTarget(LivingEntity StandSet){
        this.Target = StandSet;
        int standSetId = -1;
        if (StandSet != null){
            standSetId = StandSet.getId();
        }
        this.entityData.set(TARGET_ID, standSetId);
    }
    public void setSavedSkin(byte skin) {this.entityData.set(SAVED_SKIN,skin);}
    public byte getSavedSkin() {return this.entityData.get(SAVED_SKIN);}

    protected static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(RattEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> SAVED_SKIN = SynchedEntityData.defineId(RattEntity.class,
            EntityDataSerializers.BYTE);
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(TARGET_ID)) {
            super.defineSynchedData();
            this.entityData.define(TARGET_ID, -1);
            this.entityData.define(SAVED_SKIN,(byte)0);
        }
    }

    @Override
    public boolean forceVisualRotation(){
        return true;
    }

    @Override
    public boolean lockPos(){
        return false;
    }
    @Override
    public boolean hasNoPhysics(){
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return true;
    }


    public final AnimationState fire = new AnimationState();
    public final AnimationState loading = new AnimationState();

    @Override
    public void setupAnimationStates() {
        if (this.getUser() != null) {
            if (this.getAnimation() == FIRE) {
                this.fire.startIfStopped(this.tickCount);
            } else {
                this.fire.stop();
            }
            if (this.getAnimation() == LOADING) {
                this.loading.startIfStopped(this.tickCount);
            } else {
                this.loading.stop();
            }
        }
        super.setupAnimationStates();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() != this.getUser()) {
            if (this.getUser() != null ) {

                if (source.getEntity() instanceof LivingEntity LE){
                    if (PowerTypes.hasStandActivelyEquipped(LE)){
                        if (!PowerTypes.hasStandActive(LE)){
                            return false;
                        }
                    }
                }

                if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersRatt PR) {
                    if (!this.onGround()) {
                        return false;
                    }
                    PR.active = false;
                    this.forceDespawnSet = true;
                    PR.setCooldown(PowersRatt.SETPLACE, 60);
                    if (this.getUser() instanceof Player P && !this.level().isClientSide()) {
                        S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) P), PowersRatt.SETPLACE, 80);

                    }
                    PR.setCooldown(PowersRatt.SETPLACE, 80);
                    this.level().playSound(null, this.blockPosition(), ModSounds.RATT_DEPLACE_EVENT, SoundSource.PLAYERS, 0.5F, 1F);
                    return this.getUser().hurt(source, Mth.clamp(amount * 0.5F, 0, 6));
                }
            }


        }
        return false;
    }

    @Override
    public @Nullable Entity changeDimension(ServerLevel $$0) {
        if (this.getUser() != null) {
            StandUser SU = (StandUser) this.getUser();
            if (SU.roundabout$getStandPowers() instanceof PowersRatt PR) {
                PR.RecallClient(true);
            }
        }
        return super.changeDimension($$0);
    }


    @Override
    public void knockback(double $$0, double $$1, double $$2) {}
    @Override
    public boolean isAttackable() {return true;}
    @Override
    public boolean isPickable() {return true;}
    @Override
    public boolean skipAttackInteraction(Entity $$0) {return false;}
}

