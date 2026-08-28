package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.gui.BlackSabbathPlayerInventoryMenu;
import net.hydra.jojomod.entity.MinionAttackGoal;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.goals.*;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.entity.zombie_minion.AxolotlMinion;
import net.hydra.jojomod.entity.zombie_minion.ParrotMinion;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.stand.powers.PowersCinderella;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

public class BlackSabbathEntity extends StandEntity implements HasCustomInventoryScreen {


    public BlackSabbathEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            PART_5_ANIME = 1,
            PART_5_MANGA = 2,
            BURNING = 3,
            GIO_GIO = 4,
            VERDANT = 5,
            NIGHT = 6,
            DEPARTURE = 7,
            DAPPER = 8,
            COPPER = 9,
            PHANTOM = 10,
            SWEET = 11,
            MAGMA = 12,
            OCULUS = 13,
            SACTHOTH = 14,
            BEACH = 15;

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
        return isHunting;
    }
    @Override
    public boolean isAttackable() {
        return isHunting;
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
        return !isHunting;
    }
    @Override
    public boolean fireImmune() {
        return false;
    }
    public boolean shouldFloat = false;
    public void setShouldFloat(boolean bool){shouldFloat = bool;}
    public boolean shouldSelect = false;
    public void setShouldSelect(boolean bool){shouldSelect = bool;}
    public int tickDownSecond = 0;
    public void setTickDownSecond(int td){tickDownSecond = td;}
    public boolean isHunting = false;
    public void setIsHunting(boolean bool){isHunting = bool;}
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
        if (this.level().getBrightness(LightLayer.BLOCK, pos) < 11) {
            if (isDay) {
                if (this.level().isRaining() || this.level().isThundering()) {
                    return false;
                } else if (this.level().getBrightness(LightLayer.SKY, atVec) < 12) {
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
    protected void registerGoals() {
        //super.registerGoals();
            this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

    }
    @Override
    public void tick(){
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        if(!isHunting) {
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
        } if(isHunting) {
            if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                    if(pb.tickDown2 > -10){
                        if(this.getDeltaMovement() != null) {
                            setDeltaMovement(0, this.getDeltaMovement().y, 0);
                        }
                    } else {
                        moveToBlock();
                    }
            }
        }
       // System.out.println(damageImmunityTicks);
      //  System.out.println(this.getHealth());
     //   System.out.println(isUnderSunlight());
        hurtBlackSabbath();
        super.tick();
        travelAhead(Entity::setPos);
    }
    public void hurtBlackSabbath(){
        if(isHunting) {
            if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                if (isBlackSabbathUnderLight()) {
                    damageImmunityTicks--;
                    if (damageImmunityTicks < 1) {
                        if(pb.moveMode == 3) {
                            this.setSecondsOnFire(2);
                            if (this.getHealth() >= 1) {

                            }
                            setDamageImmunityTicks(10);
                        }
                    }
                }
            }
        }
    }

    private LivingEntity targetSabbath(){
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
        if (source.is(DamageTypes.GENERIC_KILL) || source.is(DamageTypes.FELL_OUT_OF_WORLD)){
            discard();
            return false;
        }
        if (source.getEntity() != null && source.getEntity() != this.getUser()) {
            if (this.getUser() != null ) {
                if(source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.LAVA)) {
                    setDamageImmunityTicks(0);
                    return super.hurt(source, amount + 2);
                } else if (source.is(ModDamageTypes.GO_BEYOND)){
                    return super.hurt(source, amount * 30);
                }
            }
        } else if(source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.LAVA)) {
            return super.hurt(source, amount);
        } else if (MainUtil.isStandDamage(source) && this.getRemainingFireTicks() > 0){
            return super.hurt(source, amount * 0.85F);
        }
        this.markHurt();
        return super.hurt(source, 0.001F);
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if($$0.is(ModDamageTypes.GO_BEYOND)){
            return SoundEvents.BEACON_DEACTIVATE;
        }
        if($$0.is(DamageTypes.ON_FIRE) || $$0.is(DamageTypes.IN_FIRE) || $$0.is(ModDamageTypes.STAND_FIRE) || $$0.is(ModDamageTypes.STAND_FIRE) || $$0.is(DamageTypes.LAVA)) {
            return SoundEvents.PLAYER_HURT_ON_FIRE;
        }
        return SoundEvents.PLAYER_HURT;
    }

    public void travelAhead(Entity.MoveFunction positionUpdater) {
        if (this.getUser() != null) {
            if(((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb && (pb.moveMode == 2) && !isHunting) {
                Vec3 lvec = pb.getLookAngleChest(this.getUser().getYRot(), this.getUser());
                Position pn = this.getUser().getEyePosition().add(lvec.scale(-0.75F));
                positionUpdater.accept(this, pn.x(), this.getUser().getY() + (this.getUser().getBbHeight() / 2.45), pn.z());
            }
        }
    }

    /**Mob AI movement*/
    public void moveToBlock(){
        if(this.getNavigation() != null && this.getUser() != null){
            if(this.targetSabbath() != null) {
                this.getNavigation().moveTo(this.targetSabbath(), 1.5);
                this.lookAt(targetSabbath(), 300F, 300F);
            }
        }
    }
    public void lookAt(Entity $$0, float $$1, float $$2) {
        double $$3 = $$0.getX() - this.getX();
        double $$4 = $$0.getZ() - this.getZ();
        double $$6;
        if ($$0 instanceof LivingEntity $$5) {
            $$6 = $$5.getEyeY() - this.getEyeY();
        } else {
            $$6 = ($$0.getBoundingBox().minY + $$0.getBoundingBox().maxY) / 2.0 - this.getEyeY();
        }

        double $$8 = Math.sqrt($$3 * $$3 + $$4 * $$4);
        float $$9 = (float)(Mth.atan2($$4, $$3) * 180.0F / (float)Math.PI) - 90.0F;
        float $$10 = (float)(-(Mth.atan2($$6, $$8) * 180.0F / (float)Math.PI));
        this.setXRot(this.rotlerp(this.getXRot(), $$10, $$2));
        this.setYRot(this.rotlerp(this.getYRot(), $$9, $$1));
    }
    private float rotlerp(float $$0, float $$1, float $$2) {
        float $$3 = Mth.wrapDegrees($$1 - $$0);
        if ($$3 > $$2) {
            $$3 = $$2;
        }

        if ($$3 < -$$2) {
            $$3 = -$$2;
        }

        return $$0 + $$3;
    }

    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide) {
            ((IPlayerEntityServer)player).roundabout$openBlackSabbathInventory(this, player.getInventory());
        }
    }
}
