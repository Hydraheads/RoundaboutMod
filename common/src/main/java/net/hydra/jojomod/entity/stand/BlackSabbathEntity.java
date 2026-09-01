package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.entity.MinionAttackGoal;
import net.hydra.jojomod.entity.goals.*;
import net.hydra.jojomod.entity.navigation.AutomaticStandNavigation;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.zombie_minion.AxolotlMinion;
import net.hydra.jojomod.entity.zombie_minion.ParrotMinion;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class BlackSabbathEntity extends StandEntity implements HasCustomInventoryScreen {


    public BlackSabbathEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(BlockPathTypes.BLOCKED, -1.0F);
    }

    public static final byte
            PART_5_ANIME = 1,
            PART_5_MANGA = 2,
            BURNING = 3,
            GIO_GIO = 4,
            VERDANT = 5,
            NIGHT = 6,
            DEPARTURE = 7,
            CHERRY = 8,
            GRAPE = 9,
            MINT = 10,
            TACO = 11,
            WOOL = 12,
            DAPPER = 13,
            COPPER = 14,
            PHANTOM = 15,
            SWEET = 16,
            MAGMA = 17,
            OCULUS = 18,
            SACTHOTH = 19,
            COWBOY = 20,
            BEACH = 21,
            SANTA = 22;

    public final AnimationState coat_open = new AnimationState();
    public final AnimationState chest_open = new AnimationState();
    public final AnimationState chest_close = new AnimationState();
    public final AnimationState floating = new AnimationState();
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if(this.getUser() != null){
            if (((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
                switch (pb.moveMode) {
                    case 1 -> {
                        if (pb.active) {
                            this.coat_open.stop();
                            chest_close.stop();
                            this.chest_open.startIfStopped(this.tickCount);
                        } else {
                            this.chest_open.stop();
                            this.coat_open.stop();
                            this.chest_close.startIfStopped(this.tickCount);
                        }
                    }
                    case 2 -> {
                        this.chest_open.stop();
                        this.chest_close.stop();
                        this.floating.startIfStopped(this.tickCount);
                        this.coat_open.startIfStopped(this.tickCount);
                    }
                    case 3 -> {
                        if(!pb.blackSabbathTargets.isEmpty()){
                            this.coat_open.start(this.tickCount);
                        } else {
                            this.chest_open.stop();
                            this.coat_open.stop();
                            this.chest_close.startIfStopped(this.tickCount);
                        }
                        this.chest_open.stop();
                    }
                }
            }
        } else {
            this.chest_open.stop();
            this.chest_close.stop();
            this.coat_open.startIfStopped(this.tickCount);
        }
    }
    @Override
    public boolean canStandBeHurt(){
        return true;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return getHunting() && !getRiding();
    }
    @Override
    public boolean isAttackable() {
        return getHunting() && !getRiding();
    }
    @Override
    public boolean isPickable() {
        return true;
    }
    @Override
    public boolean skipAttackInteraction(Entity $$0) {
        return false;
    }
    @Override
    public boolean isInvulnerable() {
        return !getHunting() && getRiding();
    }
    @Override
    public boolean fireImmune() {
        return false;
    }
    @Override
    public void knockback(double $$0, double $$1, double $$2){
        super.knockback($$0 * 1.65D, $$1, $$2 * 1.65D);
    }
    public boolean shouldFloat = false;
    public void setShouldFloat(boolean bool){shouldFloat = bool;}
    public boolean shouldSelect = false;
    public void setShouldSelect(boolean bool){shouldSelect = bool;}
    public int tickDownSecond = 0;
    public void setTickDownSecond(int td){tickDownSecond = td;}
    @Override
    public boolean forceVisualRotation(){
        return true;
    }
    @Override
    public boolean lockPos(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2;
        }
        return false;
    }
    @Override
    public boolean hasNoPhysics(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2 || this.is(pb.blackSelect);
        }
        return false;
    }
    @Override
    public boolean isNoGravity() {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2 || this.is(pb.blackSelect);
        }
        return false;
    }
    @Override
    public boolean standHasGravity() {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode != 2 || !this.is(pb.blackSelect);
        }
        return true;
    }
    public boolean isUnderSunlight(LivingEntity lent){
        if(lent != null) {
            BlockPos pos = lent.blockPosition();
            long timeOfDay = lent.level().getDayTime() % 24000L;
            Vec3 yes = lent.getEyePosition();
            BlockPos atVec = BlockPos.containing(yes);
            boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;
            if (lent.level().getBrightness(LightLayer.BLOCK, pos) < 11) {
                if (isDay) {
                    if (lent.level().isRaining() || lent.level().isThundering()) {
                        return false;
                    } else if (lent.level().getBrightness(LightLayer.SKY, atVec) < 12) {
                        return false;
                    } else {
                        return true;
                    }
                } else if (!isDay) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }
    public boolean isBlackSabbathUnderLight(){
        BlockPos pos = this.blockPosition();
        long timeOfDay = this.level().getDayTime() % 24000L;
        Vec3 yes = this.getEyePosition();
        BlockPos atVec = BlockPos.containing(yes);
        boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;
        if (this.level().getBrightness(LightLayer.BLOCK, pos) < 13) {
            if (isDay) {
                if (this.level().isRaining() || this.level().isThundering()) {
                    return false;
                } else if (this.level().getBrightness(LightLayer.SKY, atVec) < 15) {
                    return false;
                } else {
                    return true;
                }
            } else if (!isDay) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
    private int damageImmunityTicks = 10;
    private void setDamageImmunityTicks(int immun){damageImmunityTicks = immun;}
    @Override
    public void tick(){
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        if(!getHunting()) {
            if (shouldFloat && this.getUser() != null) {
                if (!this.level().isClientSide()) {
                    this.setXRot(pitch);
                    this.setYRot(yaw);
                    this.setYBodyRot(yaw);
                    this.xRotO = pitch;
                    this.yRotO = yaw;
                }
                if (((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                    if (tickDownSecond > 1) {
                        tickDownSecond--;

                        if (tickDownSecond == 4) {
                            this.forceDespawnSet = true;
                        }
                    }
                }
            }
        } if(getHunting()) {
            if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                    if(pb.tickDown2 > -10){
                        if(this.getDeltaMovement() != null) {
                            setDeltaMovement(0, this.getDeltaMovement().y, 0);
                        }
                    } else {
                      //  moveToBlock();
                    }
            }
        }
       // System.out.println(damageImmunityTicks);
      //  System.out.println(this.getHealth());
     //   System.out.println(isUnderSunlight());
        if(isBlackSabbathUnderLight()){
            this.getNavigation().setSpeedModifier(0.35);
        }
        if(getHunting()){
            huntingTick();
        }
        hurtBlackSabbath();
        super.tick();
        travelAhead(Entity::setPos);
    }
    public void hurtBlackSabbath(){
        if(getHunting()) {
            if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                if (isBlackSabbathUnderLight()) {
                    damageImmunityTicks--;
                    if (damageImmunityTicks < 1) {
                        if(pb.moveMode == 3) {
                            if(this.isInWater() || isInPowderSnow){
                                DamageSource damageSource = ModDamageTypes.of(this.level(), DamageTypes.HOT_FLOOR);
                                setDamageImmunityTicks(10);
                                super.hurt(damageSource, 2);
                            } else {
                                this.setSecondsOnFire(2);
                                setDamageImmunityTicks(10);
                            }
                        }
                    }
                }
            }
        }
    }

    private static final EntityDataAccessor<Boolean> IS_STARFING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getStarfing() {
        return this.entityData.get(IS_STARFING);
    }
    public final void setStarfing(Boolean bool) {
        this.entityData.set(IS_STARFING, bool);
    }
    private static final EntityDataAccessor<Boolean> IS_RIDING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getRiding() {
        return this.entityData.get(IS_RIDING);
    }
    public final void setRiding(Boolean bool) {
        this.entityData.set(IS_RIDING, bool);
    }
    private static final EntityDataAccessor<Boolean> CRIPPLED =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getCrippled() {
        return this.entityData.get(CRIPPLED);
    }
    public final void setCrippled(Boolean bool) {
        this.entityData.set(CRIPPLED, bool);
    }
    private static final EntityDataAccessor<Boolean> IS_HUNTING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getHunting() {
        return this.entityData.get(IS_HUNTING);
    }
    public final void setHunting(Boolean bool) {
        this.entityData.set(IS_HUNTING, bool);
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(CRIPPLED)) {
            super.defineSynchedData();
            this.entityData.define(CRIPPLED, false);
            this.entityData.define(IS_RIDING, false);
            this.entityData.define(IS_HUNTING, false);
            this.entityData.define(IS_STARFING, false);
        }
    }
    @Override
    public void die(@NotNull DamageSource source) {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            ((StandUser)this.getUser()).roundabout$setSealedTicks(300);
            pbs.setTickDown2(20);
            if(MainUtil.isStandDamage(source) && this.getRemainingFireTicks() > 0 || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.LAVA)) {
                setCrippled(true);
                if (this.level() instanceof ServerLevel SL) {
                    Vec3 position = this.getPosition(1);
                    Vec3 position2 = this.getEyePosition();
                    Vec3 position3 = this.getEyePosition().subtract(this.getPosition(1)).multiply(new Vec3(0.5F,
                            0.5F, 0.5F));
                    position3 = position3.add(this.getPosition(1));
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.2, 0.2, 0.1);
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.2, 0.2, 0.1);
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.2, 0.2, 0.1);


                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.5, 0.2, 0.5);
                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.5, 0.2, 0.2);

                    this.level().playSound(null, BlockPos.containing(this.position()), ModSounds.VAMPIRE_CRUMBLE_EVENT, SoundSource.PLAYERS, 1.0F, 1F);
                }
            } else if(source.is(ModDamageTypes.GO_BEYOND)){
                if (this.level() instanceof ServerLevel SL) {
                    Vec3 position = this.getPosition(1);
                    Vec3 position2 = this.getEyePosition();
                    Vec3 position3 = this.getEyePosition().subtract(this.getPosition(1)).multiply(new Vec3(0.5F,
                            0.5F, 0.5F));
                    position3 = position3.add(this.getPosition(1));

                    SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.5, 0.2, 0.5);
                    SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                    SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.5, 0.2, 0.2);

                    SL.sendParticles(ModParticles.STAR,
                            position.x, position.y, position.z,
                            0, 0.2, 0.5, 0.2, 0.5);
                    SL.sendParticles(ModParticles.STAR,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                    SL.sendParticles(ModParticles.STAR,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                }
            }
        }
    }

    public LivingEntity targetSabbath(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            if(!pbs.blackSabbathTargets.isEmpty()){
                List<LivingEntity> targent = new ArrayList<>(pbs.blackSabbathTargets);
                targent.removeIf(this::isUnderSunlight);

                LivingEntity lv = this.level().getNearestEntity(targent,
                        MainUtil.OFFER_TARGER_CONTEXT, null,
                        this.getX(), this.getY(), this.getZ());

                return lv;
            }
        }
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!getRiding()) {
            if (source.is(DamageTypes.GENERIC_KILL) || source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                discard();
                return false;
            }
            if (source.getEntity() != null && source.getEntity() != this.getUser()) {
                if (this.getUser() != null) {
                    if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.LAVA)) {
                        return super.hurt(source, amount + 2);
                    } else if (source.is(ModDamageTypes.GO_BEYOND)) {
                        return super.hurt(source, amount * 30);
                    }
                }
            } else if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.LAVA)) {
                return super.hurt(source, amount);
            } else if (MainUtil.isStandDamage(source) && this.getRemainingFireTicks() > 0) {
                return super.hurt(source, amount * 0.85F);
            }
            this.markHurt();
            return super.hurt(source, 0.0F);
        }
        return false;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if($$0.is(ModDamageTypes.GO_BEYOND)){
            return SoundEvents.BEACON_DEACTIVATE;
        }
        if($$0.is(DamageTypes.ON_FIRE) || $$0.is(DamageTypes.IN_FIRE) || $$0.is(ModDamageTypes.STAND_FIRE) || $$0.is(ModDamageTypes.STAND_FIRE) || $$0.is(DamageTypes.LAVA) || $$0.is(DamageTypes.HOT_FLOOR)) {
            return SoundEvents.PLAYER_HURT_ON_FIRE;
        }
        return SoundEvents.PLAYER_HURT;
    }

    public void travelAhead(Entity.MoveFunction positionUpdater) {
        if (this.getUser() != null) {
            if(((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb && (pb.moveMode == 2) && !getHunting()) {
                Vec3 lvec = pb.getLookAngleChest(this.getUser().getYRot(), this.getUser());
                Position pn = this.getUser().getEyePosition().add(lvec.scale(-0.75F));
                positionUpdater.accept(this, pn.x(), this.getUser().getY() + (this.getUser().getBbHeight() / 2.45), pn.z());
            }
        }
    }

    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide) {
            ((IPlayerEntityServer)player).roundabout$openBlackSabbathInventory(this, player.getInventory());
        }
    }

    /**Mob AI movement*/

    public LivingEntity shadowHidTarget() {
        if (this.level() != null) {
            List<LivingEntity> lvent = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3, 9, 3), (livingEntity) -> {
                return true;
            });
            if (lvent != null && !lvent.isEmpty()) {
                List<LivingEntity> targent = new ArrayList<>(lvent);
                for (LivingEntity value : lvent) {
                    if (value instanceof StandEntity || !this.hasLineOfSight(value)) {
                        targent.remove(value);
                    }
                    if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
                        if(pbs.blackSabbathTargets.contains(value)){
                            targent.remove(value);
                        }
                    }
                }

                lvent = targent;
            }
            LivingEntity lv = this.level().getNearestEntity(lvent,
                    MainUtil.OFFER_TARGER_CONTEXT, null,
                    this.getX(), this.getY(), this.getZ());

            return lv;
        }
        return null;
    }

    @Override
    protected PathNavigation createNavigation(Level $$0) {
        AutomaticStandNavigation nav = new AutomaticStandNavigation(this, $$0);
        nav.setAvoidLight(true);
        return nav;
    }

    public void bsStopMove() {
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0);
        this.getNavigation().setSpeedModifier(0.0);
        this.getNavigation().stop();
    }

    public Vec3 getTargetPosition() {
        Vec3 targetPos;
        if(targetSabbath() != null){
            targetPos = targetSabbath().position();
            return targetPos;
        }
        return null;
    }

    public void huntingTick(){
        if(this.targetSabbath() != null){
            if(!this.level().isClientSide) {
                this.moveToTarget();
            }
        } else if (this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){

        }

    }

    protected void moveToTarget() {
        Vec3 pos = this.getTargetPosition();
        bsMove(pos);
    }
    protected void moveToShadow() {
        if(this.shadowHidTarget() != null){
            Vec3 pos = new Vec3(shadowHidTarget().getX(), shadowHidTarget().getY(), shadowHidTarget().getZ());
            if(pos != null) {
                bsMove(pos);
            }
        }
    }

    int ticksUntilNextPathRecalculation = 15;

    public void bsMove(Vec3 targetPos) {
        ticksUntilNextPathRecalculation--;
        if (ticksUntilNextPathRecalculation <= 0) {
            ticksUntilNextPathRecalculation = 15;

            Path newPath;
            if(this.targetSabbath() != null) {
                newPath = this.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 0);
            } else {
                newPath = null;
            }


            if (newPath == null) { return; }

            this.lookAt(EntityAnchorArgument.Anchor.FEET, new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ()));

            if (!this.getNavigation().moveTo(newPath, 1.6f))
                ticksUntilNextPathRecalculation += 5;
        }
    }
}
