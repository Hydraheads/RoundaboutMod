package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntity extends LivingEntity implements IPlayerEntity{


    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract float getDestroySpeed(BlockState $$0);

    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT_POS = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Byte> DATA_KNIFE_COUNT_ID = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$DODGE_TIME = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Shadow
    @Final
    private Inventory inventory;

    /**Used by Harpoon calculations*/
    @Unique
    private int roundabout$airTime = 0;
    @Unique
    private int roundabout$clientDodgeTime = 0;

    protected PlayerEntity(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public Inventory roundaboutGetInventory(){
        return inventory;
    }

    /**Keep track of unique player animations like floating*/
    public void roundabout$SetPos(byte Pos){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT_POS, Pos);
    }
    public byte roundabout$GetPos(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT_POS);
    }
    @Unique
    @Override
    public void roundabout$setClientDodgeTime(int dodgeTime){
        roundabout$clientDodgeTime = dodgeTime;
    }
    @Unique
    @Override
    public void roundabout$setDodgeTime(int dodgeTime){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$DODGE_TIME, dodgeTime);
    }
    @Unique
    @Override
    public int roundabout$getClientDodgeTime(){
        return roundabout$clientDodgeTime;
    }
    @Unique
    @Override
    public int roundabout$getDodgeTime(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$DODGE_TIME);
    }

    @Unique
    @Override
    public int roundabout$getAirTime(){
        return this.roundabout$airTime;
    }


    /**Attack Speed Decreases when your hand is stone*/
    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        byte curse = ((StandUser)this).roundabout$getLocacacaCurse();
        if (curse > -1) {
            if ((curse == LocacacaCurseIndex.MAIN_HAND && this.getMainArm() == HumanoidArm.RIGHT)
            || (curse == LocacacaCurseIndex.OFF_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                cir.setReturnValue((float)(1.0D / (this.getAttributeValue(Attributes.ATTACK_SPEED)*0.6) * 20.0D));
            }
        }
    }
    /**Block Breaking Speed Decreases when your hand is stone*/
    private boolean roundabout$destroySpeedRecursion = false;
    @Inject(method = "getDestroySpeed", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getDestroySpeed(BlockState $$0, CallbackInfoReturnable<Float> cir) {
        byte curse = ((StandUser)this).roundabout$getLocacacaCurse();
        if (curse > -1 && !roundabout$destroySpeedRecursion) {
            if ((curse == LocacacaCurseIndex.MAIN_HAND && this.getMainArm() == HumanoidArm.RIGHT)
                    || (curse == LocacacaCurseIndex.OFF_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                roundabout$destroySpeedRecursion = true;
                float dSpeed = this.getDestroySpeed($$0);
                roundabout$destroySpeedRecursion = false;
                cir.setReturnValue((float)(dSpeed*0.6));
            }
        }
    }

    @Inject(method = "getSpeed", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getSpeed(CallbackInfoReturnable<Float> cir) {
        byte curse = ((StandUser)this).roundabout$getLocacacaCurse();
        if (curse > -1) {
            if (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG) {
                cir.setReturnValue((float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.82));
            } else if (curse == LocacacaCurseIndex.CHEST) {
                cir.setReturnValue((float) (this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.85));
            }
        }
    }

    /**if your stand guard is broken, disable shields. Also, does not run takeshieldhit code if stand guarding.*/
    @Inject(method = "blockUsingShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutTakeShieldHit(LivingEntity $$0, CallbackInfo ci) {
        if (((StandUser) this).isGuarding()) {
            if (((StandUser) this).getGuardBroken()){

                ItemStack itemStack = ((LivingEntity) (Object) this).getUseItem();
                Item item = itemStack.getItem();
                if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                    ((LivingEntity) (Object) this).releaseUsingItem();
                    ((Player) (Object) this).stopUsingItem();
                }
                ((Player) (Object) this).getCooldowns().addCooldown(Items.SHIELD, 100);
                ((Player) (Object) this).level().broadcastEntityEvent(((Player) (Object) this), EntityEvent.SHIELD_DISABLED);
            }
            ci.cancel();
        } else if (((StandUser) $$0).getMainhandOverride()){
            ci.cancel();
        }
    }

    @ModifyVariable(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"), ordinal = 0)
    public CompoundTag roundabout$addAdditionalSaveData(CompoundTag $$0){
        $$0.putByte("roundabout.LocacacaCurse", ((StandUser)this).roundabout$getLocacacaCurse());
        return $$0;
    }
    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"))
    public void roundabout$readAdditionalSaveData(CompoundTag $$0, CallbackInfo ci){
        ((StandUser)this).roundabout$setLocacacaCurse($$0.getByte("roundabout.LocacacaCurse"));
    }

    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "hurtCurrentlyUsedShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutDamageShield(float $$0, CallbackInfo ci) {
        if (((StandUser) this).isGuarding()) {
            ci.cancel();
        }
    }
    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutJump(CallbackInfo ci) {
        if (((StandUser) this).isClashing()) {
            ci.cancel();
        }
    }


    /**If you are in a barrage, does not play the hurt sound*/
    @Inject(method = "getHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutGetHurtSound(DamageSource $$0, CallbackInfoReturnable<SoundEvent> ci) {
        if (((StandUser) this).isDazed()) {
            ci.setReturnValue(null);
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {

        boolean notSkybound = (this.onGround() || this.isSwimming()|| this.onClimbable() || this.isPassenger()
                || this.hasEffect(MobEffects.LEVITATION));

        if (!((TimeStop) ((Player)(Object) this).level()).getTimeStoppingEntities().isEmpty()) {
            if (((TimeStop) ((Player) (Object) this).level()).CanTimeStopEntity(((Player) (Object) this))) {
                ci.cancel();
            } else if ((((TimeStop) ((Player) (Object) this).level()).isTimeStoppingEntity(((Player) (Object) this)))) {
                ((StandUser) this).setRoundaboutIdleTime(-1);
                roundabout$airTime = 0;
            } else {
                if (notSkybound || this.isInWater()){
                    roundabout$airTime = 0;
                } else {
                    roundabout$airTime += 1;
                }
            }
        }

    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void RoundaboutTick2(CallbackInfo ci) {
        if (((StandUser)this).getAttackTimeDuring() > -1 || this.isUsingItem()) {
            ((StandUser) this).setRoundaboutIdleTime(-1);
        } else if (!new Vec3(this.getX(), this.getY(), this.getZ()).equals(new Vec3(this.xOld, this.yOld, this.zOld))) {
            ((StandUser) this).setRoundaboutIdleTime(-1);
        } else {
            ((StandUser) this).setRoundaboutIdleTime(((StandUser) this).getRoundaboutIdleTime() + 1);
        }
    }

    @Override
    @Unique
    public final int roundabout$getKnifeCount() {
        return this.entityData.get(DATA_KNIFE_COUNT_ID);
    }
    @Override
    @Unique
    public void roundabout$addKnife() {
        byte knifeCount = this.entityData.get(DATA_KNIFE_COUNT_ID);

        knifeCount++;
        if (knifeCount <= 12){
            ((LivingEntity) (Object) this).getEntityData().set(DATA_KNIFE_COUNT_ID, knifeCount);
        }
    }
    @Override
    @Unique
    public void roundabout$setKnife(byte knives) {
        ((LivingEntity) (Object) this).getEntityData().set(DATA_KNIFE_COUNT_ID, knives);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void initDataTrackerRoundabout(CallbackInfo ci) {
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT_POS, PlayerPosIndex.NONE);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$DODGE_TIME, -1);
        ((LivingEntity)(Object)this).getEntityData().define(DATA_KNIFE_COUNT_ID, (byte)0);
    }

    @Shadow
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Shadow
    public ItemStack getItemBySlot(EquipmentSlot var1) {
        return null;
    }

    @Shadow
    public void setItemSlot(EquipmentSlot var1, ItemStack var2) {

    }

    @Shadow
    public HumanoidArm getMainArm() {
        return null;
    }
}
